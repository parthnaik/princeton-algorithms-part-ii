import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
	// do unit testing of this class
	public static void main(String[] args) {
		
	}
	
	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		digraph = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
		
		int shortestPathLength = -1;
		
		for (int i = 0; i < digraph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				
				if (shortestPathLength < 0 || dist < shortestPathLength) {
					shortestPathLength = dist;
				}
			}
		}
		
		return shortestPathLength;
	}

	// a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
	public int ancestor(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(digraph, w);
		
		int shortestPathLength = Integer.MAX_VALUE;
		int ancestor = -1;
		
		for (int i = 0; i < digraph.V(); i++) {
			if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
				int dist = bfsV.distTo(i) + bfsW.distTo(i);
				
				if (dist < shortestPathLength) {
					shortestPathLength = dist;
					ancestor = i;
				}
			}
		}
		
		return ancestor;
	}

	// length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new NullPointerException();
		}
		
		validateVertices(v);
		validateVertices(w);
		
		BreadthFirstDirectedPaths d1 = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths d2 = new BreadthFirstDirectedPaths(digraph, w);
		
		int shortestPathLength = -1;
		
		for (int i = 0; i < digraph.V(); i++) {
			if (d1.hasPathTo(i) && d2.hasPathTo(i)) {
				int dist = d1.distTo(i) + d2.distTo(i);
				
				if (shortestPathLength < 0 || dist < shortestPathLength) {
					shortestPathLength = dist;
				}
			}
		}
		
		return shortestPathLength;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) {
			throw new NullPointerException();
		}
		
		validateVertices(v);
		validateVertices(w);
		
		BreadthFirstDirectedPaths d1 = new BreadthFirstDirectedPaths(digraph, v);
		BreadthFirstDirectedPaths d2 = new BreadthFirstDirectedPaths(digraph, w);
		
		int shortestPathLength = Integer.MAX_VALUE;
		int ancestor = -1;
		
		for (int i = 0; i < digraph.V(); i++) {
			if (d1.hasPathTo(i) && d2.hasPathTo(i)) {
				int dist = d1.distTo(i) + d2.distTo(i);
				
				if (dist < shortestPathLength) {
					shortestPathLength = dist;
					ancestor = i;
				}
			}
		}
		
		return ancestor;
	}

	// Instance Variables
	private Digraph digraph;
	
	// Private Methods
	private void validateVertex(int v) {
		if (v < 0 || v > digraph.V() - 1) {
			throw new IndexOutOfBoundsException("Invalid vertex.");
		}
	}
	
	private void validateVertices(Iterable<Integer> vertices) {
		for (int v : vertices) {
			validateVertex(v);
		}
	}
}
