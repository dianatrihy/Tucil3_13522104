import java.util.*;

public class GBFS extends Node {
    private int heuristic;

    public GBFS(String word, String end, GBFS parent) {
        super(word, parent);
        this.heuristic = getHeuristic(word, end);
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

    public List<GBFS> getChildren(Set<String> dictionary, String end, Set<String> visited) {
        char[] chars = this.getWord().toCharArray();
        List<GBFS> children = new ArrayList<>();
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) {
                    continue;
                }

                chars[i] = c;
                String newWord = new String(chars);
                if (!visited.contains(newWord) && dictionary.contains(newWord)) {
                    GBFS newGBFS = new GBFS(newWord, end, this);
                    children.add(newGBFS);
                    visited.add(newWord);
                }
            }
            chars[i] = originalChar;
        }
        return children;
    }

    public Triple<List<String>, Integer, Integer> findGBFS(String endWord, Set<String> dictionary) {
        // inisiasi PriorityQueue untuk menyimpan node berdasarkan cost terurut menaik
        // inisiasi set untuk menyimpan node yang pernah terkunjungi
        Queue<GBFS> queue = new PriorityQueue<GBFS>(Comparator.comparingInt(GBFS -> GBFS.heuristic));
        Set<String> visited = new HashSet<>();
        int iterations = 0;

        // simpan startWord atau this ke dalam queue dan visited
        queue.offer(this);
        visited.add(this.getWord());

        while (!queue.isEmpty()) {
            iterations++;
            GBFS current = queue.poll();
            if (current.getWord().equals(endWord)) {
                return new Triple<>(current.getPath(), visited.size(), iterations);
            }

            List<GBFS> children = current.getChildren(dictionary, endWord, visited);
            queue.addAll(children);
        }

        return new Triple<>(null, visited.size(), iterations);
    }
}
