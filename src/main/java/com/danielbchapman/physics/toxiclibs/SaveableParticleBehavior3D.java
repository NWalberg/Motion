package com.danielbchapman.physics.toxiclibs;

import toxi.physics3d.behaviors.ParticleBehavior3D;

public abstract class SaveableParticleBehavior3D<T> implements ParticleBehavior3D, StringSerialize<T>
{
  PersistentVariables vars = new PersistentVariables(); 

  public synchronized void setRunning(boolean running)
  {
    this.vars.running = running;
  }
}
