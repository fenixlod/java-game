#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;
layout (location=2) in vec2 textureMapping;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 fragmentColor;
out vec2 textureUV;

void main()
{
	fragmentColor = color;
	textureUV = textureMapping;
	gl_Position = projMat * viewMat * vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;
in vec2 textureUV;

uniform sampler2D textureSlot;
uniform int useTexture;

out vec4 color;

void main()
{
	if(useTexture == 1)
		color = texture(textureSlot, textureUV);
	else
		color = fragmentColor;
}
