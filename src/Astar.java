import java.util.*;

public class Astar extends Node {
    private int cost;
    private int evaluasi;

    public Astar(String word, String end, Astar parent) {
        super(word, parent);
        if (parent != null){
            this.cost = parent.cost + 1;
        } else {
            this.cost = 0;
        }
        this.evaluasi = getHeuristic(word, end) + cost;
    }

    public int getEvaluasi(){
        return this.evaluasi;
    }

    public int getHeuristic(String current, String end) {
        // Fungsi heuristik (jarak) antara kata saat ini dan kata akhir
        int count = 0;
        for (int i = 0; i < end.length(); i++) {
            if (end.charAt(i) != current.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public List<Astar> getChildren(Set<String> dictionary, String end, Set<String> visited){
        char [] chars = this.getWord().toCharArray();
        List<Astar> children = new ArrayList<>();
        for (int i = 0; i < chars.length; i++){
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++){
                if (c == originalChar) {
                    continue;
                } 

                chars[i] = c;
                String newWord = new String(chars);
                if (!visited.contains(newWord) && dictionary.contains(newWord)) {
                    Astar newAstar = new Astar(newWord, end, this);
                    children.add(newAstar);
                    visited.add(newWord);
                }
            }
            chars[i] = originalChar;
        }
        return children;
    }

    public Triple<List<String>, Integer, Integer> findAstar (String endWord, Set<String> dictionary){
        // inisiasi PriorityQueue untuk menyimpan node berdasarkan cost terurut menaik
        // inisiasi set untuk menyimpan node yang pernah terkunjungi
        Queue<Astar> queue = new PriorityQueue<Astar>(Comparator.comparingInt(Astar::getEvaluasi).thenComparingInt(System::identityHashCode));
        Set<String> visited = new HashSet<>();
        int iterations = 0;

        // simpan startWord atau this ke dalam queue dan visited
        queue.offer(this);
        visited.add(this.getWord());

        while (!queue.isEmpty()){
            iterations++;
            Astar current = queue.poll();
            if (current.getWord().equals(endWord)){
                return new Triple<>(current.getPath(), visited.size(), iterations);
            }

            List<Astar> children = current.getChildren(dictionary, endWord, visited);
            queue.addAll(children);
        }

        return new Triple<>(null, visited.size(), iterations);
    }
}
