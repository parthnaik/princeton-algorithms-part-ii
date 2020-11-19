import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
	public static void main(String[] args)
	{
	    In in = new In(args[0]);
	    String[] dictionary = in.readAllStrings();
	    BoggleSolver solver = new BoggleSolver(dictionary);
	    BoggleBoard board = new BoggleBoard(args[1]);
	    int score = 0;
	    for (String word : solver.getAllValidWords(board))
	    {
	        StdOut.println(word);
	        score += solver.scoreOf(word);
	    }
	    StdOut.println("Score = " + score);
	}
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
    	// Populate dictionary
    	dict = new Trie();
    	for (String word : dictionary) {
    			dict.add(word);
    	}
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
    	SET<String> wordsFound = new SET<String>();
    	findAllWordsOnBoard(board, dict, wordsFound);
    	
    	return wordsFound;
    }
    

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
    	if (!dict.containsWord(word)) return 0;
    	int length = word.length();
    	
    	if (length <= 2) {
    		return 0;
    	} else if (length <= 4) {
    		return 1;
    	} else if (length <= 5) {
    		return 2;
    	} else if (length <= 6) {
    		return 3;
    	} else if (length <= 7) {
    		return 5;
    	} else {
    		return 11;
    	}
    }
    
    // Instance Variables
    private Trie dict;
    private static final int MIN_WORD_SIZE = 3;
    private static final int NUM_TILE_DIRECTIONS = 8;
    
    // Private Methods
    // Find all words on board
    private void findAllWordsOnBoard(BoggleBoard board, Trie dictionary, SET<String> wordsFound) {
    	boolean[][] usedTiles = new boolean[board.rows()][board.cols()];
    	
    	// find words starting from every tile in board
    	for (int i = 0; i < board.rows(); i++) {
    		for (int j = 0; j < board.cols(); j++) {
    			recFindAllWordsOnBoard("", i, j, board, usedTiles, dictionary, wordsFound);
    		}
    	}
    }
    
    // Recursive function to find all words on board from partiular tile
    private void recFindAllWordsOnBoard(String partial, int row, int col, BoggleBoard board, boolean[][] usedTiles, Trie dictionary, SET<String> wordsFound) {
    	// Base Case 0 (if row or col are out of board):
    	if (!inBounds(row, col, board)) return;
    	
    	// Base Case 1 (if tile was already used previously):
    	if (usedTiles[row][col]) return;
    	
    	// Go along this path to explore words
    	char letter = board.getLetter(row, col);
    	
    	if (letter == 'Q') {
    		partial += "QU";
    	} else {
    		partial += letter;
    	}
    	
    	// If the dictionary does not contain partial prefix
//    	if (!dictionary.keysWithPrefix(partial).iterator().hasNext()) return;
    	if (!dictionary.containsPrefix(partial)) return;
    	
    	// Continue exploring
    	usedTiles[row][col] = true;
    	
    	if (dictionary.containsWord(partial) && !wordsFound.contains(partial) && partial.length() >= MIN_WORD_SIZE) {
    		wordsFound.add(partial);
    	}
    	
    	// Explore all 8 directions from current tile
    	int adjRow = 0;
    	int adjCol = 0;
    	
    	for (int direction = 0; direction < NUM_TILE_DIRECTIONS; direction++) {
    		// Update adjRow and adjCol to all 8 possible directions
    		switch(direction) {
            // North West
            case 0:
                adjRow = row - 1;
                adjCol = col - 1;
                break;
                
            // North
            case 1:
                adjRow = row - 1;
                adjCol = col;
                break;
                
            // North East
            case 2:
                adjRow = row - 1;
                adjCol = col + 1;
                break;
            
            // East
            case 3:
                adjRow = row;
                adjCol = col + 1;
                break;
            
            // South East
            case 4:
                adjRow = row + 1;
                adjCol = col + 1;
                break;
                
            // South
            case 5:
                adjRow = row + 1;
                adjCol = col;
                break;
                
            // South West
            case 6:
                adjRow = row + 1;
                adjCol = col - 1;
                break;
                
            // West
            case 7:
                adjRow = row;
                adjCol = col - 1;
                break;
                
            default:
                break;
    		}
    		
    		recFindAllWordsOnBoard(partial, adjRow, adjCol, board, usedTiles, dictionary, wordsFound);
    	}
    	
    	// Backtrack step
    	partial = partial.substring(0, partial.length() - 1);
    	usedTiles[row][col] = false;
    }

    
    // Check if tile is in bounds
    private boolean inBounds(int row, int col, BoggleBoard board) {
    	int numRows = board.rows();
    	int numCols = board.cols();
    	
    	if (row < 0 || row > numRows - 1) return false;
    	if (col < 0 || col > numCols - 1) return false;
    	
    	return true;
    }
}
