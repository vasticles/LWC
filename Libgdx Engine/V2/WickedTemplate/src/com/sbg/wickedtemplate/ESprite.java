/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.utils.Utils;

/**
*This is an extended sprite class that adds custom functionality.
*Extended sprites have states, phase and effects (though effects have a deeper relationship with states).
*They also have an absolute X and Y fields that are used for proper parallax positioning.
*Each ESprite has a State Manager that is responsible for managing all the states of its sprite.
 **/

public class ESprite extends Sprite {
	private Random random = new Random();
	public int id; //ids are for debugging mainly
	public String name;
	public boolean isDisabled;
	
	//animation properties
	public boolean isAnimated;
	public String[] framesNames;
	public float frameDuration;
	public int playMode;
	protected Array<TextureRegion> frames;
	protected Animation anim;
	protected float animationStateTime;
	
	//other properties
	public float xScale;
	public float yScale;
	public float angle;
	public float opacity;
	private float rate;
	private Vector2 velocity = new Vector2();
	protected Vector2 tmpVect = new Vector2();
	protected Vector2 unconstrainedPosition = new Vector2();
	protected Vector2 oldPos = new Vector2();
	protected Vector2 newPosition = new Vector2();
	private Group group;
	public Phase phase;
	public State state;
	public Effect effect;
	private StateManager stateManager;
	public String atlasFilename;

	public ESprite() {}
	
	public ESprite(Sprite spr) {
		super(spr);
	}
	
	public ESprite(ESprite spr, List<State> spriteStates) {
		super(spr);
		name = spr.name;
		id = random.nextInt(100);
		frames = spr.frames;
		isAnimated = spr.isAnimated;
		if(isAnimated) {
			anim = spr.anim;
			frameDuration = spr.frameDuration;
			playMode = spr.playMode;
			animationStateTime = 0f;
		}
//		setRegion(frames.first());
//		setScale(xScale, yScale);
//		setRotation(angle);
//		setColor(getColor().r, getColor().g, getColor().b, opacity/100);
		//create state manager and give it the list of states
		stateManager = new StateManager(this, spriteStates);
	}
	
	//initialize animation here instead of constructor to save cpu cycles
	public void prepareGraphic(Group group) {
		TextureAtlas atlas;
		if(atlasFilename != null)
			atlas = Assets.getAtlas(atlasFilename);
		else
			atlas = Assets.getAtlas(group.groupAtlas);
		frames = new Array<TextureRegion>();
		for(String frameName : framesNames) {
			frames.add(atlas.findRegion(frameName));
		}
		if(isAnimated) {
			anim = new Animation(frameDuration, frames, playMode);
		}
		setRegion(frames.first());
//		setSize(getRegionWidth(), getRegionHeight());
	}
	
	//this is a ghetto method that creates a vector to hold the original coords for the sprite position
	//unmodified by the matrix, but post global scale
	//we need this vector, because we will apply the changing matrix to it
	public void setUnconstrainedPosition(float x, float y) {
//		Utils.log("Group X: "+x+" Group Y: "+y);
		unconstrainedPosition.set(x, y);
//		Utils.log(unconstrainedPosition.toString());
	}
	
	public void setVelocity(float angle, float rate) {
		this.rate = rate;
//		Utils.log(rate+"")
		velocity.set(rate, 0).setAngle(angle);
	}
	
	public float getAngle() {
		return velocity.angle();
	}
	
	public float getRate() {
		return rate;
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
	
	public boolean update(float elapsedTime, Group group) {
		this.group = group;
		
		if(isAnimated) {
			animationStateTime+=elapsedTime;
			setRegion(anim.getKeyFrame(animationStateTime));
		}
		
		tmpVect.set(velocity.x*elapsedTime, velocity.y*elapsedTime);
		newPosition = unconstrainedPosition.cpy().mul(group.matrix);
//		Utils.log("Sprite ID: "+id+" Unconstrained: "+unconstrainedPosition.toString());
//		Utils.log("Sprite ID: "+id+" Matrix constrained: "+newPosition.toString());
		setPosition(newPosition.x, newPosition.y);
//		Utils.log("Sprite ID: "+id+" Actual position: "+getX()+", "+getY());
		translate(tmpVect.x, tmpVect.y);

		//update states
		return stateManager.update(elapsedTime);
	}
}
