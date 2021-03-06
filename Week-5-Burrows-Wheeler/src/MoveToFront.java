import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;

public class MoveToFront {
	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
	}
	
	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		LinkedList<Integer> ascii = ascii();
		
		while (!BinaryStdIn.isEmpty()) {
			char ch = BinaryStdIn.readChar();
			int idx = ascii.indexOf((int) ch);
			BinaryStdOut.write((char) idx);
			int front = ascii.remove(idx);
			ascii.add(0, front);
		}
		
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		LinkedList<Integer> ascii = ascii();
		
		while (!BinaryStdIn.isEmpty()) {
			char ch = BinaryStdIn.readChar();
			int front = ascii.remove((int) ch);
			BinaryStdOut.write((char) front);
			ascii.add(0, front);
		}
		
		BinaryStdOut.close();
	}
	
	// Instance Variables
	private static final int R = 256;
	
	// Private Methods
	private static LinkedList<Integer> ascii() {
		LinkedList<Integer> ascii = new LinkedList<Integer>();
		
		for (int i = 0; i < R; i++) {
			ascii.add(i);
		}
		
		return ascii;
	}
}
