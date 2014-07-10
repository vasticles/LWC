/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Rectangle;
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
	private Array<ESprite> spritePool;
	private Array<Vector2> spawnPointPool;
	private List<State> stateList;
	private Random r = new Random();
//	private String atlas = Assets.manager.get(Assets.atlasName);
	
	public SpriteManager(Group g) {
		//useful to have group references, though maybe I should encapsulate things better
		group = g;
		sprites = new Array<ESprite>();
	}
	
	public void createSpritePool(List<ESprite> spriteSpecs) {
		if (spritePool == null) spritePool = new Array<ESprite>();
		for (ESprite s : spriteSpecs) {
			if(s.isDisabled) continue;
			s.prepareGraphic(group);
			s.setSize(s.getRegionWidth(), s.getRegionHeight());
			s.setOrigin(s.getWidth()/2, s.getHeight()/2);
			s.setScale(group.mScreenRatio);
			spritePool.add(s);
		}
	}
	
	public void createSpawnPointPool(float[][] spawnPointValues) {
		if(spawnPointPool == null) spawnPointPool = new Array<Vector2>();
		for(float[] value : spawnPointValues) {
			Vector2 point = new Vector2(Utils.percentToPixel(value[0], group.mGroupWidthPix), Utils.percentToPixel(value[1], group.mGroupHeightPix)).scl(group.mScreenRatio).add(group.getOffset());
			spawnPointPool.add(point);
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
		Vector2 point = spawnPointPool.get(r.nextInt(spawnPointPool.size)).cpy();
		return point;
	}
	
	
	private ESprite spawnRandomSprite() {
		ESprite s = new ESprite(spritePool.get(r.nextInt(spritePool.size)));
		Vector2 sP = getRandomSpawnPoint();
//		s.setUnconstrainedPosition(sP.x, sP.y);
		s.setPosition(sP.x, sP.y);
//		Utils.log("SpawnPoint set: "+s.getX()+","+s.getY());
		s.prepareStates(stateList);
//		Utils.log("StateManager started: "+s.getX()+","+s.getY());
//		Utils.log("S width:"+s.getWidth()+" S height: "+s.getHeight());
		return s;
	}
	
	private boolean isOutsideOfGroup(ESprite s) {
		Rectangle groupRec = group.getBoundingRectangle();
		Rectangle spriteRec = s.getBoundingRectangle();
//		Utils.log(groupRec.toString());
//		Utils.log(spriteRec.toString());
		float xmin = groupRec.x;
		float xmax = xmin + groupRec.width;

		float ymin = groupRec.y;
		float ymax = ymin + groupRec.height;
		return ((xmin > spriteRec.x + spriteRec.width) || (xmax < spriteRec.x))
				|| ((ymin > spriteRec.y + spriteRec.height) || (ymax < spriteRec.y));
	}
	
	public void update(float elapsedTime) {
		//using iterator for safe removal of sprites when they expire
		Iterator<ESprite> iter = sprites.iterator();
//		Utils.startTimer();
		while(iter.hasNext()) {
			ESprite s = iter.next();
//			Utils.log("Sprite ID: "+s.id+" Current pos: "+s.getX()+", "+s.getY());
			
			//a) sprite is outside of group bounds or b)sprite expired. Remove it from array.
			if(!s.update(elapsedTime, group) || isOutsideOfGroup(s))
				iter.remove();
			
//			Utils.log("Sprite ID: "+s.id+" Updated pos: "+s.getX()+", "+s.getY());
		}
//		Utils.stopTimer("Lightning Sprites");
		
		//spawn new sprites if there aren't enough
		int numOfSprites = group.getNumOfSprites();
		while(sprites.size < numOfSprites) {
			ESprite s = spawnRandomSprite();
//			Utils.log("Sprite ID: "+s.id+" Actual position(SM): "+s.getX()+", "+s.getY());
			sprites.add(s);
//			sprites.add(spawnRandomSprite());
		}
	}

	public void draw(SpriteBatch batch) {
		for(ESprite s : sprites) {
			//don't draw sprites if they are dead
			if(s.phase != Phase.DEAD) {
				s.draw(batch);
			}
		}
	}
}
