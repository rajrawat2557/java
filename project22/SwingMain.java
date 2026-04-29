import javax.swing.SwingUtilities;

public class SwingMain {
   public SwingMain() {
   }

   public static void main(String[] var0) {
      SwingUtilities.invokeLater(() -> (new AuthFrame()).setVisible(true));
   }
}
