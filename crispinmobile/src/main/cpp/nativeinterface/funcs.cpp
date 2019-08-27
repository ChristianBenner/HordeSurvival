#include <jni.h>
#include <string>
#include <freetype/tttags.h>
#include <fstream>
#include <zlib.h>
#include <sstream>
#include <android/log.h>

static const char* TAG = "CrispinNI";

FT_Library freetype;
FT_Face face;
bool initialised = false;

void log(int level, const char* message)
{
    __android_log_write(level, TAG, message);
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_loadCharacter(JNIEnv *env,
        jobject instance,
        jbyteArray fontBytes_,
        jbyte thechar_,
        jint size)
{
    // If FreeType isn't initialised yet then initialise it
    if(!initialised)
    {
        if(FT_Init_FreeType(&freetype) == 0)
        {
            // Successfully initialised FreeType
            log(ANDROID_LOG_DEBUG, "Successfully Initialised FreeType");
            initialised = true;
        }
        else
        {
            // Failed to initialise FreeType
            log(ANDROID_LOG_ERROR, "Failed to initialise FreeType");
        }
    }

    if(initialised)
    {
        // determine the needed length and allocate a buffer for it
        jsize num_bytes = env->GetArrayLength(fontBytes_);
        char *buffer = static_cast<char *>(malloc(num_bytes + 1));

        char thechar = thechar_;

        if (!buffer) {
            // handle allocation failure ...
        }

        // obtain the array elements
        jbyte* elements = env->GetByteArrayElements(fontBytes_, NULL);

        if (!elements) {
            // handle JNI error ..
        }

        // copy the array elements into the buffer, and append a terminator
        memcpy(buffer, elements, num_bytes);
        env->ReleaseByteArrayElements(fontBytes_, elements, 0);
        buffer[num_bytes] = 0;

        // convert jbytearray to bytes[]
        if(FT_New_Memory_Face(freetype, (FT_Byte*)buffer, num_bytes, 0, &face) == 0)
        {
            FT_Set_Pixel_Sizes(face, 0, size);

            if(FT_Load_Char(face, thechar, FT_LOAD_RENDER) == 0)
            {
                // todo: Create a structure that is stored every time a glyph is created/loaded
                //   more functions can allow the user to retrieve information on the glyph such as
                //   its width, rows and format.
                //   (in Android/Java), save the buffer to a .raw file and then open it in a image
                //   viewer such as photshop, specify the width and height to see the image.
                //   or just use the image in OpenGL this time trying with the real width and height
                //   of the image (might have messed up before because we where giving random width
                //   and height (YEAH)
                jbyteArray array = env->NewByteArray(face->glyph->bitmap.width * face->glyph->bitmap.rows * sizeof(unsigned char));
                env->SetByteArrayRegion(array,
                                        0,
                                        face->glyph->bitmap.width * face->glyph->bitmap.rows * sizeof(unsigned char),
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
        env->ReleaseByteArrayElements(fontBytes_, elements, JNI_ABORT);

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

extern "C" JNIEXPORT jboolean JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_initFreeType(
        JNIEnv* env,
        jobject)
{
    return FT_Init_FreeType(&freetype) == 0;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_getFaceBearingX(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap_left;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_getFaceBearingY(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap_top;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_getFaceAdvance(JNIEnv* env, jobject instance)
{
    return face->glyph->advance.x;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_getFaceWidth(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap.width;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_getFaceHeight(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap.rows;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_freeFace(JNIEnv* env, jobject instance)
{
    FT_Done_Face(face);
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_games_crispin_crispinmobile_Native_FreeTypeMethods_loadGlyph(JNIEnv *env, jobject instance,
                                                          jbyteArray fontBytes_, jbyte thechar_, jint size) {
//    log(ANDROID_LOG_DEBUG, "Loading Glyph");
//
//    // Copy the byte array data to a usable format
//    jboolean isCopy;
//    jbyte* fontBytes = env->GetByteArrayElements(fontBytes_, &isCopy);
//    jsize numFontBytes = env->GetArrayLength(fontBytes_);
//
//    // Create a new memory face using the font bytes
//    int error = FT_New_Memory_Face(freetype,
//            (const FT_Byte*)fontBytes,
//            numFontBytes,
//            0,
//            &face);
//
//    // If an error occured
//    if(error == FT_Err_Unknown_File_Format)
//    {
//        log(ANDROID_LOG_ERROR, "Failed to load the font because the font format is unsupported");
//
//        // Release the face data
//        FT_Done_Face(face);
//
//        // Release the byte array elements from memory
//        env->ReleaseByteArrayElements(fontBytes_, fontBytes, 0);
//
//        // Return error
//        return 0;
//    }
//    else if(error)
//    {
//        log(ANDROID_LOG_ERROR, "Unknown Error. File could not be opened or read.");
//
//        // Release the face data
//        FT_Done_Face(face);
//
//        // Release the byte array elements from memory
//        env->ReleaseByteArrayElements(fontBytes_, fontBytes, 0);
//
//        // Return error
//        return 0;
//    }
//
//    if(error == FT_Err_Ok)
//    {
//        // No error has occured, we can continue
//        log(ANDROID_LOG_DEBUG, "Created new memory face");
//    }
//
//    if(face->face_flags & FT_FACE_FLAG_SCALABLE)
//    {
//        log(ANDROID_LOG_DEBUG, "The font is fully scalable");
//    }
//
//    for(int i = 0; i < face->num_fixed_sizes; i++)
//    {
//        std::stringstream stringStream;
//        stringStream << "The font has a fixed size of: ";
//        stringStream << face->available_sizes[i].size;
//        stringStream << "px";
//        log(ANDROID_LOG_DEBUG, stringStream.str().c_str());
//    }
//
//    FT_Set_Pixel_Sizes(face, 0, size);
//
//    if(FT_Load_Char(face, thechar_, FT_LOAD_RENDER) == 0)
//    {
//        int sizeOfRet = face->glyph->bitmap.width * face->glyph->bitmap.rows * sizeof(unsigned char);
//
//        jbyteArray retArr = env->NewByteArray(sizeOfRet);
//        env->SetByteArrayRegion(retArr,
//                                0,
//                                sizeOfRet,
//                                reinterpret_cast<jbyte *>(face->glyph->bitmap.buffer));
//
//        // Release the face data
//        FT_Done_Face(face);
//
//        // Release the byte array elements from memory
//        env->ReleaseByteArrayElements(fontBytes_, fontBytes, 0);
//
//        log(ANDROID_LOG_DEBUG, "LOADED CHAR");
//        return retArr;
//    }
//
//    // Release the face data
//    FT_Done_Face(face);
//
//    // Release the byte array elements from memory
//    env->ReleaseByteArrayElements(fontBytes_, fontBytes, 0);
//
//    return 0;
//
//   // if(FT_New_Memory_Face(freetype, fontBytes, numFontBytes, 0, &face))
//
//
//
//
//
//
//
//
//
//    // Access the face data to see the available sizes
//




    // determine the needed length and allocate a buffer for it
    jsize num_bytes = env->GetArrayLength(fontBytes_);
    char *buffer = static_cast<char *>(malloc(num_bytes + 1));

    char thechar = thechar_;

    if (!buffer) {
        // handle allocation failure ...
    }

// obtain the array elements
    jbyte* elements = env->GetByteArrayElements(fontBytes_, NULL);

    if (!elements) {
        // handle JNI error ..
    }

// copy the array elements into the buffer, and append a terminator
    memcpy(buffer, elements, num_bytes);
    env->ReleaseByteArrayElements(fontBytes_, elements, 0);
    buffer[num_bytes] = 0;

    // convert jbytearray to bytes[]
    if(FT_New_Memory_Face(freetype, (FT_Byte*)buffer, num_bytes, 0, &face) == 0)
    {
        FT_Set_Pixel_Sizes(face, 0, size);

        if(FT_Load_Char(face, thechar, FT_LOAD_RENDER) == 0)
        {
           // todo: Create a structure that is stored every time a glyph is created/loaded
           //   more functions can allow the user to retrieve information on the glyph such as
           //   its width, rows and format.
           //   (in Android/Java), save the buffer to a .raw file and then open it in a image
           //   viewer such as photshop, specify the width and height to see the image.
           //   or just use the image in OpenGL this time trying with the real width and height
           //   of the image (might have messed up before because we where giving random width
           //   and height (YEAH)
            jbyteArray array = env->NewByteArray(face->glyph->bitmap.width * face->glyph->bitmap.rows * sizeof(unsigned char));
            env->SetByteArrayRegion(array,
                    0,
                                    face->glyph->bitmap.width * face->glyph->bitmap.rows * sizeof(unsigned char),
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
    env->ReleaseByteArrayElements(fontBytes_, elements, JNI_ABORT);

    char* err = "err";
    jbyteArray array = env->NewByteArray(4);
    env->SetByteArrayRegion(array,
                            0,
                            num_bytes,
                            reinterpret_cast<jbyte*>(err));

    log(ANDROID_LOG_DEBUG, "Failed to load Glyph");
    return array;
}