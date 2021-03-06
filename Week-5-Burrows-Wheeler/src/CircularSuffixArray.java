import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
	// unit testing of the methods (optional)
	public static void main(String[] args) {
		
	}
	
	public CircularSuffixArray(String s){
		this.s = s;
		len = s.length();
		index = new Integer[s.length()];
		for(int i = 0;i < s.length();i++)
			index[i] = i;
		calculate();
	}
	
	public int length(){
		return len;
	}
	
	public int index(int i){
		return index[i];
	}
	
	// Instance Variables
	private final String s;
	private final int len;
	private Integer[] index;
	
	// Private Methods
	private void calculate(){
		Arrays.sort(index, new Comparator<Integer>() {
			public int compare(Integer a,Integer b){
				if (a.equals(b)) {
					return 0;
				} else {
					for(int i = 0;i < len;i++)
						if (s.charAt((a + i) % len) != s.charAt((b + i) % len)) {
							return s.charAt((a + i) % len) - s.charAt((b + i) % len);
						}
								
					return 0;
				}
			}
		});
	}
}
