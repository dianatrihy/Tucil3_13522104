import java.util.*;

public class Node {
    // atribut
    private String word;
    private Node parent;

    // konstruktor
    public Node() {
        this.word = null;
        this.parent = null;
    }

    public Node(String word, Node parent) {
        this.word = word;
        this.parent = parent;
    } 

    // getter method
    public String getWord(){
        return this.word;
    }

    public Node getParent(){
        return this.parent;
    }

    public List<String> getPath(){
        List<String> path = new ArrayList<>();
        Node current = this;
        while (current != null) {
            path.add(current.getWord());
            current = current.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
