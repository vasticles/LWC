package com.sbg.wickedtemplate.layers;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.effects.Effect;
import com.sbg.wickedtemplate.utils.EffectsAdapter;

public class ForegroundEnergy extends Layer {

	public ForegroundEnergy(int i) {
		super(i);
		
		Reader configFile = Gdx.files.internal("data/ForegroundEnergy.json").reader();
		
		//Use gson to read json
		GsonBuilder gsonBuilder = new GsonBuilder();
		
		//register our custom adapter
		gsonBuilder.registerTypeAdapter(Effect.class, new EffectsAdapter());
		Gson gson = gsonBuilder.create();
		try {
			g=gson.fromJson(configFile, Group.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//initialize group
		if(g!=null) g.init();
		
	}

}
