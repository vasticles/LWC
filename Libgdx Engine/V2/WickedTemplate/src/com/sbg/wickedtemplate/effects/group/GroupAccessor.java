package com.sbg.wickedtemplate.effects.group;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.math.Vector2;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.LWP_Engine;

/**
Group Accessor is essentially a definition class for the tween engine for groups.
Here you specify the properties that need to be interpolated and how the interpolations should be performed.
 **/

public class GroupAccessor implements TweenAccessor<Group> {
	
	private Vector2 change = new Vector2();
	
	//Defined properties for manipulation
	public static final int ALPHA=0;
	public static final int ROTATION=1;
	public static final int SCALE=2;
	public static final int POSITION=3;
	public static final int VELOCITY=4;

	
	//target is the target object, tweenType is the property to be returned, returnValues are the current values of the property
	@Override
	public int getValues(Group target, int tweenType, float[] returnValues) {
		switch(tweenType) {
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		case ROTATION:
			returnValues[0] = target.getRotation();
			return 1;
		case SCALE:
			returnValues[0] = target.getScaleX();
			returnValues[1] = target.getScaleY();
			return 2;
		case POSITION:
			returnValues[0] = target.getUnconstrainedGroupPosition().x;
			returnValues[1] = target.getUnconstrainedGroupPosition().y;
			return 2;
		case VELOCITY:
			returnValues[0] = target.getAngle();
			returnValues[1] = target.getRate();
			return 2;
		default:
			LWP_Engine.log.error("IN DEFAULT FOR SOME REASON");
			assert false;
			return -1;
		}
	}
	
	//target is the target object, tweenType is the property to be modified, newValues are the new values of the property to be changed
	@Override
	public void setValues(Group target, int tweenType, float[] newValues) {
		switch(tweenType) {
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g, target.getColor().b, newValues[0]);
			break;
		case ROTATION:
			target.setRotation(newValues[0]);
			break;
		case SCALE:
			target.setScale(newValues[0], newValues[1]);
			break;
		case POSITION:
			target.setGroupPosition(change.set(newValues[0], newValues[1]));
			break;
		case VELOCITY:
			target.setVelocity(newValues[0], newValues[1]);
			break;
		default:
			assert false;
			break;
		}
	}

}
