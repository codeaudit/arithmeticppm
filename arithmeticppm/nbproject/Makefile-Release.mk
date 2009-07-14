#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc.exe
CCC=g++.exe
CXX=g++.exe
FC=g77.exe

# Macros
PLATFORM=MinGW-Windows

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=build/Release/${PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/funcoesppmc.o \
	${OBJECTDIR}/ppmccod.o

# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	${MAKE}  -f nbproject/Makefile-Release.mk dist/Release/${PLATFORM}/ppmccod.exe

dist/Release/${PLATFORM}/ppmccod.exe: ${OBJECTFILES}
	${MKDIR} -p dist/Release/${PLATFORM}
	${LINK.c} -o dist/Release/${PLATFORM}/ppmccod ${OBJECTFILES} ${LDLIBSOPTIONS} 

${OBJECTDIR}/funcoesppmc.o: funcoesppmc.c 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.c) -O2 -o ${OBJECTDIR}/funcoesppmc.o funcoesppmc.c

${OBJECTDIR}/ppmccod.o: ppmccod.c 
	${MKDIR} -p ${OBJECTDIR}
	$(COMPILE.c) -O2 -o ${OBJECTDIR}/ppmccod.o ppmccod.c

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf:
	${RM} -r build/Release
	${RM} dist/Release/${PLATFORM}/ppmccod.exe

# Subprojects
.clean-subprojects:
