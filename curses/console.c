
#include "console.h"

char *intprtkey(int ch);

WINDOW *win;

struct winsize wsize;

char **out;
int out_size;

int quit = 0;

int bufsize = 64;
char* buf;
int ch;

void *debug(){
  while (!quit)
  {
    sleep(2);
    out_print(win, "DEBUG: I AM A THREAD USING THE OUTPUT!");
    refresh();
  }
  return NULL ;
}

int init_ncurses()
{
  // initialize ncurses
  if ( (win = initscr()) == NULL )
  {
    fprintf(stderr, "Error initializing ncurses.\n");
    exit(EXIT_FAILURE);
  }

  if (ioctl(0, TIOCGWINSZ, (char *) &wsize) < 0)
  {
    printf("TIOCGWINSZ error");
    exit(EXIT_FAILURE);
  }

  noecho();          // turn off key echoing
  keypad(win, TRUE); // enable the keypad for non-char keys
  start_color();     // a set of colors and color pairs

  out_size = wsize.ws_row - 2;
  out = malloc(sizeof(char*) * out_size);
  int i;
  for (i = 0; i < out_size; i++)
  {
    out[i] = malloc(sizeof(char) * 64);
    out[i][0] = '\0';
  }
  return 0;
}

void out_print(WINDOW *win, char* c)
{
  int x;
  int y;
  getyx(win, y, x);
  int i;

  free(out[0]);
  for (i = 0; i < out_size - 1; i++)
  {
    out[i] = out[i + 1];
  }
  char* m = malloc(sizeof(char) * 64);;
  strcpy(m, c);
  out[out_size - 1] = m;

  move(1, 0);
  for (i = 0; i < wsize.ws_row - 1; i++)
    deleteln();

  for (i = out_size - 1; i > 0; i--)
  {
    if (strlen(out[i]) > 0)
      mvprintw(i, 1, " > %s", out[i]);
  }
  mvhline(wsize.ws_row - 2, 1, ACS_HLINE, wsize.ws_col - 2);
  move(y, x);
  mvprintw(wsize.ws_row - 1, wsize.ws_col - strlen(intprtkey(ch)) - 9, "0x%x (%s)", ch, intprtkey(ch));
  mvprintw(wsize.ws_row - 1, 1, " [%d] %s", strlen(buf), buf);
}

int main(int argc, char* argv[])
{

  init_ncurses();

  buf = malloc(sizeof(char) * bufsize);
  buf[0] = '\0';

  mvprintw(0, 1, "[F3]: quit");
  mvhline(wsize.ws_row - 2, 1, ACS_HLINE, wsize.ws_col - 2);
  mvprintw(wsize.ws_row - 1, 1, " [%d] ", strlen(buf));
  refresh();

  pthread_t th1;
  pthread_create(&th1, NULL, debug, NULL);

  while (!quit)
  {
    if ((ch = getch()) == KEY_F(3))
      quit = 1;

    if ((ch == KEY_ENTER) or (ch == 10) or (ch == 13))
    {
      out_print(win, buf);
      mvprintw(wsize.ws_row - 1, 1, " [%d] ", strlen(buf));
      buf[0] = '\0';
    }
    else if ((strlen(buf) > 0) && (ch == KEY_BACKSPACE))
    {
      int len = strlen(buf);
      buf[len - 1] = '\0';
    }
    else if ((strlen(buf) + 1 <= bufsize) && !(ch & KEY_CODE_YES))
    {
      int len = strlen(buf);
      buf[len] = ch;
      buf[len + 1] = '\0';
    }
    deleteln();
    mvprintw(wsize.ws_row - 1, wsize.ws_col - strlen(intprtkey(ch)) - 9, "0x%x (%s)", ch, intprtkey(ch));
    mvprintw(wsize.ws_row - 1, 1, " [%d] %s", strlen(buf), buf);
    refresh();
  }

  // clean-up
  delwin(win);
  endwin();
  // refresh();

  return EXIT_SUCCESS;
}


// struct to hold keycode/keyname information
struct keydesc {
  int  code;
  char name[20];
};


/**
 * \fn char *intprtkey(int ch)
 * \brief Returns a string describing a character passed to it.
 * \param ch the character code
 */
char *intprtkey(int ch)
{
  // define a selection of keys
  static struct keydesc keys[] =
  {
    { KEY_UP,        "Up arrow"        },
    { KEY_DOWN,      "Down arrow"      },
    { KEY_LEFT,      "Left arrow"      },
    { KEY_RIGHT,     "Right arrow"     },
    { KEY_HOME,      "Home"            },
    { KEY_END,       "End"             },
    { KEY_BACKSPACE, "Backspace"       },
    { KEY_IC,        "Insert"          },
    { KEY_DC,        "Delete"          },
    { KEY_NPAGE,     "Page down"       },
    { KEY_PPAGE,     "Page up"         },
    { KEY_ENTER,     "Enter or send"   },
    { KEY_F(1),      "Function key 1"  },
    { KEY_F(2),      "Function key 2"  },
    { KEY_F(3),      "Function key 3"  },
    { KEY_F(4),      "Function key 4"  },
    { KEY_F(5),      "Function key 5"  },
    { KEY_F(6),      "Function key 6"  },
    { KEY_F(7),      "Function key 7"  },
    { KEY_F(8),      "Function key 8"  },
    { KEY_F(9),      "Function key 9"  },
    { KEY_F(10),     "Function key 10" },
    { KEY_F(11),     "Function key 11" },
    { KEY_F(12),     "Function key 12" },
    { -1,            "<unsupported>"   }
  };
  static char keych[2] = {0};

  if ( isprint(ch) && !(ch & KEY_CODE_YES)) {
    // PRINTABLE character
    keych[0] = ch;
    return keych;
  }
  else
  {
    // NON-PRINTABLE
    int n = 0;
    do
    {
      if ( keys[n].code == ch )
      return keys[n].name;
      n++;
    } while ( keys[n].code != -1 );

    return keys[n].name;
  }

  return NULL; // we shouldn't get here
}
