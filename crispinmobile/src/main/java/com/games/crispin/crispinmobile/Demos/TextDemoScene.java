package com.games.crispin.crispinmobile.Demos;

import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.R;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Rendering.UserInterface.LinearLayout;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Utilities.Scene;

/**
 * A demonstration scene designed to show the usage of text in the Crispin engine
 *
 * @author  Christian Benner
 * @version %I%, %G%
 * @since   1.0
 */
public class TextDemoScene extends Scene
{
    // Padding between text
    private static final int PADDING_PIXELS = 50;

    // Size to load the font as
    private static final int FONT_SIZE = 64;

    // Multiply the wiggle amount (so it does the specified wiggle amount both ways)
    private static final float WIGGLE_MULTIPLIER = 2.0f;

    // Amount to divide the text width by to get half of the width
    final float TEXT_WIDTH_DIVIDE = 2.0f;

    // String for the text with standard formatting
    private static final String STANDARD_TEXT_STRING = "This is standard text. It doesn't even " +
            "have a max line length.";

    // Text string for the text object with standard formatting that has a maximum line length
    private static final String STANDARD_MAX_LENGTH_TEXT_STRING = "This is standard text. It has " +
            "a max line length so it will wrap characters when it reaches it's maximum line length";

    // Text string for the text object with centered text format
    private static final String CENTERED_TEXT_STRING = "This text has the centered format applied" +
            " to it!";

    // Text string for the text object with wrapped format
    private static final String WRAPPED_TEXT_STRING = "This text has the word wrap format applied" +
            " to it!";

    // Text string for the text object with both wrapped and centered formatting
    private static final String WRAPPED_CENTERED_TEXT_STRING = "This text has both word wrap and " +
            "centering formats.";

    // Text string for the text object that wiggles
    private static final String WIGGLE_TEXT_STRING = "Wiggle wiggle wiggle!";

    // Standard text
    private Text standardText;

    // Standard text with a maximum length
    private Text standardTextMaxLength;

    // Centered text
    private Text centeredText;

    // Wrapped text
    private Text wrappedText;

    // Wrapped and centered text
    private Text wrappedCenteredText;

    // Text that wiggles
    private Text wiggleText;

    // 2-dimensional camera
    private Camera2D camera2D;

    // Linear layout for the text tests
    private LinearLayout linearLayout;

    private Text demo;

    /**
     * Construct the text demo scene
     *
     * @since 1.0
     */
    public TextDemoScene()
    {
        Crispin.setBackgroundColour(Colour.DARK_GREY);

        camera2D = new Camera2D();


        // Load the font that the text objects will use
        final Font AILERON_REGULAR = new Font(R.raw.aileron_regular, FONT_SIZE);

        // Construct the text objects
        standardText = new Text(AILERON_REGULAR, STANDARD_TEXT_STRING);
        standardTextMaxLength = new Text(AILERON_REGULAR, STANDARD_MAX_LENGTH_TEXT_STRING,
                Crispin.getSurfaceWidth());
        centeredText = new Text(AILERON_REGULAR, CENTERED_TEXT_STRING,false,true,
                Crispin.getSurfaceWidth());
        wrappedText = new Text(AILERON_REGULAR, WRAPPED_TEXT_STRING,true,false,
                Crispin.getSurfaceWidth());
        wrappedCenteredText = new Text(AILERON_REGULAR, WRAPPED_CENTERED_TEXT_STRING,true,
                true, Crispin.getSurfaceWidth());
        wiggleText = new Text(AILERON_REGULAR, WIGGLE_TEXT_STRING,true,true,
                Crispin.getSurfaceWidth());

        // Enable wiggle on the wiggle text object
        wiggleText.enableWiggle(PADDING_PIXELS * WIGGLE_MULTIPLIER,
                Text.WiggleSpeed_E.VERY_FAST);

        standardText.setColour(Colour.RED);
        standardTextMaxLength.setColour(Colour.LIGHT_GREY);
        centeredText.setColour(Colour.CYAN);
        wrappedText.setColour(Colour.ORANGE);
        wrappedCenteredText.setColour(Colour.MAGENTA);
        wiggleText.setColour(Colour.GREEN);

        final float HALF_SURFACE_WIDTH = Crispin.getSurfaceWidth() / 2.0f;

        // Position the text objects
        standardText.setPosition(0.0f,Crispin.getSurfaceHeight() - standardText.getHeight());
        standardTextMaxLength.setPosition(0.0f,standardText.getPosition().y -
                standardTextMaxLength.getHeight() - PADDING_PIXELS);
        centeredText.setPosition(HALF_SURFACE_WIDTH - (centeredText.getWidth() /
                        TEXT_WIDTH_DIVIDE),standardTextMaxLength.getPosition().y -
                centeredText.getHeight() - PADDING_PIXELS);
        wrappedText.setPosition(0.0f,centeredText.getPosition().y - wrappedText.getHeight() -
                PADDING_PIXELS);
        wrappedCenteredText.setPosition(HALF_SURFACE_WIDTH - (wrappedCenteredText.getWidth() /
                        TEXT_WIDTH_DIVIDE),
                wrappedText.getPosition().y - wrappedCenteredText.getHeight() - PADDING_PIXELS);
        wiggleText.setPosition(HALF_SURFACE_WIDTH - (wiggleText.getWidth() / TEXT_WIDTH_DIVIDE),
                wrappedCenteredText.getPosition().y - wiggleText.getHeight() -
                        (PADDING_PIXELS * WIGGLE_MULTIPLIER));

        linearLayout = new LinearLayout(new Point2D(0.0f, 0.0f), Crispin.getSurfaceWidth(), Crispin.getSurfaceHeight(), false);

        for(int i = 0; i < 10; i++)
        {
            linearLayout.add(new Text(AILERON_REGULAR, "Test", false, false, 0.0f));
        }

        demo = new Text(AILERON_REGULAR, "Demo Text");
        demo.setPosition(0.0f, 0.0f);

        System.out.println("Standard Text Height: " + standardText.getHeight());
        System.out.println("Standard Text Max: " + standardTextMaxLength.getHeight());
    }

    /**
     * Update function that overrides the Scene base class
     *
     * @see     Scene
     * @since   1.0
     */
    @Override
    public void update(float deltaTime)
    {

    }

    /**
     * Render function that overrides the Scene base class
     *
     * @see     Scene
     * @since   1.0
     */
    @Override
    public void render()
    {
        standardText.draw(camera2D);
        standardTextMaxLength.draw(camera2D);
        centeredText.draw(camera2D);
        wrappedText.draw(camera2D);
        wrappedCenteredText.draw(camera2D);
        wiggleText.draw(camera2D);

      //  linearLayout.draw(camera2D);

       // demo.draw(camera2D);
    }
}
