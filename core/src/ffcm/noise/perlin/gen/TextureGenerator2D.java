
package ffcm.noise.perlin.gen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

import ffcm.noise.perlin.gen.interpolation.CosineInterpolation;

public class TextureGenerator2D
{
    //noise arguments
	public static final float LACUNARITY = 1.87f;
	public static final float GAIN = 0.5f;

	public int numOctaves = 8;

	private ValueNoise2d noise;
	public Pixmap pixmap;

	private int textureSize;

	public TextureGenerator2D(int matrixSize, int textureSize)
	{
		this.textureSize = textureSize;

		noise = new ValueNoise2d(new Date().getTime(), matrixSize);
	}

	public Texture GenerateNoiseTexture()
	{
		pixmap = new Pixmap(textureSize, textureSize, Pixmap.Format.RGB888);

		Gdx.app.log("NoiseGenerator", "NumOctaves = " + numOctaves);

		long startTime = TimeUtils.millis();

		for(int r = 0; r < pixmap.getHeight(); ++r)
		{
			for(int c = 0; c < pixmap.getWidth(); ++c)
			{
				float frequency = 1.0f / (float) pixmap.getWidth();
				float amplitude = GAIN;

				float noiseValue = 0;
				float maxVal = 0;

				for(int oc = 0; oc < numOctaves; ++oc)
				{
					noiseValue += noise.GetNoiseValue(c * frequency, r * frequency, new CosineInterpolation()) * amplitude;

					maxVal += amplitude;
					frequency *= LACUNARITY;
					amplitude *= GAIN;
				}

                if(noiseValue < 0)
				    noiseValue *= -1.0f;

				noiseValue /= maxVal;

				int color = Color.rgba8888(noiseValue, noiseValue, noiseValue, 1.0f);

				pixmap.drawPixel(c, r, color);
			}
		}

		Texture texture = new Texture(pixmap);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Gdx.app.log("NoiseGenerator", "Generated noise texture in " + TimeUtils.timeSinceMillis(startTime) + "ms");

		return texture;
	}

	public void Dispose()
    {
        noise = null;
    }
}
