#version 450 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVector;
out vec4 out_Color;

uniform sampler2D refractionTexture;
uniform sampler2D reflectionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform vec3 lightColor;
uniform float moveFactor;
const float waveStrength = 0.02;
const float shineDump = 20.0;
const float reflectivity = 0.6;
void main(void){
	vec2 ndc = (clipSpace.xy/clipSpace.w)/2.0+0.5;
	vec2 reflectTexCoords = vec2(-ndc.x,ndc.y);
	vec2 refractTexCoords = vec2(-ndc.x,-ndc.y);
	
	vec2 distortionl = (texture(dudvMap,vec2(textureCoords.x+moveFactor,textureCoords.y)).rg * 2.14 +1.0) * waveStrength;
	vec2 distortion2 = (texture(dudvMap,vec2(textureCoords.x+moveFactor,textureCoords.y+moveFactor)).rg * -2.14 -1.0) * waveStrength;
	vec2 totalDistortion = distortionl + distortion2;
	
	
	
	refractTexCoords += totalDistortion;
	refractTexCoords += clamp(refractTexCoords,0.001,0.999);
	
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x,0.001,0.999);
	reflectTexCoords.y = clamp(reflectTexCoords.y,-0.999,-0.001);
	
	vec4 reflectColor = texture(reflectionTexture,reflectTexCoords);
	vec4 refractColor = texture(refractionTexture,-refractTexCoords);
	
	vec3 viewVector = normalize(toCameraVector);
	float refractiveFactor = dot(viewVector,vec3(0.0,1.0,0.0));
	refractiveFactor = pow(refractiveFactor,0.5);
	vec4 normalMapColor = texture(normalMap,-distortionl*10);
	vec3 normal = vec3(normalMapColor.r *2.0-1.0,normalMapColor.b,normalMapColor.g*2.0-1.0);
	normal = normalize(normal);
	
	vec3 reflectedLight = reflect(normalize(fromLightVector),normal);
	float specular = max(dot(reflectedLight,viewVector),0.0);
	specular = pow(specular,shineDump);
	vec3 specularHighLights = lightColor * specular * reflectivity;
	
	
	out_Color = mix(reflectColor,refractColor,refractiveFactor);
	out_Color = mix(out_Color,vec4(0.0,0.3,0.5,1.0),0.2)+vec4(specularHighLights,0.5);
	
}