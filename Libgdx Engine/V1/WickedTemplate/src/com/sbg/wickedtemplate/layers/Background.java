package com.sbg.wickedtemplate.layers;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.utils.EffectsAdapter;

public class Background extends Layer {

	public Background() {
		super();
		
		//Not sure if this is the most efficient way of getting the json file into a reader
		Reader configFile = Gdx.files.internal("data/Background.json").reader();
		
		//Use gson to read json
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		//register our custom adapter
		gsonBuilder.registerTypeAdapter(Effect.class, new EffectsAdapter());
		Gson gson = gsonBuilder.create();
		try {
			group=gson.fromJson(configFile, Group.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//initialize group
		if(group!=null) group.init();
		
	}

}
