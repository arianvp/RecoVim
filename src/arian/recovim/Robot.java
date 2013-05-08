package arian.recovim;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;

public class Robot extends java.awt.Robot {

    public Robot() throws AWTException {
        super();
    }

    // "CTRL+S"
    public void typeKeys(String... keys) {
        for (String key : keys) {
            System.out.println(key);
            StringTokenizer st = new StringTokenizer(key, "+");
            List<Integer> keycodes = new ArrayList<>();

            while (st.hasMoreTokens()) {
                String token = st.nextToken().trim();
                switch (token.toLowerCase()) {
                case "ctrl":
                    keycodes.add(KeyEvent.VK_CONTROL);
                    break;
                case "shift":
                    keycodes.add(KeyEvent.VK_SHIFT);
                    break;
                default:
                    if (token.length() == 1) {
                        char ch = token.charAt(0);
                        if (Character.isUpperCase(ch) && keycodes.isEmpty()) {
                            keycodes.add(KeyEvent.VK_SHIFT);
                        }
                        keycodes.add(KeyEvent.getExtendedKeyCodeForChar(ch));
                        break;
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
            }

            for (int keycode : keycodes) {
                keyPress(keycode);
            }

            ListIterator<Integer> i = keycodes.listIterator(keycodes.size());

            while (i.hasPrevious()) {
                keyRelease(i.previous());
            }
        }
    }

    public static void main(String[] args) throws AWTException,
            InterruptedException {
        new Robot().typeKeys("i", "G", "ctrl+o", "ctrl+shift+a");
    }
}
