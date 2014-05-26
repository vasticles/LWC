/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.List;

import aurelienribon.tweenengine.Timeline;

import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.effects.Effect;

/**
State class holds various effects that can be applied to the sprite's properties.
A sprite can have an unlimited number of customizable states.
A state's duration directly related to the duration of the effects it holds.
Each state has to be associated with a phase.
Effects get compounded into the tween engine's timeline object. Whenever the state is initialized, the timeline
begins playing.
 **/

public class State {
	public Phase phase;
	public String name;
	public long duration;
	public List<Effect> effectsList;
	public Effect effect;
	public Array<Effect> effects;
	public Timeline timeline;

	public State() {
	}

	public State(State s) {
		name = s.name;
		phase = s.phase;
	}

	public void init() {
		effects = new Array<Effect>();
		// tweenManager = new TweenManager();
		timeline = Timeline.createSequence();
	}

	public void setStateDuration() {
		for (Effect e : effects) {
			// long sum = e.duration+e.offset;
			duration = e.totalDuration > duration ? e.totalDuration : duration;
		}
	}

}