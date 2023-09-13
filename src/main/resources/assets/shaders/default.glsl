#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;
layout (location=2) in vec2 textureMapping;
layout (location=3) in float textureIndex;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 fragmentColor;
out vec2 textureUV;
flat out float textureIdx;

void main()
{
	fragmentColor = color;
	textureUV = textureMapping;
	textureIdx = textureIndex;
	gl_Position = projMat * viewMat * vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;
in vec2 textureUV;
flat in float textureIdx;

uniform sampler2D textures[32];

out vec4 color;

void main()
{
	if(textureIdx > 0)
		color = fragmentColor * texture(textures[int(textureIdx)], textureUV);
	else
		color = fragmentColor;
		
	if(color.a <= 0.9) // Replacement of glAlphaFunc
        discard;
}
