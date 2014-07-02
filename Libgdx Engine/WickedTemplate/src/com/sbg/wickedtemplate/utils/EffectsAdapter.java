package com.sbg.wickedtemplate.utils;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sbg.wickedtemplate.Effect;

/**
Effects Adapter is a custom json serializer/deserializer for the abstract Effect class
 (though no serializtion is planned at the moment).
It reads the json and creates the specified objects.
A more global adapter will be written to parse all wallpaper properties (layers, groups, states and effects).
 **/

public class EffectsAdapter implements JsonSerializer<Effect>,	JsonDeserializer<Effect> {
	@Override
	public JsonElement serialize(Effect src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		result.add("effect_name", new JsonPrimitive(src.getClass().getSimpleName()));
		result.add("properties", context.serialize(src, src.getClass()));

		return result;
	}

	@Override
	public Effect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		//name of class
		String type = jsonObject.get("class_name").getAsString();
		//list of maps of fields and their values
		JsonElement element = jsonObject.get("properties");

		try {
			//path to class
			return context.deserialize(element,	Class.forName("com.sbg.wickedtemplate.effects.sprite." + type));
		} catch (ClassNotFoundException cnfe) {
			throw new JsonParseException("Unknown element type: " + type, cnfe);
		}
	}
}
