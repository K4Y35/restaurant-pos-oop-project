import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;

public class Register extends JFrame {

  //DATABASE CONNECTION START
  private Connection connection = DBConnection.getConnection();
  //DATABASE CONNECTION END

  Register() {
    Color lightBlueColor = new Color(36, 40, 55, 255);
    Color darkBlueColor = new Color(30, 28, 43, 255);
    Color textColor = new Color(255, 255, 255, 255);
    Color orangeColor = new Color(228, 76, 28);
    Font titleFont = new Font("Berlow", Font.BOLD, 30);

    //        setSize(1200, 800);
    setSize(500, 600);
    setLocationRelativeTo(null);
    setLayout(null);
    setTitle("Aakhri Pasta Registration");
    setResizable(false);
    getContentPane().setBackground(lightBlueColor);

    ImageIcon logoIcon = new ImageIcon("src/images/logo.png");

    JLabel logoLabel = new JLabel(logoIcon);
    logoLabel.setBounds(100, 0, 300, 205);
    logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
    add(logoLabel);

    JLabel nameLabel = new JLabel("Name:");
    nameLabel.setForeground(textColor);
    nameLabel.setBounds(100, 250, 100, 30);
    add(nameLabel);

    JTextField nameField = new JTextField();
    nameField.setBounds(200, 250, 200, 30);
    add(nameField);

    JLabel emailLabel = new JLabel("Email:");
    emailLabel.setForeground(textColor);
    emailLabel.setBounds(100, 300, 100, 30);
    add(emailLabel);

    JTextField emailField = new JTextField();
    emailField.setBounds(200, 300, 200, 30);
    add(emailField);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setForeground(textColor);
    passwordLabel.setBounds(100, 350, 100, 30);
    add(passwordLabel);

    JPasswordField passwordField = new JPasswordField();
    passwordField.setBounds(200, 350, 200, 30);
    add(passwordField);

    JButton registerButton = new JButton("Register");
    registerButton.setBounds(200, 400, 200, 30);
    registerButton.setBorder(null);
    registerButton.setBackground(orangeColor);
    registerButton.setForeground(textColor);
    add(registerButton);

    JLabel registerLabel = new JLabel("Already have an account? ");
    registerLabel.setForeground(textColor);
    registerLabel.setBounds(150, 460, 200, 30);
    add(registerLabel);

    JButton loginPageButton = new JButton("Login");
    loginPageButton.setBounds(300, 460, 50, 30);
    loginPageButton.setBorder(null);
    loginPageButton.setBackground(null);
    loginPageButton.setForeground(orangeColor);
    add(loginPageButton);

    registerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = nameField.getText();
        String email = emailField.getText();
        @SuppressWarnings("deprecation")
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please fill in all the fields", "Error", JOptionPane.ERROR_MESSAGE);
          return;
        }

//     // Email validation using regex
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        Pattern emailPattern = Pattern.compile(emailRegex);
//        Matcher emailMatcher = emailPattern.matcher(email);
//
//        if (!emailMatcher.matches()) {
//          JOptionPane.showMessageDialog(null, "Invalid email address", "Error", JOptionPane.ERROR_MESSAGE);
//          return;
//        }
//
//        // Password validation using regex (at least 8 characters, at least one uppercase letter, one lowercase letter, and one digit)
//        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
//        Pattern passwordPattern = Pattern.compile(passwordRegex);
//        Matcher passwordMatcher = passwordPattern.matcher(password);
//
//        if (!passwordMatcher.matches()) {
//          JOptionPane.showMessageDialog(null, "Invalid password format. Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit.", "Error", JOptionPane.ERROR_MESSAGE);
//          return;
//        }
        
        
        String insertQuery = "INSERT INTO users (name, email, pass) VALUES ('" + name + "', '" + email + "', '" + password + "')";

        try (Statement statement = connection.createStatement()) {
          int rowsAffected = statement.executeUpdate(insertQuery);

          if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new Login();
          } else {
            JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
          }
        } catch (SQLException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error storing data in the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    });

    loginPageButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
        new Login();

      }
    });

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

}