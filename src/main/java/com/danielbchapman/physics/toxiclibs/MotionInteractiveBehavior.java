package com.danielbchapman.physics.toxiclibs;

import java.io.File;
import java.util.Map;

import toxi.geom.Vec3D;
import toxi.physics3d.behaviors.ParticleBehavior3D;

import com.danielbchapman.utility.FileUtil;

public abstract class MotionInteractiveBehavior implements ParticleBehavior3D
{
  
  public PersistentVariables vars = new PersistentVariables();
  
  /**
   * <p>
   * Returns a listing of all the field names in the order
   * they are presented in ForceVariables.Fields so that
   * </p>
   * 
   * <p>Returning null for a field indicates it should be hidden from the UI</p> 
   * <p>Returning null for the map indicates everything should be displayed</p>
   * @return a Map giving usable names.
   * 
   */
  public abstract Map<String, String> getFieldNames();
  
  /**
   * return a copy of this instance for use in multiple steps.
   * 
   */
  public abstract MotionInteractiveBehavior copy();
  /**
   * @return the name for this class, null for the class name 
   */
  public abstract String getName();
  
  /**
   * Sets the position for this behavior
   * @param location the location of this behavior  
   * 
   */
  public abstract void setPosition(Vec3D location);
  /**
   * Saves the MotionBehavior from the data which is formatted:
   * <tt>
   * Line 1 className (full)
   * Line 2 data values for variables
   * 
   * </tt> 
   * @param data
   * @return A string following the above, unstable, format  
   * 
   */
  public static String save(MotionInteractiveBehavior behavior)
  {
    StringBuilder b = new StringBuilder();
    b.append(behavior.getClass().getName());
    b.append("\n");
    b.append(PersistentVariables.toLine(behavior.vars));
    b.append("\n");
//    @formatter:off
//    Map<String, String> names = behavior.getFieldNames();
//    boolean first = true;
//    for(String k : ForceVariables.Fields.ALL_FIELDS)
//    {
//      String v = names.get(k);
//      if(v != null && !v.trim().isEmpty())
//      {
//        if(first)
//          first = false;
//        else
//          b.append(",");
//        
//        b.append(v);
//      }
//    }
//      
//    b.append("\n");
//  @formatter:on    
    return b.toString();
  }

  public static <T extends MotionInteractiveBehavior> MotionInteractiveBehavior load(File file)
  {
    return load(FileUtil.readFile(file.getAbsolutePath()));
  }
  /**
   * Loads the MotionBehavior from the data which is formatted:
   * <tt>
   * Line 1 className (full)
   * Line 2 data values for variables
   * </tt> 
   * @param data
   * @return <Return Description>  
   * 
   */
  @SuppressWarnings("unchecked")
  public static <T extends MotionInteractiveBehavior> MotionInteractiveBehavior load(String data)
  {
	  if(data == null)
	  {
		  System.out.println("Unabel to load behavior with null data: ");
		  return null;
	  }
    Class<T> clazz = null;
    String[] lines = data.split("\n");
    
    if(lines.length < 2)
      throw new RuntimeException("Unable to load this data, there is not enough information");
    
    String className = lines[0].trim();
    PersistentVariables vars = PersistentVariables.fromLine(lines[1]);
//    HashMap<String, String> names = new HashMap<String, String>();
    
    try
    {
      clazz = (Class<T>) MotionInteractiveBehavior.class.getClassLoader().loadClass(className);
      T instance = clazz.newInstance();
      instance.vars = vars;
      System.out.println("Returing instance! " + instance);
      return instance;
    }
    catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
    {
      e.printStackTrace();
      return null;
    }

  }
}
