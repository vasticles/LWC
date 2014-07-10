package com.sbg.wickedtemplate;

import java.io.Reader;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Logger;
import com.google.gson.Gson;
import com.sbg.wickedtemplate.utils.Utils;

/**
Main engine class. Sets up global resources and fields. Main render loop starts running here.
Will upgrade to interpret json from this class.
 **/

public class LWP_Engine implements ApplicationListener {
	public final static String VERSION = "1.0.0";
	public static Logger log;
	private SpriteBatch batch;
	Sprite sprite;
	
	private static LWP_Config lwpSettings;
	private static String name;
	public static boolean isScrollable;
	private static List<LWP_Config.AtlasMeta> atlasMetas;
	private static List<LWP_Config.GroupMeta> groupMetas;
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
//		log.error("Device width: "+width);
//		log.error("Device height: "+height);
		
		Reader configFile = Gdx.files.internal("data/LWP_config.json").reader();

		Gson gson = new Gson();
		lwpSettings = gson.fromJson(configFile, LWP_Config.class);
		name = lwpSettings.name;
		isScrollable = lwpSettings.isScrollable;
		atlasMetas = lwpSettings.atlasMeta;
		groupMetas = lwpSettings.groupMeta;
		
		//queue atlases for loading
		Utils.startTimer();
		for(LWP_Config.AtlasMeta atlas : atlasMetas) {
			if(!atlas.isDisabled) Assets.load(atlas.filePath);
		}
		//load them synchronously
		Assets.manager.finishLoading();
		Utils.stopTimer("Loading assets took: ");
		
//		while(!Assets.manager.update())
//		LWP_Engine.log.error(Assets.manager.getProgress()*100+"%");
		
		fpslog = new FPSLogger();
		
		Utils.startTimer();
		world = new World(groupMetas);
		Utils.stopTimer("Setting up world took: ");
		world.setResolver(resolver);
		wr = new WorldRenderer(world);
		world.setRenderer(wr);
		
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
		//Propagate drawing calls throughout the world via the attached world renderer
		wr.render(batch);
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
