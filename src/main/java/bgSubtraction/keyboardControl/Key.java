package bgSubtraction.keyboardControl;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
/**
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class Key {

	protected int keyCode;
	private KeyPressType keyPressType;
	private long startTimer = 0;
	protected KeyController keyController = new KeyController();

	/**
	 * types of button - CONSTANT press with refreshing, TOGGLE, PRESS //TODO may be changed
	 * 
	 * @param keyCode
	 *            KeyEvent.VK_W ex
	 * @param keyPressType
	 *            -{@link KeyPressType};
	 */
	public Key(int keyCode, KeyPressType keyPressType) {
		this.keyCode = keyCode;
		this.keyPressType = keyPressType;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(int keyCode) {
		this.keyCode = keyCode;
	}
	
	public KeyPressType getKeyPressType() {
		return keyPressType;
	}

	public void setKeyPressType(KeyPressType keyPressType) {
		this.keyPressType = keyPressType;
	}

	/**
	 * TOGGLE -press the button in fist detection - release the button in 2nd
	 * detection after at least 500ms
	 * 
	 * @param detected
	 */
	public void executeAction(boolean detected) {
		if (this.keyPressType == KeyPressType.CONSTANT) {
			startTimer = keyController.pressAndReleaseKey(detected, keyCode, startTimer, 100, false);
		}
		if (this.keyPressType == KeyPressType.PRESS) {
			startTimer = keyController.pressAndReleaseKey(detected, keyCode, startTimer, 500, true);
		}
		if (this.keyPressType == KeyPressType.TOGGLE) {
			startTimer = keyController.pressHoldAndRelease(detected, keyCode, startTimer, 1000);
		}

	}

}
