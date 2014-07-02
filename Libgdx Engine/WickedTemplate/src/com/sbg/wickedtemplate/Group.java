/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.sbg.wickedtemplate.effects.group.GroupAccessor;
import com.sbg.wickedtemplate.effects.sprite.SpriteAccessor;
import com.sbg.wickedtemplate.utils.Utils;

/**
Group class holds a sprite manager. Groups have various fields that sprites rely on
(e.g. parallax, group dimensions).
They also hold parsed info for sprite creation which is performed by the sprite manager.
Groups dimensions interpret percent values, which internally get converted to pixels for easier handling.
For now, only one group per layer is being tested. It should support more, but further testing and adjusting is 
probably required.
Groups will later implement an interface that allows for basic manipulations that sprites employ
(e.g. scale, movement, rotation, etc.)
 **/

public class Group extends ESprite {
	
	//GROUP FIELDS
	public String groupName;
	public float groupXpercent, groupYpercent;
	public float mGroupWidthPix;
	public float mGroupHeightPix;
	public float mGroupWidthPercent;
	public float mGroupHeightPercent;
	public Vector2 mGroupOffset;
	private float mXPixelOffset;
	public float mParallaxFactor = 1; //this should be parallax * screenratio, but for now it's on the back burner
	public float mScreenRatio = 1;
	public Resolver mResolver = World.getResolver();
	private StateManager groupStateManager;
	public Matrix3 matrix = new Matrix3();
	
	//SPRITE FIELDS
//	public ESprite mSprite;
//	public Array<Sprite> mSpritePool; //group sprites are added to a pool from which they will be randomly added to active sprite array via sprite manager
//	public Array<ESprite> mSprites; 
	public String groupAtlas;
	public ESprite bgSprite;
	public List<State> bgSpriteStates;
	public String spriteNames[];
	public boolean stretched;
	public int numOfSpritesRange[]; //range of number of sprites that can be active in the group
	public int numOfSprites = 0;
	public Array<Vector2> spawnPoints; 
	public float spawnPointsValues[][]; //2D array that will be used to construct vector spawn points
	private Random r = new Random();
//	public TextureAtlas atlas = Assets.manager.get(Assets.atlasName);
//	public String atlasPath; //path to the atlas info file that locates textures within the atlas
	public float scaleFactorPercent; //scale factor that should be applied to the sprites (optimization needed)
	public List<State> stateList; //list of states that will be passed to each sprite upon construction
	public float parallax = 0; //group parallax
	private SpriteManager spriteManager;
	public List<ESprite> spriteSpecs; //list of sprite specifications such as names, texture regions, frames, frame durations
	
	//OTHER FIELDS
	private Logger l = LWP_Engine.log;
	private float elapsedTime;
	private boolean scrollable; //enables/disables scrolling in case we have to stretch the width. should be a global value later.
	public float screenWidthPix = Gdx.graphics.getWidth(); //these should be obtained from engine fields, but I was lazy.
	public float screenHeightPix = Gdx.graphics.getHeight();

	//empty constructor required for json parsing
	public Group(){}
	
	//initialize the group
	public void init() {
		
		//if we have multiple screens, modify width
		if(scrollable) screenWidthPix*=2;
		scaleGroup(scaleFactorPercent);
		setInitGroupPosition(groupXpercent, groupYpercent);
		setGroupPixelDimensions();
		bgSprite.init(this);
		setGroupGraphic(bgSprite);
		Tween.registerAccessor(Group.class, new GroupAccessor());
//		Tween.registerAccessor(ESprite.class, new SpriteAccessor());
		groupStateManager = new StateManager(this, bgSpriteStates);
//		atlas = new TextureAtlas(Gdx.files.internal(atlasPath))
		
		//create sprite manager and tell it to prepare the spritess
		spriteManager = new SpriteManager(this);
		spriteManager.createSpritePool(spriteSpecs);
		spriteManager.createSpawnPointPool(spawnPointsValues);
		spriteManager.createSpriteStateList(stateList);
		spriteManager.prepareSprites();
	}
	
	private void setGroupWidth(float w) {
		mGroupWidthPix=Utils.percentToPixel(w, screenWidthPix);
	}
	
	private void setGroupHeight(float h) {
		mGroupHeightPix=Utils.percentToPixel(h, screenHeightPix);
	}
	
