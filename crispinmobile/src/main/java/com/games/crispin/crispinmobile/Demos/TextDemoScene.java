package com.games.crispin.crispinmobile.Demos;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Utilities.Scene;

public class TextDemoScene extends Scene {
    private Text standardText;
    private Text standardTextMaxLength;
    private Text centeredText;
    private Text wrappedText;
    private Text wrappedCenteredText;
    private Camera2D camera2D;

    private static final int PADDING_PIXELS = 50;

    public TextDemoScene()
    {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        camera2D = new Camera2D();

        final Font aileronRegular = new Font(R.raw.aileron_regular, 64);

        standardText = new Text(aileronRegular,
                "This is standard text. It doesn't even have a max line length.");

        standardTextMaxLength = new Text(aileronRegular,
                "This is standard text. It has a max line length so it will wrap.",
                Crispin.getSurfaceWidth());

        centeredText = new Text(aileronRegular,
                "This text has the centered format applied to it!",
                false,
                true,
                Crispin.getSurfaceWidth());

        wrappedText = new Text(aileronRegular,
                "This text has the word wrap format applied to it!",
                true,
                false,
                Crispin.getSurfaceWidth());

        standardText.setColour(Colour.RED);
        standardTextMaxLength.setColour(Colour.LIGHT_GREY);
        centeredText.setColour(Colour.CYAN);
        wrappedText.setColour(Colour.ORANGE);

        wrappedCenteredText = new Text(aileronRegular,
                "This text has both word wrap and centering formats.",
                true,
                true,
                Crispin.getSurfaceWidth());
        wrappedCenteredText.setColour(Colour.MAGENTA);

        standardText.setPosition(0.0f,
                Crispin.getSurfaceHeight() - standardText.getHeight());

        standardTextMaxLength.setPosition(0.0f,
                standardText.getPosition().y - standardTextMaxLength.getHeight() - PADDING_PIXELS);

        centeredText.setPosition((Crispin.getSurfaceWidth() / 2.0f) -
                        (centeredText.getWidth() / 2.0f),
                standardTextMaxLength.getPosition().y - centeredText.getHeight() - PADDING_PIXELS);

        wrappedText.setPosition(0.0f,
                centeredText.getPosition().y - wrappedText.getHeight() - PADDING_PIXELS);

        wrappedCenteredText.setPosition((Crispin.getSurfaceWidth() / 2.0f) -
                        (wrappedCenteredText.getWidth() / 2.0f),
                wrappedText.getPosition().y - wrappedCenteredText.getHeight() - PADDING_PIXELS);
    }

    @Override
    public void update(float deltaTime)
    {

    }

    @Override
    public void render()
    {
        standardText.draw(camera2D);
        standardTextMaxLength.draw(camera2D);
        centeredText.draw(camera2D);
        wrappedText.draw(camera2D);
        wrappedCenteredText.draw(camera2D);
    }
}
