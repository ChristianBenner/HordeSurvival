precision mediump float;

varying vec4 aColour;
uniform vec4 uColour;

void main()
{
    gl_FragColor = uColour * aColour;
}