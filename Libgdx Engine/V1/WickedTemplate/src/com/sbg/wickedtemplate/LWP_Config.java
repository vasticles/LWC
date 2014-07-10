package com.sbg.wickedtemplate;

import java.util.List;

public class LWP_Config {
	public String name;
	public boolean isScrollable;
	public List<AtlasMeta> atlasMeta;
	public List<GroupMeta> groupMeta;
	
	public class AtlasMeta {
		public String name;
		public String filePath;
		public boolean isDisabled;
	}
	
	public class GroupMeta {
		public String name;
		public String filePath;
		public boolean isDisabled;
		public int index;
	}
	
	public LWP_Config() {}
	
}
