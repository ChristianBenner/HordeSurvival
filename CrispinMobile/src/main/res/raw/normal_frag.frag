precision mediump float;

uniform vec4 uColour;

varying vec3 vFragPos;
varying vec3 vNormal;
varying vec3 vLightPos;

void main()
{
    // Light colour
    vec3 lightColour = vec3(1.0f, 1.0f, 1.0f);

    vec4 objectColour = vec4(1.0f, 0.0f, 0.0f, 1.0f);

    vec3 norm = normalize(vNormal);
    vec3 lightDir = normalize(vLightPos - vFragPos);

    float diff = max(dot(norm, lightDir), 0.0f);
    vec3 diffuse = diff * lightColour;

    gl_FragColor = vec4(diffuse, 1.0f) * objectColour;
  //  gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
}