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
	private Random r = Utils.getRandom();
	private SpawnMode spawnMode;
	private Alignment spriteAlignment;
	
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
	
	public void setSpawnMode(SpawnMode mode) {
		spawnMode = mode;
	}
	
	public void setSpriteAlignment(Alignment alignment) {
		spriteAlignment = alignment;
	}
	
	public void createSpawnPointPool(String[] spawnPointValues) {
		//create array to hold predefined spawn points
		if(spawnPointPool == null) spawnPointPool = new Array<Vector2>();
		//convert string array to 2d float array
		float[][] values = Utils.stringArrToTwoDimFloatArr(spawnPointValues);
		//fill up the new array
		for(float[] value : values) {
			Vector2 point = new Vector2(Utils.percentToPixel(value[0], group.mGroupWidthPix), Utils.percentToPixel(value[1], group.mGroupHeightPix)).scl(group.mScreenRatio).add(group.getOffset());
			spawnPointPool.add(point);
		}
	}
	
	public void createSpriteStateList(List<State> stateList) {
		this.stateList = stateList;
	}
	
	public void prepareSprites() {
		if(spawnMode != SpawnMode.NONE) {
			int numOfSprites = group.getNumOfSprites();
			for(int i=1; i<=numOfSprites; i++) {
				sprites.add(spawnRandomSprite());
			}
		}
	}
	
	private Vector2 getSpawnPoint() {
		Vector2 point;
		float x,y;
		switch (spawnMode) {
		case RANDOM:
			x = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			y = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		case BORDER_BOTTOM:
			x = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			y = 0;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		case BORDER_TOP:
			x = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			y = group.mGroupHeightPix;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		case BORDER_LEFT:
			x = 0;
			y = r.nextFloat() * (group.mGroupHeightPix - 0) + 0;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		case BORDER_RIGHT:
			x = group.mGroupWidthPix;
			y = r.nextFloat() * (group.mGroupHeightPix - 0) + 0;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		case CUSTOM:
			point = spawnPointPool.get(r.nextInt(spawnPointPool.size)).cpy();
			break;
		//default is random
		default:
			x = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			y = r.nextFloat() * (group.mGroupWidthPix - 0) + 0;
			point = new Vector2(x, y).add(group.getOffset());
			break;
		}
		return point;
	}
	
	private void alignSpawnPoint(Vector2 point, Alignment alignment, ESprite s) {
		switch(alignment) {
		case BOTTOM_LEFT:
			break;
		case BOTTOM_MIDDLE:
			point.sub(s.getWidth()/2, 0);
			break;
		case BOTTOM_RIGHT:
			point.sub(s.getWidth(), 0);
			break;
		case CENTRE:
			point.sub(s.getWidth()/2, s.getHeight()/2);
			break;
		case MIDDLE_LEFT:
			point.sub(0, s.getHeight()/2);
			break;
		case MIDDLE_RIGHT:
			point.sub(s.getWidth(), s.getHeight()/2);
			break;
		case TOP_LEFT:
			point.sub(0, s.getHeight());
			break;
		case TOP_MIDDLE:
			point.sub(s.getWidth()/2, s.getHeight());
			break;
		case TOP_RIGHT:
			point.sub(s.getWidth(), s.getHeight());
			break;
		default:
			break;
		}
	}
	
	
	private ESprite spawnRandomSprite() {
		ESprite s = new ESprite(spritePool.get(r.nextInt(spritePool.size)));
		Vector2 sP = getSpawnPoint();
		if(s.alignment != null)
			alignSpawnPoint(sP, s.alignment, s);
		else
			alignSpawnPoint(sP, spriteAlignment, s);
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
		while(iter.hasNext()) {
			ESprite s = iter.next();
//			Utils.log("Sprite ID: "+s.id+" Current pos: "+s.getX()+", "+s.getY());
			
			//a) sprite is outside of group bounds or b)sprite expired. Remove it from array.
//			if(!s.update(elapsedTime, group) || isOutsideOfGroup(s))
//				iter.remove();

			if(!s.update(elapsedTime, group))
				iter.remove();
			
//			Utils.log("Sprite ID: "+s.id+" Updated pos: "+s.getX()+", "+s.getY());
		}
		
		//spawn new sprites if there aren't enough
		if(spawnMode != SpawnMode.NONE) {
			int numOfSprites = group.getNumOfSprites();
			while(sprites.size < numOfSprites) {
				ESprite s = spawnRandomSprite();
	//			Utils.log("Sprite ID: "+s.id+" Actual position(SM): "+s.getX()+", "+s.getY());
				sprites.add(s);
	//			sprites.add(spawnRandomSprite());
			}
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
