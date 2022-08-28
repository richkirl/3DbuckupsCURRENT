#version 450 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfacesNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility;
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

const float density = 0.0035;
const float gradient = 5.5;

uniform vec4 plane;

void main(void){
	vec4 worldPos = transformationMatrix * vec4(position,1.0);
	gl_ClipDistance[0] = dot(worldPos,plane);
	vec4 posRelativeToCam = viewMatrix*worldPos;
	gl_Position =projectionMatrix*posRelativeToCam;
	pass_textureCoords = textureCoords;
	
	surfacesNormal =(transformationMatrix* vec4(normal,0.0)).xyz;
	toLightVector = lightPos - worldPos.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPos.xyz;

	float distance = length(posRelativeToCam.xyz);
	visibility = exp(-pow((distance*density),gradient));
	visibility = clamp(visibility,0.0,1.0);	
}