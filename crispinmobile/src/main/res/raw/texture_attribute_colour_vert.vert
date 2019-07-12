
attribute vec4 vPosition;
attribute vec2 vTextureCoordinates;
attribute vec4 vColour;

uniform mat4 uMatrix;
uniform vec2 uUvMultiplier;

varying vec2 aTextureCoordinates;
varying vec4 aColour;

void main()
{
    aTextureCoordinates = uUvMultiplier * vTextureCoordinates;
    aColour = vColour;

    gl_Position = uMatrix * vPosition;
}