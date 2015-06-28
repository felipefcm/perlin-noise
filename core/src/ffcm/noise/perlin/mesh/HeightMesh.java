
package ffcm.noise.perlin.mesh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

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
    }

    public void Create()
    {
        long time = TimeUtils.millis();

        CreateModel();

        time = TimeUtils.timeSinceMillis(time);
        Gdx.app.log("HeightMesh", "Model created in " + time + "ms");
    }

    private short[] CreateIndexData()
    {
        final int numStrips = size - 1;
        final int indicesPerStrip = 2 * size;
        final int indicesForDegenTris = 2 * (numStrips - 1); //extra indices used for degenerate triangles

        final int numIndexes = numStrips * indicesPerStrip + indicesForDegenTris;

        short[] indices = new short[numIndexes];

        int current = 0;

        for(int r = 0; r < numStrips; ++r)
        {
            //copy index to create degenerate
            if(r > 0) //skip the first row
                indices[current++] = (short) (r * size); //repeat first index, c = 0

            for(int c = 0; c < size; ++c)
            {
                indices[current++] = (short) (r * size + c);
                indices[current++] = (short) ((r + 1) * size + c);
            }

            //copy index to create degenerate
            if(r < numStrips - 1) //skip the last row
                indices[current++] = (short) ((r + 2) * size - 1); //repeat last index, c = size - 1
        }

        return indices;
    }

	private void CreateModel()
	{
		ModelBuilder builder = new ModelBuilder();

		builder.begin();
		{
			MeshPartBuilder meshBuilder = builder.part(
				"terrain",
				GL20.GL_TRIANGLE_STRIP,
				VertexAttributes.Usage.Position,
				new Material()
			);

            //create vertices: top-down, left to right
            for(int r = 0; r < size; ++r)
                for(int c = 0; c < size; ++c)
			        meshBuilder.vertex(new Vector3(c, r, zValue), null, null, null);

            short[] indices = CreateIndexData();

            for(int i = 0; i < indices.length; ++i)
                meshBuilder.index(indices[i]);
		}
		model = builder.end();

		modelInstance = new ModelInstance(model);
	}

	public ModelInstance GetModelInstance()
    {
        return modelInstance;
    }
}
