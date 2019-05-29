package com.games.crispin.crispinmobile;

public class Material
{
    private boolean lightingEnabled;
    //texture
    //normal map texture
    private GLSLShader shader;

    public Material(boolean lightingEnabled)
    {
        this.lightingEnabled = lightingEnabled;
        createShader();
    }

    public boolean isLightingEnabled()
    {
        return this.lightingEnabled;
    }

    // When changing any material properties, determine what shader needs to be associated to the
    // material. Check if the shader with the correct properties exists in the ShaderCache already
    public void createShader()
    {
        // Properties required
    }

    public GLSLShader getShader()
    {
        return this.shader;
    }

    public void enableShader()
    {
        // Enable the shader, passing the objects
    }
}
