import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class Login extends JFrame {
  //DATABASE CONNECTION START	
  private Connection connection = DBConnection.getConnection();

  //DATABASE CONNECTION END

  Login() {
    Color lightBlueColor = new Color(36, 40, 55, 255);
    Color darkBlueColor = new Color(30, 28, 43, 255);
    Color textColor = new Color(255, 255, 255, 255);
    Color orangeColor = new Color(228, 76, 28);

    Font titleFont = new Font("Berlow", Font.BOLD, 30);

    setSize(500, 600);
    setLocationRelativeTo(null);
    setLayout(null);
    setTitle("Aakhri Pasta Login");
    setResizable(false);
    getContentPane().setBackground(lightBlueColor);

    ImageIcon logoIcon = new ImageIcon("src/images/logo.png");

    JLabel logoLabel = new JLabel(logoIcon);
    logoLabel.setBounds(100, 0, 300, 205);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    add(logoLabel);

    JLabel emailLabel = new JLabel("Email:");
    emailLabel.setForeground(textColor);
    emailLabel.setBounds(100, 250, 100, 30);
    add(emailLabel);

    JTextField emailField = new JTextField();
    emailField.setBounds(200, 250, 200, 30);
    emailField.setBorder(null);
    add(emailField);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setForeground(textColor);
    passwordLabel.setBounds(100, 300, 100, 30);
    add(passwordLabel);

    JPasswordField passwordField = new JPasswordField();
    passwordField.setBounds(200, 300, 200, 30);
    passwordField.setBorder(null);
    add(passwordField);

    JButton loginButton = new JButton("Login");
    loginButton.setBounds(200, 350, 200, 30);
    loginButton.setBorder(null);
    loginButton.setBackground(orangeColor);
    loginButton.setForeground(textColor);
    add(loginButton);

    JLabel registerLabel = new JLabel("Don't have an account? ");
    registerLabel.setForeground(textColor);
    registerLabel.setBounds(150, 410, 200, 30);
    add(registerLabel);

    JButton registerPageButton = new JButton("Register");
    registerPageButton.setBounds(290, 410, 50, 30);
    registerPageButton.setBorder(null);
    registerPageButton.setBackground(null);
    registerPageButton.setForeground(orangeColor);
    add(registerPageButton);

    loginButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String email = emailField.getText();
        @SuppressWarnings("deprecation")
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please fill in all the fields", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

        String query = "SELECT * FROM users WHERE email='" + email + "' AND pass='" + password + "'";
        try {
          Statement statement = connection.createStatement();
          ResultSet resultSet = statement.executeQuery(query);

          if (resultSet.next()) {
            dispose();
            new Home();
            JOptionPane.showMessageDialog(null, "Login successful", "Success", JOptionPane.INFORMATION_MESSAGE);
          } else {
            JOptionPane.showMessageDialog(null, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (SQLException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error connecting to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    registerPageButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new Register();

      }
    });

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  
}