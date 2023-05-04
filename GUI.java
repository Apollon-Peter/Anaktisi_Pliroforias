package com.howtodoinjava.demo.lucene.file;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUI extends JFrame implements ActionListener {
    private JTextField searchField;
    private JComboBox<String> searchDropdown;
    private JButton searchButton;
    private JButton searchHistoryButton;
    private JTextArea searchResultsArea;
    private String searchHistory;

    public GUI() {
        // Set up the JFrame
        super("Dropdown Search Bar Demo");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the search bar JPanel
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout());
        add(searchBarPanel, BorderLayout.NORTH);

        // Add the search dropdown
        String[] searchOptions = {"No filter", "Artist", "Title", "Album", "Year", "Date", "Lyric"};
        searchDropdown = new JComboBox<String>(searchOptions);
        searchBarPanel.add(searchDropdown);

        // Add the search field
        searchField = new JTextField(15);
        searchBarPanel.add(searchField);

        // Add the search button
        searchButton = new JButton("Search");
        searchBarPanel.add(searchButton);
        searchButton.addActionListener(this);

        // Add the search history button
        searchHistoryButton = new JButton("Search History");
        searchBarPanel.add(searchHistoryButton);
        searchHistoryButton.addActionListener(this);

        // Add the search results area
        searchResultsArea = new JTextArea(10, 50);
        add(new JScrollPane(searchResultsArea), BorderLayout.CENTER);

        // Initialize the search history
        searchHistory = "";

        // Display the JFrame
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            String selectedOption = (String) searchDropdown.getSelectedItem();
            String searchText = searchField.getText();
            // Perform search operation based on selectedOption and searchText
            String searchResult = "";
            if (selectedOption.equals("No filter")) {
                searchResult = "You searched with no filter: " + searchText;
            } else {
                searchResult = "You searched for " + selectedOption + ": " + searchText;
            }
            searchResultsArea.setText(""); // clear the search results area
            searchResultsArea.append(searchResult + "\n");
            searchHistory += searchResult + "\n";
        } else if (e.getSource() == searchHistoryButton) {
            // Display the search history
            JOptionPane.showMessageDialog(this, "Search history:\n" + searchHistory);
        }
    }


    public static void main(String[] args) {
        GUI demo = new GUI();
    }
}

