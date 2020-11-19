public class Outcast {
	// see test client below
	public static void main(String[] args) {
		
	}
	
	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		wordNet = wordnet;
	}
	
	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		String outcast = null;
		int max = 0;
		
		for (String n1 : nouns) {
			int dist = 0;
			for (String n2: nouns) {
				dist += wordNet.distance(n1, n2);
			}
			
			if (dist > max) {
				max = dist;
				outcast = n1;
			}
		}
		
		return outcast;
	}
	
	// Instance Variables
	private WordNet wordNet;
}
