
uniform mat4 uMatrix;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;
uniform vec2 uUvMultiplier;

attribute vec4 aPosition;
attribute vec3 aNormal;
attribute vec2 aTextureCoordinates;

varying vec3 vNormal;
varying vec3 vLightDir;
varying vec3 vPosition;
varying vec3 vEyeDirection;
varying vec2 vTextureCoordinates;

void main()
{
    vTextureCoordinates = uUvMultiplier * aTextureCoordinates;

    vPosition = vec3(uView * uModel * aPosition);
    gl_Position = uProjection * uView * uModel * aPosition;

    // Vertex Position camera space
    vec3 vertexPosCameraSpace = (uView * uModel * aPosition).xyz;

    // Eye Direction Camera Space
    vEyeDirection = vec3(0.0, 0.0, 0.0) - vertexPosCameraSpace;

    // Light position Camera space
    vec3 lightPosCameraSpace = (uView * vec4(0.0f, 0.0f, 2.0f, 1.0f)).xyz;

    // Light direction
    vLightDir = lightPosCameraSpace + vEyeDirection;

    vNormal = vec3(uView * uModel * vec4(aNormal, 0.0));
}