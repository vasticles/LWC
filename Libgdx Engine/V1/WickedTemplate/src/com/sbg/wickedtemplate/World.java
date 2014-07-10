/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.io.Reader;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sbg.wickedtemplate.layers.Layer;
import com.sbg.wickedtemplate.utils.EffectsAdapter;

/**
World class does as described - sets up the world (or live wallpaper in simple terms). Works in conjunction
with world renderer. World propagates all the physics before they get drawn by the World Renderer.
 **/

public class World {
	private Array<Layer> mLayers;
	private WorldRenderer mWorldRenderer;
	private static Resolver mResolver;
	
	//Construct world and its layers
	public World(List<LWP_Config.GroupMeta> groupMetas) {
		//create layers from each group meta
		if(groupMetas != null) {
			mLayers = new Array<Layer>();
			for(LWP_Config.GroupMeta groupMeta : groupMetas) {
				//if not disabled, each new layer will parse respective json file.
				//can't be optimized with concurrency
				if(!groupMeta.isDisabled) {
					Layer l = new Layer(Gdx.files.internal(groupMeta.filePath));
					l.setIndex(groupMeta.index);		
					mLayers.add(l);
				}
			}
		}
		
		//initialize each layer's groups (just one group/layer for now).
		//this can be optimized with concurrency
		if(mLayers != null) {
			for(Layer l : mLayers) {
				l.init();
			}
		}
	}
	
	public void setResolver(Resolver resolver) {
		mResolver = resolver;
	}

	public void setRenderer(WorldRenderer wr2) {
		mWorldRenderer = wr2;
	}
	
	public Array<Layer> getLayers() {
		return mLayers;
	}

	public static Resolver getResolver() {
		return mResolver;
	}
	
	public void update() {
		for(Layer l : mLayers) {
			l.getGroup().update();
		}
	}
	
	public void dispose() {
		Assets.dispose();
	}
}
