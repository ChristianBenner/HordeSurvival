cmake_minimum_required(VERSION 3.4.1)

include_directories("${CMAKE_SOURCE_DIR}/freetype-2.9/include")

add_library(
	crispinni
	SHARED
	funcs.cpp)

find_library(
	log-lib
    	log)

target_link_libraries(
	crispinni
	freetype
	${log-lib})