
package ffcm.noise.perlin.gen;

import com.badlogic.gdx.math.MathUtils;

public class ValueNoise2d
{
	private float[][] matrix;
	private int matrixSize;
	
	public ValueNoise2d(long seed, int matrixSize)
	{
		this.matrixSize = matrixSize;

		MathUtils.random.setSeed(seed);
		
		matrix = new float[matrixSize][matrixSize];
		
		for(int r = 0; r < matrixSize; ++r)
			for(int c = 0; c < matrixSize; ++c)		
                matrix[r][c] = MathUtils.random(-1.0f, 1.0f);
	}
	
	public float GetNoiseValue(final float x, final float y, final ffcm.noise.perlin.gen.interpolation.Interpolation interpolation)
	{
		int xI = (int) x;
		int yI = (int) y;
		
		float xF = x - xI;
		float yF = y - yI;
		
		xI = xI % matrixSize;
		yI = yI % matrixSize;

		int wrappedX = (xI + 1) % matrixSize;
		int wrappedY = (yI + 1) % matrixSize;

        //lattice point values
		float gTopLeft = matrix[yI][xI];
		float gTopRight = matrix[yI][wrappedX];
		float gBottomLeft = matrix[wrappedY][xI];
		float gBottomRight = matrix[wrappedY][wrappedX];
		
		float xTop = interpolation.Interpolate(gTopLeft, xF, gTopRight);
		float xBottom = interpolation.Interpolate(gBottomLeft, xF, gBottomRight);
		
		return interpolation.Interpolate(xTop, yF, xBottom);
	}
}
