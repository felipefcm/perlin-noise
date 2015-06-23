
package ffcm.noise.perlin.gen;

import com.badlogic.gdx.math.MathUtils;

public class PerlinNoise3d
{
	private float[][][] gradientMatrix;
	private int matrixSize;
	
	public PerlinNoise3d(long seed, int matrixSize)
	{
		this.matrixSize = matrixSize;

		MathUtils.random.setSeed(seed);
		
		gradientMatrix = new float[matrixSize][matrixSize][matrixSize];
		
		for(int r = 0; r < matrixSize; ++r)
			for(int c = 0; c < matrixSize; ++c)
				for(int p = 0; p < matrixSize; ++p)
					gradientMatrix[r][c][p] = MathUtils.random(-1.0f, 1.0f);
	}
	
	public float GetNoiseValue(final float x, final float y, final float z, final Interpolation interpolation)
	{
		int xI = (int) x;
		int yI = (int) y;
		int zI = (int) z;
		
		float xF = x - xI;
		float yF = y - yI;
		float zF = z - zI;
		
		xI = xI % matrixSize;
		yI = yI % matrixSize;
		zI = zI % matrixSize;
		
		int wrappedX = (xI + 1) % matrixSize;
		int wrappedY = (yI + 1) % matrixSize;
		int wrappedZ = (zI + 1) % matrixSize;
		
		//gradients
		float gTopLeftUpper = gradientMatrix[yI][xI][zI];
		float gTopRightUpper = gradientMatrix[yI][wrappedX][zI];
		float gBottomLeftUpper = gradientMatrix[wrappedY][xI][zI];
		float gBottomRightUpper = gradientMatrix[wrappedY][wrappedX][zI];
		
		float gTopLeftLower = gradientMatrix[yI][xI][wrappedZ];
		float gTopRightLower = gradientMatrix[yI][wrappedX][wrappedZ];
		float gBottomLeftLower = gradientMatrix[wrappedY][xI][wrappedZ];
		float gBottomRightLower = gradientMatrix[wrappedY][wrappedX][wrappedZ];
		
		float xTopUpper = interpolation.Interpolate(gTopLeftUpper, xF, gTopRightUpper);
		float xBottomUpper = interpolation.Interpolate(gBottomLeftUpper, xF, gBottomRightUpper);
		
		float xTopLower = interpolation.Interpolate(gTopLeftLower, xF, gTopRightLower);
		float xBottomLower = interpolation.Interpolate(gBottomLeftLower, xF, gBottomRightLower);
		
		float valueUpper = interpolation.Interpolate(xTopUpper, yF, xBottomUpper);
		float valueLower = interpolation.Interpolate(xTopLower, yF, xBottomLower);
		
		return interpolation.Interpolate(valueUpper, zF, valueLower);
	}
}
