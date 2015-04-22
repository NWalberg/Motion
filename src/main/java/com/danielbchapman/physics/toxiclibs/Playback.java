package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;

import lombok.Data;
import lombok.Getter;

@Data
public class Playback
{
  private String label;
  private long start = -1L;
  @Getter
  private boolean running;
  private RecordAction[] actions;
  
  private int last = -1;
  private int size = -1;
  
  public Playback(String label, ArrayList<RecordAction> actions)
  {
    this.actions = new RecordAction[actions.size()];
    for(int i = 0; i < actions.size(); i++)
      this.actions[i] = actions.get(i);
    
    this.label = label;
  }
  
  public void start()
  {
    size = actions.length;
    if(size > 1)
    {
      running = true;
      start = System.currentTimeMillis();  
    }
  }
  
  public void poll(MotionEngine e)
  {
    if(!running)
      return;
    
    long max = System.currentTimeMillis() - start;
    if(last == -1)
      last = 0;
    
    for(; last < size; last++)
    {
      if(actions[last].stamp > max)
        return; // try again next loop
      
      e.robot(actions[last]);
    }
    
    RecordAction copy = actions[last -1];
    copy.leftClick = false;
    copy.rightClick = false;
    copy.keyEvent = false;
    
    e.robot(copy);
    System.out.println("Polling complete");
    ArrayList<RecordAction> cp = new ArrayList<>();
    for(int i = 0; i < actions.length; i++)
      cp.add(actions[i]);
    //Recorder.save(cp, e.getWidth(), e.getHeight());
    running = false;
    last = -1;
    size = -1;
  }
}
