
uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoord;

void main()
{
	//gl_FragColor = v_color;
	//gl_FragColor = vec4(0, 0, 1.0, 1.0);
	gl_FragColor = texture2D(u_texture, v_texCoord);
}
