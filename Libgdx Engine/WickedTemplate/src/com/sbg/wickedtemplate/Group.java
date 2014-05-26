/**
 * Copyright 2014 Vas Nesterov
 */

package com.sbg.wickedtemplate;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
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

public class Group {
	
	//GROUP FIELDS
	public String name;
	public float groupX, groupY;
	public float mGroupWidthPix;
	public float mGroupHeightPix;
	public float mGroupWidthPercent;
	public float mGroupHeightPercent;
	public Vector2 mGroupLocation;
	private float mXPixelOffset;
	public float mParallaxFactor = 1; //this should be parallax * screenratio, but for now it's on the back burner
	public float mScreenRatio;
	public Resolver mResolver = World.getResolver();
	
	//SPRITE FIELDS
	public ESprite mSprite;
	public Array<Sprite> mSpritePool; //group sprites are added to a pool from which they will be randomly added to active sprite array via sprite manager
	public Array<ESprite> mSprites; 
	public String spriteNames[];
	public boolean stretched;
	public int numOfSpritesRange[]; //range of number of sprites that can be active in the group
	public int numOfSprites = 0;
	public Array<Vector2> spawnPoints; 
	public float spawnPointsValues[][]; //2D array that will be used to construct vector spawn points
	private Random r = new Random();
	public TextureAtlas atlas;
	public String atlasPath; //path to the atlas info file that locates textures within the atlas
	public float scaleFactor; //scale factor that should be applied to the sprites (optimization needed)
	public List<State> stateList; //list of states that will be passed to each sprite upon construction
	public float parallax = 0; //group parallax
	private SpriteManager spriteManager;
	
	//OTHER FIELDS
	private Logger l = LWP_Engine.log;
	private long elapsedTime;
	private boolean scrollable; //enables/disables scrolling in case we have to stretch the width. should be a global value later.
	public float screenWidthPix = Gdx.graphics.getWidth(); //these should be obtained from engine fields, but I was lazy.
	public float screenHeightPix = Gdx.graphics.getHeight();

	//empty constructor required for json parsing
	public Group(){}
	
	//initialize the grup
	public void init() {
		
		//if we have multiple screens, modify width
		if(scrollable) screenWidthPix*=2;
		setGroupPixelDimensions();
		setGroupLocation(groupX, groupY);
		scaleGroup(scaleFactor);
		atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
		
		//create sprite manager and tell it to prepare the sprites
		spriteManager = new SpriteManager(this);
		spriteManager.createSpritePool(spriteNames, atlas);
		spriteManager.createSpawnPointPool(spawnPointsValues);
		spriteManager.createSpriteStateList(stateList);
		spriteManager.prepareSprites();
	}
	
	private void setWidth(float w) {
		mGroupWidthPix=Utils.percentToPixel(w, screenWidthPix);
	}
	
	private void setHeight(float h) {
		mGroupHeightPix=Utils.percentToPixel(h, screenHeightPix);
	}
	
	private void setGroupPixelDimensions() {
		mGroupWidthPix=Utils.percentToPixel(mGroupWidthPercent, screenWidthPix);
		mGroupHeightPix=Utils.percentToPixel(mGroupHeightPercent, screenHeightPix);
	}
	
	private void setGroupLocation(float gX, float gY) {
		mGroupLocation = new Vector2(Utils.percentToPixel(gX, mGroupWidthPix), Utils.percentToPixel(gY, mGroupHeightPix));
	}
	
	private void setParallaxFactor(float pF) {
		mParallaxFactor = pF;
	}
	
	private void scaleGroup(float sF) {
		if(sF!=0)mScreenRatio = LWP_Engine.getScreenRatio()*sF;
	}
	
	public int getNumOfSprites() {
		return numOfSprites = getRandomInt(numOfSpritesRange);
	}
	
	public void render(SpriteBatch batch) {
		update();
		draw(batch);
	}
	
	public void update() {
		//timing field that I use sometimes to time the update method
		long currentTime = System.currentTimeMillis();
		
		mXPixelOffset = mResolver.getxPixelOffset();
		parallax = mXPixelOffset*mParallaxFactor;
		
		//not sure about float accuracy, so I convert to long milliseconds. will have to verify this later.
		elapsedTime = Utils.convertToMillis(Gdx.graphics.getDeltaTime());
		
		
		spriteManager.update(elapsedTime);
		
		//part of the update method timing calculation
		long millisPerUpdate = System.currentTimeMillis() - currentTime;
//		if (millisPerUpdate >= 20)
//			l.error("Update time: "+millisPerUpdate);
	}
	
	public void draw(SpriteBatch batch) {
		spriteManager.draw(batch);
	}
	
	
	//HELPER METHODS
	//should move this to Util class.
	private int getRandomInt(int[] i) {
		if (i.length == 1) return i[0];
		else return i[r.nextInt(i.length)];
	}
}
