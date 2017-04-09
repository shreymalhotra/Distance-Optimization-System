package graph;

import org.junit.Test;
import static org.junit.Assert.*;
import static java.util.Arrays.asList;
import java.util.ArrayList;

public class TraversalTesting  {

    private class DfsObj extends DepthFirstTraversal {

        protected DfsObj(Graph G) {
            super(G);
        }

        @Override
        protected boolean visit(int v) {
            _visited.add(v);
            return super.visit(v);
        }

        @Override
        protected boolean postVisit(int v) {
            _postVisited.add(v);
            return super.postVisit(v);
        }

        public ArrayList<Integer> _visited = new ArrayList<Integer>();
        public ArrayList<Integer> _postVisited = new ArrayList<Integer>();
    }

    private class BfsObj extends BreadthFirstTraversal {

        protected BfsObj(Graph G) {
            super(G);
        }

        @Override
        protected boolean visit(int v) {
            _visited.add(v);
            return super.visit(v);
        }

        public ArrayList<Integer> _visited = new ArrayList<Integer>();
    }

    @Test
    public void dfsTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(5, 4);
        g.add(5, 3);
        g.add(4, 1);
        g.add(3, 2);
        g.add(1, 5);
        DfsObj o = new DfsObj(g);
        o.traverse(5);
        assertTrue(o._visited.equals(asList(5, 4, 1, 3, 2))
                    || o._visited.equals(asList(5, 3, 2, 4, 1)));
        assertTrue(o._postVisited.equals(asList(1, 4, 2, 3, 5))
                    || o._postVisited.equals(asList(2, 3, 1, 4, 5)));
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(5, 4);
        u.add(5, 3);
        u.add(4, 1);
        u.add(3, 2);
        u.add(1, 5);
        DfsObj a = new DfsObj(u);
        a.traverse(5);
        assertTrue(a._visited.equals(asList(5, 1, 4, 3, 2))
                    || a._visited.equals(asList(5, 4, 1, 3, 2))
                    || a._visited.equals(asList(5, 3, 2, 1, 4))
                    || a._visited.equals(asList(5, 3, 2, 4, 1)));
        assertTrue(a._postVisited.equals(asList(4, 1, 2, 3, 5))
                    || a._postVisited.equals(asList(1, 4, 2, 3, 5))
                    || a._postVisited.equals(asList(2, 3, 4, 1, 5))
                    || a._postVisited.equals(asList(2, 3, 1, 4, 5)));

    }

    @Test
    public void bfsTest() {
        DirectedGraph g = new DirectedGraph();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add();
        g.add(5, 4);
        g.add(5, 3);
        g.add(4, 1);
        g.add(3, 2);
        g.add(1, 5);
        BfsObj o = new BfsObj(g);
        o.traverse(5);
        assertTrue(o._visited.equals(asList(5, 4, 3, 1, 2))
                    || o._visited.equals(asList(5, 3, 4, 1, 2))
                    || o._visited.equals(asList(5, 4, 3, 2, 1))
                    || o._visited.equals(asList(5, 3, 4, 2, 1)));
        UndirectedGraph u = new UndirectedGraph();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add();
        u.add(5, 4);
        u.add(5, 3);
        u.add(4, 1);
        u.add(3, 2);
        u.add(1, 5);
        BfsObj b = new BfsObj(u);
        b.traverse(5);
        assertTrue(b._visited.equals(asList(5, 1, 4, 3, 2))
                    || b._visited.equals(asList(5, 1, 3, 4, 2))
                    || b._visited.equals(asList(5, 4, 1, 3, 2))
                    || b._visited.equals(asList(5, 4, 3, 1, 2))
                    || b._visited.equals(asList(5, 3, 4, 1, 2))
                    || b._visited.equals(asList(5, 3, 1, 4, 2)));


    }
}
