package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.effects.group.GroupAccessor;
import com.sbg.wickedtemplate.utils.Utils;

public class Rotation extends Effect {

	public Rotation() {
	}

	public Rotation(Object s, Effect e) {
		super(e);

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}
		
		if (s instanceof Group) {
			Group g = (Group) s;
//			startValueA = Utils.percentToPixel(startValueA, g.screenWidthPix);
//			startValueB = Utils.percentToPixel(startValueB, g.screenHeightPix);
//			endValueA = Utils.percentToPixel(endValueA, g.screenWidthPix);
//			endValueB = Utils.percentToPixel(endValueB, g.screenHeightPix);

			timeline.push(Tween.set(s, GroupAccessor.ROTATION).target(startValueA))
					.push(Tween.to(s, GroupAccessor.ROTATION, duration).target(endValueA));
		} else {
			s = (ESprite) s;
			timeline.push(Tween.set(s, SpriteAccessor.ROTATION).target(startValueA))
					.push(Tween.to(s, SpriteAccessor.ROTATION, duration).target(endValueA));
		}

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
