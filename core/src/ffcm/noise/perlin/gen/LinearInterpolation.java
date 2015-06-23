
package ffcm.noise.perlin.gen;

public class LinearInterpolation implements Interpolation
{
    public float Interpolate(float v0, float a, float v1)
	{
		return ((1.0f - a) * v0 + a * v1);
	}
}
