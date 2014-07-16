package com.sbg.wickedtemplate;

import com.google.gson.annotations.SerializedName;

/**
Spawn mode indicates how the items should spawn. There are 7 spawn modes: random, border top, border bottom, border left,
border right, custom and none. None means there's no spawning.
 **/

public enum SpawnMode {
	@SerializedName("random")
	RANDOM,
	
	@SerializedName("border_top")
	BORDER_TOP, 
	
	@SerializedName("border_bottom")
	BORDER_BOTTOM, 
	
	@SerializedName("border_left")
	BORDER_LEFT, 
	
	@SerializedName("border_right")
	BORDER_RIGHT,
	
	@SerializedName("custom")
	CUSTOM,
	
	@SerializedName("none")
	NONE;
}