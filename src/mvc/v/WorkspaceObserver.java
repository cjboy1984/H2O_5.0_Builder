package mvc.v;

import javax.swing.table.DefaultTableModel;

public interface WorkspaceObserver
{
  public void AddProjectView(String Name, String Toolchain, String Project, DefaultTableModel Command);
  
  public void RemoveProjectView(int Index);
}
