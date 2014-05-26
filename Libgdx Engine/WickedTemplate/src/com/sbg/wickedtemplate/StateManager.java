/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.Iterator;
import java.util.List;

import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.effects.Effect;

/**
State Manager is responsible for managing the states of its sprite throughout its lifetime.
 **/

public class StateManager {
	private ESprite sprite;
	private State state;
	private Array<State> states;
	private Phase phase;
	private TweenManager tweenManager; //one tween manager for all sprite's effects is sufficient. maybe it can even be used for all sprites (needs verification)
	final private Iterator<State> iter; //we'll use this iterator for state switching
	
	public StateManager(ESprite sprite, List<State> spriteStates) {
		this.sprite = sprite;
		states = new Array<State>();
		iter = states.iterator();
		
		//run through the state list and create each state
		for (State st : spriteStates) {
			State s = new State(st);
			//initialize the state
			s.init();
			
			//parallels are used for simultaneous effects. for sequential effects, simply create more states
			s.timeline.beginParallel();
			
			//run through the effects under each state and add them to the timeline
			for (Effect e : st.effectsList) {
				
				//create effect using effect factory
				Effect eff = Effect.createNewEffect(e, sprite);
				
				//effects array isn't really used, but I kept it for future reference just in case
				s.effects.add(eff);
				
				//add effect to the state's timeline
				s.timeline.push(eff.getTimeline());
			}
			
			//set the total duration of the state based on effects durations
			s.setStateDuration();
			
			//stop the parallel
			s.timeline.end();
			
			//add the intilized state to the array
			states.add(s);
		}
		
		//set indices
		state = states.get(0);
		phase = state.phase;
		
		//create tween manager
		tweenManager = new TweenManager();
		
		//plug in the first timeline
		state.timeline.start(tweenManager);
	}
	
	//not used
	public void addState(State s) {
		states.add(s);
	}
	
	//not used
	public void removeState(State s) {
		states.removeValue(s, true);
	}
	
	//state switching method. I had issues with the iterator properly assigning references, so I had to null current state before assigning new one.
	public void nextState() {
		if(iter.hasNext()) {
			state = null;
			state = iter.next();
			phase = null;
			phase = state.phase;
			sprite.setState(state); //not really used, but kept for consistency
			sprite.setPhase(phase); 
			state.timeline.start(tweenManager); //start the next timelne
		}
	}
	
	public boolean update(long elapsedTime) {
		state.duration -= elapsedTime;
		
		//static sprite, no updating needed
		if(phase == Phase.ETERNAL)
			return true;
		
		//sprite expired, return false to remove it from array
		if(state.duration <= 0 && state.phase == Phase.DEAD) {
			return false;
		}
		
		//switch state
		if (state.duration <= 0) {
			nextState();
		}
		
		//update the timeline and its effects. I should verify if this can properly work with elapsedTime/1000f
		state.timeline.update(elapsedTime/1000f);
		
		//debugging
		Group g = sprite.getGroup();
		if(g.name.equals("ForegroundEnergy")) LWP_Engine.log.error("ID: "+sprite.id+" State: "+state.name+" Duration: "+state.duration+" Phase: "+state.phase.toString()+/*" Position: "+sprite.getX()+", "+sprite.getY())*/" Rotation: "+sprite.getRotation());
		
		return true;
	}
	
	
}
