package impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Graph implementation
 *
 * @param <V> - vertex value type
 */
public class SimpleGraph<V> {

    private List<Vertex> vertices;
    private List<Edge> edges;

    public SimpleGraph() {
        vertices = new LinkedList<>();
        edges = new LinkedList<>();
    }

    /**
     * Getting graph size
     *
     * @return number of vertices
     */
    public int size() {
        return vertices.size();
    }

    /**
     * Getting vertex degree
     *
     * @param vertex current vertex
     * @return number of incident edges for current vertex
     */
    public int degree(Vertex vertex) {
        return vertex.adjacent.size();
    }

    public Vertex[] getEndVertices(Edge edge) {
        Vertex[] endV = (Vertex[]) (new Object[2]);
        endV[0] = edge.vertex1;
        endV[1] = edge.vertex2;
        return endV;
    }

    public Vertex getOppositeVertex(Vertex vertex, Edge edge) {
        if (edge.vertex1.equals(vertex)) {
            return edge.vertex2;
        } else {
            return edge.vertex1;
        }
    }

    public boolean areAdjacent(Vertex vertexFrom, Vertex vertexTo) {
        for (Edge edge : vertexFrom.adjacent) {
            if (getOppositeVertex(vertexFrom, edge).equals(vertexTo)) {
                return true;
            }
        }
        return false;
    }

    public List<Edge> getIncidentEdges(Vertex vertex) {
        return vertex.adjacent;
    }

    public List<Vertex> getIncidentVertices(Vertex vertex) {
        List<Vertex> incidentVertices = new LinkedList<>();
        for (Edge edge : vertex.adjacent) {
            incidentVertices.add(
                    getOppositeVertex(vertex, edge)
            );
        }
        return incidentVertices;
    }

    public Vertex getVertex(V value) {
        for (Vertex vertex : vertices) {
            if (vertex.value.equals(value)) {
                return vertex;
            }
        }
        return null;
    }

    public Edge getEdge(Vertex from, Vertex to) {
        for (Edge edge : edges) {
            if ((edge.vertex1.equals(from) && edge.vertex2.equals(to))
                    || (edge.vertex2.equals(from) && edge.vertex1.equals(to))) {
                return edge;
            }
        }
        return null;
    }

    public Vertex insertVertex(V vertexName) {
        if (getVertex(vertexName) == null) {
            Vertex v = new Vertex(vertexName);
            vertices.add(v);
            return v;
        }
        return null;
    }

    public Edge insertEdge(Vertex from, Vertex to, int weight) {
        Edge e = new Edge(from, to, weight);
        from.adjacent.add(e);
        to.adjacent.add(e);
        edges.add(e);
        return e;
    }

    public boolean removeVertex(V value) {
        Vertex vertexToRemove = getVertex(value);
        Vertex opposite;

        if (vertexToRemove != null) {
            for (Edge e : vertexToRemove.adjacent) {
                opposite = getOppositeVertex(vertexToRemove, e);
                opposite.adjacent.remove(e);
                edges.remove(e);
            }
            vertices.remove(vertexToRemove);
            return true;
        }
        return false;
    }

    public boolean removeEdge(Vertex from, Vertex to) {
        Edge edge = getEdge(from, to);
        return removeEdge(edge);
    }

    public boolean removeEdge(Edge edge) {
        if (edge != null) {
            edge.vertex1.adjacent.remove(edge);
            edge.vertex2.adjacent.remove(edge);
            edges.remove(edge);
            return true;
        }
        return false;
    }

    /**
     * Breadth First Search for weighted or unweighted graph
     *
     * @param start - start vertex
     */
    public void bfs(Vertex start) {
        for (Vertex vertex : vertices) {
            vertex.distance = Integer.MAX_VALUE;
        }
        start.distance = 0;

        Queue<Vertex> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            Vertex vertex = queue.remove();

            // try to improve state
            for (Edge adjacent : vertex.adjacent) {
                Vertex child = getOppositeVertex(vertex, adjacent);
                if (vertex.distance + adjacent.weight < child.distance) {
                    child.distance = vertex.distance + adjacent.weight;
                }
            }

            Vertex child;
            while ((child = vertex.getUnvisitedAdjacent()) != null) {
                child.visited = true;
                queue.add(child);
            }
        }

        // clear visited
        for (Vertex vertex : vertices) {
            vertex.visited = false;
        }
    }

    /**
     * Getting the shortest path from one vertex to another
     *
     * @param start  - start vertex
     * @param finish - end vertex
     * @return shortest path
     */
    public List<String> getShortestPath(Vertex start, Vertex finish) {
        List<String> path = new LinkedList<>();

        bfs(start);
        int distance = finish.distance;
        Vertex current = finish;

        while (distance > 0) {
            path.add(current.getValue().toString());
            current = current.getAdjacentWithLessDistance(distance);
            distance = current.distance;
        }

        path.add(start.getValue().toString());

        // reverse path
        List<String> tmp = new LinkedList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            tmp.add(path.get(i));
        }
        path = tmp;

        // set distance to infinity
        for (Vertex vertex : vertices) {
            vertex.distance = Integer.MAX_VALUE;
        }

        return path;
    }


    public class Vertex {

        private V value;
        private List<Edge> adjacent;
        private boolean visited;
        private int distance;

        Vertex(V value) {
            this.value = value;
            this.adjacent = new LinkedList<>();
            this.visited = false;
            this.distance = Integer.MAX_VALUE;
        }

        public V getValue() {
            return this.value;
        }

        public Vertex getUnvisitedAdjacent() {
            for (Edge edge : adjacent) {
                Vertex vertex = getOppositeVertex(this, edge);
                if (vertex.visited == false) {
                    return vertex;
                }
            }
            return null;
        }

        public Vertex getAdjacentWithLessDistance(int dist) {
            for (Edge edge : adjacent) {
                Vertex vertex = getOppositeVertex(this, edge);
                if (vertex.distance < dist) {
                    return vertex;
                }
            }
            return null;
        }

    }

    public class Edge {

        private Vertex vertex1;
        private Vertex vertex2;
        private int weight;

        Edge(Vertex vertex1, Vertex vertex2, int weight) {
            this.vertex1 = vertex1;
            this.vertex2 = vertex2;
            this.weight = weight;
        }
    }

}
