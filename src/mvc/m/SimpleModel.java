package mvc.m;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

import mvc.v.ProjectObserver;
import mvc.v.WorkspaceObserver;

public class SimpleModel
{
  private static SimpleModel            mInstance;
  
  private int                           mPrjIndex           = 0;
  private LinkedList<ProjectModel>      mPrjList            = new LinkedList<ProjectModel>();
  private LinkedList<WorkspaceObserver> mWorkspaceObservers = new LinkedList<WorkspaceObserver>();
  private LinkedList<ProjectObserver>   mProjectObservers   = new LinkedList<ProjectObserver>();
  
  /**
   * Constructor.
   */
  public SimpleModel(ClassLock Lock)
  {
  }
  
  public static SimpleModel Api()
  {
    if (mInstance == null)
      mInstance = new SimpleModel(new ClassLock());
    
    return mInstance;
  }
  
  public void AddCommand()
  {
    String[] NewData = {"New"};
    mPrjList.get(mPrjIndex).mCommand.addRow(NewData);
    
    // Notify all the observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      Ob.UpdateCommand(mPrjList.get(mPrjIndex).mCommand.getRowCount() - 1);
    }
  }
  
  public void AddCommand(String Command)
  {
    String[] NewData = {Command};
    mPrjList.get(mPrjIndex).mCommand.addRow(NewData);
    
    // Notify all the observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      Ob.UpdateCommand(mPrjList.get(mPrjIndex).mCommand.getRowCount() - 1);
    }
  }
  
  public void RemoveCommand(int Index)
  {
    if (Index >= mPrjList.get(mPrjIndex).mCommand.getRowCount())
      return;
    
    mPrjList.get(mPrjIndex).mCommand.removeRow(Index);
    
    // Notify all the observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      if (Index < mPrjList.get(mPrjIndex).mCommand.getRowCount())
        Ob.UpdateCommand(Index);
      else if (mPrjList.get(mPrjIndex).mCommand.getRowCount() > 0)
        Ob.UpdateCommand(mPrjList.get(mPrjIndex).mCommand.getRowCount() - 1);
      else
        Ob.UpdateCommand(-1);
    }
  }
  
  public void AddProject(String Name, String ToolchainPath, String ProjectPath, DefaultTableModel Command)
  {
    // create project model and append it to the end of the workspace.
    ProjectModel Project = new ProjectModel(Name, ToolchainPath, ProjectPath,
      Command);
    
    // TODO string assignment.
    
    if (!mPrjList.add(Project))
      return;
    
    // Notify all the observers.
    for (WorkspaceObserver Ob : mWorkspaceObservers)
    {
      Ob.AddProjectView(Project.mName, Project.mToolchain, Project.mProject,
        Project.mCommand);
    }
  }
  
  public void DumpAll()
  {
    System.out.println("- Start ------------------------------------>");
    for (int i = 0; i < mPrjList.size(); ++i)
    {
      System.out.println("[" + i + "]");
      System.out.println("name      = " + mPrjList.get(i).mName);
      System.out.println("toolchain = " + mPrjList.get(i).mToolchain);
      System.out.println("project   = " + mPrjList.get(i).mProject);
      System.out.println("command   = ");
      int Size = mPrjList.get(i).mCommand.getRowCount();
      for (int j = 0; j < Size; j++)
      {
        System.out.println("[" + j + "] " + mPrjList.get(i).Command(j));
      }
      System.out.println();
    }
    System.out.println("- End -------------------------------------->");
  }
  
  public void DuplicateProject()
  {
    AddProject(
      ProjectName(),
      ToolchainPath(),
      ProjectPath(),
      mPrjList.get(mPrjIndex).mCommand);
  }
  
  public DefaultTableModel ProjectCommand()
  {
    return mPrjList.get(mPrjIndex).mCommand;
  }
  
  public String ProjectName()
  {
    if (Size() == 0)
      return null;
    
    return mPrjList.get(mPrjIndex).mName;
  }
  
  public String ProjectPath()
  {
    if (Size() == 0)
      return null;
    
    return mPrjList.get(mPrjIndex).mProject;
  }
  
  public boolean RegisterProjectObserver(ProjectObserver Observer)
  {
    return mProjectObservers.add(Observer);
  }
  
  public boolean RegisterWorkspaceObserver(WorkspaceObserver Observer)
  {
    return mWorkspaceObservers.add(Observer);
  }
  
  public void RemoveProject()
  {
    mPrjList.remove(mPrjIndex);
    
    // Notify all the observers.
    for (WorkspaceObserver Ob : mWorkspaceObservers)
    {
      Ob.RemoveProjectView(mPrjIndex);
    }
  }
  
  public void RemoveProject(int pIndex)
  {
    if (pIndex >= mPrjList.size())
      return;
    
    mPrjList.remove(pIndex);
    
    // Notify all the observers.
    for (WorkspaceObserver Ob : mWorkspaceObservers)
    {
      Ob.RemoveProjectView(pIndex);
    }
  }
  
  public int SelectedProject()
  {
    return mPrjIndex;
  }
  
  public boolean SelectProject(int pIndex)
  {
    if (pIndex >= Size() || pIndex < 0)
      return false;
    
    mPrjIndex = pIndex;
    
    return true;
  }
  
  public void SetProjectName(String Name)
  {
    mPrjList.get(mPrjIndex).mName = Name;
  }
  
  public void SetProjectPath(String Path)
  {
    mPrjList.get(mPrjIndex).mProject = Path;
    
    // Notify observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      Ob.UpdateProject(Path);
    }
  }
  
  public void SetToolchainPath(String Path)
  {
    mPrjList.get(mPrjIndex).mToolchain = Path;
    
    // Notify observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      Ob.UpdateToolchain(Path);
    }
  }
  
  public int Size()
  {
    return mPrjList.size();
  }
  
  public void SwapCommand(int IndexA, int IndexB)
  {
    mPrjList.get(mPrjIndex).SwapCommand(IndexA, IndexB);
    
    // Notify observers.
    for (ProjectObserver Ob : mProjectObservers)
    {
      Ob.UpdateCommand(IndexB);
    }
  }
  
  public String ToolchainPath()
  {
    if (Size() == 0)
      return null;
    
    return mPrjList.get(mPrjIndex).mToolchain;
  }
  
  public boolean UnRegisterProjectObserver(ProjectObserver Observer)
  {
    return mProjectObservers.remove(Observer);
  }
  
  public boolean UnRegisterWorkspaceObserver(WorkspaceObserver Observer)
  {
    return mWorkspaceObservers.remove(Observer);
  }
  
  private static class ClassLock
  {
  }
  
  private class ProjectModel
  {
    protected String            mName;
    protected String            mToolchain;
    protected String            mProject;
    protected DefaultTableModel mCommand;
    
    public ProjectModel(String Name, String Toolchain, String Project,
      DefaultTableModel Command)
    {
      mName = Name;
      mToolchain = Toolchain;
      mProject = Project;
      
      if (Command == null)
      {
        String[] Col = {""};
        mCommand = new DefaultTableModel(Col, 0);
      }
      else
      {
        Vector<String> Col = new Vector<String>();
        Col.add("");
        
        Vector<Vector<String>> Data = new Vector<Vector<String>>();
        int Size = Command.getRowCount();
        
        for (int i = 0; i < Size; i++)
        {
          Vector<String> V = new Vector<String>();
          V.add((String) Command.getValueAt(i, 0));
          Data.add(V);
        }
        
        mCommand = new DefaultTableModel(Data, Col);
      }
    }
    
    public String Command(int Index)
    {
      if (Index >= mCommand.getRowCount())
        return null;
      
      return (String) mCommand.getValueAt(Index, 0);
    }
    
    public void SwapCommand(int I, int J)
    {
      // Swap element.
      Collections.swap(mCommand.getDataVector(), I, J);
    }
  }
  
}
