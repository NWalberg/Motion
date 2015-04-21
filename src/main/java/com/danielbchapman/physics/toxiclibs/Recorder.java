package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.danielbchapman.utility.FileUtil;

import lombok.Data;
import lombok.Getter;
import processing.core.PConstants;
import processing.event.MouseEvent;

@Data
public class Recorder
{
  public final static String CAPTURE_FOLDER = "captures";
  
  public static void save(ArrayList<RecordAction> actions, int w, int h)
  {
    StringBuilder b = new StringBuilder();
    
    for(int i = 0; i < actions.size(); i++)
    {
     b.append(RecordAction.toFloatFormat(actions.get(i), w, h));
     b.append("\n");
    }
    
    FileUtil.makeDirs(new File(CAPTURE_FOLDER));
    FileUtil.writeFile(CAPTURE_FOLDER + "/capture-" + new SimpleDateFormat("yyyyMMdd-HH-mm-ss").format(new Date()), b.toString().getBytes());
  }
  
  ArrayList<RecordAction> actions = new ArrayList<RecordAction>();
  @Getter
  boolean recording = false;
  long start = -1L;

  public void capture(MouseEvent e)
  {
    if (!recording)
      return;

    int y = e.getY();
    int x = e.getX();
    int time = (int) (System.currentTimeMillis() - start);

    boolean left = false;
    boolean right = false;
    // boolean center = false;
    int button = e.getButton();
    if (button == PConstants.LEFT)
    {
      left = true;
    }
    else
      if (button == PConstants.RIGHT)
      {
        right = true;
      }
    // Key events are disabled...
    RecordAction tmp = new RecordAction("Captured", time, x, y, left, right, false);
    actions.add(tmp);
    System.out.println("Adding action " + tmp);
  }

  public ArrayList<RecordAction> stop()
  {
    recording = false;
    start = -1L;
    ArrayList<RecordAction> cp = new ArrayList<>();
    for (RecordAction a : actions)
      cp.add(a);
    actions.clear();
    return cp;
  }

  public void start()
  {
    recording = true;
    start = System.currentTimeMillis();
  }

  /**
   * Take a list of actions and turn it into a cue stack
   * @param actions the actions to prepare
   * @param layer the layer to use (future hook)
   * @param engine the engine to use
   * @return A constructed CueStack object
   * 
   */
  public static Playback playback(ArrayList<RecordAction> actions, Layer layer, MotionEngine engine)
  {
    System.out.println("Creating stack: " + actions.size());

    return new Playback("playback", actions);
  }
}
