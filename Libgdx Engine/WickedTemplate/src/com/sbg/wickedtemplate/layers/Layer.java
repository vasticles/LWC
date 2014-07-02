/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.Resolver;
import com.sbg.wickedtemplate.World;

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
	private OrthographicCamera camera;
//	private World world;
	private Resolver resolver;
	private float parallax = 1;
	
	public Layer(int i, float p) {
//		world = World.getWorld();
		index = i;
		parallax = p;
		resolver = World.getResolver();
//		opacity=100;
		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);
		camera.position.set(width/2, height/2, 0);
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
	
	public void update() {
		g.update();
	}
	
	public void draw(SpriteBatch batch) {
		camera.position.x = (width / 2) - resolver.getxPixelOffset()*parallax;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		g.drawGroup(batch);
		batch.end();
	}
	
}
