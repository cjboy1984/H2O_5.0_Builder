package mvc;

public class SimpleEvent
{
  private Object       mSource;
  private SimpleAction mAction;
  
  public SimpleEvent(Object pSource, SimpleAction pAction)
  {
    mSource = pSource;
    mAction = pAction;
  }
  
  public Object Source()
  {
    return mSource;
  }
  
  public SimpleAction Action()
  {
    return mAction;
  }
  
}
