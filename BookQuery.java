package HangCuaHuy;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class BookQuery extends JFrame implements ActionListener, ListSelectionListener {
	static final long serialVersionUID = 1L;
	static JPanel panel;
	JTextField BT; //BookTitle
	JTextArea BTD; //BookDisplay
	static JTextField PY; //PublicationYear
	JTextArea PYD; //PublicationYearDisplay
	static JTextField Au; //Authors
	JTextArea AuD; //AuthorsDisplay
	JTextField AR; //Average Rating
	JTextArea ARD; //BIDDisplay
	JTextField BISBN; //BookISBN
	JTextArea BISBND; //BISBNDisplay
	JButton Search;
	TreeMap<String,Book> BookISBNTree;
	static ArrayList<Book> BookArray;
	static ArrayList<BookFound> bookFound;
	static JList<String> BookList;
	static DefaultListModel<String> listModel;
	JTextArea searchTitle;
	JTextArea bookNotFound;
	JTextArea listTitle;
	JTextArea infoTitle;
	BufferedImage BookImage;
	JTextArea bigTitleDisplay;
	static JLabel label;
	JTextArea infoDisplay;
	JTextArea highRated;
	int disRM = 120; //displayRightMargin
	int disTM = 70; //displayTopMargin
	int disL = 100; //displayLength
	int disW = 30; //displayWidth
	int txtRM = disRM + 100; //textboxTopMargin
	int txtTM = disTM - 5; //textboxTopMargin
	int txtL = 200; //textboxWidth
	int txtW = 25; //textboxWidth
	int spaceBTR = 30; //spaceBetweenTwoRow
	int lsRM = 20; //listScrollerRightMargin
	int lsTM = 320; //listScrollerTopMargin
	int lsL = 520; //listScrollerLength
	int lsW = 320; //listScrollerWidth
	int infoRM = 570; //infoRightMargin
	int infoTM = 150; //infoTopMargin
	int infoL = 700; //infoLength
	int infoW = 700; //infoWidth
	
	public BookQuery() {
		showUI();
		double getStart = System.nanoTime();
		getFromFile("./books.csv");
		double getEnd = System.nanoTime();
		System.out.print("Time spent on loading: " + Double.toString((getEnd - getStart)/1000000000) + " seconds.\n");
	}
	
	private void showUI() {
		panel = new JPanel();
		BT = new JTextField();
		BTD = new JTextArea("Book Title");
		PY = new JTextField();
		PYD = new JTextArea("Publication Year");
		Au = new JTextField();
		AuD = new JTextArea("Authors");
		AR = new JTextField();
		ARD = new JTextArea("Lowest Average Rating");
		BISBN = new JTextField();
		BISBND = new JTextArea("Book ISBN");
		Search = new JButton("Search");
		listModel = new DefaultListModel<String>();
		BookList = new JList<String>(listModel);
		searchTitle = new JTextArea("SEARCH BOX");
		listTitle = new JTextArea("SEARCH RESULT");
		infoTitle = new JTextArea("BOOK INFOMATION");
		this.setSize(1100, 685);
		this.setVisible(true);
		this.setTitle("Book Searcher");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		panel.setLayout(null);
		this.add(panel);
		BTD.setBounds(disRM,disTM,disL,disW);
		PYD.setBounds(disRM,disTM+spaceBTR,disL,disW);
		AuD.setBounds(disRM,disTM+2*spaceBTR,disL,disW);
		BISBND.setBounds(disRM,disTM+3*spaceBTR,disL,disW);
		BT.setBounds(txtRM,txtTM,txtL,txtW);
		PY.setBounds(txtRM,txtTM+spaceBTR,txtL,txtW);
		Au.setBounds(txtRM,txtTM+2*spaceBTR,txtL,txtW);
		BISBN.setBounds(txtRM,txtTM+3*spaceBTR,txtL,txtW);
		searchTitle.setBounds(disRM+40,disTM-50,300,40);
		Search.setBounds(disRM+100,disTM+4*spaceBTR, 95, 30);
		listTitle.setBounds(lsRM+110,lsTM-90,300,40);
		infoTitle.setBounds(infoRM+100,30,300,40);
		BTD.setOpaque(false);
		PYD.setOpaque(false);
		AuD.setOpaque(false);
		ARD.setOpaque(false);
		BISBND.setOpaque(false);
		searchTitle.setOpaque(false);
		listTitle.setOpaque(false);
		infoTitle.setOpaque(false);
		BTD.setBackground(new Color(0, 0, 0, 0));
		PYD.setBackground(new Color(0, 0, 0, 0));
		AuD.setBackground(new Color(0, 0, 0, 0));
		BISBND.setBackground(new Color(0, 0, 0, 0));
		searchTitle.setBackground(new Color(0, 0, 0, 0));
		listTitle.setBackground(new Color(0, 0, 0, 0));
		infoTitle.setBackground(new Color(0, 0, 0, 0));
		searchTitle.setFont(new Font("Dialog",Font.BOLD,30));
		listTitle.setFont(new Font("Dialog",Font.BOLD,30));
		infoTitle.setFont(new Font("Dialog",Font.BOLD,30));
		panel.add(BTD);
		panel.add(BT);
		panel.add(PYD);
		panel.add(PY);
		panel.add(AuD);
		panel.add(Au);
		panel.add(BISBND);
		panel.add(BISBN);
		panel.add(searchTitle);
		panel.add(listTitle);
		panel.add(infoTitle);
		panel.add(Search);
		Search.addActionListener(this);
		BookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		BookList.setLayoutOrientation(JList.VERTICAL);
		BookList.setVisibleRowCount(20);
		BookList.setOpaque(false);
		BookList.setBackground(new Color(0, 0, 0, 0));
		BookList.addListSelectionListener(this);
		BookList.setCellRenderer(new CustomListRenderer());
		JScrollPane listScroller = new JScrollPane(BookList);
		listScroller.setBounds(lsRM, lsTM-40, lsL, lsW+42);
		listScroller.setOpaque(false);
		listScroller.setBackground(new Color(0, 0, 0, 0));
		panel.add(listScroller);		
	}

	public void getFromFile(String fileName) {
		/*
		 We need 1 AVL Tree and a 1d array to store our book for fast-searching purpose
		 */
		BookISBNTree = new TreeMap<String,Book>(new SortByString());
		BookArray = new ArrayList<Book>();
		try {
			BufferedReader csvReader = null;
			csvReader = new BufferedReader(new FileReader(fileName));
			String bookInfo = csvReader.readLine();
			while ((bookInfo = csvReader.readLine()) != null) {
			    Book book = new Book(bookInfo.split(",")[1],bookInfo.split(",")[2],
			    		bookInfo.split(",")[3],bookInfo.split(",")[4],
			    		bookInfo.split(",")[5],bookInfo.split(",")[7],bookInfo.split(",")[8],
			    		bookInfo.split(",")[9],bookInfo.split(",")[10],bookInfo.split(",")[11],
			    		bookInfo.split(",")[12],bookInfo.split(",")[13],
			    		bookInfo.split(",")[14],bookInfo.split(",")[15],
			    		bookInfo.split(",")[16],bookInfo.split(",")[17],
			    		bookInfo.split(",")[18],bookInfo.split(",")[19],
			    		bookInfo.split(",")[20],bookInfo.split(",")[21],bookInfo.split(",")[22]);
				BookISBNTree.put(book.BookISBN,book);
			    BookArray.add(book);
			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (!BISBN.getText().isEmpty()) {
			double getStart = System.nanoTime();
			bookFound = new ArrayList<BookFound>();
			bookFound.add(new BookFound(0.0,BookISBNTree.get(BISBN.getText())));
			showBookList();
			double getEnd = System.nanoTime();
			System.out.print("Time spent on searching by Book ISBN: " + Double.toString((getEnd - getStart)/1000000000) + " seconds.\n");
		}
		
		else if (!BT.getText().isEmpty()) {
			searchBookByBookTitle(BT.getText());
		}
		
		else if (!Au.getText().isEmpty()) {
			searchBookByAuthors(Au.getText());
		}
		
		else if (!PY.getText().isEmpty()) {
			searchBookByPublicationYear(PY.getText());
		}
	}
	
	public void searchBookByBookTitle (String title) {
		double getStart = System.nanoTime();
		Iterator<Book> bookIter = BookArray.iterator();
		bookFound = new ArrayList<BookFound>();
		if (Au.getText().isEmpty() && PY.getText().isEmpty()) {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(title,book.BookTitle);
				if (stringCompare > 0.7){
					bookFound.add(new BookFound(stringCompare,book));
				}
			}
		}
		else if (!Au.getText().isEmpty() && PY.getText().isEmpty()) {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(title,book.BookTitle);
				if (stringCompare > 0.7){
					if (StringCompare(Au.getText(),book.Authors) > 0.7) {
						bookFound.add(new BookFound(stringCompare,book));
					}
				}
			}
		}
		else if (Au.getText().isEmpty() && !PY.getText().isEmpty()) {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(title,book.BookTitle);
				if (stringCompare > 0.7){
					if (PY.getText().compareTo(book.PublicationYear) == 0) {
						bookFound.add(new BookFound(stringCompare,book));
					}
				}
			}
		}
		else {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(title,book.BookTitle);
				if (stringCompare > 0.7){
					if (StringCompare(Au.getText(),book.Authors) > 0.7 && PY.getText().compareTo(book.PublicationYear) == 0) {
						bookFound.add(new BookFound(stringCompare,book));
					}
				}
			}
		}
		this.showBookList();
		double getEnd = System.nanoTime();
		System.out.print("Time spent on searching by book title, authors and publication year: " + Double.toString((getEnd - getStart)/1000000000) + " seconds.\n");
	}
	
	public void searchBookByAuthors (String authors) {
		Iterator<Book> bookIter = BookArray.iterator();
		bookFound = new ArrayList<BookFound>();
		if (!PY.getText().isEmpty()) {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(authors,book.Authors);
				if (stringCompare > 0.7){
					bookFound.add(new BookFound(stringCompare,book));
				}
			}
		}
		else {
			while (bookIter.hasNext()) {
				Book book = bookIter.next();
				double stringCompare = StringCompare(authors,book.Authors);
				if (stringCompare > 0.7){
					if (PY.getText().compareTo(book.PublicationYear) == 0) {
						bookFound.add(new BookFound(stringCompare,book));
					}
				}
			}
		}
		this.showBookList();
	}
	
	public void searchBookByPublicationYear (String year) {
		Iterator<Book> bookIter = BookArray.iterator();
		bookFound = new ArrayList<BookFound>();
		while (bookIter.hasNext()) {
			Book book = bookIter.next();
			if (year.compareTo(book.PublicationYear) == 0){
				bookFound.add(new BookFound(0.0,book));
			}
		}
		this.showBookList();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			return;
		}
		String selectedBook = BookList.getSelectedValue();
		Iterator<BookFound> bookIter = bookFound.iterator();
		while (bookIter.hasNext()) {
			Book book = bookIter.next().book;
			if (StringCompare(selectedBook,"Book " + Integer.toString(1) + ":"
					+ "\nTitle: " + book.BookTitle
					+ "\nAuthor(s): " + book.AuthorsUnmerged
					+ "\nPublication Year: " + book.PublicationYear) > 0.95){
				showBookInfo(book);
				break;
			}
		}
	}

	public void showBookInfo (Book book) {
		double getStart = System.nanoTime();
		if (label != null) {
			panel.remove(bigTitleDisplay);
			panel.remove(label);
			panel.remove(infoDisplay);
		}
		bigTitleDisplay = new JTextArea(book.original_title);
		URL url = null;
		try {
			url = new URL(book.image_url);
			BookImage = ImageIO.read(url);
			label = new JLabel(new ImageIcon(BookImage),JLabel.CENTER);
		} catch (MalformedURLException ex) {
	        System.out.println("Malformed URL");
	    } catch (IOException iox) {
	        System.out.println("Can not load file");
	    }
		infoDisplay = new JTextArea("\nTitle: " + book.BookTitle
				+ "\nAuthor(s): " + book.AuthorsUnmerged
				+ "\nPublication Year: " + book.PublicationYear
				+"\nBookID: " + book.BookID
				+"\nBest Book ID: " + book.best_book_id
				+"\nWork ID: " + book.work_id
				+"\nBooks Count: " + book.books_count
				+"\nBook ISBN: " + book.BookISBN
				+"\nAuthors: " + book.Authors
				+"\nPublicationYear: " + book.PublicationYear
				+"\nOriginal Title: " + book.original_title
				+"\nBook Title: " + book.BookTitle
				+"\nLanguage Code: " + book.language_code
				+"\nAverage Rating: " + book.average_rating
				+"\nRatings Count: " + book.ratings_count
				+"\nWork Ratings Count: " + book.work_ratings_count
				+"\nWork Text Reviews Count: " + book.work_text_reviews_count
				+"\nRatings 1: " + book.ratings_1
				+"\nRatings 2: " + book.ratings_2
				+"\nRatings 3: " + book.ratings_3
				+"\nRatings 4: " + book.ratings_4
				+"\nRatings 5: " + book.ratings_5
				+"\nImage URL: " + book.image_url
				+"\nSmall Image URL: " + book.small_image_url);
		highRated = new JTextArea("High Rated!");
		bigTitleDisplay.setBounds(infoRM+20,infoTM-20,450,30);
		label.setBounds(infoRM+400, infoTM-10, 100, 200);
		infoDisplay.setBounds(infoRM, infoTM, infoL, infoW);
		bigTitleDisplay.setOpaque(false);
		bigTitleDisplay.setBackground(new Color(0, 0, 0, 0));
		bigTitleDisplay.setFont(new Font("Dialog",Font.BOLD,20));
		infoDisplay.setOpaque(false);
		infoDisplay.setBackground(new Color(0, 0, 0, 0));
		panel.add(bigTitleDisplay);
		panel.add(label);
		panel.add(infoDisplay);
		panel.remove(highRated);
		if (!book.average_rating.isEmpty()) {
			if (Double.parseDouble(book.average_rating) > 4.0) {
				highRated.setBounds(infoRM+415,infoTM+170,70,30);
				highRated.setOpaque(false);
				highRated.setBackground(new Color(0, 0, 0, 0));
				highRated.setForeground(Color.BLUE);
				highRated.setFont(new Font("Dialog",Font.BOLD,12));
				panel.add(highRated);
			}
		}
		panel.repaint();
		double getEnd = System.nanoTime();
		System.out.print("Time spent on Displaying: " + Double.toString((getEnd - getStart)/1000000000) + " seconds.\n");
	}
	
	private void showBookList() {
		if (bookNotFound != null)
		{
			panel.remove(bookNotFound);
			panel.repaint();
		}
		if (bookFound.size() == 0) {
			bookNotFound = new JTextArea("Book Not Found!");
			bookNotFound.setBounds(disRM+200,disTM+4*spaceBTR+5, 100, 30);
			bookNotFound.setOpaque(false);
			bookNotFound.setBackground(new Color(0, 0, 0, 0));
			bookNotFound.setForeground(Color.RED);
			bookNotFound.setFont(new Font("Dialog",Font.BOLD,12));
			panel.add(bookNotFound);
			panel.repaint();
		}
		bookFound.sort(new Comparator<BookFound>(){
			@Override
			public int compare(BookFound book0, BookFound book1) {
				return Double.compare(book1.sortElement,book0.sortElement);
			}
		});
		if (!listModel.isEmpty())
			listModel.removeAllElements();
		for (int i = 0; i < bookFound.size(); i++) {
			if (i == 100) break;
			Book book = bookFound.get(i).book;
			String bookStr = "Book " + Integer.toString(i + 1) + ":"
			+ "\nTitle: " + book.BookTitle
			+ "\nAuthor(s): " + book.AuthorsUnmerged
			+ "\nPublication Year: " + book.PublicationYear;
			listModel.addElement(bookStr);
		}
	}

	private static double StringCompare(String str, String bookStr) {
		String[] strArray = str.split(" ");
		double score = 0;
		for (int i = 0; i < strArray.length; i++) {
			score += maxScore(strArray[i],bookStr);
		}
		return score/strArray.length;
	}

	private static double maxScore(String str, String bookStr) {
		String[] bookStrArray = bookStr.split(" ");
		double maxScore = 0;
		for (int i = 0; i < bookStrArray.length; i++) {
			double newScore = 1.0 - 1.0*new LevenshteinDistance().apply(str.toLowerCase(), bookStrArray[i].toLowerCase())/str.length();
			if (newScore > maxScore) {
				maxScore = (double) newScore;
			}
		}
		return maxScore;
	}
	
	public static void main(String[] args) throws UnknownHostException,
	IOException {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				new BookQuery();
			}
		});
	}
}

