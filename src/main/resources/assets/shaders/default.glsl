#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;

out vec4 fragmentColor;

void main()
{
	fragmentColor = color;
	gl_Position = vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;

out vec4 color;

void main()
{
	color = fragmentColor;
}