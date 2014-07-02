package com.sbg.wickedtemplate;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.AtlasTmxMapLoader.AtlasTiledMapLoaderParameters;

public class Assets {

	public final static AssetManager manager = new AssetManager(); 
	
	public static String prefix = "data/";
//	public static String atlasName;
	
	public static void load(String atlasFilename) {
		String atlasPath = prefix.concat(atlasFilename);
		
//		AtlasTiledMapLoaderParameters params = new AtlasTiledMapLoaderParameters()
//		params.textureMinFilter = TextureFilter.MipMapLinearNearest;
//		params.magFilter = TextureFilter.Nearest;
//		params.genMipMaps = true;
		
		manager.load(atlasPath, TextureAtlas.class);
	}
	
	public static TextureAtlas getAtlas (String name) {
		return manager.get(prefix.concat(name));
	}
	
	public static void dispose() {
		manager.dispose();
	}
}
