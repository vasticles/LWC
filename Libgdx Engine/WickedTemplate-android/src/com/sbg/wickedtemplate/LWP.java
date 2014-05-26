/*
This is the Android Live Wallpaper wrapper for the main code.
 */

package com.sbg.wickedtemplate;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidLiveWallpaperService;
import com.badlogic.gdx.backends.android.AndroidWallpaperListener;

public class LWP extends AndroidLiveWallpaperService {
	public static float pixelOffset = 0;
	
	@Override
    public void onCreateApplication () {
        super.onCreateApplication();

        final AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = true;
        config.useCompass = false;
        config.useWakelock = false;
        config.useAccelerometer = false;
        config.getTouchEventsForLiveWallpaper = true;

        final ApplicationListener listener = new WallpaperListener();
        initialize(listener, config);
    }

	public static class WallpaperListener extends LWP_Engine implements AndroidWallpaperListener {
        @Override
        public void create() {
            super.resolver = new Resolver() {
                @Override
                public float getxPixelOffset() {
                    return pixelOffset;
                }
        };
        	android.os.Debug.waitForDebugger();
            super.create();
        };

        /*
         * never use xOffset/yOffset and xOffsetStep/yOffsetStep, because custom launchers will mess with your 
         * brain and this problem can't be fixed! Use only xPixelOffset/yPixelOffset (who used yPixelOffset???)))
         */

        @Override
        public void offsetChange (float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            pixelOffset = xPixelOffset;
        }

        @Override
        public void previewStateChange (boolean isPreview) {
        }
    }
}