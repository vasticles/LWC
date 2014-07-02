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
	Sprite sprite;
	
	//Vas changes
	private World world;
	private WorldRenderer wr;
	public Resolver resolver = null;
	public static float width;
	public static float height;
	
	private long diff, start;
	public final static float targetFPS = 30f; // 20-30 is enough
	private final long targetDelay = 1000 / (long) targetFPS;
	private FPSLogger fpslog;
	public static boolean isFirstLaunch = true;
	
	
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
		
//		camera = new OrthographicCamera(width, height);
//		camera.setToOrtho(false, width, height);
//		camera.position.set(width/2, height/2, 0);
		
		//SpriteBatch is the underlying object responsible for drawing
		batch = new SpriteBatch();
//		Gdx.graphics.setContinuousRendering(false);
		
	}

	
	//Clean up
	@Override
	public void dispose() {
		batch.dispose();
		if (world!=null) world.dispose();
	}

	
	//Main render loop
	@Override
	public void render() {		
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//Update the physics before drawing
		world.update();
		//		camera.position.x = (width / 2) - resolver.getxPixelOffset();
		//		camera.update();
		//		batch.setProjectionMatrix(camera.combined);
		//Tell the spritebatch that we're ready to draw
		//		batch.begin();
		//Propagate drawing calls throughout the world via the attached world renderer
		wr.render(batch);
		//Tell spritebatch that we're done
		//		batch.end();
		//Sleep thread as per FPS
		limitFPS();
//		fpslog.log();
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
