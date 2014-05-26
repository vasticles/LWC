/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.Group;

/**
Abstract Layer class. Layer is responsible for holding groups. This is an earlier design, prior to json parsing.
Will ditch all the extended layer classes once json parsing for layers is set up.
At the moment json parsing begins at group level from within each layer.
I haven't thought about deeper functionality for layers other than controlling visibility(on/off), opacity and
order of drawing.
Layers can have their own dimensions, however this is not yet configured.
 **/

public abstract class Layer {

	public Array<Group> groups;
	public Group g;
	public int index;
	public int opacity;
	public boolean enabled = true;
	public float width = Gdx.graphics.getWidth();
	public float height = Gdx.graphics.getHeight();
	
	public Layer(int i) {
		index=i;
		opacity=100;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index=i;
	}
	
	public void setOpacity(int op) {
		opacity=op;
	}
	
	public int getOpacity() {
		return opacity;
	}
	
	public void setEnabled(boolean enab) {
		enabled=enab;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public Array<Group> getGroups() {
		return groups;
	}
	
	public Group getGroup() {
		return g;
	}
	
}
