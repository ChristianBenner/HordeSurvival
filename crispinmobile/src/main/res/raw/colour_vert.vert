
attribute vec4 vPosition;
uniform mat4 uMatrix;

void main()
{
    gl_Position = uMatrix * vPosition;
}