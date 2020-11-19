import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		try {
			// Read in number of teams
			int N = in.readInt();
			
			// Initialize private variables
			teamToId = new ST<String, Integer>();
			wins = new int[N];
			loss = new int[N];
			left = new int[N];
			gamesLeft = new int[N][N];
			maxWins = Integer.MIN_VALUE;
			
			// Populate data
			for (int id = 0; id < N; id++) {
				String team = in.readString();
				teamToId.put(team, id);
				wins[id] = in.readInt();
				loss[id] = in.readInt();
				left[id] = in.readInt();
				
				for (int j = 0; j < N; j++) {
					gamesLeft[id][j] = in.readInt();
				}
				
				if (wins[id] > maxWins) {
					maxWins = wins[id];
					leader = team;
				}
			}
		} finally {
			in.close();
		}
	}
	
	// number of teams
	public int numberOfTeams() {
		return teamToId.size();
	}
	
	// all teams
	public Iterable<String> teams() {
		return teamToId.keys();
	}
	
	// number of wins for given team
	public int wins(String team) {
		validateTeam(team);
		return wins[teamToId.get(team)];
	}
	
	// number of losses for given team
	public int losses(String team) {
		validateTeam(team);
		return loss[teamToId.get(team)];
	}
	
	// number of remaining games for given team
	public int remaining(String team) {
		validateTeam(team);
		return left[teamToId.get(team)];
	}
	
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		validateTeam(team1);
		validateTeam(team2);
		
		return gamesLeft[teamToId.get(team1)][teamToId.get(team2)];
	}
	
	// is given team eliminated?
	public boolean isEliminated(String team) {
		validateTeam(team);
		int id = teamToId.get(team);
		
		if (isTriviallyEliminated(id)) {
			return true;
		}
		
		Graph g = buildGraphFor(id);
		for (FlowEdge e : g.network.adj(g.source)) {
			if (e.flow() < e.capacity()) {
				return true;
			}
		}
		
		return false;
	}
	
	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		validateTeam(team);
		
		int id = teamToId.get(team);
		SET<String> set = new SET<String>();
		if (isTriviallyEliminated(id)) {
			set.add(leader);
			return set;
		}
		
		Graph g = buildGraphFor(id);
		for (FlowEdge e : g.network.adj(g.source)) {
			if (e.flow() < e.capacity()) {
				for (String t : teams()) {
					int tid = teamToId.get(t);
					if (g.ff.inCut(tid)) {
						set.add(t);
					}
				}
			}
		}
		
		if (set.isEmpty()) return null;
		return set;
	}
	
	// Instance Variables
	private ST<String, Integer> teamToId;
	private int[] wins;
	private int[] loss;
	private int[] left;
	private int[][] gamesLeft;
	private int maxWins;
	private String leader;
	
	// Private Class
	private class Graph {
		FlowNetwork network;
		FordFulkerson ff;
		int source;
		
		public Graph(FlowNetwork network, FordFulkerson ff, int source) {
			this.network = network;
			this.ff = ff;
			this.source = source;
		}
	}
	
	// Private Methods
	private void validateTeam(String team) {
		if (!teamToId.contains(team)) {
			throw new IllegalArgumentException("Team " + team + " does not exist");
		}
	}
	
	private boolean isTriviallyEliminated(int id) {		
		for (int i = 0; i < numberOfTeams(); i++) {
			if (wins[id] + left[id] < wins[i]) {
				return true;
			}
		}
		
		return false;
	}
		
	private Graph buildGraphFor(int id) {
		int n = numberOfTeams();
		int matchNodeCount = (n - 1) * (n - 2) / 2;
		int matchNode = n;
		int source = n + matchNodeCount;
		int target = n + matchNodeCount + 1;
		FlowNetwork network = new FlowNetwork(n + matchNodeCount + 2); // + 2 for source and target
		
		for (int i = 0; i < n; i++) {
			if (i != id) {
				// add edges from team nodes to target
				int capacity = wins[id] + left[id] - wins[i];
				network.addEdge(new FlowEdge(i, target, capacity));
				
				// add edges from source to matchNodes and matchNodes to teamNodes
				for (int j = i + 1; j < n; j++) {
					if (j != id) {
						network.addEdge(new FlowEdge(source, matchNode, gamesLeft[i][j]));
						network.addEdge(new FlowEdge(matchNode, i, Double.POSITIVE_INFINITY));
						network.addEdge(new FlowEdge(matchNode, j, Double.POSITIVE_INFINITY));
						matchNode++;
					}
				}
			}
		}
		
		FordFulkerson ff = new FordFulkerson(network, source, target);
		return new Graph(network, ff, source);
	}
}
