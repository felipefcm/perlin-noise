
uniform mat4 u_projTrans;

attribute vec4 position;

void main()
{
	gl_Position = u_projTrans * position;
}
