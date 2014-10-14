
package ffcm.perlin;

import java.util.Random;

import com.badlogic.gdx.math.Vector3;

public class PerlinNoise3d
{
	Vector3[][][] gradientMatrix;
	int matrixSize;
	
	Random random;
	
	public PerlinNoise3d(long seed, int matrixSize)
	{
		this.matrixSize = matrixSize;

		random = new Random(seed);
		
		gradientMatrix = new Vector3[matrixSize][matrixSize][matrixSize];
		
		for(int r = 0; r < matrixSize; ++r)
		{
			for(int c = 0; c < matrixSize; ++c)
			{
				for(int p = 0; p < matrixSize; ++p)
				{
					gradientMatrix[r][c][p] = new Vector3
						(
							random.nextInt(),
							random.nextInt(),
							random.nextInt()
						).nor();
				}
			}
		}
	}
	
	private float LinearInterpolate(float x, float y, float a)
	{ 
		return ((1.0f - a) * x + a * y);
	}
	
	private float NonLinearInterpolate(float x, float y, float a)
	{	
		//float value = 1.0f - (a*a * (3.0f - 2.0f * a));
		float value = 1.0f - (6.0f*a*a*a*a*a - 15.0f*a*a*a*a + 10.0f*a*a*a);
		
		return (value * x + (1.0f - value) * y);
	}
	
	private float Interpolate(float x, float y, float a)
	{
		return LinearInterpolate(x, y, a);
		//return NonLinearInterpolate(x, y, a);
	}
	
	public float GetNoiseValue(final float x, final float y, final float z)
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
		
		final Vector3 p = new Vector3(xI + xF, yI + yF, zI + zF);
		
		//displacement vectors
		Vector3 pTopLeftUpper = new Vector3(xI, yI, zI).sub(p);
		Vector3 pTopRightUpper = new Vector3(xI + 1, yI, zI).sub(p);
		Vector3 pBottomLeftUpper = new Vector3(xI, yI + 1, zI).sub(p);
		Vector3 pBottomRightUpper = new Vector3(xI + 1, yI + 1, zI).sub(p);
		
		Vector3 pTopLeftLower = new Vector3(xI, yI, zI + 1).sub(p);
		Vector3 pTopRightLower = new Vector3(xI + 1, yI, zI + 1).sub(p);
		Vector3 pBottomLeftLower = new Vector3(xI, yI + 1, zI + 1).sub(p);
		Vector3 pBottomRightLower = new Vector3(xI + 1, yI + 1, zI + 1).sub(p);
		
		final int wrappedX = (xI + 1) % matrixSize;
		final int wrappedY = (yI + 1) % matrixSize;
		final int wrappedZ = (zI + 1) % matrixSize;
		
		//gradients
		final Vector3 gTopLeftUpper = gradientMatrix[yI][xI][zI];
		final Vector3 gTopRightUpper = gradientMatrix[yI][wrappedX][zI];
		final Vector3 gBottomLeftUpper = gradientMatrix[wrappedY][xI][zI];
		final Vector3 gBottomRightUpper = gradientMatrix[wrappedY][wrappedX][zI];
		
		final Vector3 gTopLeftLower = gradientMatrix[yI][xI][wrappedZ];
		final Vector3 gTopRightLower = gradientMatrix[yI][wrappedX][wrappedZ];
		final Vector3 gBottomLeftLower = gradientMatrix[wrappedY][xI][wrappedZ];
		final Vector3 gBottomRightLower = gradientMatrix[wrappedY][wrappedX][wrappedZ];
		
		//weights
		float valTopLeftUpper = gTopLeftUpper.dot(pTopLeftUpper);
		float valTopRightUpper = gTopRightUpper.dot(pTopRightUpper);
		float valBottomLeftUpper = gBottomLeftUpper.dot(pBottomLeftUpper);
		float valBottomRightUpper = gBottomRightUpper.dot(pBottomRightUpper);
		
		float valTopLeftLower = gTopLeftLower.dot(pTopLeftLower);
		float valTopRightLower = gTopRightLower.dot(pTopRightLower);
		float valBottomLeftLower = gBottomLeftLower.dot(pBottomLeftLower);
		float valBottomRightLower = gBottomRightLower.dot(pBottomRightLower);
		
		float xTopUpper = Interpolate(valTopLeftUpper, valTopRightUpper, xF);
		float xBottomUpper = Interpolate(valBottomLeftUpper, valBottomRightUpper, xF);
		
		float xTopLower = Interpolate(valTopLeftLower, valTopRightLower, xF);
		float xBottomLower = Interpolate(valBottomLeftLower, valBottomRightLower, xF);
		
		float valueUpper = Interpolate(xTopUpper, xBottomUpper, yF);
		float valueLower = Interpolate(xTopLower, xBottomLower, yF);
		
		return Interpolate(valueUpper, valueLower, zF);
	}
}
