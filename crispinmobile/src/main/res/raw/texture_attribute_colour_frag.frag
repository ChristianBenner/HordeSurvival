precision mediump float;

uniform sampler2D uTexture;

varying vec2 aTextureCoordinates;
varying vec4 aColour;

void main()
{
    gl_FragColor = aColour * texture2D(uTexture, aTextureCoordinates);
}