package com.sbg.wickedtemplate;

import com.google.gson.annotations.SerializedName;

/**
Phases are simple indicators of the sprite's lifespan.
Generally, a sprite should go through 4 phases: Spawning, Alive, Dying and Dead.
Dead phase acts effectively as the respawn timer (i.e. when Dead phase expires the sprite is removed from the
array, and a new one can be inserted).
Eternal phase is intended for sprites that are not meant to die and respawn (eg. backgrounds or static sprites).
 **/

public enum Phase {
	@SerializedName("eternal")
	ETERNAL,
	
	@SerializedName("spawning")
	SPAWNING, 
	
	@SerializedName("alive")
	ALIVE, 
	
	@SerializedName("dying")
	DYING, 
	
	@SerializedName("dead")
	DEAD;
}
