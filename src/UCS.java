import java.util.*;

public class UCS extends Node {
    private int cost;

    public UCS(String word, UCS parent) {
        super(word, parent);
        if (parent != null){
            this.cost = parent.cost + 1;
        } else {
            this.cost = 0;
        }
    }

    public int getCost(){
        return this.cost;
    }

    public List<UCS> getChildren(Set<String> dictionary, Set<String> visited) {
        char[] chars = this.getWord().toCharArray();
        List<UCS> children = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) {
                    continue;
                }
                chars[i] = c;
                String newWord = new String(chars);
                if (!visited.contains(newWord) && dictionary.contains(newWord)) {
                    UCS newUCS = new UCS(newWord, this);
                    children.add(newUCS);
                    visited.add(newWord);
                }
            }
            chars[i] = originalChar;
        }
        return children;
    }

    public Triple<List<String>, Integer, Integer> findUCS(String endWord, Set<String> dictionary) {
        Queue<UCS> queue = new PriorityQueue<>(Comparator.comparingInt(UCS::getCost).thenComparingInt(System::identityHashCode));
        Set<String> visited = new HashSet<>();
        int iterations = 0;

        queue.offer(this);
        visited.add(this.getWord());

        while (!queue.isEmpty()) {
            iterations++;
            UCS current = queue.poll();
            if (current.getWord().equals(endWord)) {
                return new Triple<>(current.getPath(), visited.size(), iterations);
            }
            List<UCS> children = current.getChildren(dictionary, visited);
            queue.addAll(children);
        }
        return new Triple<>(null, visited.size(), iterations);
    }
}
