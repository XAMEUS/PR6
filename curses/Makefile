CC = gcc
CFLAGS = -Wall -pthread -lncurses
LDFLAGS= -pthread -lncurses
EXEC = main
HEADERS = $(wildcard *.h)
OBJECTS = $(patsubst %.c, %.o, $(wildcard *.c))

all: $(EXEC)

%.o: %.c $(HEADERS)
	$(CC) -c $< $(CFLAGS)

main: console.o
	$(CC) $^ -o console $(LDFLAGS)

clean:
	rm -f $(OBJECTS)

indent:
	indent -nbad -bap -nbc -bbo -hnl -br -brs -c33 -cd33 -ncdb -ce -ci4 -cli0 -d0 -di1 -nfc1 -i8 -ip0 -l80 -lp -npcs -nprs -npsl -sai -saf -saw -ncs -nsc -sob -nfca -cp33 -ss -ts8 -il1 *.c -l125

mrproper: clean
	rm -f $(EXEC)
