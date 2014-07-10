package com.sbg.wickedtemplate.effects.sprite;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.math.Vector2;
import com.sbg.wickedtemplate.ESprite;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.utils.Utils;

public class Position extends Effect {

	public Position() {
	}

	public Position(Effect e, Object o) {
		super(e);
		

		if (startDelay > 0) {
			timeline = Timeline.createSequence().pushPause(startDelay);
		} else {
			timeline = Timeline.createSequence();
		}

		
		ESprite sp = (ESprite) o;
		Group g = sp.getGroup();
		Vector2 offset = g.getOffset();
		startValueA = Utils.percentToPixel(startValueA, g.getWidth())+offset.x;
		startValueB = Utils.percentToPixel(startValueB, g.getHeight())+offset.y;
		endValueA = Utils.percentToPixel(endValueA, g.getWidth())+offset.x;
		endValueB = Utils.percentToPixel(endValueB, g.getHeight())+offset.y;
		
		//if no start delay, then preset the start value directly
		if(startDelay <= 0) {
			ESprite s = (ESprite) o;
			s.setPosition(startValueA, startValueB);
		}

		timeline.push(Tween.set(sp, SpriteAccessor.POSITION).target(startValueA, startValueB))
				.push(Tween.to(sp, SpriteAccessor.POSITION, duration).target(endValueA, endValueB));

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
