
package ffcm.noise.perlin.gen.interpolation;

import com.badlogic.gdx.math.MathUtils;

public class CosineInterpolation implements Interpolation
{
    @Override
    public float Interpolate(float v0, float a, float v1)
    {
        float alpha = ((float) Math.cos(a * MathUtils.PI)) * 0.5f + 0.5f;
        return (1.0f - alpha) * v1 + alpha * v0;
    }
}
