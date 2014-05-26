package com.sbg.wickedtemplate;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;

/**
Main engine class. Sets up global resources and fields. Main render loop starts running here.
Will upgrade to interpret json from this class.
 **/

public class LWP_Engine implements ApplicationListener {
	public static Logger log;
//	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture texture;
	Sprite sprite;
	
	//Vas changes
	private World world;
	private WorldRenderer wr;
	public Resolver resolver = null;
	public static float width;
	public static float height;
	
	private long diff, start;
	private final float targetFPS = 30f; // 20-30 is enough
	private final long targetDelay = 1000 / (long) targetFPS;
	private FPSLogger fpslog;
	
	
	//Set up global fields and create world
	@Override
	public void create() {		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		log = new Logger("Wicked Template");
		log.error("Device width: "+width);
		log.error("Device height: "+height);
		fpslog = new FPSLogger();
		
		world = new World(resolver);
		wr = new WorldRenderer(world);
		world.setRenderer(wr);
		
//		camera = new OrthographicCamera(1, h/w);
		
		//SpriteBatch is the underlying object responsible for drawing
		batch = new SpriteBatch();
//		batch.enableBlending();
		
//		texture = new Texture(Gdx.files.internal("data/goku.png"));
//		TextureRegion region = new TextureRegion(texture, 0, 0, 512, 275);
		
//		sprite = new Sprite(texture);
//		sprite.setSize(500, 500);
//		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
//		sprite.setPosition(0,0);
	}

	
	//Clean up
	@Override
	public void dispose() {
		batch.dispose();
		if (texture!=null) texture.dispose();
	}

	
	//Main render loop
	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
//		batch.setProjectionMatrix(camera.combined);
//		sprite.setColor(1, 1, 1, 0.5f);

		//Update the physics before drawing
		world.update();
		//Tell the spritebatch that we're ready to draw
		batch.begin();
		//Propagate drawing calls throughout the world via the attached world renderer
		wr.render(batch);
//		sprite.draw(batch);
//		batch.draw(sprite.getTexture(), sprite.getX()+0, sprite.getY(), sprite.getOriginX(), 0, sprite.getWidth(), sprite.getHeight(), 1, 1, 0, sprite.getRegionX(), sprite.getRegionY(), sprite.getRegionWidth(), sprite.getRegionHeight(), false, false);
		//Tell spritebatch that we're done
		batch.end();
		
		//Sleep thread as per FPS
		limitFPS();
		fpslog.log();
	}

	//This method updates the global width and height in case user rotated the device
	@Override
	public void resize(int width, int height) {
		LWP_Engine.width = width;
		LWP_Engine.height = height;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public static float getScreenRatio() {
		return width*2/height;
	}
	
	public void limitFPS() {
		diff = System.currentTimeMillis() - start;

		if (diff < targetDelay) {
			try {
				Thread.sleep(targetDelay - diff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		start = System.currentTimeMillis();
	}
}
