package com.sbg.wickedtemplate;

import com.google.gson.annotations.SerializedName;

/**
Spawn mode indicates how the items should spawn. There are 7 spawn modes: random, border top, border bottom, border left,
border right, custom and none. None means there's no spawning.
 **/

public enum Alignment {
	@SerializedName("bottom_left")
	BOTTOM_LEFT,
	
	@SerializedName("bottom_right")
	BOTTOM_RIGHT,
	
	@SerializedName("bottom_middle")
	BOTTOM_MIDDLE,
	
	@SerializedName("top_left")
	TOP_LEFT,
	
	@SerializedName("top_middle")
	TOP_MIDDLE,
	
	@SerializedName("top_right")
	TOP_RIGHT,

	@SerializedName("middle_left")
	MIDDLE_LEFT,
	
	@SerializedName("middle_right")
	MIDDLE_RIGHT,
	
	@SerializedName("centre")
	CENTRE;
}
