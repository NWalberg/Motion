package com.danielbchapman.physics.toxiclibs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import processing.core.PGraphics;
import toxi.geom.Vec3D;

public abstract class Emitter<T extends Point>
{
  ForceVariables vars = new ForceVariables();
  ArrayList<T> children = new ArrayList<>();
  Random rand = new Random();
  long lastTime = -1L;
  long nextDelta = -1L;
  public Emitter(Vec3D position, Vec3D heading, int lifeSpan, int rate, float randomVector, int randomTime)
  {
    vars.position = position;
    vars.force = heading;
    vars.userA = lifeSpan;
    vars.userB = randomVector;
    vars.userC = randomTime;
    vars.timeStep = rate;
  }
  
  public void update(long time)
  {
    
    if(lastTime == -1L)
    {
      lastTime = time;
      addPoint(time);
      return;
    }
    
    if(time - lastTime > nextDelta)
    {
      addPoint(time);
      lastTime = time;
    }
//    else
//      System.out.println("[SKIPPED] Calling update" + time + ", " + lastTime + " " + nextDelta);
  }
  
  public abstract T createPoint(float x, float y, float z, float w);
  
  public void addPoint(long time)
  {
    T p = createPoint(vars.position.x, vars.position.y, vars.position.z, 1f);
    p.addForce(vars.force);
    if(vars.userB > 0)
      p.addForce(Vec3D.randomVector().scaleSelf(vars.userB));
    if(vars.userC > 0.1f)
      nextDelta = (long) (vars.timeStep + rand.nextInt((int)vars.userC));
    else
      nextDelta = (long) vars.timeStep;
    
    p.life = (int) vars.userA;
    p.created = time;
    children.add(p);
    
    Iterator<T> it = children.iterator();
    while(it.hasNext())
    {
      Point px = it.next(); //This should probably implement a "fadeable" interface
      if(px != null)
        if(px.life < time - px.created)
        {
          Actions.engine.getPhysics().removeParticle(px);
          it.remove();
        }
    }
    
    Actions.engine.getPhysics().addParticle(p);
  }
  
  public abstract void draw(PGraphics g);
}