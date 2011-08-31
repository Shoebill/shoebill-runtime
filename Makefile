CC = g++
CFLAG = -O3 -Wall -ansi -fPIC
DFLAG = -DLINUX
INCDIR = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/linux \
	-I/usr/include
LIBDIR = -L$(JAVA_HOME)/jre/lib/i386 -L$(JAVA_HOME)/jre/lib/i386/client
WrapperDIR = Source/Wrapper/
JNIDIR = Source/JNI/
samppluginDIR = Dependencies/sampplugin/

OBJS = $(JNIDIR)encoding.o $(JNIDIR)jni_functions.o $(JNIDIR)jni_core.o \
	$(JNIDIR)samp_core.o $(JNIDIR)linux.o $(WrapperDIR)callback.o \
	$(WrapperDIR)core.o $(WrapperDIR)function.o \
	$(samppluginDIR)amxplugin.o $(samppluginDIR)amx/getch.o

.SUFFIXES: .o

all: libShoebill.so

libShoebill.so: $(OBJS)
	$(CC) -shared $(OBJS) $(LIBDIR) -ljvm $(CFLAG) -o $@
	mv $@ Binary/$@

%.o: %.cpp
	$(CC) $< $(CFLAG) $(DFLAG) $(INCDIR) $(LIBDIR) -c
	mv *.o $@

.PHONY: clean
clean:
	-rm Binary/libShoebill.so
	-rm $(OBJS)
