
attribute vec3 a_position;
attribute vec4 a_color;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform sampler2D u_texture;
uniform vec2 u_textureSize;

varying vec4 v_color;

void main()
{
	//v_color = a_color;
	v_color = texture2D(u_texture, a_position.xy / u_textureSize.xy);

	gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}
