/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.Iterator;
import java.util.List;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.utils.Array;

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
	private static float oneFrame = LWP_Engine.targetFPS/1000f; //duration of one frame
	
	
	//SPRITE STATE MANAGER
	public StateManager(ESprite sprite, List<State> spriteStates) {
		this.sprite = sprite;
		states = new Array<State>();
		iter = states.iterator();
		
		//run through the state list and create each state
		for (State st : spriteStates) {
			State s = new State(st);
			//initialize the state
			s.init();
			Timeline timeline = s.getTimeline();
			
			//parallels are used for simultaneous effects. for sequential effects, simply create more states
			timeline.beginParallel();
			
			//run through the effects under each state and add them to the timeline
			for (Effect e : st.effectsList) {
				
				//create effect using effect factory
				Effect eff;
				if(!e.isDisabled) {
					if(e.durations != null) {
						eff = Effect.createNewEffect(e, sprite);
					
						//effects array isn't really used, but I kept it for future reference just in case
						s.effects.add(eff);
						
						//add effect to the state's timeline
						timeline.push(eff.getTimeline());
					}
				}
			}
			
			//set the total duration of the state based on effects durations
			s.setStateDuration();
			
			//stop the parallel
			timeline.end();
			
			//add the intialized state to the array
			if(s.getStateDuration() != 0 || s.getPhase() == Phase.DEAD || s.getPhase() == Phase.ETERNAL) states.add(s);
		}
		
		//set indices
		state = states.get(0);
		phase = state.phase;
		
		//create tween manager
		tweenManager = new TweenManager();
	}
	
	
	//GROUP STATE MANAGER
	public StateManager(Group group, List<State> spriteStates) {
		this.sprite = group;
		states = new Array<State>();
		iter = states.iterator();
		
		//run through the state list and create each state
		for (State st : spriteStates) {
			State s = new State(st);
			//initialize the state
			s.init();
			Timeline timeline = s.getTimeline();
			
			//parallels are used for simultaneous effects. for sequential effects, simply create more states
			timeline.beginParallel();
			
			//run through the effects under each state and add them to the timelinee
			for (Effect e : st.effectsList) {
				
				//create effect using effect factory
				Effect eff;
				if(!e.isDisabled) {
					if(e.durations != null) {
						eff = Effect.createNewEffect(e, group);
					
						//effects array isn't really used, but I kept it for future reference just in case
						s.effects.add(eff);
						
						//add effect to the state's timeline
						timeline.push(eff.getTimeline());
					}
				}
			}
			
			//set the total duration of the state based on effects durations
			s.setStateDuration();
			
			//stop the parallel
			timeline.end();
			
			//add the intialized state to the array
			if(s.getStateDuration() != 0 || s.getPhase() == Phase.DEAD || s.getPhase() == Phase.ETERNAL) states.add(s);
		}
		
		//set indices
		state = states.get(0);
		phase = state.phase;
		
		//create tween manager
		tweenManager = new TweenManager();
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
			phase = state.getPhase();
			sprite.setState(state); //not really used, but kept for consistency
			sprite.setPhase(phase); 
		}
	}
	
	public boolean update(float elapsedTime) {
		//update state duration
		float stateDuration = state.updateDuration(elapsedTime);
		
		//obtain state timeline
		Timeline stateTimeline = state.getTimeline();
		
		//start it if it's not already startedd
		if(!stateTimeline.isStarted()) stateTimeline.start(tweenManager);
		
		//update the timeline and its effects
		stateTimeline.update(elapsedTime);
		
		//static sprite, no updating needed
		if(phase == Phase.ETERNAL)
			return true;
		
		//sprite expired, return false to remove it from array
		if(stateDuration <= oneFrame && phase == Phase.DEAD) {
			return false;
		}
		
		//debugging
//		Group g = sprite.getGroup();
//		LWP_Engine.log.error("ID: "+sprite.id+" State: "+state.name+" Duration: "+stateDuration+" Phase: "+phase.toString()+" Scale: "+sprite.getScaleX());

		//switch statee
		if (stateDuration <= oneFrame) {
			nextState();
		}
		
		return true;
	}
}
