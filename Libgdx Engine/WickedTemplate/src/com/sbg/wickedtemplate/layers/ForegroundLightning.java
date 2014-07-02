package com.sbg.wickedtemplate.layers;

import java.io.Reader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.sbg.wickedtemplate.Effect;
import com.sbg.wickedtemplate.Group;
import com.sbg.wickedtemplate.utils.EffectsAdapter;

public class ForegroundLightning extends Layer {

	public ForegroundLightning(int i, float p) {
		super(i,p);

		Reader configFile = Gdx.files.internal("data/ForegroundLightning.json").reader();
		
		//Prior experimentation with other parsers
		int n = 1;
		switch(n) {
		case 0 :
		
//			//jackson
//			ObjectMapper mapper = new ObjectMapper();
//			try {
//				g = mapper.readValue(configFile, Group.class);
//			} catch (JsonParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (JsonMappingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			break;
		
		case 1 :
			//gson
			GsonBuilder gsonBuilder = new GsonBuilder();
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
			break;
		}
		
		
		
		//yamlbeans
		
//		try {
//			Reader configFile = Gdx.files.internal("data/ForegroundLightning.yml").reader();
//			YamlReader reader = new YamlReader(configFile);
//			g=reader.read(Group.class);
//		} catch (YamlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		if(g!=null) g.init();
	}

}
