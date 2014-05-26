/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.google.gson.annotations.SerializedName;
import com.sbg.wickedtemplate.effects.Effect;

/**
*This is an extended sprite class that adds custom functionality.
*Extended sprites have states, phase and effects (though effects have a deeper relationship with states).
*They also have an absolute X and Y fields that are used for proper parallax positioning.
*Each ESprite has a State Manager that is responsible for managing all the states of its sprite.
 **/

public class ESprite extends Sprite {
	private Random r = new Random();
	public int id; //ids are for debugging mainly
	public float absoluteX; //these are used to properly add parallax
	public float absoluteY;
	private Group group;
	public Phase phase;
	public State state;
	public Effect effect;
	private StateManager stateManager;

	public ESprite(Sprite spr) {
		super(spr);
		setAbsoluteXY(getX(), getY());
	}
	
	public ESprite(Sprite spr, List<State> spriteStates) {
		super(spr);
		id = r.nextInt(100);
		//create state manager and give it the list of states
		stateManager = new StateManager(this, spriteStates);
	}
	
	public void setAbsoluteX(float x) {
		absoluteX=x;
	}
	
	public void setAbsoluteY(float y) {
		absoluteY=y;
	}
	
	public void setAbsoluteXY(float x, float y) {
		setAbsoluteX(x);
		setAbsoluteY(y);
	}
	
	public float getAbsoluteX() {
		return absoluteX;
	}
	
	public float getAbsoluteY() {
		return absoluteY;
	}

	public Group getGroup() {
		return group;
	}
	
	public void setState(State s) {
		state = s;
	}
	
	public State getState() {
		return state;
	}
	
	public void setPhase(Phase p) {
		phase = p;
	}
	
	public Phase getPhase() {
		return phase;
	}
	
	public StateManager getStateManager() {
		return stateManager;
	}

	public boolean update(long elapsedTime, Group group) {
		//dirty way of getting the sprite's group reference. I was debugging and I oughtta clean this up
		this.group = group;
		
		setX(getAbsoluteX()+group.parallax);
		setY(getAbsoluteY()); //not really necessary I suppose
		
//		LWP_Engine.log.error("Sprite id: "+id);
		
		//update states
		return stateManager.update(elapsedTime);
	}
}
