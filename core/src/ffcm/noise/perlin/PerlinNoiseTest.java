
package ffcm.noise.perlin;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Date;

import ffcm.noise.perlin.gen.Interpolation;
import ffcm.noise.perlin.gen.LinearInterpolation;
import ffcm.noise.perlin.gen.PerlinNoise2d;

public class PerlinNoiseTest extends ApplicationAdapter 
{
	public static final int V_WIDTH = 512;
	public static final int V_HEIGHT = 512;
	public static final float DESKTOP_SCALE = 2.0f;
	
	public static final int TEXTURE_SIZE = 1024;
	
	//noise arguments
	public static final int MATRIX_SIZE = 16;
	public static final float LACUNARITY = 1.87f;
	public static final float GAIN = 0.5f;

	private static final float[][] COLOR_GRADIENT =
    {
        { 0.1f, Color.rgb888(0f, 0f, 1.0f)}, //water
        { 0.11f, Color.rgb888(1f, 1f, 0.53f)}, //sand
        { 0.15f, Color.rgb888(0.49f, 0.24f, 0f)}, //dirt
        { 0.32f, Color.rgb888(0.08f, 0.71f, 0.05f)}, //grass
        { 0.4f, Color.rgb888(0.8f, 0.95f, 1f)} //ice
    };
	
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	private Pixmap pixmap;
	private Texture texture;
	
	private FitViewport viewport;
	private Camera camera;
	
	private PerlinNoise2d noise;
	
	private int numOctaves = 8;
	
	@Override
	public void create() 
	{
		camera = new OrthographicCamera();
		viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
		
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		CreateNoiseGenerator();
		GenerateNoiseTexture();
		
		Gdx.input.setInputProcessor
		(
			new InputAdapter()
			{
				@Override
				public boolean touchDown(int screenX, int screenY, int pointer, int button)
				{
					if(button == 0)
						++numOctaves;
					else
						--numOctaves;
					
					GenerateNoiseTexture();
					
					return true;
				}

                @Override
                public boolean keyDown(int keycode)
                {
                    if(keycode == Input.Keys.G)
                    {
                        CreateNoiseGenerator();
                        GenerateNoiseTexture();
                        return true;
                    }

                    return false;
                }
            }
		);
	}
	
	private void CreateNoiseGenerator()
	{
		noise = new PerlinNoise2d(new Date().getTime(), MATRIX_SIZE);
	}
	
	private void GenerateNoiseTexture()
	{
		pixmap = new Pixmap(TEXTURE_SIZE, TEXTURE_SIZE, Pixmap.Format.RGB888);

		System.out.println("NumOctaves = " + numOctaves);
		
		long startTime = System.currentTimeMillis();

        //Interpolation interpolation = new PolyInterpolation();
        Interpolation interpolation = new LinearInterpolation();

		float maxVal = 0;

		for(int r = 0; r < pixmap.getHeight(); ++r)
		{
			for(int c = 0; c < pixmap.getWidth(); ++c)
			{
				float frequency = 1.0f / (float) pixmap.getWidth();
				float amplitude = GAIN;
				
				float noiseValue = 0;
				
				for(int oc = 0; oc < numOctaves; ++oc)
				{
					noiseValue += noise.GetNoiseValue(c * frequency, r * frequency, interpolation) * amplitude;
					frequency *= LACUNARITY;
					amplitude *= GAIN;
				}
				
				if(noiseValue > Math.abs(maxVal))
					maxVal = Math.abs(noiseValue);
				
				if(noiseValue < 0)
					noiseValue *= -1.0f;
				
				noiseValue = Math.min(noiseValue, 1.0f);
				
				//int pixelColor = MakeMapColors(noiseValue);
                int pixelColor = Color.rgba8888(noiseValue, noiseValue, noiseValue, 1.0f);
				
				pixmap.drawPixel(c, r, pixelColor);
			}
		}
		
		System.out.println("Amplitude = " + maxVal);
		
		if(texture != null)
			texture.dispose();
			
		texture = new Texture(pixmap);
		
		System.out.println("Generated noise in " + (System.currentTimeMillis() - startTime) + "ms\n");
	}
	
	private int MakeMapColors(float noiseValue)
	{		
		int pixelColor = 0;
		int baseLight = Color.rgba8888(0.1f, 0.1f, 0.1f, 1.0f);

		noiseValue = Math.min(noiseValue, 0.6f);
		
		for(float[] i : COLOR_GRADIENT)
        {
            if(noiseValue <= i[0])
            {
                pixelColor = (int) i[1];
                break;
            }
        }
		
		return pixelColor + baseLight;
	}

	@Override
    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Update();

        //spriteBatch.disableBlending();
		spriteBatch.begin();
		{
			spriteBatch.draw(texture, 0, 0, V_WIDTH, V_HEIGHT);
		}
		spriteBatch.end();
	}

	private void Update()
	{

	}

	@Override
	public void resize(int width, int height)
	{
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		super.resize(width, height);
	}

	@Override
	public void dispose()
	{
		texture.dispose();
		pixmap.dispose();
		spriteBatch.dispose();
		
		super.dispose();
	}
}
