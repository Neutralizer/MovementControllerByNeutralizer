package bgSubtraction.keyboardControl;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public enum KeyPressType {

	CONSTANT, TOGGLE, PRESS, SPECIAL;

	public static KeyPressType getType(int type) {
		if(type == 0) {
			return CONSTANT;
		}
		if(type == 1) {
			return TOGGLE;
		}
		if(type == 2) {
			return PRESS;
		}
		if(type == 3) {
			return SPECIAL;
		}
		return null;
	}
}
