package com.games.crispin.hordesurvival;

import com.games.crispin.crispinmobile.Geometry.Point2D;
import com.games.crispin.crispinmobile.Geometry.Scale2D;
import com.games.crispin.crispinmobile.Rendering.Models.FontSquare;
import com.games.crispin.crispinmobile.Rendering.Models.Square;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Button;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Image;
import com.games.crispin.crispinmobile.Rendering.UserInterface.LinearLayout;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Plane;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera2D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Camera3D;
import com.games.crispin.crispinmobile.Rendering.Data.Colour;
import com.games.crispin.crispinmobile.Crispin;
import com.games.crispin.crispinmobile.Rendering.Models.Cube;
import com.games.crispin.crispinmobile.Geometry.Point3D;
import com.games.crispin.crispinmobile.Rendering.Utilities.Font;
import com.games.crispin.crispinmobile.Rendering.Utilities.Material;
import com.games.crispin.crispinmobile.Rendering.Utilities.RenderObject;
import com.games.crispin.crispinmobile.Rendering.UserInterface.Text;
import com.games.crispin.crispinmobile.Rendering.Utilities.Texture;
import com.games.crispin.crispinmobile.Utilities.OBJThreadTest;
import com.games.crispin.crispinmobile.Utilities.Scene;

import java.util.ArrayList;

public class TestScene extends Scene {
    static Scene.Constructor TEST_SCENE_CONSTRUCTION = () -> new TestScene();

    private Cube cubeTwo;
    private Cube cubeThree;

    private Camera3D camera;
    private Camera2D camera2D;
    private float angle = 0.0f;

  //  private Text text;
    private Material brickMaterial;

//    private Text fpsText;
    private Material mRed;
    private Material mGreen;
    private Material mBlue;

  //  private OBJThreadTest dinomodelThread;
    private RenderObject dinomodel;
    private OBJThreadTest carmodelThread;
    private RenderObject carmodel;
    private OBJThreadTest personmodelThread;
    private RenderObject personmodel;

    private Square square;
    private Square texSquare;
    private Image imageUI;
    private Plane planeUI;

    private LinearLayout linearLayout;

    int getNumChars(String word)
    {
        int lastIndex = 0;
        int count = 0;

        while (lastIndex != -1) {

            lastIndex = word.indexOf("\n", lastIndex);

            if (lastIndex != -1) {
                count++;
                lastIndex += "\n".length();
            }
        }

        return word.length() - count;
    }

