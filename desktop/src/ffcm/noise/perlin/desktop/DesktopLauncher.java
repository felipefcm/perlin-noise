
package ffcm.noise.perlin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ffcm.noise.perlin.PerlinNoiseApp;

public class DesktopLauncher 
{
	public static void main(String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "PerlinNoiseTest";
		config.width = (int)(PerlinNoiseApp.V_WIDTH * PerlinNoiseApp.DESKTOP_SCALE);
		config.height = (int)(PerlinNoiseApp.V_HEIGHT * PerlinNoiseApp.DESKTOP_SCALE);
		
		new LwjglApplication(new PerlinNoiseApp(), config);
	}
}
