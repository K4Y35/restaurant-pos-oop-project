import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Home extends JFrame {

  class OrderedItem {
    private int itemId;
    private int quantity;

    public OrderedItem(int itemId, int quantity) {
      this.itemId = itemId;
      this.quantity = quantity;
    }

    public int getItemId() {
      return itemId;
    }

    public int getQuantity() {
      return quantity;
    }
  }

  private Connection connection = DBConnection.getConnection();

  private JPanel contentPanel, rightSidebar, navBar;
  JScrollPane MainContentAreascrollPane, rightsideBarScrollPane;
  JButton homeButton, settingsButton, orderBtn, dashBoardButton;
  private ImageIcon uploadItemImagePreview;

  private Color lightBlueColor = new Color(36, 40, 55, 255);
  private Color darkBlueColor = new Color(30, 28, 43, 255);
  private Color textColor = new Color(255, 255, 255, 255);
  private Color orangeColor = new Color(228, 76, 28);

  String uploadedImageFileName;
  int orderCardY = 60;
  float subTotal = 0;
  Font titleFont = new Font("Berlow", Font.BOLD, 30);
  Font cardtitleFont = new Font("Berlow", Font.BOLD, 24);
  String mainQuantityToAdd;

  JLabel SubTotalLabel, SubTotalValue, RightLabel;

  ArrayList < OrderedItem > orderedIteminRightSidebar = new ArrayList < > ();
  
  DecimalFormat decimalFormat = new DecimalFormat("#.##");

  public Home() {

    setSize(1300, 700);
    setLocationRelativeTo(null);
    setLayout(null);
    setTitle("Aakhri Pasta");
    getContentPane().setBackground(lightBlueColor);
    setResizable(false);

    JPanel sidebar = new JPanel();
    sidebar.setBackground(darkBlueColor);
    sidebar.setBounds(0, 0, 60, 700);
    sidebar.setLayout(null);
    add(sidebar);

    homeButton = new JButton("");
    ImageIcon homeIcon = new ImageIcon("src/images/home.png");
    homeButton.setIcon(homeIcon);
    homeButton.setBackground(null);
    homeButton.setBorder(null);
    homeButton.setBounds(10, 100, 40, 40);

    sidebar.add(homeButton);

    dashBoardButton = new JButton("");
    ImageIcon dashboardIcon = new ImageIcon("src/images/dashboard.png");
    dashBoardButton.setIcon(dashboardIcon);
    dashBoardButton.setBackground(null);
    dashBoardButton.setBounds(10, 150, 40, 40);
    dashBoardButton.setBorder(null);
    sidebar.add(dashBoardButton);

    settingsButton = new JButton("");
    ImageIcon settingsIcon = new ImageIcon("src/images/setttings.png");
    settingsButton.setIcon(settingsIcon);
    settingsButton.setBackground(null);
    settingsButton.setBorder(null);
    settingsButton.setBounds(10, 200, 40, 40);
    sidebar.add(settingsButton);

    homeButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateContent("homepage");
      }
    });

    dashBoardButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateContent("dashboard");
      }
    });

    settingsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateContent("settings");
      }
    });

    navBar = new JPanel();
    navBar.setBackground(darkBlueColor);
    navBar.setLayout(null);
    navBar.setBounds(0, 0, 1000, 80);
    add(navBar);

    ImageIcon homeLogo = new ImageIcon("src/images/logoHoriMain.png");

    JLabel homeTitle = new JLabel(homeLogo);
    homeTitle.setFont(titleFont);
    homeTitle.setBounds(100, 0, 200, 80);
    homeTitle.setForeground(textColor);
    homeTitle.setBorder(null);
    navBar.add(homeTitle);

    JTextField searchField = new JTextField();
    searchField.setBounds(700, 20, 250, 30);
    searchField.setBorder(null);

    navBar.add(searchField);

    SubTotalValue = new JLabel(String.valueOf(subTotal));
    SubTotalValue.setBounds(1100, 560, 100, 40);
    SubTotalValue.setForeground(textColor);
    SubTotalValue.setBackground(orangeColor);
    add(SubTotalValue);

    RightLabel = new JLabel("Order#1563");
    RightLabel.setBounds(1010, 20, 200, 40);
    RightLabel.setForeground(textColor);
    RightLabel.setFont(titleFont);
    RightLabel.setBackground(orangeColor);
    add(RightLabel);

    contentPanel = new JPanel();
    contentPanel.setBackground(lightBlueColor);
    contentPanel.setLayout(null);

    MainContentAreascrollPane = new JScrollPane(contentPanel);
    MainContentAreascrollPane.setBounds(60, 80, 940, 580);
    MainContentAreascrollPane.setBorder(null);
    MainContentAreascrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    JScrollBar verticalScrollBar = MainContentAreascrollPane.getVerticalScrollBar();
    verticalScrollBar.setUnitIncrement(20);

    add(MainContentAreascrollPane);

    HomePageContent();

    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  private void updateContent(String content) {
    contentPanel.removeAll();

    if ("homepage".equals(content)) {

      HomePageContent();

    } else if ("settings".equals(content)) {

      settingsPageContent();

    } else if ("dashboard".equals(content)) {
      dashBoardContent();
    } else {
      JLabel contentLabel = new JLabel(content);
      contentLabel.setForeground(Color.WHITE);
      contentPanel.add(contentLabel);
    }

    contentPanel.revalidate();
    contentPanel.repaint();
  }

  private void HomePageContent() {

    homeButton.setBackground(orangeColor);
    settingsButton.setBackground(null);
    dashBoardButton.setBackground(null);

    SubTotalValue.setVisible(true);
    navBar.setBounds(0, 0, 1000, 80);

    rightSidebar = new JPanel();
    rightSidebar.setBackground(darkBlueColor);
    rightSidebar.setLayout(null);

    rightsideBarScrollPane = new JScrollPane(rightSidebar);
    rightsideBarScrollPane.setBorder(null);

    rightsideBarScrollPane.setBounds(1000, 80, 300, 480);
    rightsideBarScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

    JScrollBar verticalOrderScrollBar = rightsideBarScrollPane.getVerticalScrollBar();

    add(rightsideBarScrollPane);
    orderBtn = new JButton("PLACE ORDER");
    orderBtn.setBorder(null);
    orderBtn.setBounds(1030, 610, 230, 40);
    orderBtn.setBackground(orangeColor);
    orderBtn.setForeground(textColor);

    add(orderBtn);

    SubTotalLabel = new JLabel("Sub Total : ");
    SubTotalLabel.setBounds(1030, 560, 100, 40);
    SubTotalLabel.setForeground(textColor);
    SubTotalLabel.setBackground(orangeColor);
    add(SubTotalLabel);

    verticalOrderScrollBar.setUnitIncrement(20);

    MainContentAreascrollPane.setBounds(60, 80, 940, 580);

    String selectItemsQuery = "SELECT * FROM items ORDER BY id DESC";

    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(selectItemsQuery);

      int cardX = 50;
      int cardY = 60;
      int cardsPerRow = 3;

      while (resultSet.next()) {
        String imageUrl = resultSet.getString("imageUrl");
        String itemName = resultSet.getString("name");
        String itemPrice = resultSet.getString("price");
        String itemQuantity = resultSet.getString("quantity");
        int itemId = resultSet.getInt("id");

        Font cardFont = new Font("Berlow", Font.BOLD, 14);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(darkBlueColor);
        cardPanel.setLayout(null);
        cardPanel.setBounds(cardX, cardY, 260, 320);
        contentPanel.add(cardPanel);

        ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/images/" + imageUrl).getImage().getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH));
        JLabel card1Imgae = new JLabel(imageIcon);
        card1Imgae.setBounds(0, 0, 260, 200);

        JLabel itemNameLabel = new JLabel(itemName, SwingConstants.CENTER);
        itemNameLabel.setForeground(Color.white);
        itemNameLabel.setFont(cardFont);
        itemNameLabel.setBounds(0, 100, 260, 200);
        JLabel itemPriceLabel = new JLabel("$" + itemPrice);
        itemPriceLabel.setForeground(Color.white);
        itemPriceLabel.setFont(cardFont);
        itemPriceLabel.setBounds(60, 140, 250, 200);
        JLabel itemQuantityLabel = new JLabel(itemQuantity + " Bowls Available");
        itemQuantityLabel.setForeground(Color.white);
        itemQuantityLabel.setFont(cardFont);
        itemQuantityLabel.setBounds(110, 140, 250, 200);
        JButton addItemBtn = new JButton("ADD");
        addItemBtn.setBorder(null);
        addItemBtn.setBackground(orangeColor);
        addItemBtn.setForeground(textColor);
        addItemBtn.setBounds(50, 260, 80, 30);

        ImageIcon minuIcon = new ImageIcon("src/images/minusicon.png");
        JButton minusButton = new JButton(minuIcon);
        minusButton.setBackground(null);
        minusButton.setBorder(null);
        minusButton.setBounds(140, 260, 30, 30);

        JTextField quantityField = new JTextField("0");
        quantityField.setBorder(null);
        quantityField.setBounds(170, 265, 30, 20);

        ImageIcon plusIcon = new ImageIcon("src/images/plusicon.png");
        JButton plusButton = new JButton(plusIcon);
        plusButton.setBackground(null);
        plusButton.setBorder(null);
        plusButton.setBounds(200, 260, 30, 30);

        plusButton.addActionListener(e -> {

          int max = Integer.parseInt(itemQuantity);
          int currentQuantity = Integer.parseInt(quantityField.getText());

          if (currentQuantity <= (max - 1)) {
            quantityField.setText(String.valueOf(currentQuantity + 1));
          }

        });

        minusButton.addActionListener(e -> {

          int currentQuantity = Integer.parseInt(quantityField.getText());

          if (currentQuantity > 0) {
            quantityField.setText(String.valueOf(currentQuantity - 1));
          }
        });

        cardPanel.add(card1Imgae);
        cardPanel.add(itemNameLabel);
        cardPanel.add(itemPriceLabel);
        cardPanel.add(itemQuantityLabel);
        cardPanel.add(addItemBtn);
        cardPanel.add(plusButton);
        cardPanel.add(minusButton);
        cardPanel.add(quantityField);

        addItemBtn.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {

            int  quantityToAdd = Integer.valueOf(quantityField.getText());
            float totalitemPrice = quantityToAdd * Float.valueOf(itemPrice);
            String totalPriceString = String.valueOf(totalitemPrice);
            mainQuantityToAdd = quantityField.getText();

            if (totalitemPrice < 1) {
              JOptionPane.showMessageDialog(null, "Please add quantity", "Information", JOptionPane.INFORMATION_MESSAGE);

            } else {
              calculateSubTotal(totalPriceString);

              OrderedItem orderedItem = new OrderedItem(itemId, quantityToAdd);

              orderedIteminRightSidebar.add(orderedItem);

              displayAllOrderQueueCard();

            }

          }
        });

        cardX += 300;

        if ((cardX + 250) > (1140 - 50)) {
          cardX = 50;
          cardY += 340;
        }
      }
      contentPanel.setPreferredSize(new Dimension(1140, cardY + 300));

    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error retrieving data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    orderBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
    	  
    	  if(!orderedIteminRightSidebar.isEmpty()) {
    	      try {
    	          ArrayList < OrderedItem > orderedItems = orderedIteminRightSidebar;
    	          PdfGenerator pdfGenerator = new PdfGenerator();
    	          pdfGenerator.generatePdf(orderedItems, "orders.pdf");
    	          updatedbAfterOrder();
    	          orderedIteminRightSidebar.clear();
    	          
    	          
    	        } catch (Exception ex) {
    	          ex.printStackTrace();
    	          JOptionPane.showMessageDialog(null, "Error generating receipt", "Error", JOptionPane.ERROR_MESSAGE);
    	        }
    		  
    	  }else {
    		  JOptionPane.showMessageDialog(null, "Please Add Order");
    	  }
  
      }
    });

    displayAllOrderQueueCard();
    rightSidebar.revalidate();
    rightSidebar.repaint();

  }

  private void settingsPageContent() {

    SubTotalValue.setVisible(false);
    MainContentAreascrollPane.setBounds(60, 80, 1300, 580);
    navBar.setBounds(0, 0, 1300, 80);
    settingsButton.setBackground(orangeColor);
    homeButton.setBackground(null);
    dashBoardButton.setBackground(null);

    Font LabelFont = new Font("Berlow", Font.BOLD, 12);

    JPanel itemInput = new JPanel();
    itemInput.setBackground(null);
    itemInput.setLayout(null);
    itemInput.setBounds(20, 60, 260, 320);

    itemInput.setBorder(new LineBorder(orangeColor, 2));

    contentPanel.add(itemInput);

    contentPanel.add(itemInput);

    JLabel imageLabel = new JLabel("Food Image:");
    imageLabel.setForeground(Color.WHITE);
    imageLabel.setFont(LabelFont);
    imageLabel.setBounds(10, 10, 210, 20);

    ImageIcon imageIcon = new ImageIcon("src/images/imageIcon.png");
    JButton uploadImageButton = new JButton(imageIcon);
    uploadImageButton.setBounds(10, 30, 100, 50);
    uploadImageButton.setBorderPainted(false);
    uploadImageButton.setContentAreaFilled(false);

    JLabel titleLabel = new JLabel("Item Name:");
    titleLabel.setFont(LabelFont);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setBounds(10, 80, 220, 20);
    JTextField titleInput = new JTextField();
    titleInput.setBorder(null);
    titleInput.setBounds(10, 100, 240, 30);

    JLabel priceLabel = new JLabel("Price:");
    priceLabel.setFont(LabelFont);
    priceLabel.setForeground(Color.WHITE);
    priceLabel.setBounds(10, 135, 260, 20);
    JTextField priceInput = new JTextField();
    priceInput.setBorder(null);
    priceInput.setBounds(10, 155, 240, 30);

    JLabel quantityLabel = new JLabel("Quantity:");
    quantityLabel.setFont(LabelFont);
    quantityLabel.setForeground(Color.WHITE);
    quantityLabel.setBounds(10, 190, 260, 20);
    JTextField quantityInput = new JTextField();
    quantityInput.setBorder(null);
    quantityInput.setBounds(10, 210, 240, 30);

    JButton addBtn = new JButton("ADD ITEM");
    addBtn.setBackground(orangeColor);
    addBtn.setBorder(null);
    addBtn.setForeground(Color.white);
    addBtn.setBounds(20, 270, 220, 30);

    itemInput.add(uploadImageButton);
    itemInput.add(quantityLabel);
    itemInput.add(priceLabel);
    itemInput.add(titleLabel);
    itemInput.add(titleInput);
    itemInput.add(priceInput);
    itemInput.add(quantityInput);
    itemInput.add(addBtn);
    itemInput.add(imageLabel);

    uploadImageButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
          File selectedFile = fileChooser.getSelectedFile();

          File destinationFile = new File("src/images/" + selectedFile.getName());
          try {
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
          } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error uploading image", "Error", JOptionPane.ERROR_MESSAGE);
          }

          JOptionPane.showMessageDialog(null, "Image uploaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

          uploadedImageFileName = selectedFile.getName();

          uploadItemImagePreview = new ImageIcon(new ImageIcon(destinationFile.getAbsolutePath()).getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH));

          uploadImageButton.setIcon(uploadItemImagePreview);
        }
      }
    });

    addBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String title = titleInput.getText();
        String price = priceInput.getText();
        String quantity = quantityInput.getText();

        if (title.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please Fill The Item Information", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String insertItemQuery = "INSERT INTO items (imageUrl,name,price,quantity) VALUES ('" + uploadedImageFileName + "','" + title + "','" + price + "','" + quantity + "')";

        try (Statement statement = connection.createStatement()) {
          int rowsAffected = statement.executeUpdate(insertItemQuery);

          if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "New Item Added", "Success", JOptionPane.INFORMATION_MESSAGE);

          } else {
            JOptionPane.showMessageDialog(null, "Failed to add new item", "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (SQLException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error storing data in the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    String selectItemsQuery = "SELECT * FROM items";
    try (Statement statement = connection.createStatement()) {
      ResultSet resultSet = statement.executeQuery(selectItemsQuery);

      int cardX = 20;
      int cardY = 420;

      while (resultSet.next()) {
        String imageUrl = resultSet.getString("imageUrl");
        String itemName = resultSet.getString("name");
        String itemPrice = resultSet.getString("price");
        String itemQuantity = resultSet.getString("quantity");
        int itemId = resultSet.getInt("id");

        Font cardFont = new Font("Berlow", Font.BOLD, 14);

        JPanel cardPanel = new JPanel();
        cardPanel.setBackground(darkBlueColor);
        cardPanel.setLayout(null);
        cardPanel.setBounds(cardX, cardY, 280, 320);
        contentPanel.add(cardPanel);

        ImageIcon imageIcon1 = new ImageIcon(new ImageIcon("src/images/" + imageUrl).getImage().getScaledInstance(140, 140, java.awt.Image.SCALE_SMOOTH));
        JLabel card1Imgae = new JLabel(imageIcon1);
        card1Imgae.setBounds(0, 0, 260, 200);
        JLabel itemNameLabel = new JLabel(itemName, SwingConstants.CENTER);
        itemNameLabel.setForeground(Color.white);
        itemNameLabel.setFont(cardFont);
        itemNameLabel.setBounds(0, 100, 260, 200);
        JLabel itemPriceLabel = new JLabel("$" + itemPrice);
        itemPriceLabel.setForeground(Color.white);
        itemPriceLabel.setFont(cardFont);
        itemPriceLabel.setBounds(60, 140, 250, 200);
        JLabel itemQuantityLabel = new JLabel(itemQuantity + " Bowls Available");
        itemQuantityLabel.setForeground(Color.white);
        itemQuantityLabel.setFont(cardFont);
        itemQuantityLabel.setBounds(110, 140, 250, 200);

        JButton editItemBtn = new JButton("EDIT");
        editItemBtn.setBackground(orangeColor);
        editItemBtn.setForeground(textColor);
        editItemBtn.setBounds(100, 260, 80, 30);

        cardPanel.add(card1Imgae);
        cardPanel.add(itemNameLabel);
        cardPanel.add(itemPriceLabel);
        cardPanel.add(itemQuantityLabel);
        cardPanel.add(editItemBtn);

        editItemBtn.addActionListener(new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {

            new EditItemPage(itemId);
            contentPanel.revalidate();
            contentPanel.repaint();

          }
        });

        cardX += 300;

        if ((cardX + 280) > (1300 - 50)) {
          cardX = 20;
          cardY += 340;
        }
      }
      contentPanel.setPreferredSize(new Dimension(1140, cardY + 300));

    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error retrieving data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void dashBoardContent() {
	  	SubTotalValue.setVisible(false);
	    MainContentAreascrollPane.setBounds(60, 80, 1300, 580);
	    navBar.setBounds(0, 0, 1300, 80);
	    settingsButton.setBackground(null);
	    homeButton.setBackground(null);
	    dashBoardButton.setBackground(orangeColor);
	    float totalAmountSaleToday=0,overallMonthSaleAmount=0;
	    
	    String salesTodayQuery = "SELECT * FROM sales WHERE DATE(date) = CURDATE()";
	    String overallMonthSale = "SELECT * FROM sales WHERE MONTH(date) = MONTH(CURDATE()) AND YEAR(date) = YEAR(CURDATE())";


	    try (Statement statement = connection.createStatement()) {
	        ResultSet result = statement.executeQuery(salesTodayQuery);

	        totalAmountSaleToday = 0;

	        while (result.next()) {
	            float amount = result.getFloat("amount");
	            totalAmountSaleToday += amount;
	        }

	    } catch (SQLException e) {
	        System.out.println(e);
	    }

	    try (Statement statement = connection.createStatement()) {
	        ResultSet OverallMonth = statement.executeQuery(overallMonthSale);

	        overallMonthSaleAmount = 0;

	        while (OverallMonth.next()) {
	            float overallMonthValue = OverallMonth.getFloat("amount");
	            overallMonthSaleAmount += overallMonthValue;
	        }

	    } catch (SQLException e) {
	        System.out.println(e);
	    }

	    
	    
	    JPanel salesTodayPanel = new JPanel();
	    salesTodayPanel.setBounds(20,20,300,200);
	    salesTodayPanel.setBackground(null);
	    salesTodayPanel.setBorder(new LineBorder(orangeColor, 5));
	    salesTodayPanel.setLayout(null);

	    JLabel salesLabel = new JLabel("Total Sales Today");
	    salesLabel.setBounds(40, 40, 400, 50);
	    salesLabel.setFont(cardtitleFont);
	    salesLabel.setForeground(textColor);
	  	
	    JLabel salesToday = new JLabel(decimalFormat.format(totalAmountSaleToday)+"$");
	    salesToday.setBounds(120, 100, 100, 50);
	   
	    salesToday.setFont(titleFont);
	    salesToday.setForeground(textColor);
	    salesTodayPanel.add(salesLabel);
	    salesTodayPanel.add(salesToday);
	    
	    JPanel overallSaleThisMonthPanel = new JPanel();
	    overallSaleThisMonthPanel.setBounds(340, 20, 300, 200);
	    overallSaleThisMonthPanel.setBackground(null);
	    overallSaleThisMonthPanel.setBorder(new LineBorder(orangeColor, 5));
	    overallSaleThisMonthPanel.setLayout(null);

	    JLabel overallSaleThisMonthLabel = new JLabel("Overall sales this month");
	    overallSaleThisMonthLabel.setBounds(10, 40, 400, 50);
	    overallSaleThisMonthLabel.setFont(cardtitleFont);
	    overallSaleThisMonthLabel.setForeground(textColor);

	    JLabel overallSaleThisMonthAmount = new JLabel(decimalFormat.format(overallMonthSaleAmount)+"$");
	    overallSaleThisMonthAmount.setBounds(120, 100, 100, 50);
	    overallSaleThisMonthAmount.setFont(titleFont);
	    overallSaleThisMonthAmount.setForeground(textColor);

	    overallSaleThisMonthPanel.add(overallSaleThisMonthLabel);
	    overallSaleThisMonthPanel.add(overallSaleThisMonthAmount);

	    // Add both panels to the contentPanel
	    contentPanel.add(salesTodayPanel);
	    contentPanel.add(overallSaleThisMonthPanel);
	    
	  
  }

  
  private void updatedbAfterOrder() {
	    try {
	        for (OrderedItem orderedItem : orderedIteminRightSidebar) {
	            int itemId = orderedItem.getItemId();
	            int quantityToReduce = orderedItem.getQuantity();

	            String selectQuantityQuery = "SELECT * FROM items WHERE id = " + itemId;

	            try (Statement statement = connection.createStatement()) {
	                ResultSet resultSet = statement.executeQuery(selectQuantityQuery);

	                if (resultSet.next()) {
	                    int currentQuantity = resultSet.getInt("quantity");
	                    float itemPrice = resultSet.getFloat("price");

	                    int newQuantity = currentQuantity - quantityToReduce;
	                    if (newQuantity < 0) {
	                        newQuantity = 0;
	                    }

	                    String updateQuantityQuery = "UPDATE items SET quantity = " + newQuantity + " WHERE id = " + itemId;
	                    statement.executeUpdate(updateQuantityQuery);

	                    // Insert data into sales table
	                    String insertSaleQuery = "INSERT INTO sales (itemId, saleQuantity, amount, date) VALUES (" + itemId + ", " + quantityToReduce + ", " + (quantityToReduce * itemPrice) + ", NOW())";
	                 

	                    statement.executeUpdate(insertSaleQuery);
	                }
	            }
	        }

	        orderedIteminRightSidebar.clear();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error updating database after order", "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


  
  private void calculateSubTotal(String totalPriceString) {
    float individualTotalint = Float.valueOf(totalPriceString);
    subTotal += individualTotalint;
    
    SubTotalValue.setText(decimalFormat.format(subTotal));
  }

  private void recalculateSubTotal(float removedSubtotal) {
    subTotal -= removedSubtotal;

    
    SubTotalValue.setText(decimalFormat.format(subTotal));
  }

  private void displayAllOrderQueueCard() {

    orderCardY = 10;

    for (Home.OrderedItem orderedItem: orderedIteminRightSidebar) {
      int itemId = orderedItem.getItemId();
      int itemQuantity = orderedItem.getQuantity();
      String selectItemQuery = "SELECT * FROM items WHERE id = " + itemId;

      try (Statement statement = connection.createStatement()) {
        ResultSet resultSet = statement.executeQuery(selectItemQuery);

        if (resultSet.next()) {
          String orderedImageUrl = resultSet.getString("imageUrl");
          String orderedItemName = resultSet.getString("name");
          String orderedItemPrice = resultSet.getString("price");

          JPanel orderPanel = new JPanel();
          orderPanel.setBackground(darkBlueColor);
          orderPanel.setLayout(null);
          orderPanel.setBorder(new LineBorder(orangeColor));
          orderPanel.setBounds(5, orderCardY, 270, 100);

          JLabel itemIdLabel = new JLabel(String.valueOf(itemId));
          itemIdLabel.setForeground(Color.white);
          itemIdLabel.setFont(new Font("Berlow", Font.BOLD, 14));
          itemIdLabel.setBounds(80, 40, 160, 40);
          itemIdLabel.setVisible(false);

          JLabel orderLabel = new JLabel(orderedItemName);
          orderLabel.setForeground(Color.white);
          orderLabel.setFont(new Font("Berlow", Font.BOLD, 14));
          orderLabel.setBounds(60, 10, 160, 40);

          JLabel orderItemQuantity = new JLabel(String.valueOf(itemQuantity));
          orderItemQuantity.setForeground(Color.white);
          orderItemQuantity.setFont(new Font("Berlow", Font.BOLD, 14));
          orderItemQuantity.setBounds(60, 40, 200, 40);

          JLabel orderItemPrice = new JLabel(orderedItemPrice);
          orderItemPrice.setForeground(Color.white);
          orderItemPrice.setFont(new Font("Berlow", Font.BOLD, 14));
          orderItemPrice.setBounds(230, 10, 200, 40);

          JButton deleteItemFromOrderBtn = new JButton("");
          deleteItemFromOrderBtn.setBounds(230, 50, 30, 30);
          ImageIcon deleteIcon = new ImageIcon("src/images/Delete.png");
          deleteItemFromOrderBtn.setIcon(deleteIcon);
          deleteItemFromOrderBtn.setBackground(null);
          deleteItemFromOrderBtn.setBorder(new LineBorder(orangeColor));

          ImageIcon smallImageIcon = new ImageIcon(new ImageIcon("src/images/" + orderedImageUrl).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH));
          JLabel smallImageLabel = new JLabel(smallImageIcon);
          smallImageLabel.setBounds(10, 10, 40, 40);

          orderPanel.add(smallImageLabel);
          orderPanel.add(orderLabel);
          orderPanel.add(orderItemQuantity);
          orderPanel.add(orderItemPrice);
          orderPanel.add(deleteItemFromOrderBtn);
          orderPanel.add(itemIdLabel);
          rightSidebar.add(orderPanel);
          rightSidebar.setPreferredSize(new Dimension(300, orderCardY + 120));

          orderCardY += 120;

          deleteItemFromOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

              JPanel parentPanel = (JPanel) deleteItemFromOrderBtn.getParent();

              //getting the id of item for deletion
              JLabel itemIdLabelComp = (JLabel) parentPanel.getComponent(5);
              int itemIdLabelInt = Integer.valueOf(itemIdLabelComp.getText());

              //getting the item price and quantity to recalculate the sub total after deletion
              JLabel orderItemPriceLabel = (JLabel) parentPanel.getComponent(3);

              JLabel orderItemQuantityLabel = (JLabel) parentPanel.getComponent(2);

              Float removedItemPrice = Float.valueOf(orderItemPriceLabel.getText());
              float removedItemQuantity = Float.valueOf(orderItemQuantityLabel.getText());
              float removedItemTotal = removedItemPrice * removedItemQuantity;

              removeOrdersPanelItemById(itemIdLabelInt);

              rightSidebar.remove(parentPanel);

              rightSidebar.setPreferredSize(new Dimension(300, rightSidebar.getHeight() - 120));
              rightSidebar.revalidate();
              rightSidebar.repaint();

              orderCardY -= 120;

              recalculateSubTotal(removedItemTotal);
              fetchAndDisplayOrders();
            }
          });

          rightSidebar.revalidate();
          rightSidebar.repaint();

        }
      } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error retrieving item data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void fetchAndDisplayOrders() {
    rightSidebar.removeAll();

    displayAllOrderQueueCard();

    rightSidebar.setPreferredSize(new Dimension(300, orderCardY + 120));
    rightSidebar.revalidate();
    rightSidebar.repaint();
  }

  public void removeOrdersPanelItemById(int itemId) {
  
    for (OrderedItem item: orderedIteminRightSidebar) {
      
      if (item.getItemId() == itemId) {
        
        orderedIteminRightSidebar.remove(item);
        break;
      }
    }
  }
//  
//	public static void main(String[] args) {
//		
//		new Home();
//	}

 
}