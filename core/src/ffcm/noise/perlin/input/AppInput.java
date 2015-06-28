
package ffcm.noise.perlin.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import ffcm.noise.perlin.PerlinNoiseApp;

public class AppInput extends InputAdapter
{
    private PerlinNoiseApp app;

    public AppInput()
    {
        app = PerlinNoiseApp.instance;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.G)
        {
            //app.CreateNoiseGenerator();
            //app.GenerateNoiseTexture();

            return true;
        }

        return false;
    }
}
