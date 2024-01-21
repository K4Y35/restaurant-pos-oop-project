import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfGenerator {
	  DecimalFormat decimalFormat = new DecimalFormat("#.##");

  public void generatePdf(ArrayList < Home.OrderedItem > orderedItems, String outputPath) {
    try {
      Document document = new Document();
      PdfWriter.getInstance(document, new FileOutputStream(outputPath));
      document.open();

      Image logo = Image.getInstance("src/images/logo.png");
      logo.scaleToFit(200, 200);
      document.add(logo);

      float totalOrderPrice = 0;

      for (Home.OrderedItem orderedItem: orderedItems) {
        int itemId = orderedItem.getItemId();
        int quantity = orderedItem.getQuantity();

        String selectItemQuery = "SELECT * FROM items WHERE id = " + itemId;
        try (Statement statement = DBConnection.getConnection().createStatement()) {
          ResultSet resultSet = statement.executeQuery(selectItemQuery);

          if (resultSet.next()) {
            String itemName = resultSet.getString("name");
            String itemPrice = resultSet.getString("price");

            document.add(new Paragraph("Item Name: " + itemName));
            document.add(new Paragraph("Quantity: " + quantity));
            document.add(new Paragraph("Price: $" + itemPrice));

            totalOrderPrice += Float.parseFloat(itemPrice) * quantity;

            document.add(new Paragraph("--------------------------------------"));
          }
        } catch (SQLException ex) {
          ex.printStackTrace();
          JOptionPane.showMessageDialog(null, "Error retrieving item data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
      }

      document.add(new Paragraph("Total Order Price: $" + decimalFormat.format(totalOrderPrice)));

      document.close();

      JOptionPane.showMessageDialog(null, "Print Order Receipt", "Success", JOptionPane.INFORMATION_MESSAGE);

      openPdfWithDefaultViewer(new File(outputPath));

    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error generating PDF", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  void openPdfWithDefaultViewer(File pdfFile) {
    try {
      if (Desktop.isDesktopSupported()) {
        Desktop desktop = Desktop.getDesktop();
        if (desktop.isSupported(Desktop.Action.OPEN)) {
          desktop.open(pdfFile);
        } else {
          JOptionPane.showMessageDialog(null, "Desktop open action not supported", "Error", JOptionPane.ERROR_MESSAGE);
        }
      } else {
        JOptionPane.showMessageDialog(null, "Desktop not supported", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error opening PDF file", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}