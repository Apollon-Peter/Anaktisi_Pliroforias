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
    private String stemmedSearchText;
    private static ReadIndex Read;
    public JTextArea searchResultsArea;
    private JToggleButton sortButton;
    public boolean sort = false;
    public boolean sorted = false;

    public GUI() throws Exception
    {
        // Set up the JFrame
        super("Songs: Search Engine");
        setSize(1000, 650);
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
        
        //Add the sortByYear Button
        sortButton = new JToggleButton("Sort by Year");
        searchBarPanel.add(sortButton);
        sortButton.addActionListener(this);
        
        // Add the search results area
        searchResultsArea = new JTextArea(10, 50);
        add(new JScrollPane(searchResultsArea), BorderLayout.CENTER);

        // Initialize the search history
        searchHistory = "";

        // Display the JFrame
        setVisible(true);
    }
    
    public void Highlight() {
    	//Highlight text
        Highlighter highlighter = searchResultsArea.getHighlighter();
        Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
        String text = searchResultsArea.getText().toLowerCase();
        String search = searchText + " " + stemmedSearchText;
        String[] searchTerm = search.toLowerCase().split(" ");
        for (String i : searchTerm) {
        	int pos = text.indexOf(i);
            int lineCount = searchResultsArea.getLineCount();
            int startLine = Math.min(2, lineCount); // start searching from line 2 or last line if there are fewer than 2 lines

            while(pos >= 0) {
                try {
					if (searchResultsArea.getLineOfOffset(pos) < startLine) {
					    pos = text.indexOf(i, pos + i.length());
					    continue; // skip highlighting if the search term is in the first 2 lines
					}
				} catch (BadLocationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                int endpos = pos + i.length();
                try {
                    highlighter.addHighlight(pos, endpos, painter);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
                pos = text.indexOf(i, endpos);
            }
        }
    }

    public void actionPerformed(ActionEvent e)
    {
    	if (e.getSource() == searchButton) {
    		sorted = sort;
            selectedOption = (String) searchDropdown.getSelectedItem();
            searchText = searchField.getText();
            // Perform search operation based on selectedOption and searchText
            String searchResult = "";
            String isSorted = "";
            if(sorted) {
            	isSorted = "The results are sorted by Year. || ";
            }
            if (selectedOption.equals("No filter")) {
                searchResult = "You searched with no filter: ";
            } else {
                searchResult = "You searched with filter - " + selectedOption + ": ";
            }
            searchResultsArea.setText(""); // clear the search results area 
            searchResultsArea.append(isSorted + searchResult + "\n");
            try {
            	stemmedSearchText = Read.stemmingUser(searchText);
                Read.ReadIndex(selectedOption, stemmedSearchText, searchResultsArea, sorted);
            }catch (Exception err) {
            	System.out.println("Error occured " + err.getMessage());
            }
            searchHistory += searchResult + searchText + "\n";
            nextResultsButton.setEnabled(true);
            Highlight();
        } else if (e.getSource() == searchHistoryButton) {
            // Display the search history
            JOptionPane.showMessageDialog(this, "Search history:\n" + searchHistory);
        } else if (e.getSource() == nextResultsButton) {
            // Perform search operation based on selectedOption and searchText
            String searchResult = "";
            String isSorted = "";
            if(sorted) {
            	isSorted = "The results are sorted by Year. || ";
            }
            if (selectedOption.equals("No filter")) {
                searchResult = "You searched with no filter: ";
            } else {
                searchResult = "You searched with filter - " + selectedOption + ": ";
            }
            searchResultsArea.setText(""); // clear the search results area 
            searchResultsArea.append(isSorted + searchResult + "\n");
            try {
                Read.nextResults(selectedOption, stemmedSearchText, searchResultsArea, sorted);
            }catch (Exception err) {
            	System.out.println("Error occured " + err.getMessage());
            }
            Highlight();
        }else if(e.getSource()== sortButton) {
	        if (sort) {
				sort = false;
			}else{
				sort = true;
			}
        }
    }
    
    public static void main(String[] args)
    {
    	try {
    		String filePath = "C:\\Users\\apoll\\Downloads\\LuceneDemo\\indexedFiles";

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