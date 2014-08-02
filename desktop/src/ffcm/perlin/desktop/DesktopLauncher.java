
package ffcm.perlin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import ffcm.perlin.PerlinNoiseTest;

public class DesktopLauncher 
{
	public static void main(String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "PerlinNoiseTest";
		config.width = (int)(PerlinNoiseTest.V_WIDTH * PerlinNoiseTest.DESKTOP_SCALE);
		config.height = (int)(PerlinNoiseTest.V_HEIGHT * PerlinNoiseTest.DESKTOP_SCALE);
		
		new LwjglApplication(new PerlinNoiseTest(), config);
	}
}