    public TestScene()
    {
        /*
        SpaceLeft := LineWidth
        for each Word in Text
            if (Width(Word) + SpaceWidth) > SpaceLeft
                insert line break before Word in Text
                SpaceLeft := LineWidth - Width(Word)
            else
                SpaceLeft := SpaceLeft - (Width(Word) + SpaceWidth)
         */


        class Line extends ArrayList<String>
        {

        }

        final float LINE_WIDTH = 20f;
        final float SPACE_WIDTH = 1f;
        float spaceLeft = LINE_WIDTH;

        ArrayList<String> words = new ArrayList<>();
        words.add("test");
        words.add("the");
        words.add("line");
        words.add("wrap");
        words.add("algorithm");
        for(int i = 0; i < words.size(); i++)
        {
            if(getNumChars(words.get(i)) + SPACE_WIDTH > spaceLeft)
            {
                System.out.println();
                spaceLeft = LINE_WIDTH - getNumChars(words.get(i));
            }
            else
            {
                spaceLeft = spaceLeft - (getNumChars(words.get(i)) + SPACE_WIDTH);
            }

            System.out.print(words.get(i) + " ");
        }
        System.out.println();

        String textString = "this is a sentence that will be cut up into multiple words";
        String[] words2 = textString.split("\\s+");
        for(int i = 0; i < words2.length; i++)
        {
            System.out.println("WORD: " + words2[i] + ": Length[" + words2[i].length() + "]");
        }


        // Set the background colour to yellow
        Crispin.setBackgroundColour(Colour.LIGHT_GREY);

        // Create the camera
        camera = new Camera3D();
        camera.setPosition(new Point3D(0.0f, 0.0f, 7.0f));

        camera2D = new Camera2D();

      //  Material brickMaterial = new Material(new Texture(R.drawable.brick), Colour.YELLOW);

        brickMaterial = new Material(new Texture(R.drawable.brick), new Scale2D(0.25f, 0.25f));
       // brickMaterial.ignoreData(Material.IGNORE_TEXEL_DATA_FLAG);

//        dinomodelThread = new OBJThreadTest(R.raw.dinomodel);
   //     new Thread(dinomodelThread).start();
//        personmodelThread = new OBJThreadTest(R.raw.personmodel);
//        new Thread(personmodelThread).start();
//        carmodelThread = new OBJThreadTest(R.raw.shotty);
//        new Thread(carmodelThread).start();

        mRed = new Material();
        mRed.setColour(Colour.RED);

        //mGreen = new Material(new Texture(R.drawable.shotgun));
       // mGreen.setColour(Colour.GREEN);

        mBlue = new Material();
        mBlue.setColour(Colour.BLUE);

  //3      renderObject.setMaterial(brickMaterial);

        cubeTwo = new Cube(brickMaterial);
        cubeTwo.setPosition(new Point3D(-0.1f, 3.2f, -2.0f));

        cubeThree = new Cube(brickMaterial, true, true);
        cubeThree.setPosition(new Point3D(2.0f, 3.0f, 0.0f));

       // Font font = new Font(R.raw.opensans, 64);

/*        text = new Text(font,
                "Testing1 Testing2 Testing3 Testing4 Testing5 Testing6 Testing7",
                true,
                false,
                Crispin.getSurfaceWidth() / 2.0f);

        //text.enableWiggle(-text.getHeight() / 2.0f, Text.WiggleSpeed_E.VERY_FAST);

        // Position the text to the top right corner
        text.setPosition((Crispin.getSurfaceWidth() / 2.0f),
                Crispin.getSurfaceHeight()/2.0f);

        Font fontTwo = new Font(R.raw.opensans, 64);
        fpsText = new Text(fontTwo,
                "FPS: X",
                true,
                false,
                Crispin.getSurfaceWidth());
        fpsText.setPosition(5.0f, 5.0f);*/

   /* public Square(Material material, boolean renderTexels)
    public Square(boolean renderTexels)
    public Square(Material material)
    public Square()*/

    //    square = new Square(brickMaterial, true);
     //   square = new Square(brickMaterial);
        // square = new Square(true);

        square = new Square();
        square.setMaterial(brickMaterial);

        Material man = new Material(new Texture(R.drawable.man));
        texSquare = new Square();
        texSquare.setMaterial(man);
        texSquare.setPosition(0.0f, 0.0f);
        texSquare.setScale(100.0f, 100.0f);
        // Create the render object square
        squareTest = new FontSquare(man,
                new Point3D(0.0f, 100.0f, 0.0f), new Point2D(0.0f, 0.0f));
        squareTest.setScale(100.0f, 100.0f);


        linearLayout = new LinearLayout(new Point2D(0.0f, 50.0f), Crispin.getSurfaceWidth(), 0.0f, false);
        imageUI = new Image(R.drawable.man);
        planeUI = new Plane(new Scale2D(100.0f, 100.0f));
        planeUI.setColour(Colour.BLUE);
       // linearLayout.add(imageUI);
       // linearLayout.add(planeUI);

        Text t = new Text(new Font(R.raw.opensans, 64), "Testing Text");
        t.showBounds();
        linearLayout.add(t);
        for(int i = 0; i < 30; i++)
        {
            linearLayout.add(new Image(R.drawable.man));
        }

        Colour colour = Colour.ORANGE;
        linearLayout.setColour(colour);
       // linearLayout.setOpacity(0.5f);

        linearLayout.add(new Button(new Font(R.raw.opensans, 64), "iiiiiii iiiiiii"));
    }
    FontSquare squareTest;

    boolean onetimeDino = false;
    float modelFadeInDino = 0.0f;
    boolean startModelFadeInDino = false;

    boolean onetimeCar = false;
    float modelFadeInCar = 0.0f;
    boolean startModelFadeInCar = false;

    boolean onetimePerson = false;
    float modelFadeInPerson = 0.0f;
    boolean startModelFadeInPerson = false;

