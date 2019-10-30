
uniform mat4 uMatrix;

uniform mat4 uProjection;
uniform mat4 uView;
uniform mat4 uModel;

attribute vec4 aPosition;
attribute vec3 aNormal;

varying vec3 vFragPos;
varying vec3 vNormal;
varying vec3 vLightDir;
varying vec3 vPosition;

/*varying vec3 vPosition;
varying vec3 vLightPos;
varying vec3 vNormal;*/

void main()
{
/*    gl_Position = uProjection * uView * uModel * aPosition;
    vFragPos = vec3(uView * uModel * aPosition);
    vNormal = vec3(uView * uModel * vec4(aNormal, 0.0f));
    vLightDir = vec3(uView * vec4(2.0f, 3.0f, 2.0f, 1.0f));*/


    vPosition = vec3(uView * uModel * aPosition);
    gl_Position = uProjection * uView * uModel * aPosition;

    // Vertex Position camera space
    vec3 vertexPosCameraSpace = (uView * uModel * aPosition).xyz;

    // Eye Direction Camera Space
    vec3 eyeDirectionCameraSpace = vec3(0.0, 0.0, 0.0) - vertexPosCameraSpace;

    // Light position Camera space
    vec3 lightPosCameraSpace = (uView * vec4(0.0f, 0.0f, 2.0f, 1.0f)).xyz;

    // Light direction
    vLightDir = lightPosCameraSpace + eyeDirectionCameraSpace;

    vNormal = vec3(uView * uModel * vec4(aNormal, 0.0));




/*    vPosition = vec3(uView * uModel * aPosition);
    vNormal = vec3(uView * uModel * vec4(aNormal, 0.0));
    vLightDir = vec3(0.0f, 0.0f, 0.0f);
    gl_Position = uProjection * uView * uModel * aPosition;*/
}