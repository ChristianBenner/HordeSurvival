precision mediump float;

uniform sampler2D uTexture;

varying vec2 aTextureCoordinates;
varying vec4 aColour;
uniform vec4 uColour;

void main()
{
    gl_FragColor = uColour * aColour * texture2D(uTexture, aTextureCoordinates);
}