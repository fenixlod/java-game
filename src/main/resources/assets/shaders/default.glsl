#type vertex
#version 330 core

layout (location=0) in vec3 position;
layout (location=1) in vec4 color;
layout (location=2) in vec2 textureMapping;
layout (location=3) in float textureIndex;
layout (location=5) in float outlined;

uniform mat4 projMat;
uniform mat4 viewMat;

out vec4 fragmentColor;
out vec2 textureUV;
flat out float textureIdx;
flat out float isOutlined;

void main()
{
	fragmentColor = color;
	textureUV = textureMapping;
	textureIdx = textureIndex;
	isOutlined = outlined;
	gl_Position = projMat * viewMat * vec4(position, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;
in vec2 textureUV;
flat in float textureIdx;
flat in float isOutlined;

uniform sampler2D textures[32];

out vec4 color;

void main()
{
	if(isOutlined > 0) {
		if(textureUV.x + 0.005 > 1.0f || textureUV.y + 0.005 > 1.0f ||
			textureUV.x - 0.005 < 0.0f || textureUV.y - 0.005 < 0.0f) {
			
			if(textureIdx > 0) {
				color = texture2D(textures[int(textureIdx)], textureUV);
			} else {
				color = fragmentColor;
			}
        	
        	if(color.a <= 0.5) // Replacement of glAlphaFunc
       			discard;
        
			color = vec4(0.0, 0.0, 1.0, 1.0);
		} else {
			if(textureIdx > 0) {
				vec2 offsetx;
				vec2 offsety;
				offsetx.x = 0.005;
				offsety.y = 0.005;
				
				float alpha = texture2D(textures[int(textureIdx)], textureUV).a;
				
				alpha = max(alpha, texture2D(textures[int(textureIdx)], textureUV + offsetx).a);
				alpha = max(alpha, texture2D(textures[int(textureIdx)], textureUV - offsetx).a);
				alpha = max(alpha, texture2D(textures[int(textureIdx)], textureUV + offsety).a);
				alpha = max(alpha, texture2D(textures[int(textureIdx)], textureUV - offsety).a);
				
				//color = vec4(alpha, alpha, alpha, 1.0);
				color = fragmentColor * texture2D(textures[int(textureIdx)], textureUV);
				if(color.a < 0.9) {
					if(alpha > color.a)
						color = vec4(0.0, 0.0, 1.0, 1.0);
				}
			} else {
				color = fragmentColor;
			}
		}
	} else {
		if(textureIdx > 0)
			color = fragmentColor * texture(textures[int(textureIdx)], textureUV);
		else
			color = fragmentColor;
	}
	
	if(color.a <= 0.9) // Replacement of glAlphaFunc
        discard;
}
