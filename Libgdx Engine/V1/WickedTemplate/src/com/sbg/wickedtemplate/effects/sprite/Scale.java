package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;

public class Scale extends Effect {

	public Scale() {
	}

	public Scale(Effect e, Object o) {
		super(e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}
		
		//if no start delay, then preset the start value directly
		if(startDelay <= 0) {
			ESprite s = (ESprite) o;
			s.setScale(startValueA, startValueB);
		}

		timeline.push(Tween.set(o, SpriteAccessor.SCALE).target(startValueA, startValueB)).push(
				Tween.to(o, SpriteAccessor.SCALE, duration).target(endValueA, endValueB));

		if (endPause > 0)
			timeline.pushPause(endPause);

		if (repeat) {
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
			else
				timeline.repeatYoyo(reps, repDelay).build();
		} else
			timeline.build();
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
	}

}
