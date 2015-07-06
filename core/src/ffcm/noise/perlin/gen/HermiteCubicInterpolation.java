
package ffcm.noise.perlin.gen;

public class HermiteCubicInterpolation implements Interpolation
{
    public float Interpolate(float v0, float a, float v1)
	{
		float value = a*a * (3.0f - 2.0f * a);
		return ((1.0f - value) * v0 + value * v1);
	}
}