class Book {
	String BookID;
	String best_book_id;
	String work_id;
	String books_count;
	String BookISBN;
	String Authors;
	String PublicationYear;
	String original_title;
	String BookTitle;
	String language_code;
	String average_rating;
	String ratings_count;
	String work_ratings_count;
	String work_text_reviews_count;
	String ratings_1;
	String ratings_2;
	String ratings_3;
	String ratings_4;
	String ratings_5;
	String image_url;
	String small_image_url;
	String AuthorsUnmerged;

	public Book(String BookID,String best_book_id,String work_id,String books_count,String BookISBN,
			String Authors,String PublicationYear,String original_title,String BookTitle,String language_code,
			String average_rating,String ratings_count,String work_ratings_count,String work_text_reviews_count,
			String ratings_1,String ratings_2,String ratings_3,String ratings_4,String ratings_5,String image_url,String small_image_url)
	{
		this.BookID = BookID;
		this.best_book_id = best_book_id;
		this.work_id = work_id;
		this.books_count = books_count;
		this.BookISBN = BookISBN;
		this.Authors = MergedAuthors(Authors);
		this.PublicationYear = PublicationYear;
		this.original_title = original_title;
		this.BookTitle = BookTitle;
		this.language_code = language_code;
		this.average_rating = average_rating;
		this.ratings_count = ratings_count;
		this.work_ratings_count = work_ratings_count;
		this.work_text_reviews_count = work_text_reviews_count;
		this.ratings_1 = ratings_1;
		this.ratings_2 = ratings_2;
		this.ratings_3 = ratings_3;
		this.ratings_4 = ratings_4;
		this.ratings_5 = ratings_5;
		this.image_url = image_url;
		this.small_image_url = small_image_url;
		this.AuthorsUnmerged = Authors;
	}
	
	public static String MergedAuthors(String authors) {
		String[] authorsArray = authors.split(";");
		Arrays.sort(authorsArray);
		String mergedAuthors = "";
		int index = authorsArray.length;
		while (--index > 0) {
			mergedAuthors = authorsArray[index] + " " + mergedAuthors;
		}
		return mergedAuthors;
	}
}

class SortByString implements Comparator<String>{
	@Override
	public int compare(String a, String b) {
		return a.compareToIgnoreCase(b);
	}
	
}

class SortByFloat implements Comparator<Float> {
	@Override
	public int compare(Float a, Float b) {
		return (a > b)?1:-1;
	}
}
class CustomListRenderer implements ListCellRenderer<String> {
	@Override
	public Component getListCellRendererComponent(JList<? extends String> list, String value, int index,
			boolean isSelected, boolean cellHasFocus) {
		JTextArea renderer = new JTextArea(4,1);
        renderer.setText(value);
        renderer.setLineWrap(false);
        return renderer;
	}
}

class BookFound {
	Double sortElement;
	Book book;
	public BookFound(Double sortElement, Book book) {
		this.sortElement = sortElement;
		this.book = book;
	}
}
