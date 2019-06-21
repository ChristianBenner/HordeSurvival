precision mediump float;

uniform vec4 uColour;
uniform sampler2D uTexture;

varying vec2 aTextureCoordinates;

void main()
{
    gl_FragColor = uColour * texture2D(uTexture, aTextureCoordinates);
}