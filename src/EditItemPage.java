import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class EditItemPage extends JFrame {

  private Connection connection = DBConnection.getConnection();
  String imageUrlForUpdate, itemNameForUpdate, itemPriceForUpdate, itemQuantityForUpdate, updatedImageFileName;
  private ImageIcon uploadItemImagePreview;

  EditItemPage(int id) {
    Color lightBlueColor = new Color(36, 40, 55, 255);
    Color darkBlueColor = new Color(30, 28, 43, 255);
    Color textColor = new Color(255, 255, 255, 255);
    Color orangeColor = new Color(228, 76, 28);

    Font titleFont = new Font("Berlow", Font.BOLD, 14);
    Font inputFont = new Font("Berlow", Font.BOLD, 14);

    setSize(350, 500);
    setLocationRelativeTo(null);
    setTitle("Edit Item");
    setResizable(false);
    getContentPane().setBackground(darkBlueColor);

    String getThatItemData = "SELECT * FROM items where id = " + id;
    try (Statement statement = connection.createStatement()) {
      ResultSet result = statement.executeQuery(getThatItemData);

      while (result.next()) {
        imageUrlForUpdate = result.getString("imageUrl");
        itemNameForUpdate = result.getString("name");
        itemPriceForUpdate = result.getString("price");
        itemQuantityForUpdate = result.getString("quantity");

      }

    } catch (Exception e) {
      System.out.println(e);
    }

    JPanel itemInput = new JPanel();
    itemInput.setBackground(null);
    itemInput.setLayout(null);
    itemInput.setBounds(60, 60, 260, 320);

    itemInput.setBorder(new LineBorder(orangeColor, 2));

    JLabel imageLabel = new JLabel("Food Image:");
    imageLabel.setForeground(Color.WHITE);
    imageLabel.setFont(titleFont);
    imageLabel.setBounds(20, 20, 210, 20);

    ImageIcon imageIcon = new ImageIcon(new ImageIcon("src/images/" + imageUrlForUpdate).getImage().getScaledInstance(100, 100, java.awt.Image.SCALE_SMOOTH));
    JButton uploadImageButton = new JButton(imageIcon);
    uploadImageButton.setBounds(20, 50, 100, 100);
    uploadImageButton.setBorderPainted(false);
    uploadImageButton.setContentAreaFilled(false);

    JLabel titleLabel = new JLabel("Item Name:");
    titleLabel.setFont(titleFont);
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setBounds(20, 170, 220, 20);
    JTextField titleInput = new JTextField(itemNameForUpdate);
    titleInput.setBorder(null);
    titleInput.setFont(inputFont);
    titleInput.setBounds(20, 200, 300, 30);

    JLabel priceLabel = new JLabel("Price:");
    priceLabel.setFont(titleFont);
    priceLabel.setForeground(Color.WHITE);
    priceLabel.setBounds(20, 250, 260, 20);
    JTextField priceInput = new JTextField(itemPriceForUpdate);
    priceInput.setBorder(null);
    priceInput.setFont(inputFont);
    priceInput.setBounds(20, 280, 300, 30);

    JLabel quantityLabel = new JLabel("Available Quantity:");
    quantityLabel.setFont(titleFont);
    quantityLabel.setForeground(Color.WHITE);
    quantityLabel.setBounds(20, 330, 260, 20);
    JTextField quantityInput = new JTextField(itemQuantityForUpdate);
    quantityInput.setBorder(null);
    quantityInput.setFont(inputFont);
    quantityInput.setBounds(20, 360, 300, 30);

    JButton UpdateItemBtn = new JButton("UPDATE ITEM");
    UpdateItemBtn.setBackground(orangeColor);
    UpdateItemBtn.setBorder(null);
    UpdateItemBtn.setForeground(Color.white);
    UpdateItemBtn.setBounds(100, 410, 150, 30);

    itemInput.add(uploadImageButton);
    itemInput.add(quantityLabel);
    itemInput.add(priceLabel);
    itemInput.add(titleLabel);
    itemInput.add(titleInput);
    itemInput.add(priceInput);
    itemInput.add(quantityInput);
    itemInput.add(UpdateItemBtn);
    itemInput.add(imageLabel);

    add(itemInput);

    setVisible(true);

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

          updatedImageFileName = selectedFile.getName();

          uploadItemImagePreview = new ImageIcon(new ImageIcon(destinationFile.getAbsolutePath()).getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH));

          uploadImageButton.setIcon(uploadItemImagePreview);
        }
      }
    });

    UpdateItemBtn.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (updatedImageFileName != null) {
          imageUrlForUpdate = updatedImageFileName;
        }

        String updatedName = titleInput.getText();
        float updatedPrice = Float.valueOf(priceInput.getText());
        int updatedQuantity = Integer.parseInt(quantityInput.getText());

        String updateItemQuery = "UPDATE items SET imageUrl='" + imageUrlForUpdate + "', name='" + updatedName +
          "', price=" + updatedPrice + ", quantity=" + updatedQuantity + " WHERE id=" + id;

        try (Statement statement = connection.createStatement()) {

          int result = statement.executeUpdate(updateItemQuery);

          if (result == 1) {
            dispose();

            JOptionPane.showMessageDialog(null, "Item Updated Successfully");

          }

        } catch (Exception e2) {
          System.out.println(e2);
        }

      }
    });

  }

}