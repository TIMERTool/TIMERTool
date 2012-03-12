/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package converter;

import java.io.*;

/**
 *
 * @author Peter Hoek
 */
public class VAST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("input.csv")));
        PrintStream ps = new PrintStream(new File("output.csv"));
        
        br.readLine();
        
        String in;
        while((in = br.readLine()) != null) {
            String[] split = in.split(",");            
            split[2] = split[2].replaceAll("2006", "").replaceAll("\\s", "");
            
            for(int i = 0; i < split.length; i++) {
                ps.print(split[i] + ",");
            }
            
            ps.println();
        }
    }
}
