
package ffcm.noise.perlin.mesh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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
        short[] indices = CreateIndexData();

        Mesh mesh = new Mesh(
            true,
            size * size,
            indices.length,
            new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position"),
            new VertexAttribute(VertexAttributes.Usage.ColorUnpacked, 4, "a_color")
        );

        float[] vertices = new float[7 * size * size];
        int index = 0;

        for(int r = 0; r < size; ++r)
        {
            for(int c = 0; c < size; ++c)
            {
                vertices[index++] = c;
                vertices[index++] = -r;
                vertices[index++] = zValue;

                vertices[index++] = 0;
                vertices[index++] = 0;
                vertices[index++] = 1.0f;
                vertices[index++] = 1.0f;
            }
        }

        mesh.setVertices(vertices);
        mesh.setIndices(indices);

        ModelBuilder builder = new ModelBuilder();
		builder.begin();
        {
		    builder.part("terrain", mesh, GL20.GL_TRIANGLE_STRIP, new Material());
        }
		model = builder.end();

		modelInstance = new ModelInstance(model);
    }

	public ModelInstance GetModelInstance()
    {
        return modelInstance;
    }
}
