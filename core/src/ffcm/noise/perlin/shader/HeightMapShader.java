
package ffcm.noise.perlin.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class HeightMapShader implements Shader
{
    private ShaderProgram shaderProgram;
    private Camera camera;
    private RenderContext renderContext;

    private int u_worldTransUniformLocation;
    private int u_projViewTransUniformLocation;

    @Override
    public void init()
    {
        shaderProgram = new ShaderProgram(Gdx.files.internal("noise.vert"), Gdx.files.internal("noise.frag"));

		if(!shaderProgram.isCompiled())
		    throw new GdxRuntimeException(shaderProgram.getLog());

        u_worldTransUniformLocation = shaderProgram.getUniformLocation("u_worldTrans");
        u_projViewTransUniformLocation = shaderProgram.getUniformLocation("u_projViewTrans");
    }

    @Override
    public void begin(Camera currentCamera, RenderContext context)
    {
        camera = currentCamera;
        renderContext = context;

        shaderProgram.begin();

        shaderProgram.setUniformMatrix(u_projViewTransUniformLocation, camera.combined);

        renderContext.setDepthTest(GL20.GL_LEQUAL);
        renderContext.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable)
    {
        shaderProgram.setUniformMatrix(u_worldTransUniformLocation, renderable.worldTransform);

        renderable.mesh.render(
            shaderProgram,
            renderable.primitiveType,
            renderable.meshPartOffset,
            renderable.meshPartSize
        );
    }

    @Override
    public void end()
    {
        shaderProgram.end();
    }

    @Override
    public int compareTo(Shader other)
    {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance)
    {
        return true;
    }

    @Override
    public void dispose()
    {
        shaderProgram.dispose();
    }
}