    @Override
    public void update(float deltaTime)
    {
        angle += 1.0f;
        cubeTwo.setRotation(0.0f, angle, angle);
        cubeThree.setRotation( angle, 0.0f, angle);
        square.setRotation(angle, 0.0f, angle);

      //  text.setText("Rotations: " + (int)(angle / 360));
     //   text.setPosition(5.0f, Crispin.getSurfaceHeight() - text.getHeight() - 5f);

//        if(dinomodelThread.isComplete() && !onetimeDino)
//        {
//          //  renderObject = OBJModelLoader.readObjFile(R.raw.dragon);
//            dinomodel = dinomodelThread.getRenderObject();
//            dinomodel.setPosition(-2.0f, 2.0f, 0.0f);
//            dinomodel.setScale(0.005f, 0.005f, 0.005f);
//            dinomodel.setMaterial(mRed);
//            startModelFadeInDino = true;
//            onetimeDino = true;
//        }
////
//        if(dinomodelThread.isComplete())
//        {
//            dinomodel.setRotation(15.0f, angle, 0.0f);
//
//            if(startModelFadeInDino && modelFadeInDino != 1.0f)
//            {
//                modelFadeInDino += 0.03f;
//                if(modelFadeInDino >= 0.5f)
//                {
//                    modelFadeInDino = 0.5f;
//                }
//                dinomodel.getMaterial().getColour().setAlpha(modelFadeInDino);
//            }
//        }
//
//        if(carmodelThread.isComplete() && !onetimeCar)
//        {
//            //  renderObject = OBJModelLoader.readObjFile(R.raw.dragon);
//            carmodel = carmodelThread.getRenderObject();
//            carmodel.setPosition(0.0f, -2.5f, 0.0f);
//          //  carmodel.setScale(0.025f, 0.025f, 0.025f);
//            carmodel.setMaterial(mGreen);
//            startModelFadeInCar = true;
//            onetimeCar = true;
//        }
//
//        if(carmodelThread.isComplete())
//        {
//            carmodel.setRotation(15.0f, angle, 0.0f);
//
//            if(startModelFadeInCar && modelFadeInCar != 1.0f)
//            {
//                modelFadeInCar += 0.03f;
//                if(modelFadeInCar >= 1f)
//                {
//                    modelFadeInCar = 1f;
//                }
//                carmodel.getMaterial().getColour().setAlpha(modelFadeInCar);
//            }
//        }
//
//        if(personmodelThread.isComplete() && !onetimePerson)
//        {
//            //  renderObject = OBJModelLoader.readObjFile(R.raw.dragon);
//            personmodel = personmodelThread.getRenderObject();
//            personmodel.setPosition(2.0f, -2.5f, 0.0f);
//            personmodel.setScale(0.01f, 0.01f, 0.01f);
//            personmodel.setMaterial(mBlue);
//            startModelFadeInPerson = true;
//            onetimePerson = true;
//        }
//
//        if(personmodelThread.isComplete())
//        {
//            personmodel.setRotation(15.0f, angle, 0.0f);
//
//            if(startModelFadeInPerson && modelFadeInPerson != 1.0f)
//            {
//                modelFadeInPerson += 0.03f;
//                if(modelFadeInPerson >= 0.5f)
//                {
//                    modelFadeInPerson = 0.5f;
//                }
//                personmodel.getMaterial().getColour().setAlpha(modelFadeInPerson);
//            }
//        }
    }

    float time = 0.0f;
    float colourR = 0.0f;

    long startF = System.currentTimeMillis();
    int frames = 0;
    @Override
    public void render()
    {
        time += 0.1f;
        colourR = ((float)Math.sin(time) + 1f) / 2f;
        brickMaterial.setColour(new Colour(colourR, 1.0f, 1.0f));

        // Draw the cube
        cubeThree.render(camera);
        square.render(camera);
     //   texSquare.render(camera2D);
       // squareTest.render(camera2D);
       // imageUI.draw(camera2D);
        //planeUI.draw(camera2D);
        //cubeTwo.render(camera);

        linearLayout.draw(camera2D);

        if(dinomodel != null)
        {
            dinomodel.render(camera);
        }

        if(carmodel != null)
        {
            carmodel.render(camera);
        }

        if(personmodel != null)
        {
            personmodel.render(camera);
        }

    //    text.draw(camera2D);
       // fpsText.render(camera2D);

        frames++;

        if(System.currentTimeMillis() - startF >= 1000)
        {
          //  fpsText.setText("FPS: " + frames);
            startF = System.currentTimeMillis();
            frames = 0;
        }
    }
}
