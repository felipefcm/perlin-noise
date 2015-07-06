
package ffcm.noise.perlin.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

    private Texture noiseTexture;

    private int u_worldTransUniformLocation;
    private int u_projViewTransUniformLocation;
    private int u_textureUniformLocation;
    private int u_textureSizeUniformLocation;

    @Override
    public void init()
    {
        shaderProgram = new ShaderProgram(Gdx.files.internal("noise.vert"), Gdx.files.internal("noise.frag"));

		if(!shaderProgram.isCompiled())
		    throw new GdxRuntimeException(shaderProgram.getLog());

        if(shaderProgram.getLog().length() > 0)
            Gdx.app.log("ShaderLog", "ShaderLog: " + shaderProgram.getLog());

        u_worldTransUniformLocation = shaderProgram.getUniformLocation("u_worldTrans");
        u_projViewTransUniformLocation = shaderProgram.getUniformLocation("u_projViewTrans");
        u_textureUniformLocation = shaderProgram.getUniformLocation("u_texture");
        u_textureSizeUniformLocation = shaderProgram.getUniformLocation("u_textureSize");
    }

    @Override
    public void begin(Camera currentCamera, RenderContext context)
    {
        camera = currentCamera;
        renderContext = context;

        shaderProgram.begin();

        if(noiseTexture != null)
        {
            noiseTexture.bind(0);
            shaderProgram.setUniformi(u_textureUniformLocation, 0);
            shaderProgram.setUniformf(u_textureSizeUniformLocation, (float) noiseTexture.getWidth());
        }

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

    public void SetNoiseTexture(Texture texture)
    {
        noiseTexture = texture;
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

    public ShaderProgram GetProgram()
    {
        return shaderProgram;
    }
}
