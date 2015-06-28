
package ffcm.noise.perlin.gen;

public class TextureGenerator
{
    //noise arguments
	public static final int MATRIX_SIZE = 16;
	public static final float LACUNARITY = 1.87f;
	public static final float GAIN = 0.5f;

	private PerlinNoise2d noise;

	public int numOctaves = 8;

	/*
	public void CreateNoiseGenerator()
	{
		noise = new PerlinNoise2d(new Date().getTime(), MATRIX_SIZE);
	}

	public void GenerateNoiseTexture()
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

				int pixelColor = MakeMapColors(noiseValue);
                //int pixelColor = Color.rgba8888(noiseValue, noiseValue, noiseValue, 1.0f);

				pixmap.drawPixel(c, r, pixelColor);
			}
		}

		System.out.println("Amplitude = " + maxVal);

		if(texture != null)
			texture.dispose();

		texture = new Texture(pixmap);

		System.out.println("Generated noise in " + (System.currentTimeMillis() - startTime) + "ms\n");
	}
	*/
}
