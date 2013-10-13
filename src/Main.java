import javax.swing.SwingUtilities;

import mvc.v.MainView;

public class Main
{
  public static void main(String[] Args)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        MainView View = new MainView();
        View.setVisible(true);
      }
    });
  }
}
