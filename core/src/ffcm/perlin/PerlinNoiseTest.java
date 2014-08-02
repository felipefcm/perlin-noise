
package ffcm.perlin;

import java.util.Date;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PerlinNoiseTest extends ApplicationAdapter 
{
	public static final int V_WIDTH = 256;
	public static final int V_HEIGHT = 256;
	public static final float DESKTOP_SCALE = 3.0f;
	
	public static final int TEXTURE_SIZE = 1024;
	
	//noise arguments
	public static final int MATRIX_SIZE = 16;
	public static final float LACUNARITY = 1.8f;
	public static final float GAIN = 0.65f;
	
	SpriteBatch spriteBatch;
	Pixmap pixmap;
	Texture texture;
	
	Viewport viewport;
	Camera camera;
	
	PerlinNoise2d noise;
	
	int numOctaves = 12;
	
	@Override
	public void create() 
	{
		camera = new OrthographicCamera();
		viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		spriteBatch = new SpriteBatch(100);
		
		pixmap = new Pixmap(TEXTURE_SIZE, TEXTURE_SIZE, Format.RGB888);
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
		
		CreateNoiseGenerator();
		GenerateNoiseTexture();
		
		Gdx.input.setInputProcessor
		(
			new InputAdapter()
			{
				@Override
				public boolean touchUp(int screenX, int screenY, int pointer, int button)
				{
					CreateNoiseGenerator();

					/*
					if(button == 0)
						++octaves;
					else
						--octaves;
					*/
					
					GenerateNoiseTexture();
					
					return true;
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
		float maxVal = 0;
		
		long startTime = System.currentTimeMillis();
		
		for(int r = 0; r < pixmap.getHeight(); ++r)
		{
			for(int c = 0; c < pixmap.getWidth(); ++c)
			{
				float frequency = 1.0f / (float) pixmap.getWidth();
				float amplitude = GAIN;
				
				float noiseValue = 0;
				
				for(int oc = 0; oc < numOctaves; ++oc)
				{
					noiseValue += noise.GetNoiseValue(c * frequency, r * frequency) * amplitude;
					frequency *= LACUNARITY;
					amplitude *= GAIN;
				}
				
				if(noiseValue > Math.abs(maxVal))
					maxVal = Math.abs(noiseValue);
				
				if(noiseValue < 0)
					noiseValue *= -1.0f;
				
				noiseValue = Math.min(noiseValue, 1.0f);
				
				int pixelColor;
				
				pixelColor = Color.rgba8888(noiseValue, noiseValue, noiseValue, 1.0f);
				//pixelColor = MakeMapColors(noiseValue);
				//pixelColor = MakeSin(noiseValue);
				
				pixmap.drawPixel(c, r, pixelColor);
			}
		}
		
		System.out.println("Amplitude = " + maxVal);
		
		if(texture != null)
			texture.dispose();
			
		texture = new Texture(pixmap);
		
		System.out.println("Generated noise in " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	private int MakeMapColors(float noiseValue)
	{		
		int pixelColor;
		int baseLight = Color.rgba8888(0.1f, 0.1f, 0.1f, 1.0f);
		
		noiseValue = Math.max(noiseValue, 0.06f);
		
		if(noiseValue <= 0.1f)
			pixelColor = Color.rgba8888(0, 0, noiseValue * 7.0f, 1.0f); //water
		else 
			if(noiseValue <= 0.11f)
				pixelColor = Color.rgba8888(noiseValue * 2.0f, noiseValue * 2.0f, 0, 1.0f); //sand
			else
				if(noiseValue <= 0.15f)
					pixelColor = Color.rgba8888(0.5f * noiseValue * 2.5f, 0.2f * noiseValue * 2.5f, 0, 1.0f); //dirt
				else
					if(noiseValue <= 0.32f)
						pixelColor = Color.rgba8888(0, noiseValue * 0.9f, 0, 1.0f); //grass
					else
						if(noiseValue <= 0.4f)
							pixelColor = Color.rgba8888(noiseValue * 2.0f, noiseValue * 2.0f, noiseValue * 2.0f, 1.0f); //snow
						else
							pixelColor = Color.rgba8888(0.9f, 0.9f, 0.9f, 1.0f);
		
		return pixelColor + baseLight;
	}

	private int MakeSin(float noiseValue)
	{
		float sin = (float) Math.sin(noiseValue);
		
		return Color.rgba8888(sin, sin, sin, 1.0f);
	}
	
	@Override
	public void render() 
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		{
			spriteBatch.draw(texture, 0, 0, V_WIDTH, V_HEIGHT);
		}
		spriteBatch.end();
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
