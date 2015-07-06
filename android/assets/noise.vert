
attribute vec3 a_position;
attribute vec4 a_color;

uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;

uniform sampler2D u_texture;
uniform float u_textureSize;

varying vec2 v_texCoord;
varying vec4 v_color;

void main()
{
	float edgeValue = u_textureSize - 1.0;
	
	vec2 texturePoint = vec2(a_position.x, -a_position.y) / edgeValue;
	v_texCoord = texturePoint;
	
	float noiseValue = texture2D(u_texture, texturePoint).r * 15.0;
	vec4 vertexPos = vec4(a_position.xy, noiseValue, 1.0);

	gl_Position = u_projViewTrans * u_worldTrans * vertexPos;
}
