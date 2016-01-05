import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {	
	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new NullPointerException();
		}
		
		pic = new Picture(picture);
	}
	
	// current picture
	public Picture picture() {
		return new Picture(pic);
	}
	
	// width of current picture
	public int width() {
		return pic.width();
	}
	
	// height of current picture
	public int height() {
		return pic.height();
	}
	
	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		validateIndices(x, y);
		
		if (isAtBorder(x, y)) {
			return BORDER_PIXEL_ENERGY;
		}
		
		return Math.sqrt(deltaSquareX(x, y) + deltaSquareY(x, y));
	}
	
	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		pic = transpose(pic);
		int[] seam = findVerticalSeam();
		pic = transpose(pic);
		
		return seam;
	}
	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		int[][] edgeTo = new int[height()][width()];
		double[][] distTo = new double[height()][width()];
		double[][] energy = new double[height()][width()];
		
		// Initialize arrays
		for (int row = 0; row < height(); row++) {
			for (int col = 0; col < width(); col++) {
				if (row == 0) {
					distTo[row][col] = 0;
				} else {
					distTo[row][col] = Double.POSITIVE_INFINITY;
				}
				
				energy[row][col] = energy(col, row);
				edgeTo[row][col] = -1;
			}
		}
		
		// Relax edges for each col in each row
		for (int row = 0; row < height() - 1; row++) {
			for (int col = 0; col < width(); col++) {				
				// bottom left
				if (col - 1 >= 0) {
					relax(row, col, row + 1, col - 1, edgeTo, distTo, energy);
				}
				
				// bottom
				relax(row, col, row + 1, col, edgeTo, distTo, energy);
				
				// bottom right
				if (col + 1 < width()) {
					relax(row, col, row + 1, col + 1, edgeTo, distTo, energy);
				}
			}
		}
		
		// Find seam with smallest distance
		double minDist = Double.POSITIVE_INFINITY;
		int seamCol = -1;
		
		for (int col = 0; col < width(); col++) {
			if (distTo[height() - 1][col] < minDist) {
				minDist = distTo[height() - 1][col];
				seamCol = col;
			}
		}
		
		return getVerticalSeam(seamCol, edgeTo);
	}
	
	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException();
		}
		
		if (seam.length != width()) {
			throw new IllegalArgumentException("Array needs to contain pixels equal to width.");
		}
		
		if (pic.height() <= 1) {
			throw new IllegalArgumentException("Picture has to have a height larger than 1 pixel.");
		}
		
		validateHorizontalSeam(seam);
		validateAdjacentVertices(seam);
			
        pic = transpose(pic);
		removeVerticalSeam(seam);
		pic = transpose(pic);
	}
	
	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
		if (seam == null) {
			throw new NullPointerException();
		}
		
		if (seam.length != height()) {
			throw new IllegalArgumentException("Array needs to contain pixels equal to height.");
		}
		
		if (pic.width() <= 1) {
			throw new IllegalArgumentException("Picture has to have a width larger than 1 pixel.");
		}
		
		validateVerticalSeam(seam);
		validateAdjacentVertices(seam);
		
		Picture original = pic;
		Picture carved = new Picture(original.width() - 1, original.height());
		
		for (int row = 0; row < carved.height(); row++) {
			for (int col = 0; col < seam[row]; col++) {
				carved.set(col, row, original.get(col, row));
			}
			
			for (int col = seam[row]; col < carved.width(); col++) {
				carved.set(col, row, original.get(col + 1, row));
			}
		}
		
		pic = carved;
	}
	
	// Instance Variables
	private static final double BORDER_PIXEL_ENERGY = 1000.0;
	private Picture pic;
	
	// Private Methods
	private void validateIndices(int x, int y) {
		if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	private boolean isAtBorder(int x, int y) {
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
			return true;
		} else {
			return false;
		}
	}
	
	private double deltaSquareX(int x, int y) {
		Color c1 = pic.get(x - 1, y);
		Color c2 = pic.get(x + 1, y);
		
		return 	Math.pow(c2.getRed() - c1.getRed(), 2) + 
				Math.pow(c2.getGreen() - c1.getGreen(), 2) + 
				Math.pow(c2.getBlue() - c1.getBlue(), 2);
	}
	
	private double deltaSquareY(int x, int y) {
		Color c1 = pic.get(x, y - 1);
		Color c2 = pic.get(x, y + 1);
		
		return 	Math.pow(c2.getRed() - c1.getRed(), 2) + 
				Math.pow(c2.getGreen() - c1.getGreen(), 2) + 
				Math.pow(c2.getBlue() - c1.getBlue(), 2);
	}
	
	private void relax(int fromRow, int fromCol, int toRow, int toCol, 
			int[][] edgeTo, double[][] distTo, double[][] energy) {
		double weight = energy[toRow][toCol];
		if (distTo[toRow][toCol] > distTo[fromRow][fromCol] + weight) {
			distTo[toRow][toCol] = distTo[fromRow][fromCol] + weight;
			edgeTo[toRow][toCol] = fromCol;
		}
	}
	
	// Traverse seam and return an array with edges
	private int[] getVerticalSeam(int seamCol, int[][] edgeTo) {
		if (seamCol < 0) {
			throw new IllegalArgumentException();
		}
		
		int[] verticalSeam = new int[height()];
		int currRow = height();
		verticalSeam[--currRow] = seamCol;
		
		while (currRow > 0) {
			int prevEdge = edgeTo[currRow][seamCol];
			verticalSeam[--currRow] = prevEdge;
			seamCol = prevEdge;
		}
		
		return verticalSeam;
	}
	
	// Transpose image
	private Picture transpose(Picture picture) 
	{
		Picture transpose = new Picture(picture.height(), picture.width());
		
        for (int row = 0; row < transpose.height(); row++) {
        	for (int col = 0; col < transpose.width(); col++) {
        		transpose.set(col, row, picture.get(row, col));
        	}
        }
        
        return transpose;
	}
	
	// Validate seams	
	private void validateHorizontalSeam(int[] seam) {
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] < 0 || seam[i] > height() - 1) {
				throw new IllegalArgumentException("Invalid seam at position " + i);
			}
		}
	}
	
	private void validateVerticalSeam(int[] seam) {
		for (int i = 0; i < seam.length; i++) {
			if (seam[i] < 0 || seam[i] > width() - 1) {
				throw new IllegalArgumentException("Invalid seam at position " + i);
			}
		}
	}
	
	private void validateAdjacentVertices(int[] seam) {
		for (int i = 1; i < seam.length; i++) {
			if (Math.abs(seam[i] - seam[i - 1]) > 1) {
				throw new IllegalArgumentException("Invalid seam at position " + i);
			}
		}
	}
}
