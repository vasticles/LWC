/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.layers.Background;
import com.sbg.wickedtemplate.layers.BackgroundEnergy;
import com.sbg.wickedtemplate.layers.BackgroundLightning;
import com.sbg.wickedtemplate.layers.ForegroundEnergy;
import com.sbg.wickedtemplate.layers.ForegroundLightning;
import com.sbg.wickedtemplate.layers.Goku;
import com.sbg.wickedtemplate.layers.Layer;

/**
World class does as described - sets up the world (or live wallpaper in simple terms). Works in conjunction
with world renderer. World propagates all the physics before they get draw by the World Renderer.
 **/

public class World {
	private Array<Layer> mLayers;
	private WorldRenderer mWorldRenderer;
	private static Resolver mResolver;
	
	
	//Construct world and its layers
	public World(Resolver resolver) {
		mResolver = resolver;
		mLayers = new Array<Layer>();
//		mLayers.add(new Background(0));
//		mLayers.add(new BackgroundLightning());
//		mLayers.add(new BackgroundEnergy());
//		mLayers.add(new Goku(3));
		mLayers.add(new ForegroundEnergy(4));
//		mLayers.add(new ForegroundLightning(5));
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
	
}
