/**
No Effect can be used to idle a sprite without any manipulation for a specified duration.
 **/

package com.sbg.wickedtemplate.effects;

import aurelienribon.tweenengine.Timeline;

import com.sbg.wickedtemplate.ESprite;

public class NoEffect extends Effect {
	public NoEffect() {
	}

	public NoEffect(ESprite s, Effect e) {
		super(s, e);
		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		timeline.pushPause(duration).build();

		if (endPause > 0)
			timeline.pushPause(endPause).build();

		if (repeat) {
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
			else
				timeline.repeatYoyo(reps, repDelay).build();
		}
	}

	// @Override
	// public ESprite update(long elapsedTime, ESprite sprite) {
	// // TODO Auto-generated method stub
	// return sprite;
	// }

	@Override
	public void draw() {
		// TODO Auto-generated method stub

	}

}
