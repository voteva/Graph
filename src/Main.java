import impl.SimpleGraph;

import java.io.*;
import java.util.*;

public class Main {

    static SimpleGraph buildGraph(String pathToFile) {
        SimpleGraph<String> graph = new SimpleGraph<>();

        try {
            Scanner in = new Scanner(new File(pathToFile));
            // inserting vertices
            String vertexLine = in.nextLine();
            String[] vertices = {};

            if (!vertexLine.equals(""))
                vertices = vertexLine.split("\\s+");

            for (String v : vertices)
                graph.insertVertex(v);

            // inserting edges
            SimpleGraph.Vertex v1, v2;
            String edgeLine = in.nextLine();

            if (!edgeLine.equals(""))
                vertices = edgeLine.split("\\s+");

            for (int i = 0; i < vertices.length - 1; i += 2) {
                v1 = graph.getVertex(vertices[i]);
                v2 = graph.getVertex(vertices[i + 1]);

                // add edge if it's not "!"
                if (!((v1.getValue().toString().split("-")[1].equals("DU")
                        && v2.getValue().toString().split("-")[1].equals("R"))
                        || (v2.getValue().toString().split("-")[1].equals("DU")
                        && v1.getValue().toString().split("-")[1].equals("R"))
                        || (v1.getValue().toString().split("-")[1].equals("DG")
                        && v2.getValue().toString().split("-")[1].equals("R"))
                        || (v2.getValue().toString().split("-")[1].equals("DG")
                        && v1.getValue().toString().split("-")[1].equals("R")))) {
                    graph.insertEdge(v1, v2, 1);
                }
            }
            in.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return graph;
    }

    static void printOutput(String pathToFile, List ... results) {
        try {
            File flt = new File(pathToFile);
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new FileWriter(flt))
            );

            if (!flt.exists()) {
                flt.createNewFile();
            }

            for (List result : results) {
                out.print(result.size() - 1 + " ");

                for (Object aResult : result) {
                    out.print(aResult + " ");
                }

                out.println();
            }
            out.flush();

            System.out.println("See result in " + pathToFile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SimpleGraph<String> graph = buildGraph("resources/cities.txt");

        // case 1
        SimpleGraph.Vertex startCity = graph.getVertex("Melitopol-U");
        SimpleGraph.Vertex finishCity = graph.getVertex("Rostov-R");
        List<String> path1 = graph.getShortestPath(startCity, finishCity);

        // case 2
        startCity = graph.getVertex("Sukhumi-DG");
        finishCity = graph.getVertex("Lugansk-DU");
        List<String> path2 = graph.getShortestPath(startCity, finishCity);

        printOutput("resources/travel-now.txt", path1, path2);
    }

}