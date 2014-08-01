
package ffcm.perlin;

import java.util.Random;

import com.badlogic.gdx.math.Vector2;

public class PerlinNoise
{
	Vector2[][] gradientMatrix;
	int matrixSize;
	
	Random random;
	
	public PerlinNoise(long seed, int matrixSize)
	{
		this.matrixSize = matrixSize;

		random = new Random(seed);
		
		gradientMatrix = new Vector2[matrixSize][matrixSize];
		
		for(int r = 0; r < matrixSize; ++r)
		{
			for(int c = 0; c < matrixSize; ++c)		
			{
				gradientMatrix[r][c] = new Vector2
				(
					random.nextFloat() * (random.nextBoolean() ? -1.0f : 1.0f), 
					random.nextFloat() * (random.nextBoolean() ? -1.0f : 1.0f)
				).nor();
			}
		}
	}
	
	private float LinearInterpolate(float x, float y, float a)
	{ 
		return (a * x + (1.0f - a) * y);
	}
	
	private float NonLinearInterpolate(float x, float y, float a)
	{	
		float value = 1.0f - (a*a * (3.0f - 2.0f * a));
		
		return (value * x + (1.0f - value) * y);
	}
	
	public float GetNoiseValue(final float x, final float y)
	{
		int xI = (int) x;
		int yI = (int) y;
		
		float xF = x - xI;
		float yF = y - yI;
		
		xI = xI % (matrixSize - 1);
		yI = yI % (matrixSize - 1);
		
		final Vector2 p = new Vector2(xI + xF, yI + yF);
				
		Vector2 pTopLeft = new Vector2(xI, yI).sub(p);
		Vector2 pTopRight = new Vector2(xI + 1, yI).sub(p);
		Vector2 pBottomLeft = new Vector2(xI, yI + 1).sub(p);
		Vector2 pBottomRight = new Vector2(xI + 1, yI + 1).sub(p);
		
		final Vector2 gTopLeft = gradientMatrix[yI][xI];
		final Vector2 gTopRight = gradientMatrix[yI][xI + 1];
		final Vector2 gBottomLeft = gradientMatrix[yI + 1][xI];
		final Vector2 gBottomRight = gradientMatrix[yI + 1][xI + 1];
		
		float valTopLeft = gTopLeft.dot(pTopLeft);
		float valTopRight = gTopRight.dot(pTopRight);
		float valBottomLeft = gBottomLeft.dot(pBottomLeft);
		float valBottomRight = gBottomRight.dot(pBottomRight);
		
		float xTop = NonLinearInterpolate(valTopLeft, valTopRight, xF);
		float xBottom = NonLinearInterpolate(valBottomLeft, valBottomRight, xF);
		
		return NonLinearInterpolate(xTop, xBottom, yF);
	}
}
