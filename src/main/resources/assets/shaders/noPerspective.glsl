#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 fragmentColor;

void main()
{
	fragmentColor = color;
	mat4 newView = mat4(viewMat);
	newView[1][1] = 0f;
	newView[2][1] = 1f;
	newView[1][2] = -1f;
	newView[2][2] = 0f;
	gl_Position = projMat * newView * vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;

out vec4 color;

void main()
{
	color = fragmentColor;
}
