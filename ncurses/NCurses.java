
/* A JNI test */
public class NCurses {
	public NCurses() {
	}

	/** initializes the ncurses library */
	public native static int install();

	/** disposes the ncurses library */
	public native static int uninstall();

	/** set character on screen */
	public native static void setCharAt(int x, int y, int ch);

	/** update the screen */
	public native static void refresh();

	/** get the number of rows */
	public native int getRowCount();

	/** get the number of columns */
	public native int getColumnCount();

	/** inverse video */
	public native int standout();

	/** normal video */
	public native int standend();

	/** draw a string at the given position */
	public void drawString(String s, int x, int y) {
		int maxx = getColumnCount();
		for (int i = 0; i < s.length() && i + x < maxx; ++i) {
			if (i + 3 < s.length() && s.substring(i, i + 3).equals("<b>")) {
				standout();
				i += 2;
				continue;
			} else if (i + 4 < s.length() && s.substring(i, i + 4).equals("</b>")) {
				standend();
				i += 3;
				continue;
			}
			setCharAt(i + x, y, s.charAt(i));
		}
	}

	public static void main(String[] args) {
		// load the C library
		System.loadLibrary("NCurses");
		// init curses
		NCurses.install();

		NCurses sample = new NCurses();

		// draw a box
		for (int i = 1; i + 1 < sample.getRowCount(); ++i) {
			sample.setCharAt(1, i, '*');
			sample.setCharAt(sample.getColumnCount() - 2, i, '*');
		}
		for (int i = 1; i + 1 < sample.getColumnCount(); ++i) {
			sample.setCharAt(i, 1, '*');
			sample.setCharAt(i, sample.getRowCount() - 2, '*');
		}
		/* draw some strings */
		int n = 0;
		for (int i = 2; i + 3 < sample.getRowCount(); i += 3) {
			sample.drawString("<b>Menu \t" + (++n) + "</b>: Hello Menu", 5, i);
		}
		/* update the screen */
		sample.refresh();
		/* wait 10 sec */
		try {
			Thread.sleep(10000);
		} catch (Exception err) {
		}
		// dispose ncurses
		NCurses.uninstall();
	}
}
