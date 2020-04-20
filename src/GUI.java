import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.simple.parser.ParseException;

import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.awt.Image;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JList;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import javax.swing.JSeparator;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;

import java.awt.SystemColor;

import javax.swing.border.LineBorder;
import java.awt.Color;


public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	
	private ArrayList<Product> searchReasult;
	private Product clickedProduct = null;

	/**
	 * Launch the application.
	 */
	public void launch() {
		/*UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels(); 
        for (UIManager.LookAndFeelInfo look : looks) { 
            System.out.println(look.getClassName()); 
        }*/ 
        
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		getContentPane().setLayout(null);
		contentPane.setLayout(null);
		
		textField_1 = new JTextField();
		textField_1.setBounds(10, 11, 301, 20);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		//Detail elements
		JTextArea titleLabel = new JTextArea();
		titleLabel.setRows(2);
		titleLabel.setEditable(false);
		titleLabel.setBounds(416, 42, 260, 34);
		contentPane.add(titleLabel);
		titleLabel.setWrapStyleWord(true);
		titleLabel.setText("Title");
		titleLabel.setLineWrap(true);
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		titleLabel.setBackground(SystemColor.menu);
		
		//image
		JLabel imageLabel = new JLabel("<html>No<br/>Image</html>");
		imageLabel.setBounds(321, 45, 90, 90);

		
		contentPane.add(imageLabel);
		
		JLabel rankLabel = new JLabel("Rank");
		rankLabel.setBounds(421, 101, 255, 14);
		contentPane.add(rankLabel);
		
		JLabel priceLabel = new JLabel("Price");
		priceLabel.setBounds(421, 126, 220, 14);
		contentPane.add(priceLabel);
		
		JLabel brandLabel = new JLabel("Brand");
		brandLabel.setBounds(421, 76, 255, 14);
		contentPane.add(brandLabel);
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(421, 146, 255, 78);
		scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane2.setBorder(null);	
		contentPane.add(scrollPane2);
		
		JTextArea descriptionArea = new JTextArea();
		scrollPane2.setViewportView(descriptionArea);
		descriptionArea.setBackground(UIManager.getColor("CheckBox.background"));
		descriptionArea.setFont(new Font("Arial", Font.ITALIC, 11));
		descriptionArea.setEditable(false);
		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		//textArea.setRows(2);
		//textArea.setBounds(312, 145, 354, 54);
		descriptionArea.setText("Description");
		
		//search result list
		JList list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//list.setBounds(10, 42, 292, 157);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JList list = (JList) evt.getSource();
				int index = list.locationToIndex(evt.getPoint());
				clickedProduct = searchReasult.get(index);
				
				setDetails(clickedProduct, titleLabel, brandLabel, rankLabel, priceLabel, descriptionArea, imageLabel,90);
			}
		});
		JScrollPane scrollPane1 = new JScrollPane(list);
		scrollPane1.setBounds(10, 42, 301, 182);
		scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane1.setBorder(null);
		contentPane.add(scrollPane1);		
		getContentPane().add(scrollPane1);	
		
		JProgressBar searchProgressBar = new JProgressBar();
		searchProgressBar.setBounds(10, 27, 301, 14);
		contentPane.add(searchProgressBar);
		searchProgressBar.setIndeterminate(true);
		searchProgressBar.setValue(50);
		searchProgressBar.setVisible(false);
		
		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchProgressBar.setVisible(true);
				new SwingWorker<Void, Void>() {//async function
					@Override
					protected Void doInBackground() {
						// search button event
						ArrayList<String> searchResult = searchBtnEvent(textField_1.getText());
						list.setListData(searchResult.toArray());
						return null;
					}

					@Override
					protected void done() {
						searchProgressBar.setVisible(false);
					}

				}.execute();
			}
		});
		btnNewButton.setBounds(321, 10, 90, 23);
		getContentPane().add(btnNewButton);	
			
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel.setBounds(12, 248, 212, 182);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel r1_imageLabel = new JLabel("<html>No<br/>Image</html>");
		r1_imageLabel.setBounds(10, 11, 70, 70);
		panel.add(r1_imageLabel);
		
		JTextArea r1_titleLabel = new JTextArea();
		r1_titleLabel.setBounds(6, 92, 200, 79);
		panel.add(r1_titleLabel);
		r1_titleLabel.setWrapStyleWord(true);
		r1_titleLabel.setText("Title");
		r1_titleLabel.setRows(2);
		r1_titleLabel.setLineWrap(true);
		r1_titleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		r1_titleLabel.setEditable(false);
		r1_titleLabel.setBackground(SystemColor.menu);
		
		JLabel r1_brandLabel = new JLabel("Brand");
		r1_brandLabel.setBounds(90, 12, 116, 14);
		panel.add(r1_brandLabel);
		
		JLabel r1_rankLabel = new JLabel("Rank");
		r1_rankLabel.setBounds(90, 37, 116, 14);
		panel.add(r1_rankLabel);
		
		JLabel r1_priceLabel = new JLabel("Price");
		r1_priceLabel.setBounds(90, 62, 116, 14);
		panel.add(r1_priceLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(8, 235, 667, 2);
		contentPane.add(separator);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_1.setLayout(null);
		panel_1.setBounds(236, 249, 212, 182);
		contentPane.add(panel_1);
		
		JLabel r2_imageLabel = new JLabel("<html>No<br/>Image</html>");
		r2_imageLabel.setBounds(10, 11, 70, 70);
		panel_1.add(r2_imageLabel);
		
		JTextArea r2_titleLabel = new JTextArea();
		r2_titleLabel.setWrapStyleWord(true);
		r2_titleLabel.setText("Title");
		r2_titleLabel.setRows(2);
		r2_titleLabel.setLineWrap(true);
		r2_titleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		r2_titleLabel.setEditable(false);
		r2_titleLabel.setBackground(SystemColor.menu);
		r2_titleLabel.setBounds(6, 92, 200, 79);
		panel_1.add(r2_titleLabel);
		
		JLabel r2_brandLabel = new JLabel("Brand");
		r2_brandLabel.setBounds(90, 12, 116, 14);
		panel_1.add(r2_brandLabel);
		
		JLabel r2_rankLabel = new JLabel("Rank");
		r2_rankLabel.setBounds(90, 37, 116, 14);
		panel_1.add(r2_rankLabel);
		
		JLabel r2_priceLabel = new JLabel("Price");
		r2_priceLabel.setBounds(90, 62, 116, 14);
		panel_1.add(r2_priceLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panel_2.setLayout(null);
		panel_2.setBounds(460, 250, 212, 182);
		contentPane.add(panel_2);
		
		JLabel r3_imageLabel = new JLabel("<html>No<br/>Image</html>");
		r3_imageLabel.setBounds(10, 11, 70, 70);
		panel_2.add(r3_imageLabel);
		
		JTextArea r3_titleLabel = new JTextArea();
		r3_titleLabel.setWrapStyleWord(true);
		r3_titleLabel.setText("Title");
		r3_titleLabel.setRows(2);
		r3_titleLabel.setLineWrap(true);
		r3_titleLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		r3_titleLabel.setEditable(false);
		r3_titleLabel.setBackground(SystemColor.menu);
		r3_titleLabel.setBounds(6, 92, 200, 79);
		panel_2.add(r3_titleLabel);
		
		JLabel r3_brandLabel = new JLabel("Brand");
		r3_brandLabel.setBounds(90, 12, 116, 14);
		panel_2.add(r3_brandLabel);
		
		JLabel r3_rankLabel = new JLabel("Rank");
		r3_rankLabel.setBounds(90, 37, 116, 14);
		panel_2.add(r3_rankLabel);
		
		JLabel r3_priceLabel = new JLabel("Price");
		r3_priceLabel.setBounds(90, 62, 116, 14);
		panel_2.add(r3_priceLabel);
		
		JProgressBar getRecomProgressBar = new JProgressBar();
		getRecomProgressBar.setIndeterminate(true);
		getRecomProgressBar.setBounds(321, 223, 90, 10);
		contentPane.add(getRecomProgressBar);
		getRecomProgressBar.setVisible(false);
		
		JButton getRecomButton = new JButton("Find Similar");
		getRecomButton.setFont(new Font("Tahoma", Font.PLAIN, 11));
		getRecomButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getRecomProgressBar.setVisible(true);
				new SwingWorker<Void, Void>() {// async function
					@Override
					protected Void doInBackground() {
						// get recommendation button event
						ArrayList<Product> recommenedProducts = getRecommendationBtnEvent();
						Product p1 = recommenedProducts.get(0);
						setDetails(p1, r1_titleLabel, r1_brandLabel, r1_rankLabel, r1_priceLabel, null, r1_imageLabel,70);
						Product p2 = recommenedProducts.get(1);
						setDetails(p2, r2_titleLabel, r2_brandLabel, r2_rankLabel, r2_priceLabel, null, r2_imageLabel,70);
						Product p3 = recommenedProducts.get(2);
						setDetails(p3, r3_titleLabel, r3_brandLabel, r3_rankLabel, r3_priceLabel, null, r3_imageLabel,70);
						return null;
					}

					@Override
					protected void done() {
						getRecomProgressBar.setVisible(false);
					}
				}.execute();
			}
		});
		getRecomButton.setBounds(321, 204, 89, 20);
		contentPane.add(getRecomButton);	
	}
	
	private ArrayList<String> searchBtnEvent(String queryString) {
		Search search = new Search();		

		try {
			searchReasult = search.getSearchResult("title", queryString);
		} catch (IOException | ParseException e) {
			System.out.println("Search failed!");
			e.printStackTrace();
		}
		ArrayList<String> titles = new ArrayList<String>();
		for (Product p : searchReasult) {
			titles.add(p.getTitle());
		}
		return titles;
	}
	
	private ArrayList<Product> getRecommendationBtnEvent(){
		try {
			return new Recommendation().getRecomendation(3, clickedProduct);
		} catch (Exception e) {
			System.out.println("Get recommendation failed");
			e.printStackTrace();
		}
		return null;
	}
	
	private void setDetails(Product product, JTextArea titleArea, JLabel brandLabel, JLabel rankLabel, JLabel priceLabel, JTextArea descriptionArea, JLabel imageLabel, int imageSize) {
		try {
			titleArea.setText(product.getTitle());
			brandLabel.setText(product.getBrand());
			rankLabel.setText(product.getRankString().isBlank() ? "Rank Not Avaiable" : product.getRankString().replaceAll("in", " in ").replaceAll("amp;", "&").replaceAll("\\(",""));
			priceLabel.setText("$ "+ product.getPrice());
			if(descriptionArea != null) {
				descriptionArea.setText(product.getDescription().isBlank() ? "Description Not Avaiable" : product.getDescription());	
				descriptionArea.setCaretPosition(0);//scroll to top
			}
		} catch (Exception e) {
			e.printStackTrace();
		}			
		
		if(!product.getImageUrl().isBlank()) {
			try {
				Image image = null;
				String usrlString = product.getImageUrl().toString();
				URL url = new URL(usrlString.replaceAll("_SS40_", "_SS"+imageSize +"_"));//get 90*90 image
				image = ImageIO.read(url);
				if(image != null) {
					imageLabel.setIcon(new ImageIcon(image));
					imageLabel.setText("");
				}
			} catch (Exception e) {
				imageLabel.setIcon(null);
				imageLabel.setText("No Image");
			}		
		}else {
			imageLabel.setIcon(null);
			imageLabel.setText("No Image");
		}
	}
}

