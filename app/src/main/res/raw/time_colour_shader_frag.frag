precision mediump float;

uniform vec4 uColour;
uniform float uTime;

void main()
{
    vec4 colour = uColour;
    colour.r = colour.r * sin(uTime);
    colour.g = colour.g * cos(uTime);

    gl_FragColor = colour;
}