import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
	
	// do unit testing of this class
	public static void main(String[] args) {
		
	}
		
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) {
			throw new NullPointerException();
		}
		
		idToSynset = new ST<Integer, String>();
		nounToIds = new ST<String, Bag<Integer>>();
		
		readSynsets(synsets);
		readHypernyms(hypernyms);
		
		validateCycle(digraph);
		validateRoot(digraph);
		
		sap = new SAP(digraph);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return nounToIds;
	}
	
	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return nounToIds.contains(word);
	}

	// distance between nounA and nounB 
	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException("Invalid nouns.");
		}
		
		return sap.length(nounToIds.get(nounA), nounToIds.get(nounB));
	}

	// a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
	// in a shortest ancestral path 
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA) || !isNoun(nounB)) {
			throw new IllegalArgumentException("Invalid nouns.");
		}
		
		int ancestorId =  sap.ancestor(nounToIds.get(nounA), nounToIds.get(nounB));
		return idToSynset.get(ancestorId);
	}
	
	// Instance Variables
	private ST<Integer, String> idToSynset;
	private ST<String, Bag<Integer>> nounToIds;
	private Digraph digraph;
	private SAP sap;
	
	// Private Methods
	// read synsets from file and populate symbol tables
	private void readSynsets(String synsets) {
		In in = new In(synsets);
		Bag<Integer> bag;
		
		while (in.hasNextLine()) {
			String line = in.readLine();
			String[] args = line.split(",");
			
			int id = Integer.parseInt(args[0]);
			String nouns = args[1];
			idToSynset.put(id, nouns);
			
			for (String noun : args[1].split(" ")) {
				bag = nounToIds.get(noun);
				
				if (bag == null) {
					bag = new Bag<Integer>();
					bag.add(id);
					nounToIds.put(noun, bag);
				} else {
					bag.add(id);
				}
			}
		}
	}
	
	// read hypernyms from file and populate directed graph
	private void readHypernyms(String hypernyms) {
		digraph = new Digraph(idToSynset.size());
		In in = new In(hypernyms);
		
		while (in.hasNextLine()) {
			String line = in.readLine();
			String[] ids = line.split(",");
			
			int idV = Integer.parseInt(ids[0]);
			for (int i = 1; i < ids.length; i++) {
				Integer idW = Integer.parseInt(ids[i]);
				
				if (idW < 0 || idW > idToSynset.size()) {
					throw new IndexOutOfBoundsException();
				}
				
				digraph.addEdge(idV, idW);
			}
		}
	}
	
	// validates that directed graph does not have a cycle
	private void validateCycle(Digraph digraph) {
		DirectedCycle d = new DirectedCycle(digraph);
		
		if (d.hasCycle()) {
			throw new IllegalArgumentException("Graph has cycles.");
		}
	}
	
	// validates that directed graph has only 1 root
	private void validateRoot(Digraph digraph) {
		int roots = 0;
		
		for (int i = 0, n = digraph.V(); i < n; i++) {
			if (!digraph.adj(i).iterator().hasNext()) {
				roots++;
			}
		}
		
		if (roots != 1) {
			throw new IllegalArgumentException("The number of roots in the graph is not equal to 1.");
		}
	}
}
