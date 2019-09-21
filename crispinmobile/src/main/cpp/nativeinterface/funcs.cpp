#include <jni.h>
#include <string>
#include <freetype/tttags.h>
#include <fstream>
#include <zlib.h>
#include <sstream>
#include <android/log.h>

// Tag used for logging
static const char* TAG = "CrispinNI";

// FreeType library
FT_Library freetype;

// FreeType face that holds the character face data
FT_Face face;

// Whether or not the FreeType library has been initialised
bool freeTypeInitialised = false;

/**
 * Log text to the console
 *
 * @param level     The level of logging (e.g. debug, info, error)
 * @param message   The message to log
 * @author Christian Benner
 * @since 1.0
 */
void log(int level, const char* message)
{
    __android_log_write(level,
            TAG,
            message);
}

/**
 * Load a specified character from a font
 *
 * @param env           The address of the JNI environment (supplied by JNI)
 * @param instance       Application instance
 * @param fontBytes_    The font to load the character from. This is the font file as an array of
 *                      bytes
 * @param theChar_      The ASCII character to load
 * @param fontSize_     The size of the font to load
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_loadCharacter(JNIEnv *env,
        jobject instance,
        jbyteArray fontBytes_,
        jbyte theChar_,
        jint fontSize_)
{
    // If FreeType isn't freeTypeInitialised yet then initialise it
    if(!freeTypeInitialised)
    {
        // Initialise FreeType library
        if(FT_Init_FreeType(&freetype) == 0)
        {
            // Successfully freeTypeInitialised FreeType
            log(ANDROID_LOG_DEBUG, "Successfully Initialised FreeType");
            freeTypeInitialised = true;
        }
        else
        {
            // Failed to initialise FreeType
            log(ANDROID_LOG_ERROR, "Failed to initialise FreeType");
        }
    }

    // Check if FreeType is initialised before attempting to load a character
    if(freeTypeInitialised)
    {
        // Determine the needed length and allocate a buffer for it
        jsize num_bytes = env->GetArrayLength(fontBytes_);
        char *buffer = static_cast<char *>(malloc(num_bytes + 1));

        char thechar = theChar_;

        // Check if the buffer pointer is 0 (null)
        if (!buffer)
        {
            // handle allocation failure ...
        }

        // obtain the array elements
        jbyte* elements = env->GetByteArrayElements(fontBytes_, NULL);

        // Check if failed to allocated byte array
        if (!elements)
        {
            // handle JNI error ..
        }

        // Copy the array elements into the buffer, and append a terminator
        memcpy(buffer, elements, num_bytes);
        env->ReleaseByteArrayElements(fontBytes_,
                elements,
                0);
        buffer[num_bytes] = 0;

        // Convert jbytearray to bytes[]
        if(FT_New_Memory_Face(freetype,
                (FT_Byte*)buffer,
                num_bytes,
                0,
                &face) == 0)
        {
            FT_Set_Pixel_Sizes(face,
                    0,
                    fontSize_);

            // Load the specified character into the face
            if(FT_Load_Char(face,
                    thechar,
                    FT_LOAD_RENDER) == 0)
            {
                // todo: Create a structure that is stored every time a glyph is created/loaded
                //   more functions can allow the user to retrieve information on the glyph such as
                //   its width, rows and format.
                //   (in Android/Java), save the buffer to a .raw file and then open it in a image
                //   viewer such as photshop, specify the width and height to see the image.
                //   or just use the image in OpenGL this time trying with the real width and height
                //   of the image (might have messed up before because we where giving random width
                //   and height (YEAH)
                jbyteArray array = env->NewByteArray(face->glyph->bitmap.width *
                        face->glyph->bitmap.rows *
                        sizeof(unsigned char));
                env->SetByteArrayRegion(array,
                                        0,
                                        face->glyph->bitmap.width *
                                        face->glyph->bitmap.rows *
                                        sizeof(unsigned char),
                                        reinterpret_cast<jbyte*>(face->glyph->bitmap.buffer));
                //  FT_Done_Face(face);
                log(ANDROID_LOG_DEBUG, "Loaded Glyph");
                return array;
            }
            else
            {
                // error occurred: failed to load glyph
                //returnString = "failed to load glyph";
            }
        }
        else
        {
            // error occurred: failed to load font
            //returnString = "failed to load font";
        }

        // Do not forget to release the element array provided by JNI:
        env->ReleaseByteArrayElements(fontBytes_,
                elements,
                JNI_ABORT);

        char* err = "err";
        jbyteArray array = env->NewByteArray(4);
        env->SetByteArrayRegion(array,
                                0,
                                num_bytes,
                                reinterpret_cast<jbyte*>(err));

        log(ANDROID_LOG_DEBUG, "Failed to load Glyph");
        return array;
    }
}

/**
 * Retrieve the bearing x from the currently loaded FreeType face
 *
 * @param env       The address of the JNI environment (supplied by JNI)
 * @param instane   Application instance
 * @return          The bearing x of the currently loaded face
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_getFaceBearingX(JNIEnv* env,
        jobject instance)
{
    return face->glyph->bitmap_left;
}

/**
 * Retrieve the bearing y from the currently loaded FreeType face
 *
 * @param env       The address of the JNI environment (supplied by JNI)
 * @param instane   Application instance
 * @return          The bearing y of the currently loaded face
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_getFaceBearingY(JNIEnv* env,
        jobject instance)
{
    return face->glyph->bitmap_top;
}

/**
 * Retrieve the advance x from the currently loaded FreeType face
 *
 * @param env       The address of the JNI environment (supplied by JNI)
 * @param instane   Application instance
 * @return          The advance x of the currently loaded face
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_getFaceAdvance(JNIEnv* env,
        jobject instance)
{
    return face->glyph->advance.x;
}

/**
 * Retrieve the width from the currently loaded FreeType face
 *
 * @param env       The address of the JNI environment (supplied by JNI)
 * @param instane   Application instance
 * @return          The width of the currently loaded face
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_getFaceWidth(JNIEnv* env,
        jobject instance)
{
    return face->glyph->bitmap.width;
}

/**
 * Retrieve the height from the currently loaded FreeType face
 *
 * @param env       The address of the JNI environment (supplied by JNI)
 * @param instane   Application instance
 * @return          The height of the currently loaded face
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_getFaceHeight(JNIEnv* env,
        jobject instance)
{
    return face->glyph->bitmap.rows;
}

/**
 * Free the currently loaded face
 *
 * @param env           The address of the JNI environment (supplied by JNI)
 * @param instane       Application instance
 * @author Christian Benner
 * @since 1.0
 */
extern "C" JNIEXPORT void JNICALL
Java_com_games_crispin_crispinmobile_Native_CrispinNativeInterface_freeFace(JNIEnv* env,
        jobject instance)
{
    FT_Done_Face(face);
}