import java.util.*;

public class UCS extends Node {
    // atribut
    private int cost;

    // konstruktor
    public UCS(String word, UCS parent) {
        super(word, parent);
        if (parent != null){
            this.cost = parent.cost + 1;
        } else {
            this.cost = 0;
        }
    }

    // getter method
    public int getCost(){
        return this.cost;
    }

    public List<UCS> getChildren(Set<String> dictionary, Set<String> visited) {
        // inisialisasi
        char[] chars = this.getWord().toCharArray();
        List<UCS> children = new ArrayList<>();

        // iterasi untuk setiap pengubahan huruf
        for (int i = 0; i < chars.length; i++) {
            char originalChar = chars[i];
            for (char c = 'a'; c <= 'z'; c++) {
                if (c == originalChar) {
                    continue;
                }
                chars[i] = c;
                String newWord = new String(chars);
                // cek apakah valid dalam dictionary dan belum tercatat di visited
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

    // main method
    public Triple<List<String>, Integer, Integer> findUCS(String endWord, Set<String> dictionary) {
        // inisialisasi antrian node (queue), node terkunjungi (visited), jumlah iterasi
        Queue<UCS> queue = new PriorityQueue<>(Comparator.comparingInt(UCS::getCost).thenComparingInt(System::identityHashCode));
        Set<String> visited = new HashSet<>();
        int iterations = 0;

        // masukan node ke dalam queue dan visited
        queue.offer(this);
        visited.add(this.getWord());

        // looping hingga queue kosong atau node tujuan ditemukan
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
        // output: path, jumlah simpul hidup, jumlah simpul ekspans (iterasi)
    }
}
