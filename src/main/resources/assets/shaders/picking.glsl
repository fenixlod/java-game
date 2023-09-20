#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;
layout (location=2) in vec2 textureMapping;
layout (location=3) in float textureIndex;
layout (location=4) in float objectID;

uniform mat4 viewXProj;

out vec4 fragmentColor;
out vec2 textureUV;
flat out float textureIdx;
flat out float entityID;

void main()
{
	fragmentColor = color;
	textureUV = textureMapping;
	textureIdx = textureIndex;
	entityID = objectID;
	gl_Position = viewXProj * vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;
in vec2 textureUV;
flat in float textureIdx;
flat in float entityID;

uniform sampler2D textures[32];

out vec3 color;

void main()
{
	vec4 texColor = vec4(1, 1, 1, 1);
	if(textureIdx > 0)
		texColor = fragmentColor * texture(textures[int(textureIdx)], textureUV);
		
	if(texColor.a < 0.1) {
		discard;
	}
	
	color = vec3(entityID, entityID, entityID);
}
