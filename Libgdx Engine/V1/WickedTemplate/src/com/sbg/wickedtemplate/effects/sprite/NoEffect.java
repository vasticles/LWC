/**
No Effect can be used to idle a sprite without any manipulation for a specified duration.
 **/

package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;

public class NoEffect extends Effect {
	public NoEffect() {
	}

	public NoEffect(Effect e) {
		super(e);
		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		timeline.pushPause(duration);

		if (endPause > 0)
			timeline.pushPause(endPause);

		if (repeat) {
			if (!yoyo)
				timeline.repeat(reps, repDelay).build();
			else
				timeline.repeatYoyo(reps, repDelay).build();
		} else {
			timeline.build();
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
