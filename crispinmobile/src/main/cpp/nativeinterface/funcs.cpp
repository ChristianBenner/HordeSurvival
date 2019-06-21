//#include FT_FREETYPE_H
//#include "../psaux/pstypes.h"
//#include "../../include/freetype/tttags.h"
//#include "../../include/freetype/freetype.h"

//FT_Library freetype;
//static bool init()
//{
//    return
//    if(FT_Init_FreeType(&freetype))
//    {
//
//    }
//}

#include <jni.h>
#include <string>
#include <freetype/tttags.h>
#include <fstream>
#include <zlib.h>
#include <sstream>

FT_Library freetype;

extern "C" JNIEXPORT jboolean JNICALL
Java_com_games_crispin_crispinmobile_Crispin_initFreeType(
        JNIEnv* env,
        jobject)
{
    return FT_Init_FreeType(&freetype) == 0;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_nativelibrarytest_MainActivity_glyphBuffer(
        JNIEnv* env,
        jobject,
        jstring path)
{
    const char* nativeString = env->GetStringUTFChars(path, 0);
    std::string returnString;

    std::ofstream file("crispininfo.txt");
    file << "testing\n";
    file.close();

    std::string line;
    std::ifstream read("crispininfo.txt");
    if(read.is_open())
    {
        getline(read, line);
        read.close();
    }

    // use
    FT_Face face;

    if(FT_New_Face(freetype, nativeString, 0, &face) == 0)
    {
        env->ReleaseStringUTFChars(path, nativeString);

        FT_Set_Pixel_Sizes(face, 0, 48);

        if(FT_Load_Char(face, 'X', FT_LOAD_RENDER) == 0)
        {
            return env->NewStringUTF((char*)face->glyph->bitmap.buffer);
        }
        else
        {
            // error occurred: failed to load glyph
            returnString = "failed to load glyph";
    }
    }
    else
    {
        // error occurred: failed to load font
        returnString = "failed to load font: " + std::string(nativeString) + ". Test: " + line;
    }

    env->ReleaseStringUTFChars(path, nativeString);

    return env->NewStringUTF(returnString.c_str());
}

FT_Face face;

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Crispin_getFaceWidth(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap.width;
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_games_crispin_crispinmobile_Crispin_getFaceHeight(JNIEnv* env, jobject instance)
{
    return face->glyph->bitmap.rows;
}

extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_games_crispin_crispinmobile_Crispin_loadGlyph(JNIEnv *env, jobject instance,
                                                          jbyteArray bytes_, jbyte thechar_) {
    // determine the needed length and allocate a buffer for it
    jsize num_bytes = env->GetArrayLength(bytes_);
    char *buffer = static_cast<char *>(malloc(num_bytes + 1));

    char thechar = thechar_;

    if (!buffer) {
        // handle allocation failure ...
    }

// obtain the array elements
    jbyte* elements = env->GetByteArrayElements(bytes_, NULL);

    if (!elements) {
        // handle JNI error ..
    }

// copy the array elements into the buffer, and append a terminator
    memcpy(buffer, elements, num_bytes);
    buffer[num_bytes] = 0;

    // convert jbytearray to bytes[]
    if(FT_New_Memory_Face(freetype, (FT_Byte*)buffer, num_bytes, 0, &face) == 0)
    {
        FT_Set_Pixel_Sizes(face, 0, 96);

        if(FT_Load_Char(face, thechar, FT_LOAD_RENDER) == 0)
        {
           // env->ReleaseByteArrayElements(bytes_, bytes_, 0);

           // todo: Create a structure that is stored every time a glyph is created/loaded
           //   more functions can allow the user to retrieve information on the glyph such as
           //   its width, rows and format.
           //   (in Android/Java), save the buffer to a .raw file and then open it in a image
           //   viewer such as photshop, specify the width and height to see the image.
           //   or just use the image in OpenGL this time trying with the real width and height
           //   of the image (might have messed up before because we where giving random width
           //   and height (YEAH)
            jbyteArray array = env->NewByteArray(num_bytes);
            env->SetByteArrayRegion(array,
                    0,
                    num_bytes,
                    reinterpret_cast<jbyte*>(face->glyph->bitmap.buffer));


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
    env->ReleaseByteArrayElements(bytes_, elements, JNI_ABORT);

    char* err = "err";
    jbyteArray array = env->NewByteArray(4);
    env->SetByteArrayRegion(array,
                            0,
                            num_bytes,
                            reinterpret_cast<jbyte*>(err));

    return array;

//    int len = env->GetArrayLength(bytes_);
//    char* buf = new char[len];
//    env->GetByteArrayRegion(bytes_, 0, len, reinterpret_cast<jbyte*>(buf));
//
//    std::stringstream ss;
//    for(int i = 0; i < len; i++)
//    {
//        ss << buf[i];
//    }
//
//    return env->NewStringUTF(ss.str().c_str());


//    // TODO
//
//    std::string returnString;
//
//    std::ofstream file("crispininfo.txt");
//    file << "testing\n";
//    file.close();
//
//    std::string line;
//    std::ifstream read("crispininfo.txt");
//    if(read.is_open())
//    {
//        getline(read, line);
//        read.close();
//    }
//
//    // use
//    FT_Face face;
//
//    // convert jbytearray to bytes[]
//    if(FT_New_Memory_Face(freetype, (FT_Byte*)buf, len, 0, &face) == 0)
//    {
//        FT_Set_Pixel_Sizes(face, 0, 48);
//
//        if(FT_Load_Char(face, 'a', FT_LOAD_RENDER) == 0)
//        {
//           // env->ReleaseByteArrayElements(bytes_, bytes_, 0);
//
//            std::stringstream ss;
//            ss << "W[" << face->glyph->bitmap.width << "], R[" << face->glyph->bitmap.rows << "], Data: ";
//            ss << face->glyph->bitmap.buffer;
//            ss << ", S[" << sizeof(face->glyph->bitmap_left) << ']';
//
//            //(char*)face->glyph->bitmap.buffer
//            return env->NewStringUTF(buf);
//        }
//        else
//        {
//            // error occurred: failed to load glyph
//            returnString = "failed to load glyph";
//        }
//    }
//    else
//    {
//        // error occurred: failed to load font
//        returnString = "failed to load font";
//    }
//
//  //  env->ReleaseByteArrayElements(bytes_, bytes, 0);
//
//    return env->NewStringUTF(returnString.c_str());
}