package graph;

import java.util.LinkedList;


/* See restrictions in Graph.java. */

/**
 * Represents a general unlabeled directed graph whose vertices are denoted by
 * positive integers. Graphs may have self edges.
 *
 * @author Shrey Malhotra
 */
public class DirectedGraph extends GraphObj {

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public int inDegree(int v) {
        int index = 0;
        for (int[] k : edges()) {
            if (k[1] == v) {
                index += 1;
            }
        }
        return index;
    }

    @Override
    public int predecessor(int v, int k) {
        for (int[] i : edges()) {
            if (k == 0 && i[1] == v) {
                return i[0];
            } else if (i[1] == v) {
                k -= 1;
            }
        }
        return 0;
    }

    @Override
    public Iteration<Integer> predecessors(int v) {
        LinkedList<Integer> predecessor = new LinkedList<Integer>();
        for (int[] k : edges()) {
            if (k[1] == v) {
                predecessor.add(k[0]);
            }
        }
        return Iteration.iteration(predecessor);
    }
}
