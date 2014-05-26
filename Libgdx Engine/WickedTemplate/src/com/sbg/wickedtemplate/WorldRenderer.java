/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.layers.Layer;

/**
World Renderer is responsible for drawing the world.
 **/

public class WorldRenderer {
	
	private World mWorld;
	private Array<Layer> mLayers;
	
	public WorldRenderer(World world) {
		mWorld=world;
		mLayers=world.getLayers();
	}
	
	public void render(SpriteBatch batch) {
		for(Layer l : mLayers) {
			l.getGroup().draw(batch);
		}
	}
}
