
attribute vec4 vPosition;
uniform mat4 uMatrix;
uniform float uTime;

void main()
{
    gl_Position = uMatrix * vec4(vPosition.x - (vPosition.x  * (sin(uTime) / 2.0f)), vPosition.y - (vPosition.y  * (cos(uTime) / 2.0f)), vPosition.zw);
}
