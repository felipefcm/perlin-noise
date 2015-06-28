
package ffcm.noise.perlin.mesh;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class HeightMesh
{
    private int size;
    private float zValue;

    private Model model;
    private ModelInstance modelInstance;

    public HeightMesh(int size, float zValue)
    {
        this.size = size;
        this.zValue = zValue;

        CreateModel();
    }

    private Vector3[][] CreateTrianglePlanePoints()
	{
		Vector3[][] points = new Vector3[size][size];

		for(int r = 0; r < size; ++r)
		{
			for(int c = 0; c < size; ++c)
			{
				points[r][c] = new Vector3();
			}
		}

		return points;
	}

	private void CreateModel()
	{
		ModelBuilder builder = new ModelBuilder();

		builder.begin();
		{
			MeshPartBuilder meshBuilder = builder.part(
				"terrain",
				GL20.GL_TRIANGLE_STRIP,
				VertexAttributes.Usage.Position, //| Usage.TextureCoordinates,
				new Material()
			);

			//meshBuilder.vertex(
		}
		model = builder.end();

		modelInstance = new ModelInstance(model);
	}

	public ModelInstance GetModelInstance()
    {
        return modelInstance;
    }
}
