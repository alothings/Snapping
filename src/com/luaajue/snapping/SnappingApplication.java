package com.luaajue.snapping;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class SnappingApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();  
		Parse.initialize(this, "CR7NHAf9tp73Gx88LoXnNwp41vSXLRrgTMk1MtnG", "i2XuUGq6fUr8JJTvCDAyIc3zxZsY0hgvKq5G0xDk");
	
		
	}	
		
}
