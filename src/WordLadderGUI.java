import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WordLadderGUI extends JFrame implements ActionListener {
    // atribut
    private JTextField startField, endField;
    private JTextArea resultArea;
    private JButton findButton;
    private JCheckBox ucsCheckBox, gbfsCheckBox, aStarCheckBox;

    // konstruktor
    public WordLadderGUI() {
        setTitle("Word Ladder Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        // bagian input kata
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(255, 192, 203));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 10, 10, 10);

        Font labelFont = new Font("Arial", Font.BOLD, 14); 

        JLabel labelKataAwal = new JLabel("Kata Awal:");
        labelKataAwal.setFont(labelFont);
        inputPanel.add(labelKataAwal, gbc);

        gbc.gridy++;

        JLabel labelKataAkhir = new JLabel("Kata Akhir:");
        labelKataAkhir.setFont(labelFont);
        inputPanel.add(labelKataAkhir, gbc);

        gbc.gridy++;

        // bagian pilih algoritma
        JLabel labelAlgoritma = new JLabel("Pilih algoritma yang akan dijalankan:");
        labelAlgoritma.setFont(labelFont);
        inputPanel.add(labelAlgoritma, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        startField = new JTextField();
        startField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputPanel.add(startField, gbc);
        startField.setBackground(new Color(255, 240, 245));
        gbc.gridy++;
        endField = new JTextField();
        endField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        inputPanel.add(endField, gbc);
        endField.setBackground(new Color(255, 240, 245));

        gbc.gridy++;
        ucsCheckBox = new JCheckBox("UCS");
        gbfsCheckBox = new JCheckBox("Greedy Best-First");
        aStarCheckBox = new JCheckBox("A*");
        ucsCheckBox.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbfsCheckBox.setFont(new Font("SansSerif", Font.BOLD, 14));
        aStarCheckBox.setFont(new Font("SansSerif", Font.BOLD, 14));
        ucsCheckBox.setBackground(new Color(255, 192, 203));
        gbfsCheckBox.setBackground(new Color(255, 192, 203));
        aStarCheckBox.setBackground(new Color(255, 192, 203));

        JPanel checkBoxPanel = new JPanel(new GridLayout(1, 3));
        checkBoxPanel.setBackground(new Color(255, 192, 203));
        checkBoxPanel.add(ucsCheckBox);
        checkBoxPanel.add(gbfsCheckBox);
        checkBoxPanel.add(aStarCheckBox);
        inputPanel.add(checkBoxPanel, gbc);

        // bagian button cari jalur
        JPanel buttonPanel = new JPanel();
        findButton = new JButton("Cari Jalur");
        findButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        findButton.addActionListener(this);
        buttonPanel.add(findButton);
        buttonPanel.setBackground(new Color(255, 192, 203));
        buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // bagian result area
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 192, 203)); 
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        
        resultArea = new JTextArea("Hasil pencarian akan muncul di sini.");
        resultArea.setEditable(false);
        resultArea.setBackground(new Color(255, 240, 245)); // Memberikan warna latar belakang (Light Blue)
        resultArea.setForeground(Color.BLACK); // Memberikan warna teks (Hitam)
        resultArea.setMargin(new Insets(10, 10, 10, 10)); // Menambahkan padding
        resultArea.setLineWrap(true); // Mengaktifkan pembungkus baris
        resultArea.setWrapStyleWord(true); 
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        mainPanel.add(resultArea, BorderLayout.LINE_END);
        add(mainPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == findButton) {
            String start = startField.getText().toLowerCase();
            String end = endField.getText().toLowerCase();
            
            // Memastikan panjang kedua kata sama
            if (start.length() != end.length()) {
                JOptionPane.showMessageDialog(this, "Kata awal dan kata akhir harus memiliki panjang yang sama.");
                return; 
            }

            Set<String> words = loadDictionary("dictionary.txt");

            // memastikan kata inputan tersedia di dictionary
            if (!words.contains(start)){
                JOptionPane.showMessageDialog(this, "Kata awal tidak terdaftar dalam kamus.");
                return; 
            }
            if (!words.contains(end)){
                JOptionPane.showMessageDialog(this, "Kata akhir tidak terdaftar dalam kamus.");
                return; 
            }

            resultArea.setText("");

            // pemanggilan method sesuai pilihan algoritma
            long startTime, endTime;
            if (ucsCheckBox.isSelected()) {
                resultArea.append("UCS:\n");
                startTime = System.nanoTime();
                UCS startUCS = new UCS(start, null);
                Triple<List<String>, Integer, Integer> ucsLadder = startUCS.findUCS(end, words);
                printLadder(ucsLadder.first);
                if (ucsLadder.first != null){
                    resultArea.append("Panjang path: " + (ucsLadder.first.size()-1) + "\n");
                }
                resultArea.append("Jumlah node dikunjungi (simpul ekspan): " + ucsLadder.third + "\n");
                resultArea.append("Jumlah simpul hidup: " + ucsLadder.second + "\n");
                endTime = System.nanoTime();
                resultArea.append("Waktu eksekusi UCS: " + ((endTime - startTime) / 1000000) + " milidetik\n");
            }
            if (gbfsCheckBox.isSelected()) {
                resultArea.append("\nGreedy Best-First:\n");
                startTime = System.nanoTime();
                GBFS startGBFS = new GBFS(start, end, null);
                Triple<List<String>, Integer, Integer> gbfsLadder = startGBFS.findGBFS(end, words);
                printLadder(gbfsLadder.first);
                if (gbfsLadder.first != null){
                    resultArea.append("Panjang path: " + (gbfsLadder.first.size()-1) + "\n");
                }
                    resultArea.append("Jumlah node dikunjungi (simpul ekspan): " + gbfsLadder.third + "\n");
                resultArea.append("Jumlah simpul hidup: " + gbfsLadder.second + "\n");
                endTime = System.nanoTime();
                resultArea.append("Waktu eksekusi GBFS: " + ((endTime - startTime) / 1000000) + " milidetik\n");
            }
            if (aStarCheckBox.isSelected()) {
                resultArea.append("\nA*:\n");
                startTime = System.nanoTime();
                Astar startAstar = new Astar(start, end, null);
                Triple<List<String>, Integer, Integer> AstarLadder = startAstar.findAstar(end, words);
                printLadder(AstarLadder.first);
                if (AstarLadder.first != null){
                    resultArea.append("Panjang path: " + (AstarLadder.first.size()-1) + "\n");
                }
                resultArea.append("Jumlah node dikunjungi (simpul ekspan): " + AstarLadder.third + "\n");
                resultArea.append("Jumlah simpul hidup: " + AstarLadder.second + "\n");
                endTime = System.nanoTime();
                resultArea.append("Waktu eksekusi A*: " + ((endTime - startTime) / 1000000) + " milidetik\n");
            }
            if (!ucsCheckBox.isSelected() && !gbfsCheckBox.isSelected() && !aStarCheckBox.isSelected()){
                resultArea.append("Tidak ada algoritma pencarian yang dipilih.");
            }
        }
    }
    
    // method untuk mengubah file txt dictionary menjadi HashSet
    public static Set<String> loadDictionary(String filename) {
        Set<String> dictionary = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                dictionary.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

    // method untuk mencetak hasil path
    public void printLadder(List<String> ladder) {
        if (ladder == null) {
            resultArea.append("Tidak ada jalur yang ditemukan.\n");
        } else {
            resultArea.append("Jalur ditemukan:\n");
            for (String word : ladder) {
                resultArea.append("   [" + word + "]\n");
            }
        }
    }

    // main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WordLadderGUI().setVisible(true);
        });
    }
}
