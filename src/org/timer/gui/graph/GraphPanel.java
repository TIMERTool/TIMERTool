/*
 * Copyright (c) 2012, Peter Hoek
 * All rights reserved.
 */
package org.timer.gui.graph;

import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;
import org.timer.model.TimeLink;
import org.timer.model.TimeManager;

/**
 *
 * @author Peter Hoek
 */
public class GraphPanel extends JPanel {

    private final TimeManager manager;
    private Graph<GraphVertex, GraphEdge> graph;
    private SpringLayout layout;
    private VisualizationViewer vv;
    private DefaultModalGraphMouse<GraphVertex, GraphEdge> graphMouse;

    public GraphPanel(TimeManager manager) {
        super();

        this.manager = manager;

        initComponents();
    }

    private void initComponents() {
        graph = new DirectedSparseGraph<>();

        graphMouse = new DefaultModalGraphMouse<>();

        layout = new SpringLayout2(graph);
        layout.setSize(new Dimension(1000, 400));

        vv = new VisualizationViewer(layout);
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<GraphVertex>());
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        vv.getRenderContext().setEdgeStrokeTransformer(new Transformer<GraphEdge, Stroke>() {

            @Override
            public Stroke transform(GraphEdge edge) {
                return new BasicStroke(edge.getCount());
            }
        });
        vv.getRenderContext().setVertexFillPaintTransformer(new PickableVertexPaintTransformer<GraphVertex>(vv.getPickedVertexState(), Color.green, Color.yellow));
        vv.setGraphMouse(graphMouse);

        vv.setAutoscrolls(true);
        vv.setAlignmentX(CENTER_ALIGNMENT);
        vv.setAlignmentY(CENTER_ALIGNMENT);

        vv.setSize(new Dimension(1000, 400));
        vv.setPreferredSize(new Dimension(1000, 400));

        add(vv);
    }

    private class GraphVertex {

        private String id;

        public GraphVertex(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof GraphVertex) {
                return id.equals(((GraphVertex) other).id);
            }

            return false;
        }

        @Override
        public int hashCode() {
            return 53 * 7 + Objects.hashCode(id);
        }
    }

    private class GraphEdge {

        private GraphVertex to, from;
        private int count;

        public GraphEdge(GraphVertex to, GraphVertex from) {
            this.to = to;
            this.from = from;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int edgeCount) {
            this.count = edgeCount;
        }

        @Override
        public String toString() {
            return to.toString() + " - " + from.toString();
        }
    }

    public void update(Iterator<TimeLink> data) {
        for (GraphVertex vertex : new ArrayList<>(graph.getVertices())) {
            graph.removeVertex(vertex);
        }

        ArrayList<GraphVertex> vertices = new ArrayList<>();
        ArrayList<GraphEdge> edges = new ArrayList<>();

        while (data.hasNext()) {
            TimeLink link = data.next();
            GraphVertex vertexTop = new GraphVertex(manager.getNodeName(link.getTopNode()));
            GraphVertex vertexBottom = new GraphVertex(manager.getNodeName(link.getBottomNode()));
            GraphEdge edge = new GraphEdge(vertexTop, vertexBottom);

            if (!vertices.contains(vertexTop)) {
                vertices.add(vertexTop);
                graph.addVertex(vertexTop);
            }

            if (!vertices.contains(vertexBottom)) {
                vertices.add(vertexBottom);
                graph.addVertex(vertexBottom);
            }

            if (edges.contains(edge)) {
                GraphEdge theEdge = getEdge(edge);
                theEdge.setCount(theEdge.getCount() + 1);
            } else {
                edges.add(edge);
                graph.addEdge(edge, vertexTop, vertexBottom);
            }
        }

        repaint();
    }

    public GraphEdge getEdge(GraphEdge toFind) {
        for (GraphEdge edge : graph.getEdges()) {
            if (edge.equals(toFind)) {
                return edge;
            }
        }

        return null;
    }
}
