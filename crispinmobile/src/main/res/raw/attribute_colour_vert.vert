
attribute vec3 vPosition;
attribute vec4 vColour;

uniform mat4 uMatrix;

varying vec4 aColour;

void main()
{
    aColour = vColour;
    gl_Position = uMatrix * vec4(vPosition, 1.0f);
}