	private void setGroupPixelDimensions() {
		mGroupWidthPix=Utils.percentToPixel(mGroupWidthPercent, screenWidthPix);
		mGroupHeightPix=Utils.percentToPixel(mGroupHeightPercent, screenHeightPix);
		setSize(mGroupWidthPix, mGroupHeightPix);
	}
	
	private void setInitGroupPosition(float gX, float gY) {
		//convert from percent to pixels and set positionn
		mGroupOffset = new Vector2(Utils.percentToPixel(gX, screenWidthPix), Utils.percentToPixel(gY, screenHeightPix));
		setOrigin(getWidth()/2, getHeight()/2);
		setPosition(mGroupOffset.x, mGroupOffset.y);
		setUnmodifiedPosition(mGroupOffset.x, mGroupOffset.y);
//		Utils.log(""+getOriginX()+getOriginY());
	}
	
	public void setGroupPosition(Vector2 newPos) {
//		tmpVect = unmodifiedPosition.cpy(); //make a copy of the previos unmodifiedPosition (vector)
		oldPos.set(getX(), getY()); //make a copy of the previos unmodifiedPosition (vector)
//		Utils.log("Old Position: "+tmpVect.toString());
//		unmodifiedPosition.set(newPos); //set unmodifiedPosition to new unmodifiedPosition (vector)
//		Utils.log("New Position: "+newPos.toString());
		newPos.sub(oldPos); //find out the difference; i.e how much the unmodifiedPosition shifted
//		Utils.log("Difference: "+newPos.toString());
		matrix.translate(newPos); //translate the matrix by the difference
//		translate(newPos.x, newPos.y); //translate the unmodifiedPosition (actual xy coords) of the group/sprite by the difference
//		Utils.log(matrix.toString())
		tmpVect = unmodifiedPosition.cpy().mul(matrix);
		setPosition(tmpVect.x, tmpVect.y);
		Utils.log("New Position: "+tmpVect.toString());
	}
	
	public Vector2 getGroupPosition() {
		return unmodifiedPosition;
	}
	
//	public float getGroupX() {
//		return unmodifiedPosition.x;
//	}
	
//	public float getGroupY() {
//		return unmodifiedPosition.y;
//	}
	
	private void setParallaxFactor(float pF) {
		mParallaxFactor = pF;
	}
	
	private void setGroupGraphic(ESprite s) {
		isAnimated = s.isAnimated;
		if(isAnimated) {
			anim = s.anim;
			frames = s.frames;
			frameDuration = s.frameDuration;
			playMode = s.playMode;
			animationStateTime = 0f;
		}
	}
	
	private void scaleGroup(float sF) {
		if(sF!=0) mScreenRatio = LWP_Engine.getScreenRatio()*(sF/100);
		setScale(mScreenRatio);
	}
	
	public int getNumOfSprites() {
		return numOfSprites = getRandomInt(numOfSpritesRange);
	}
	
	public void update() {
		
//		mXPixelOffset = mResolver.getxPixelOffset(
//		parallax = mXPixelOffset*mParallaxFactor;
		
		
		//since setting up the world takes time, getDeltaTime will return a rather lengthy period.
		//the tween engine doesn't care how long the world took to set up. it interprets elapsed time which is delta
		//henceforth i applied this nasty hack that updates delta, but passes 0 as elapsed time,
		//the result is, when wp loads, we don't see sprites and groups in mid-effect.
		//effects (or rather delta) are properly initiated only as soon as the wp is all loaded
		if(LWP_Engine.isFirstLaunch) { 
			elapsedTime = 0;
			Gdx.graphics.getDeltaTime();
			LWP_Engine.isFirstLaunch = false;
		} else
			elapsedTime = Gdx.graphics.getDeltaTime();
		
		groupStateManager.update(elapsedTime);
		
		
		Utils.log("Group X: "+getX()+" Group Y: "+getY());
		
		if(isAnimated) {
			animationStateTime+=elapsedTime;
			setRegion(anim.getKeyFrame(animationStateTime));
		}
		
		spriteManager.update(elapsedTime);
		
	}
	
	public void drawGroup(SpriteBatch batch) {
		this.draw(batch);
		spriteManager.draw(batch);
	}
	
	
	//HELPER METHODS
	//should move this to Util classs.
	private int getRandomInt(int[] i) {
		if (i.length == 1) return i[0];
		else return i[r.nextInt(i.length)];
	}
}
