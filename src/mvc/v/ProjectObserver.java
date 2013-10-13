package mvc.v;

public interface ProjectObserver
{
  public void UpdateProject(String Path);
  
  public void UpdateToolchain(String Path);
  
  public void UpdateCommand(int Index);
}
