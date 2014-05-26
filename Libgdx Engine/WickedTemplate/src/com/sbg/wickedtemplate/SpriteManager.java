/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.sbg.wickedtemplate.utils.Utils;

/**
Sprite Manager is responsible for sprite creation, updating and rendering.
Perhaps more controls could be added in the future.
 **/

public class SpriteManager {
	
	private Group group;
	private Array<ESprite> sprites;
	private Array<Sprite> spritePool;
	private Array<Vector2> spawnPointPool;
	private List<State> stateList;
	private Random r = new Random();
	
	public SpriteManager(Group g) {
		//useful to have group references, though maybe I encapsulate things better
		group = g;
		sprites = new Array<ESprite>();
	}
	
	public void createSpritePool(String[] spriteNames, TextureAtlas atlas) {
		if (spritePool == null) spritePool = new Array<Sprite>();
		for (String spriteName : spriteNames) {
			Sprite s = atlas.createSprite(spriteName);
			s.setOrigin(s.getWidth()/2, s.getHeight()/2);
			spritePool.add(s);
		}
	}
	
	public void createSpawnPointPool(float[][] spawnPointValues) {
		if(spawnPointPool == null) spawnPointPool = new Array<Vector2>();
		for(float[] value : spawnPointValues) {
			spawnPointPool.add(new Vector2(Utils.percentToPixel(value[0], group.screenWidthPix), Utils.percentToPixel(value[1], group.screenHeightPix)));
		}
	}
	
	public void createSpriteStateList(List<State> stateList) {
		this.stateList = stateList;
	}
	
	public void prepareSprites() {
		int numOfSprites = group.getNumOfSprites();
		for(int i=1; i<=numOfSprites; i++) {
			sprites.add(spawnRandomSprite());
		}
	}
	
	private Vector2 getRandomSpawnPoint() {
		return spawnPointPool.get(r.nextInt(spawnPointPool.size));
	}
	
	private ESprite spawnRandomSprite() {
		ESprite s = new ESprite(spritePool.get(r.nextInt(spritePool.size)), stateList);
		Vector2 sP = getRandomSpawnPoint();
		s.setPosition(sP.x-s.getWidth()/2, sP.y-s.getHeight()/2);
		s.setAbsoluteXY(sP.x-s.getWidth()/2, sP.y-s.getHeight()/2);
		s.setScale(group.mScreenRatio, group.mScreenRatio);
		s.setX(s.getAbsoluteX()+group.parallax);
		return s;
	}
	

	public void update(long elapsedTime) {
		//using iterator for safe removal of sprites when they expire
		Iterator<ESprite> iter = sprites.iterator();
		while(iter.hasNext()) {
			ESprite s = iter.next();
			
			//if this returns false, that means sprite expired and we have to remove it
			if(!s.update(elapsedTime, group))
				iter.remove();
		}
		
		//spawn a new sprite if there aren't enough
		if(sprites.size < group.numOfSprites) {
			sprites.add(spawnRandomSprite());
		}
	}
	
	public void draw(SpriteBatch batch) {
		for(ESprite s : sprites) {
			//don't draw sprites if they are dead
			if(s.phase != Phase.DEAD) {
//				//debug
//				if(group.name.equals("ForegroundEnergy") && s.phase == Phase.DEAD) {
//					LWP_Engine.log.error("JUST DREW A DEAD SPRITE!");
//					LWP_Engine.log.error("ID: "+s.id+" State: "+s.state.name+" Duration: "+s.state.duration+" Phase: "+s.state.phase.toString()/*" Position: "+sprite.getX()+", "+sprite.getY())*/);
//				}
				s.draw(batch);
			}
		}
	}
}
