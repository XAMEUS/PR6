/**
* \file console.h
* \brief Terminal application (sample)
* \author Gourgoulhon Maxime
* \version 0.1
* \date 04 28 2016
*
* Using curses.h ; trying to split input / output (unsing threads).
*
*/

#include <stdlib.h>
#include <ctype.h>
#include <curses.h>
#include <sys/ioctl.h>
#include <string.h>
#include <iso646.h>
#include <pthread.h>
#include <unistd.h>

extern WINDOW *win;

/**
 * \fn
 * \brief Initializes ncurses.
 * \return 0 if succeeded
 */
int init_ncurses();

/**
 * \fn void out_print(WINDOW *win, char* c)
 * \brief Prints the given char*
 * \param c chars to print.
 */
void out_print(WINDOW *win, char* c);
