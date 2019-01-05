package bgSubtraction.keyboardControl;

/**
 * make new object for every new special key <br>
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class SpecialKey extends Key {

	/**
	 * changed to static, because there is only 1 special key
	 */
	private static boolean switched;
	private long startTimer;
	private int keyLimiter;

	/**
	 * the code and type which governs which action will be executed<br>
	 * 
	 * this key will make the boolean toggle change (stored internally)-
	 * ws/mouse/pause
	 * 
	 * KeyPressType.SPECIAL currently is only for visual indication = to be more
	 * special
	 * 
	 * @param keyCode
	 * @param keyPressType - {@link KeyPressType}
	 * @param keyLimiter
	 *            - after how many MS you will be able to switch the keys again
	 */
	public SpecialKey(int keyCode, KeyPressType keyPressType, int keyLimiter) {
		super(keyCode, keyPressType);
		switched = false;
		startTimer = 0;
		this.keyLimiter = keyLimiter;
	}
	
	/**
	 * set constant keyLimiter because of the properties file
	 * @param keyCode
	 * @param keyPressType
	 */
	public SpecialKey(int keyCode, KeyPressType keyPressType) {
		super(keyCode, keyPressType);
		switched = false;
		startTimer = 0;
		this.keyLimiter = 1000;
	}

	public static boolean getSwitched() {
		return switched;
	}

	/**
	 * executing this key action is to switch a boolean value, which will be used
	 * elsewhere <br>
	 * switch when detected and start a cooldown timer <br>
	 * 2 ROI - if 1st is triggered - change value in 2nd (externally from switched boolean value)
	 */
	public void executeAction(boolean detected) {
		switchKeyIfNeeded(detected);
		startTimer = keyController.toggleSwitchKeyLimiter(detected, startTimer, keyLimiter);
	}

	private void switchKeyIfNeeded(boolean detected) {
		if (startTimer == 0 && detected == true) {
			switched = !switched;
		}
	}

}
