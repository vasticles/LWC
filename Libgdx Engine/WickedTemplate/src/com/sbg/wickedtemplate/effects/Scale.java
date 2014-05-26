package com.sbg.wickedtemplate.effects;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;

public class Scale extends Effect {

	public Scale() {
	}

	public Scale(ESprite s, Effect e) {
		super(s, e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		timeline.push(Tween.set(s, SpriteAccessor.SCALE).target(1f, 1f)).push(
				Tween.to(s, SpriteAccessor.SCALE, duration).target(2.5f, 3.5f));

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
