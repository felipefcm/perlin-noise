
package ffcm.noise.perlin.gen.interpolation;

public class HermiteCubicInterpolation implements Interpolation
{
    public float Interpolate(float v0, float a, float v1)
	{
		float alpha = a*a * (3.0f - 2.0f * a);
		return ((1.0f - alpha) * v0 + alpha * v1);
	}
}
