# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.16

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/doublets_tests_alt.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/doublets_tests_alt.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/doublets_tests_alt.dir/flags.make

CMakeFiles/doublets_tests_alt.dir/doublets.c.o: CMakeFiles/doublets_tests_alt.dir/flags.make
CMakeFiles/doublets_tests_alt.dir/doublets.c.o: ../doublets.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/doublets_tests_alt.dir/doublets.c.o"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/doublets_tests_alt.dir/doublets.c.o   -c "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets.c"

CMakeFiles/doublets_tests_alt.dir/doublets.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/doublets_tests_alt.dir/doublets.c.i"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets.c" > CMakeFiles/doublets_tests_alt.dir/doublets.c.i

CMakeFiles/doublets_tests_alt.dir/doublets.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/doublets_tests_alt.dir/doublets.c.s"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets.c" -o CMakeFiles/doublets_tests_alt.dir/doublets.c.s

CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o: CMakeFiles/doublets_tests_alt.dir/flags.make
CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o: ../doublets_tests.c
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -o CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o   -c "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets_tests.c"

CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing C source to CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.i"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets_tests.c" > CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.i

CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling C source to assembly CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.s"
	/usr/bin/gcc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/doublets_tests.c" -o CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.s

# Object files for target doublets_tests_alt
doublets_tests_alt_OBJECTS = \
"CMakeFiles/doublets_tests_alt.dir/doublets.c.o" \
"CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o"

# External object files for target doublets_tests_alt
doublets_tests_alt_EXTERNAL_OBJECTS = \
"/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/lib/dict_alt.o"

doublets_tests_alt: CMakeFiles/doublets_tests_alt.dir/doublets.c.o
doublets_tests_alt: CMakeFiles/doublets_tests_alt.dir/doublets_tests.c.o
doublets_tests_alt: ../lib/dict_alt.o
doublets_tests_alt: CMakeFiles/doublets_tests_alt.dir/build.make
doublets_tests_alt: ../lib/libcriterion.so
doublets_tests_alt: CMakeFiles/doublets_tests_alt.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir="/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_3) "Linking C executable doublets_tests_alt"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/doublets_tests_alt.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/doublets_tests_alt.dir/build: doublets_tests_alt

.PHONY : CMakeFiles/doublets_tests_alt.dir/build

CMakeFiles/doublets_tests_alt.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/doublets_tests_alt.dir/cmake_clean.cmake
.PHONY : CMakeFiles/doublets_tests_alt.dir/clean

CMakeFiles/doublets_tests_alt.dir/depend:
	cd "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets" "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets" "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug" "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug" "/mnt/c/Oliver Killane's Files/A - Computer Science Degree/COMP40009 - Programming/C Programming/C Past Papers/Doublets/doublets/cmake-build-debug/CMakeFiles/doublets_tests_alt.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : CMakeFiles/doublets_tests_alt.dir/depend

