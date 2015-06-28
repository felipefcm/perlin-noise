
package ffcm.noise.perlin;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ffcm.noise.perlin.input.AppInput;
import ffcm.noise.perlin.mesh.HeightMesh;

public class PerlinNoiseApp extends ApplicationAdapter
{
	public static PerlinNoiseApp instance;

	public static final int V_WIDTH = 1024;
	public static final int V_HEIGHT = 768;
	public static final float DESKTOP_SCALE = 1.0f;

	private static final float[][] COLOR_GRADIENT =
    {
        { 0.10f, Color.rgba8888(0f, 0f, 1.0f, 1.0f)}, //water
        { 0.11f, Color.rgba8888(1f, 1f, 0.53f, 1.0f)}, //sand
        { 0.15f, Color.rgba8888(0.49f, 0.24f, 0f, 1.0f)}, //dirt
        { 0.32f, Color.rgba8888(0.08f, 0.71f, 0.05f, 1.0f)}, //grass
        { 0.40f, Color.rgba8888(0.8f, 0.95f, 1f, 1.0f)} //ice
    };

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

		modelBatch = new ModelBatch();

		camera = new PerspectiveCamera(60.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0.0f, 0.0f, 10.0f);
		camera.lookAt(0.0f, 0.0f, 0.0f);
		camera.near = 1.0f;
		camera.far = 100.0f;
		camera.update();

		//viewport = new FitViewport(V_WIDTH, V_HEIGHT, camera);

		shaderProgram = new ShaderProgram(Gdx.files.internal("noise.vert"), Gdx.files.internal("noise.frag"));

		if(shaderProgram.getLog().length() > 0)
		{
			Gdx.app.log("PerlinNoise", shaderProgram.getLog());
		}
		
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);

		heightMesh = new HeightMesh(50, 0.0f);

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
	}
}
