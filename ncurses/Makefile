CC = gcc
HEADERS = $(wildcard *.h)
JNI = -I/opt/java/jdk1.8.0_71/include -I/opt/java/jdk1.8.0_71/include/linux

all:
	javac NCurses.java
	javah NCurses
	gcc -fpic -c NCurses.c -o NCurses.o $(JNI)
	gcc -shared -o libNCurses.so NCurses.o -lcurses $(JNI)
	export LD_LIBRARY_PATH=${PWD}:/usr/local/lib:/usr/lib

clean:
	rm -f *.class *.so *.o *.h
