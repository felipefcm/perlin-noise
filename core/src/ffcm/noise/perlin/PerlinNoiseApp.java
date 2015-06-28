
package ffcm.noise.perlin;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ffcm.noise.perlin.input.AppInput;
import ffcm.noise.perlin.mesh.HeightMesh;
import ffcm.noise.perlin.mesh.HeightMeshShaderProvider;

public class PerlinNoiseApp extends ApplicationAdapter
{
	public static PerlinNoiseApp instance;

	public static final int V_WIDTH = 1024;
	public static final int V_HEIGHT = 768;
	public static final float DESKTOP_SCALE = 1.0f;

	private ShaderProgram shaderProgram;

	private FitViewport viewport;
	private PerspectiveCamera camera;

	private HeightMesh heightMesh;

	private ModelBatch modelBatch;
	private ModelInstance modelInstance;
	
	@Override
	public void create() 
	{
		instance = this;

		camera = new PerspectiveCamera(60.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0.0f, 0.0f, 10.0f);
		camera.lookAt(0.0f, 0.0f, 0.0f);
		camera.near = 1.0f;
		camera.far = 100.0f;
		camera.update();

		//viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

		modelBatch = new ModelBatch(new HeightMeshShaderProvider());
		//modelBatch = new ModelBatch();
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		heightMesh = new HeightMesh(10, 0.0f);
		heightMesh.Create();

		modelInstance = new ModelInstance(heightMesh.GetModelInstance());

		InputMultiplexer inputMultiplexer = new InputMultiplexer(new CameraInputController(camera), new AppInput());
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
    public void render()
    {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		
		Update();

		modelBatch.begin(camera);
		{
			modelBatch.render(modelInstance);
		}
		modelBatch.end();
	}

	private void Update()
	{

	}

	@Override
	public void resize(int width, int height)
	{
		//viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		super.resize(width, height);
	}

	@Override
	public void dispose()
	{
		super.dispose();

		modelBatch.dispose();
	}
}
