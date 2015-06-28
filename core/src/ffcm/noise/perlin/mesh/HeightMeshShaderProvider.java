
package ffcm.noise.perlin.mesh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class HeightMeshShaderProvider extends DefaultShaderProvider
{
    private ShaderProgram shaderProgram;
    private DefaultShader.Config config;

    public HeightMeshShaderProvider()
    {
        shaderProgram = new ShaderProgram(Gdx.files.internal("noise.vert"), Gdx.files.internal("noise.frag"));

		if(shaderProgram.getLog().length() > 0)
		{
			Gdx.app.log("PerlinNoise", shaderProgram.getLog());
		}

		if(!shaderProgram.isCompiled())
		{
			Gdx.app.log("PerlinNoise", "ShaderProgram failed to compile, exiting...");
			Gdx.app.exit();
        }

        config = new DefaultShader.Config(shaderProgram.getVertexShaderSource(), shaderProgram.getFragmentShaderSource());
    }

    @Override
    protected Shader createShader(Renderable renderable)
    {
        return new DefaultShader(renderable, config);
    }
}
