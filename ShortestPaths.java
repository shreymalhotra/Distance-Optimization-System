package graph;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * The shortest paths through an edge-weighted graph.
 * By overrriding methods getWeight, setWeight, getPredecessor, and
 * setPredecessor, the client can determine how to represent the weighting
 * and the search results.  By overriding estimatedDistance, clients
 * can search for paths to specific destinations using A* search.
 *
 * @author Shrey Malhotra
 */
public abstract class ShortestPaths {
    /**
     * A shortest path in G from SOURCE to DEST.
     */
    public ShortestPaths(Graph G, int source, int dest) {
        _G = G;
        _source = source;
        _dest = dest;
    }

    /**
     * Initialize the shortest paths.  Must be called before using
     * getWeight, getPredecessor, and pathTo.
     */
    public void setPaths() {
        route = new LinkedList<Integer>();
        astarroute = new AStar(_G, new AStarPriorityQueue());
        for (Integer i : _G.vertices()) {
            setWeight(i, Double.MAX_VALUE);
            setPredecessor(i, 0);
        }
        setWeight(getSource(), 0);
        astarroute.traverse(getSource());
    }

    /**
     * Returns the starting vertex.
     */
    public int getSource() {
        return _source;
    }

    /**
     * Returns the target vertex, or 0 if there is none.
     */
    public int getDest() {
        return _dest;
    }

    /**
     * Returns the current weight of vertex V in the graph.  If V is
     * not in the graph, returns positive infinity.
     */
    public abstract double getWeight(int v);

    /**
     * Set getWeight(V) to W. Assumes V is in the graph.
     */
    protected abstract void setWeight(int v, double w);

    /**
     * Returns the current predecessor vertex of vertex V in the graph, or 0 if
     * V is not in the graph or has no predecessor.
     */
    public abstract int getPredecessor(int v);

    /**
     * Set getPredecessor(V) to U.
     */
    protected abstract void setPredecessor(int v, int u);

    /**
     * Returns an estimated heuristic weight of the shortest path from vertex
     * V to the destination vertex (if any).  This is assumed to be less
     * than the actual weight, and is 0 by default.
     */
    protected double estimatedDistance(int v) {
        return 0.0;
    }

    /**
     * Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     * not in the graph, returns positive infinity.
     */
    protected abstract double getWeight(int u, int v);

    /**
     * Returns a list of vertices starting at _source and ending
     * at V that represents a shortest path to V.  Invalid if there is a
     * destination vertex other than V.
     */
    public List<Integer> pathTo(int v) {
        while (v != getSource()) {
            route.addFirst(v);
            v = getPredecessor(v);
        }
        route.addFirst(getSource());
        return route;
    }

    /**
     * Returns a list of vertices starting at the source and ending at the
     * destination vertex. Invalid if the destination is not specified.
     */
    public List<Integer> pathTo() {
        return pathTo(getDest());
    }

    /**
     * A start traversal.
     */
    class AStar extends Traversal {

        /**
         * The ASTAR Method Constructor.
         *
         * @param G      g
         * @param fringe fringe
         */
        protected AStar(Graph G, Queue<Integer> fringe) {
            super(G, fringe);
        }

        @Override
        protected boolean visit(int v) {
            if (v == ShortestPaths.this.getDest()) {
                return false;
            } else {
                for (Integer w : _G.successors(v)) {
                    double n = getWeight(v, w);
                    double distW = getWeight(w);
                    double distV = getWeight(v);
                    double newDist = n + distV;
                    if (distW > newDist) {
                        setWeight(w, newDist);
                        setPredecessor(w, v);
                    }
                }
                return true;
            }
        }
    }

    /**
     * A Priority Queue for a star.
     */
    class AStarPriorityQueue extends AbstractQueue<Integer> {

        /**
         * A Vertex for a star.
         */
        class Vertex {

            /**
             * A Vertex Initializer.
             *
             * @param vertex vertex
             * @param value  value
             */
            public Vertex(int vertex, double value) {
                _vertex = vertex;
                _value = value;
            }

            /**
             * Return the current vertex.
             */
            public int getVertex() {
                return _vertex;
            }

            /**
             * Set the current vertex.
             *
             * @param vertex vertex
             */
            public void setVertex(int vertex) {
                this._vertex = vertex;
            }

            /**
             * Return the current value.
             */
            public double getValue() {
                return _value;
            }

            /**
             * Set for the current value.
             *
             * @param value value
             */
            public void setValue(double value) {
                this._value = value;
            }

            /**
             * Refers to the current vertex no.
             */
            private int _vertex;
            /**
             * Value of the vertex.
             */
            private double _value;
        }

        /**
         * A comparator implementation for vertex.
         */
        class VComparator implements Comparator<Vertex> {
            @Override
            public int compare(Vertex vert1, Vertex vert2) {
                if (vert1.getValue() < vert2.getValue()) {
                    return -1;
                } else if (vert1.getValue() == vert2.getValue()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }

        /**
         * A priority Queue for the Astar class.
         */
        public AStarPriorityQueue() {
            priorityqueue = new PriorityQueue<Vertex>(1, new VComparator());
            vertexindex = new ArrayList<Integer>();
        }

        @Override
        public boolean offer(Integer e) {
            vertexindex.add(e);
            return priorityqueue
                    .offer(new Vertex(e, getWeight(e) + estimatedDistance(e)));
        }

        @Override
        public Integer poll() {
            Vertex index = priorityqueue.poll();
            if (index == null) {
                return null;
            } else {
                vertexindex.remove(new Integer(index.getVertex()));
                return index.getVertex();
            }
        }

        @Override
        public Integer peek() {
            Vertex index = priorityqueue.peek();
            if (index == null) {
                return null;
            } else {
                return index.getVertex();
            }
        }

        @Override
        public Iterator<Integer> iterator() {
            return vertexindex.iterator();
        }

        @Override
        public int size() {
            return priorityqueue.size();
        }

        /**
         * A PQ for store vertex.
         */
        private PriorityQueue<Vertex> priorityqueue;
        /**
         * ArrayList of vertex number.
         */
        private ArrayList<Integer> vertexindex;
    }

    /**
     * The shortest paths in G from SOURCE.
     */
    public ShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /**
     * The graph being searched.
     */
    protected final Graph _G;
    /**
     * The starting vertex for the graph.
     */
    private final int _source;
    /**
     * The target vertex for the graph.
     */
    private final int _dest;
    /**
     * A Star traversal declaration.
     */
    private AStar astarroute;
    /**
     * A new route declaration.
     */
    private LinkedList<Integer> route;

}
