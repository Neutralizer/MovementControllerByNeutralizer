package bgSubtraction.keyboardControl;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public enum KeyPressType {

	CONSTANT, PRESS, TOGGLE, SPECIAL;

	public static KeyPressType getType(int type) {
		if(type == 0) {
			return CONSTANT;
		}
		if(type == 1) {
			return PRESS;
		}
		if(type == 2) {
			return TOGGLE;
		}
		if(type == 3) {
			return SPECIAL;
		}
		return null;
	}
}
