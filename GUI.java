package com.howtodoinjava.demo.lucene.file;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import org.apache.commons.io.FileUtils;

public class GUI extends JFrame implements ActionListener {
    private JTextField searchField;
    private JComboBox<String> searchDropdown;
    private JButton nextResultsButton;
    private JButton searchButton;
    private JButton searchHistoryButton;
    private String searchHistory;
    private String selectedOption;
    private String searchText;
    private static ReadIndex Read;
    public JTextArea searchResultsArea;

    public GUI() throws Exception
    {
        // Set up the JFrame
        super("Songs: Search Engine");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the search bar JPanel
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.setLayout(new FlowLayout());
        add(searchBarPanel, BorderLayout.NORTH);

        // Add the search dropdown
        String[] searchOptions = {"No filter", "Artist", "Title", "Album", "Year", "Date", "Lyrics"};
        searchDropdown = new JComboBox<String>(searchOptions);
        searchBarPanel.add(searchDropdown);

        // Add the search field
        searchField = new JTextField(15);
        searchBarPanel.add(searchField);

        // Add the search button
        searchButton = new JButton("Search");
        searchBarPanel.add(searchButton);
        searchButton.addActionListener(this);

        // Add the next 10 results button
        nextResultsButton = new JButton("Next 10 results");
        searchBarPanel.add(nextResultsButton);
        nextResultsButton.addActionListener(this);
        nextResultsButton.setEnabled(false);
        
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

    public void actionPerformed(ActionEvent e)
    {
    	if (e.getSource() == searchButton) {
            selectedOption = (String) searchDropdown.getSelectedItem();
            searchText = searchField.getText();
            // Perform search operation based on selectedOption and searchText
            String searchResult = "";
            if (selectedOption.equals("No filter")) {
                searchResult = "You searched with no filter: ";
            } else {
                searchResult = "You searched for " + selectedOption + ": ";
            }
            searchResultsArea.setText(""); // clear the search results area 
            searchResultsArea.append(searchResult + "\n");
            try {
                Read.ReadIndex(selectedOption, searchText, searchResultsArea);
            }catch (Exception err) {
            	System.out.println("Error occured " + err.getMessage());
            }
            searchHistory += searchResult + searchText + "\n";
            nextResultsButton.setEnabled(true);
            
          //Highlight text
            Highlighter highlighter = searchResultsArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            String text = searchResultsArea.getText().toLowerCase();
            String searchTerm = searchText.toLowerCase();

            int pos = text.indexOf(searchTerm);
            int lineCount = searchResultsArea.getLineCount();
            int startLine = Math.min(2, lineCount); // start searching from line 2 or last line if there are fewer than 2 lines

            while(pos >= 0) {
                try {
					if (searchResultsArea.getLineOfOffset(pos) < startLine) {
					    pos = text.indexOf(searchTerm, pos + searchTerm.length());
					    continue; // skip highlighting if the search term is in the first 2 lines
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                int endpos = pos + searchTerm.length();
                try {
                    highlighter.addHighlight(pos, endpos, painter);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                pos = text.indexOf(searchTerm, endpos);
            }

            
        } else if (e.getSource() == searchHistoryButton) {
            // Display the search history
            JOptionPane.showMessageDialog(this, "Search history:\n" + searchHistory);
        } else if (e.getSource() == nextResultsButton) {
            // Perform search operation based on selectedOption and searchText
            String searchResult = "";
            if (selectedOption.equals("No filter")) {
                searchResult = "You searched with no filter: ";
            } else {
                searchResult = "You searched for " + selectedOption + ": ";
            }
            searchResultsArea.setText(""); // clear the search results area
            searchResultsArea.append(searchResult + "\n");
            try {
                Read.nextResults(selectedOption, searchText, searchResultsArea);
            }catch (Exception err) {
            	System.out.println("Error occured " + err.getMessage());
            }
            
          //Highlight text
            Highlighter highlighter = searchResultsArea.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
            String text = searchResultsArea.getText().toLowerCase();
            String searchTerm = searchText.toLowerCase();

            int pos = text.indexOf(searchTerm);
            int lineCount = searchResultsArea.getLineCount();
            int startLine = Math.min(2, lineCount); // start searching from line 2 or last line if there are fewer than 2 lines

            while(pos >= 0) {
                try {
					if (searchResultsArea.getLineOfOffset(pos) < startLine) {
					    pos = text.indexOf(searchTerm, pos + searchTerm.length());
					    continue; // skip highlighting if the search term is in the first 2 lines
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                int endpos = pos + searchTerm.length();
                try {
                    highlighter.addHighlight(pos, endpos, painter);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                pos = text.indexOf(searchTerm, endpos);
            }
        }
    }
    
    public static void main(String[] args)
    {
    	try {
    		String filePath = "C:\\Users\\ggian\\Desktop\\LuceneDemo\\indexedFiles";

    		File file = new File(filePath);

    		FileUtils.deleteDirectory(file);

    		file.delete();
    		ReadIndex Reader = new ReadIndex();
    		Read = Reader;
    		WriteIndex Writer = new WriteIndex();
        	Writer.WriteIndex();
            GUI demo = new GUI();
    	}catch (Exception err) {
        	System.out.println("Error occured " + err.getMessage());
    	}
    }
}