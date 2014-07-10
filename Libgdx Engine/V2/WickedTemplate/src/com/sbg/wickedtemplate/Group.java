/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Tween;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
	private OrthographicCamera camera;
	
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
	
	//constructor (not used yet)
	public Group(ESprite s, List<State> groupStates) {
		super(s, groupStates);
		
		
	}
	
	//initialize the group
	public void init() {
		
		//if we have multiple screens, modify screen width
		if(scrollable) screenWidthPix*=2;
		
		//prepares the sprite object that holds our data. specifically the texture or animation
		bgSprite.prepareGraphic(this);
		//set region to the first (and only, if not applicable) frame
		setGroupGraphic(bgSprite);
		//converts stored dimensions in % to pixels and stores them (width,height)
		setGroupPixelDimensions();
		//scales group by the scale factor
		scaleGroup(scaleFactorPercent);
		//sets origin to centre
		setOrigin(getWidth()/2, getHeight()/2);
		//places the group at the stored point (first converts from % to pixels). 
		//this point is also used as the offset
		//keeps the stored point as the unconstrained position that gets later modified by the matrix
		setInitGroupPosition(groupXpercent, groupYpercent);
		
		//register tween accessors. (subclasses before their superclasses)
		Tween.registerAccessor(Group.class, new GroupAccessor());
		Tween.registerAccessor(ESprite.class, new SpriteAccessor());
		//set up a state manager for the group
		groupStateManager = new StateManager(this, bgSpriteStates);
//		atlas = new TextureAtlas(Gdx.files.internal(atlasPath))
		
		//create sprite manager
		spriteManager = new SpriteManager(this);
		//create a pool of sprites from the deserialized list of sprite configs
		spriteManager.createSpritePool(spriteSpecs);
		//create a pool of spawn points from the deserialized array of spawn point values
		spriteManager.createSpawnPointPool(spawnPointsValues);
		//create a list of sprite states from the deserialized list of state configs
		spriteManager.createSpriteStateList(stateList);
		//fill up the array with sprites
		//essentially it takes a sprite, gives it a list of states and sets its location to a spawn point,
		//until the array has been filled up (as per number of sprites allowance in the group)
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
//		setRegion(region);
		setSize(mGroupWidthPix*mScreenRatio, mGroupHeightPix*mScreenRatio);
	}
	
	private void scaleGroup(float sF) {
		if(sF!=0) mScreenRatio = LWP_Engine.getScreenRatio()*(sF/100);
//		setScale(mScreenRatio);
//		setSize(mGroupWidthPix*mScreenRatio, mGroupHeightPix*mScreenRatio);
	}

	private void setInitGroupPosition(float gX, float gY) {
		//convert from percent to pixels and set positionn
		float groupX = Utils.percentToPixel(gX, screenWidthPix)*mScreenRatio;
		float groupY = Utils.percentToPixel(gY, screenHeightPix)*mScreenRatio;
//		translate(mScreenRatio, mScreenRatio);
		
//		mGroupOffset = new Vector2(Utils.percentToPixel(gX, screenWidthPix), Utils.percentToPixel(gY, screenHeightPix));
		mGroupOffset = new Vector2(groupX, groupY);
		setUnconstrainedPosition(mGroupOffset.x, mGroupOffset.y);
		setPosition(mGroupOffset.x, mGroupOffset.y);
		newPosition.set(mGroupOffset);
//		setUnconstrainedPosition(0, 0);
//		Utils.log(""+getOriginX()+getOriginY());
	}
	
	public void setGroupPosition(Vector2 newPos) {
		/**Here we should clear the matrix and apply the newly received position (newPos) to it.**/
		matrix.idt();
		matrix.translate(newPos.scl(mScreenRatio)); //translate the matrix by the newPos value
		newPosition = unconstrainedPosition.cpy().mul(matrix);
	}
	
	public Vector2 getUnconstrainedGroupPosition() {
		return unconstrainedPosition;
	}
	
	@Override
	public void setRotation(float degrees) {
		super.setRotation(degrees);
//		Utils.log(degrees+"");
		matrix.idt();
		matrix.rotate(degrees);
		newPosition = unconstrainedPosition.cpy().mul(matrix);
//		Utils.log(matrix.toString());
	}
	
	public void setCamera() {
		camera = new OrthographicCamera(screenWidthPix/2, screenHeightPix);
		camera.setToOrtho(false, screenWidthPix/2, screenHeightPix);
//		camera.position.set(mGroupWidthPix/2, mGroupHeightPix/2, 0);
		camera.position.set(300, 300, 0);
	}
	
	private void setParallaxFactor(float pF) {
		mParallaxFactor = pF;
	}
	
	private void setGroupGraphic(ESprite s) {
		isAnimated = s.isAnimated;
		frames = s.frames;
		if(isAnimated) {
			anim = s.anim;
			frameDuration = s.frameDuration;
			playMode = s.playMode;
			animationStateTime = 0f;
		}
		setRegion(frames.first());
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
		setPosition(newPosition.x, newPosition.y);
		
		
		Utils.log("Group X: "+getX()+" Group Y: "+getY());
//		Utils.log(groupName+" x:"+getX()+" y:"+getY());
		if(isAnimated) {
			animationStateTime+=elapsedTime;
			setRegion(anim.getKeyFrame(animationStateTime));
		}
		
		spriteManager.update(elapsedTime);
		
	}
	
	public void drawGroup(SpriteBatch batch) {
//		camera.position.x = (mGroupWidthPix / 2) - mResolver.getxPixelOffset()*mParallaxFactor;
//		camera.position.x = 300 - mResolver.getxPixelOffset()*mParallaxFactor;
//		camera.update();
//		batch.setProjectionMatrix(camera.combined);
//		batch.begin();
		this.draw(batch);
		spriteManager.draw(batch);
//		batch.end();
	}
	
	
	//HELPER METHODS
	//should move this to Util classs.
	private int getRandomInt(int[] i) {
		if (i.length == 1) return i[0];
		else return i[r.nextInt(i.length)];
	}
}
