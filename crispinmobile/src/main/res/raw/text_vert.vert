
attribute vec4 vPosition;
attribute vec2 vTextureCoordinates;

uniform mat4 uMatrix;

varying vec2 aTextureCoordinates;

void main()
{
    aTextureCoordinates = vTextureCoordinates;
    gl_Position = uMatrix * vPosition;
}