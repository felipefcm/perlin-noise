
package ffcm.noise.perlin.gen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

public class TextureGenerator2D
{
    //noise arguments
	public static final float LACUNARITY = 1.87f;
	public static final float GAIN = 0.5f;

	public int numOctaves = 8;

	private ValueNoise2d noise;

	private int textureSize;

	public TextureGenerator2D(int matrixSize, int textureSize)
	{
		this.textureSize = textureSize;

		noise = new ValueNoise2d(new Date().getTime(), matrixSize);
	}

	public Texture GenerateNoiseTexture()
	{
		Pixmap pixmap = new Pixmap(textureSize, textureSize, Pixmap.Format.RGB888);

		Gdx.app.log("NoiseGenerator", "NumOctaves = " + numOctaves);

		long startTime = TimeUtils.millis();

        //Interpolation interpolation = new HermiteCubicInterpolation();
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

				int color = Color.rgba8888(noiseValue, noiseValue, noiseValue, 1.0f);

				pixmap.drawPixel(c, r, color);
			}
		}

		Texture texture = new Texture(pixmap);

		Gdx.app.log("NoiseGenerator", "Amplitude = " + maxVal);
		Gdx.app.log("NoiseGenerator", "Generated noise texture in " + TimeUtils.timeSinceMillis(startTime) + "ms");

		return texture;
	}

	public void Dispose()
    {
        noise = null;
    }
}
