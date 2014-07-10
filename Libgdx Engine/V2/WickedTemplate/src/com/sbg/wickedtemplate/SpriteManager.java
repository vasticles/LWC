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
		//useful to have group references, though maybe I should encapsulate things betterr
		group = g;
		sprites = new Array<ESprite>();
	}
	
	public void createSpritePool(List<ESprite> spriteSpecs) {
		if (spritePool == null) spritePool = new Array<ESprite>();
		for (ESprite s : spriteSpecs) {
			if(s.isDisabled) continue;
			s.prepareGraphic(group);
//			ESprite s = new ESprite(sprite);
//			Sprite s = atlas.createSprite(spriteName);;
			s.setSize(s.getRegionWidth(), s.getRegionHeight());
			s.setOrigin(s.getWidth()/2, s.getHeight()/2);
			s.setScale(group.mScreenRatio);
			spritePool.add(s);
		}
	}
	
//	public void createSpritePooll(String[] spriteNames) {
//		if (spritePool == null) spritePool = new Array<ESprite>();
//		for (String spriteName : spriteNames) {
//			Sprite s = atlas.createSprite(spriteName);
//			s.setOrigin(s.getWidth()/2, s.getHeight()/2);
//			spritePool.add(s);;
//		}
//	}
	
	public void createSpawnPointPool(float[][] spawnPointValues) {
		if(spawnPointPool == null) spawnPointPool = new Array<Vector2>();
		for(float[] value : spawnPointValues) {
			Vector2 point = new Vector2(Utils.percentToPixel(value[0], group.mGroupWidthPix), Utils.percentToPixel(value[1], group.mGroupHeightPix)).scl(group.mScreenRatio).add(group.mGroupOffset);
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
//		Utils.log("SP before matrix: "+point.toString());
//		Utils.log(group.matrix.toString());
//		point.mul(group.matrix);
//		Utils.log("SP after matrix: "+point.toString());
//		Vector2 point = spawnPointPool.get(r.nextInt(spawnPointPool.size));/*.cpy().mul(group.matrix);*/
//		Utils.log(group.matrix.toString());
//		Utils.log("Point: "+point.toString());
		return point;
	}
	
//	public void translateSpawnPointss(Matrix3 matrix) {
//		Utils.log(matrix.toString());
//		for(Vector2 point : spawnPointPool) {
//			point.set(point.cpy().mul(matrix));
//			Utils.log(point.toString());
//		}
//	}
	
	private ESprite spawnRandomSprite() {
		ESprite s = new ESprite(spritePool.get(r.nextInt(spritePool.size)), stateList);
		Vector2 sP = getRandomSpawnPoint();
//		Utils.log("SP before origin adj:"+sP.toString());
//		s.setPosition(sP.x-s.getWidth()/2, sP.y-s.getHeight()/2);
		s.setUnconstrainedPosition(sP.x, sP.y);
//		s.setUnconstrainedPosition(0, 0);
		Vector2 adjustedPos = sP.cpy().mul(group.matrix);
		s.setPosition(adjustedPos.x, adjustedPos.y);
//		Utils.log("S width:"+s.getWidth()+" S height: "+s.getHeight());
//		Utils.log("SP after origin adj:"+s.getX()+","+s.getY());
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
//		return groupRec.contains(spriteRec);
	}
	
//	private boolean contains (Rectangle rectangle) {
//		float xmin = rectangle.x;
//		float xmax = xmin + rectangle.width;
//
//		float ymin = rectangle.y;
//		float ymax = ymin + rectangle.height;
//
//		return ((xmin > x && xmin < x + width) && (xmax > x && xmax < x + width))
//			&& ((ymin > y && ymin < y + height) && (ymax > y && ymax < y + height));
//	}
	
	public void update(float elapsedTime) {
		//using iterator for safe removal of sprites when they expire
		Iterator<ESprite> iter = sprites.iterator();
//		Utils.startTimer();
		while(iter.hasNext()) {
			ESprite s = iter.next();
			
			//a) sprite is outside of group bounds or b)sprite expired. Remove it from array.
			if(!s.update(elapsedTime, group) || isOutsideOfGroup(s))
//			if(!s.update(elapsedTime, group))
				iter.remove();
		}
//		Utils.stopTimer("Lightning Sprites");
		
		//spawn new sprites if there aren't enough
		int numOfSprites = group.getNumOfSprites();
		while(sprites.size < numOfSprites) {
			ESprite s = spawnRandomSprite();
			Utils.log("Sprite ID: "+s.id+" Actual position(SM): "+s.getX()+", "+s.getY());
			sprites.add(s);
//			sprites.add(spawnRandomSprite());
		}
	}

	public void draw(SpriteBatch batch) {
		for(ESprite s : sprites) {
			//don't draw sprites if they are deadd
			if(s.phase != Phase.DEAD) {
				s.draw(batch);
			}
		}
	}
}
