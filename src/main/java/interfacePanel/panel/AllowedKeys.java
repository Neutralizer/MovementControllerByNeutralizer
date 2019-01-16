package interfacePanel.panel;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public final class AllowedKeys {

	private Map<String, Integer> allowedMap = new TreeMap<>();
	private String[] allowedKeys;

	public AllowedKeys() {
		allowedMap.put("A", KeyEvent.VK_A);
		allowedMap.put("B", KeyEvent.VK_B);
		allowedMap.put("C", KeyEvent.VK_C);
		allowedMap.put("D", KeyEvent.VK_D);
		allowedMap.put("E", KeyEvent.VK_E);
		allowedMap.put("F", KeyEvent.VK_F);
		allowedMap.put("G", KeyEvent.VK_G);
		allowedMap.put("H", KeyEvent.VK_H);
		allowedMap.put("I", KeyEvent.VK_I);
		allowedMap.put("J", KeyEvent.VK_J);
		allowedMap.put("K", KeyEvent.VK_K);
		allowedMap.put("L", KeyEvent.VK_L);
		allowedMap.put("M", KeyEvent.VK_M);
		allowedMap.put("N", KeyEvent.VK_N);
		allowedMap.put("O", KeyEvent.VK_O);
		allowedMap.put("P", KeyEvent.VK_P);
		allowedMap.put("Q", KeyEvent.VK_Q);
		allowedMap.put("R", KeyEvent.VK_R);
		allowedMap.put("S", KeyEvent.VK_S);
		allowedMap.put("T", KeyEvent.VK_T);
		allowedMap.put("U", KeyEvent.VK_U);
		allowedMap.put("V", KeyEvent.VK_V);
		allowedMap.put("W", KeyEvent.VK_W);
		allowedMap.put("X", KeyEvent.VK_X);
		allowedMap.put("Y", KeyEvent.VK_Y);
		allowedMap.put("Z", KeyEvent.VK_Z);

		allowedMap.put("1", KeyEvent.VK_1);
		allowedMap.put("2", KeyEvent.VK_2);
		allowedMap.put("3", KeyEvent.VK_3);
		allowedMap.put("4", KeyEvent.VK_4);
		allowedMap.put("5", KeyEvent.VK_5);
		allowedMap.put("6", KeyEvent.VK_6);
		allowedMap.put("7", KeyEvent.VK_7);
		allowedMap.put("8", KeyEvent.VK_8);
		allowedMap.put("9", KeyEvent.VK_9);
		allowedMap.put("0", KeyEvent.VK_0);

		allowedMap.put("ESCAPE", KeyEvent.VK_ESCAPE);
		allowedMap.put("TAB", KeyEvent.VK_TAB);
		allowedMap.put("CAPSLOCK", KeyEvent.VK_CAPS_LOCK);
		allowedMap.put("SHIFT", KeyEvent.VK_SHIFT);
		allowedMap.put("CONTROL", KeyEvent.VK_CONTROL);
		allowedMap.put("ALT", KeyEvent.VK_ALT);
		allowedMap.put("SPACE", KeyEvent.VK_SPACE);
		allowedMap.put("LEFT", KeyEvent.VK_LEFT);
		allowedMap.put("RIGHT", KeyEvent.VK_RIGHT);
		allowedMap.put("UP", KeyEvent.VK_UP);
		allowedMap.put("DOWN", KeyEvent.VK_DOWN);
		allowedMap.put("ENTER", KeyEvent.VK_ENTER);

		allowedKeys = allowedMap.keySet().toArray(new String[allowedMap.keySet().size()]);

	}

	public String[] getAllowedKeys() {
		return allowedKeys;
	}

	public int getKeyCode(String stringKey) {
		return allowedMap.get(stringKey);
	}

}
