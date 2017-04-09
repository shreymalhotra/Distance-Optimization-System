package graph;

import java.util.TreeSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

/* See restrictions in Graph.java. */

/**
 * A partial implementation of Graph containing elements common to
 * directed and undirected graphs.
 *
 * @author Shrey Malhotra
 */
abstract class GraphObj extends Graph {

    /**
     * An adjacency list for graph.
     */
    private LinkedList<List<Integer>> graphList;
    /**
     * A list storing graph edges.
     */
    private LinkedList<List<Integer>> graphedges;

    /**
     * A new, empty Graph.
     */
    GraphObj() {
        graphList = new LinkedList<List<Integer>>();
        graphedges = new LinkedList<List<Integer>>();
    }

    @Override
    public int vertexSize() {
        int index = 0;
        for (int k = 0; k < graphList.size(); k++) {
            if (graphList.get(k) != null) {
                index++;
            }
        }
        return index;
    }

    @Override
    public int maxVertex() {
        for (int k = graphList.size(); k > 0; k--) {
            if (graphList.get(k - 1) != null) {
                return k;
            }
        }
        return 0;
    }

    @Override
    public int edgeSize() {
        int size = 0;
        for (int k = 0; k < graphedges.size(); k++) {
            if (graphedges.get(k) != null) {
                size++;
            }
        }
        return size;
    }

    @Override
    public abstract boolean isDirected();

    @Override
    public int outDegree(int v) {
        if (contains(v)) {
            if (graphList.get(v - 1) == null) {
                return 0;
            } else {
                return graphList.get(v - 1).size();
            }
        }
        return 0;
    }

    @Override
    public abstract int inDegree(int v);

    @Override
    public boolean contains(int u) {
        return containcheck(u);
    }

    @Override
    public boolean contains(int u, int v) {
        if (contains(u) && contains(v)) {
            if (isDirected()) {
                return graphList.get(u - 1).contains(v);
            } else {
                return graphList.get(v - 1).contains(u)
                        || graphList.get(u - 1).contains(v);
            }
        }
        return false;
    }

    @Override
    public int add() {
        int index;
        if (tempvertexstore.isEmpty()) {
            graphList.add(new LinkedList<Integer>());
            index = graphList.size() - 1;
        } else {
            index = tempvertexstore.pollFirst();
            graphList.set(index, new LinkedList<Integer>());
        }
        return index + 1;
    }

    @Override
    public int add(int u, int v) {
        if (containcheck(u) && containcheck(v) && !contains(u, v)) {
            if (isDirected()) {
                graphList.get(u - 1).add(v);
                addhelper(u, v);
            } else {
                graphList.get(u - 1).add(v);
                if (u != v) {
                    graphList.get(v - 1).add(u);
                }
                addhelper(u, v);
            }
        }
        return edgeId(u, v);
    }

    /**
     * A helper method to add edge to the given graph.
     * @param u u
     * @param v v
     */
    private void addhelper(int u, int v) {
        if (tempIDstore.isEmpty()) {
            graphedges.add(asList(u, v));
        } else {
            int index = tempIDstore.pollFirst();
            graphedges.set(index, asList(u, v));
        }
    }

    @Override
    public void remove(int v) {
        if (contains(v)) {
            if (graphList.get(v - 1) == null) {
                return;
            }
            tempvertexstore.add(v - 1);
            for (int k = 0; k < graphList.size(); k++) {
                if (graphList.get(k) != null) {
                    if (graphList.get(k).contains(v)) {
                        graphList.get(k).remove(new Integer(v));
                        removehelper(k + 1, v);
                    }
                }
            }
            for (int successor : graphList.get(v - 1)) {
                removehelper(v, successor);
            }
            graphList.set(v - 1, null);
        }
    }

    /**
     * A helper method to remove edge from graph edges.
     * @param u u
     * @param v v
     */
    private void removehelper(int u, int v) {
        int ind = graphedges.indexOf(asList(u, v));
        if (ind != -1) {
            graphedges.set(ind, null);
            tempIDstore.add(ind);
        }
    }

    @Override
    public void remove(int u, int v) {
        if (!isDirected()) {
            graphList.get(v - 1).remove(new Integer(u));
            graphList.get(u - 1).remove(new Integer(v));
            removehelper(u, v);
        } else {
            graphList.get(u - 1).remove(new Integer(v));
            removehelper(u, v);
        }
    }

    @Override
    public Iteration<Integer> vertices() {
        LinkedList<Integer> verticeStore = new LinkedList<Integer>();
        for (int k = 0; k < graphList.size(); k++) {
            if (graphList.get(k) != null) {
                verticeStore.add(k + 1);
            }
        }
        return Iteration.iteration(verticeStore);
    }

    @Override
    public int successor(int v, int k) {
        if (contains(v)) {
            for (int i : graphList.get(v - 1)) {
                if (k == 0) {
                    return i;
                } else {
                    k--;
                }
            }
        }
        return 0;
    }

    @Override
    public abstract int predecessor(int v, int k);

    @Override
    public Iteration<Integer> successors(int v) {
        if (graphList.get(v - 1) == null) {
            return Iteration.iteration(new LinkedList<Integer>());
        }
        return Iteration.iteration(graphList.get(v - 1));
    }

    @Override
    public abstract Iteration<Integer> predecessors(int v);

    @Override
    public Iteration<int[]> edges() {
        ArrayList<int[]> edgestore = new ArrayList<int[]>();
        for (int k = 0; k < graphedges.size(); k++) {
            if (graphedges.get(k) == null) {
                continue;
            }
            edgestore.add(new int[]{graphedges.get(k).get(0),
                    graphedges.get(k).get(1)});
        }
        return Iteration.iteration(edgestore);
    }

    @Override
    protected boolean containcheck(int v) {
        return (v <= graphList.size()) && (graphList.get(v - 1) != null);
    }

    @Override
    protected void checkMyVertex(int v) {
        if (!containcheck(v)) {
            throw new Error("the vertex is not in the graph");
        }
    }

    @Override
    protected int edgeId(int u, int v) {
        if (contains(u, v)) {

            if (isDirected()) {
                return cantar(u, v);
            }
            return cantar(u + v, u * v);
        }
        return 0;
    }

    /**
     * Uses cantar function.
     *
     * @param x x
     * @param y y
     * @return the result from the cantar.
     */
    private int cantar(int x, int y) {
        return (x + y) * (x + y) + (3 * x) + y;
    }

    /**
     * ONLY FOR CHECKING Temprarily store the edge IDs which has been
     * removed.
     */
    private TreeSet<Integer> tempIDstore = new TreeSet<Integer>();
    /**
     * ONLY FOR CHECKING Temprarily store the vertices index which has
     * been removed.
     */
    private TreeSet<Integer> tempvertexstore = new TreeSet<Integer>();
}
