
uniform mat4 uMatrix;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

attribute vec4 aPosition;
attribute vec3 aNormal;

varying vec3 vFragPos;
varying vec3 vNormal;
varying vec3 vLightPos;

void main()
{
    gl_Position = uProjection * uView * uModel * aPosition;
    vFragPos = vec3(uView * uModel * aPosition);
    vNormal = vec3(uView * uModel * vec4(aNormal, 0.0f));
   // vLightPos = vec3(uView * uModel * vec4(0.0f, 0.0f, 0.0f, 1.0f));
    vLightPos = vec3(0.0f, 1.0f, 3.0f);
}