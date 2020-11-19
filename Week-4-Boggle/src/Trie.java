public class Trie {
    // Initializes an empty trie
    public Trie() {
        root = new Node();
    }
    
    // Checks if the trie contains a given key
    public boolean containsWord(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        return x.isString;
    }

    // Checks if the trie contains a given prefix
    public boolean containsPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        return x != null;
    }
    
    // Adds key to string if not already present
    public void add(String key) {
        root = add(root, key, 0);
    }
    
    // Instance Variables
    private static final int R = 26; // for every letter of the alphabet
    private static final int SHIFT = 'A';
    private Node root; // root of trie
    
    // Private Classes
    private static class Node {
        private Node[] next = new Node[R];
        private boolean isString;
    }
    
    // Private Methods
    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - SHIFT], key, d + 1);
    }
    
    private Node add(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.isString = true;
        } else {
            char c = key.charAt(d);
            x.next[c - SHIFT] = add(x.next[c - SHIFT], key, d + 1);
        }
        
        return x;
    }
}
