/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate.layers;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.LWP_Engine;
import com.sbg.wickedtemplate.Resolver;
import com.sbg.wickedtemplate.World;
import com.sbg.wickedtemplate.utils.EffectsAdapter;

/**
Abstract Layer class. Layer is responsible for holding groups. This is an earlier design, prior to json parsing.
Will ditch all the extended layer classes once json parsing for layers is set up.
At the moment json parsing begins at group level from within each layer.
I haven't thought about deeper functionality for layers other than controlling visibility(on/off), opacity and
order of drawing.
Layers can have their own dimensions, however this is not yet configured.
 **/

public class Layer {

	public Array<Group> groups;
	public Group group;
	public int index;
	public int opacity;
	public boolean enabled = true;
	public float width = Gdx.graphics.getWidth();
	public float height = Gdx.graphics.getHeight();
	private OrthographicCamera camera;
//	private World world;
	private Resolver resolver;
	protected float parallax = 1;
	private float scale = LWP_Engine.getScreenRatio();
	
	//this is just a temporary line so I dont have to delete all the extended layers
	public Layer() {}
	
	public Layer(FileHandle config) {
		//parse group configs from json
		parseJsonFile(config);

//		resolver = World.getResolver();
		camera = new OrthographicCamera(width, height);
		camera.setToOrtho(false, width, height);
//		camera.position.set(width/2, height/2, 0);
	}
	
	public void parseJsonFile(FileHandle file) {
		Reader configFile = file.reader();
		
		//Use gson to read json
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		//register our custom adapter
		gsonBuilder.registerTypeAdapter(Effect.class, new EffectsAdapter());
		Gson gson = gsonBuilder.create();
		try {
			group=gson.fromJson(configFile, Group.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init() {
		if(group!=null) {
			//initialize group
			group.init();
			//update parallax
			parallax=group.getParallax();
		}
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
		return group;
	}
	
	public void update() {
		group.update();
	}
	
	public void draw(SpriteBatch batch) {
		if(resolver == null) resolver = World.getResolver();
		camera.position.x = width/2 - resolver.getxPixelOffset()*parallax;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		group.drawGroup(batch);
		batch.end();
	}
	
}
