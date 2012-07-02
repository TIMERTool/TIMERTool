/*
 * Copyright (c) 2012, Keeley Hoek
 * All rights reserved.
 * 
 * Redistribution and use of this software in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *   Redistributions of source code must retain the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer.
 * 
 *   Redistributions in binary form must reproduce the above
 *   copyright notice, this list of conditions and the
 *   following disclaimer in the documentation and/or other
 *   materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import javax.swing.JPanel;
import org.apache.commons.collections15.Transformer;
import org.timer.model.TimeLink;
import org.timer.model.TimeManager;

/**
 *
 * @author Keeley Hoek (escortkeel)
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
            GraphVertex vertexTop =  new GraphVertex(manager.getNodeName(link.getTopNode()));
            GraphVertex vertexBottom =  new GraphVertex(manager.getNodeName(link.getBottomNode()));
            GraphEdge edge = new GraphEdge(vertexTop, vertexBottom);
            
            if (!vertices.contains(vertexTop)) {
                vertices.add(vertexTop);
                graph.addVertex(vertexTop);
            }
            
            if (!vertices.contains(vertexBottom)) {
                vertices.add(vertexBottom);
                graph.addVertex(vertexBottom);
            }
            
            if(edges.contains(edge)) {
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
        for(GraphEdge edge : graph.getEdges()) {
            if(edge.equals(toFind)) {
                return edge;
            }
        }
        
        return null;
    }
}
