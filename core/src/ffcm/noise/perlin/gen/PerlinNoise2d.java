
package ffcm.noise.perlin.gen;

import com.badlogic.gdx.math.MathUtils;

public class PerlinNoise2d
{
	private float[][] gradientMatrix;
	private int matrixSize;
	
	public PerlinNoise2d(long seed, int matrixSize)
	{
		this.matrixSize = matrixSize;

		MathUtils.random.setSeed(seed);
		
		gradientMatrix = new float[matrixSize][matrixSize];
		
		for(int r = 0; r < matrixSize; ++r)
			for(int c = 0; c < matrixSize; ++c)		
                gradientMatrix[r][c] = MathUtils.random(-1.0f, 1.0f);
	}
	
	public float GetNoiseValue(final float x, final float y, final Interpolation interpolation)
	{
		int xI = (int) x;
		int yI = (int) y;
		
		float xF = x - xI;
		float yF = y - yI;
		
		xI = xI % matrixSize;
		yI = yI % matrixSize;

		int wrappedX = (xI + 1) % matrixSize;
		int wrappedY = (yI + 1) % matrixSize;

		//gradients
		float gTopLeft = gradientMatrix[yI][xI];
		float gTopRight = gradientMatrix[yI][wrappedX];
		float gBottomLeft = gradientMatrix[wrappedY][xI];
		float gBottomRight = gradientMatrix[wrappedY][wrappedX];
		
		float xTop = interpolation.Interpolate(gTopLeft, xF, gTopRight);
		float xBottom = interpolation.Interpolate(gBottomLeft, xF, gBottomRight);
		
		return interpolation.Interpolate(xTop, yF, xBottom);
	}
}
