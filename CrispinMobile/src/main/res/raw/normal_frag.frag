precision mediump float;

uniform vec4 uColour;

varying vec3 vFragPos;
varying vec3 vNormal;
varying vec3 vLightDir;
varying vec3 vPosition;


/*
varying vec3 vPosition;
varying vec3 vLightPos;
varying vec3 vNormal;*/

void main()
{
/*    // Light colour
    float intensity = 0.3f;
    vec3 lightColour = vec3(1.0f, 1.0f, 1.0f);
    vec4 objectColour = vec4(1.0f, 0.0f, 0.0f, 1.0f);

    // Ambient light calculation
    float ambientStrength = 0.3;
    vec3 ambient = ambientStrength * lightColour;

    vec4 result = vec4(ambient, 1.0f) * objectColour;
    gl_FragColor = result;

    vec3 norm = normalize(vNormal);
    vec3 lightDir = normalize(vLightDir - vFragPos);

    float diff = max(dot(norm, lightDir), 0.0f);
    vec3 diffuse = diff * lightColour / (intensity*intensity);

    gl_FragColor = vec4(diffuse, 1.0f) * objectColour;
  //  gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);*/




    float distance = length(vLightDir - vPosition);
    float lightIntensity = 65.0f;
    vec3 n = normalize(vNormal);
    vec3 l = normalize(vLightDir);
    vec3 lightColour = vec3(1.0, 1.0, 1.0);
    float cosTheta = clamp(dot(n, l), 0.0f, 1.0f);
    gl_FragColor = vec4(lightColour * lightIntensity * cosTheta / (distance*distance), 1.0);

 //   gl_FragColor = vec4(cosTheta, cosTheta, cosTheta, 1.0f);
 //   gl_FragColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);



/*    vec3 pos = vLightDir;
    vec3 lightColour = vec3(1.0f, 1.0f, 1.0f);
    float intensity = 5.0f;
    float distance = length(pos - vPosition);
    vec3 lightVector = normalize(pos - vPosition);
    float diffuse = max(dot(vNormal, lightVector) * 1.0f, 0.1);
    diffuse = diffuse * (1.0 / (1.0 + ((1.0/intensity) * distance * distance)));
    diffuse = diffuse * 10.0;
    gl_FragColor = vec4(lightColour * max(0.2f, diffuse), 1.0f);*/
}