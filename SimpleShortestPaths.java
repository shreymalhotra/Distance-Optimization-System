package graph;

/* See restrictions in Graph.java. */

/**
 * A partial implementation of ShortestPaths that contains the weights of
 * the vertices and the predecessor edges.   The client needs to
 * supply only the two-argument getWeight method.
 *
 * @author Shrey Malhotra
 */
public abstract class SimpleShortestPaths extends ShortestPaths {

    /**
     * The shortest paths in G from SOURCE.
     */
    public SimpleShortestPaths(Graph G, int source) {
        this(G, source, 0);
    }

    /**
     * A shortest path in G from SOURCE to DEST.
     */
    public SimpleShortestPaths(Graph G, int source, int dest) {
        super(G, source, dest);
    }

    /**
     * Returns the current weight of edge (U, V) in the graph.  If (U, V) is
     * not in the graph, returns positive infinity.
     */
    @Override
    protected abstract double getWeight(int u, int v);

    @Override
    public double getWeight(int v) {
        if (weights.get(v) == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return weights.get(v);
        }
    }

    @Override
    protected void setWeight(int v, double w) {
        weights.put(v, w);
    }

    @Override
    public int getPredecessor(int v) {
        if (preds.get(v) == null) {
            return 0;
        } else {
            return preds.get(v);
        }
    }

    @Override
    protected void setPredecessor(int v, int u) {
        preds.put(v, u);
    }

    /**
     * ONLY FOR TESTING Stores the graph predecessors.
     */
    private java.util.HashMap<Integer, Integer> preds
            = new java.util.HashMap<Integer, Integer>();

    /**
     * ONLY FOR TESTING Stores the graph weight.
     */
    private java.util.HashMap<Integer, Double> weights
            = new java.util.HashMap<Integer, Double>();

}
