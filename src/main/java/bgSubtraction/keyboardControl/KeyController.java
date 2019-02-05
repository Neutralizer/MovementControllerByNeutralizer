package bgSubtraction.keyboardControl;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;

import bgSubtraction.detector.movementDetector.ROI;

/**
 * 
 * @author Tsvetan "Neutralizer" Trifonov
 *
 */
public class KeyController {
	private static Robot r;

	public KeyController() {
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * TOGGLE -press the button in fist detection - release the button in 2nd
	 * detection after at least 500ms(intervalMS)
	 * @param isDetected
	 * @param keyCode
	 * @param startTimer
	 * @param intervalMS blockage between detection and key actions
	 * @return
	 */
	public long pressHoldAndRelease(boolean isDetected, int keyCode, long startTimer, int intervalMS) {
		
		if (isDetected && startTimer == 0) {
			startTimer = pressKeyTimer(keyCode, startTimer);
		} 
		if(startTimer == 1 || (startTimer > 1 && startTimer < 300 )) {//wait 300 ms before it can be pressed
			startTimer = startTimer +10;//make it increment until 300 ms have passed
		} else if(startTimer > 300 && startTimer < 1000000 ){//300ms have passed - make it able to be pressed
			startTimer = 0;
		}
		long endTimer = 0;
		//intended only for starttimer to be 0 or 10000000
		if(startTimer == 0 ||  startTimer > 1000000 ) {
			endTimer = System.currentTimeMillis() - startTimer;
		}

		if(isDetected && (endTimer > intervalMS)) {
			startTimer = releaseKeyTimer(keyCode, startTimer, intervalMS);
			startTimer = 1;
		}

		return startTimer;
	}
	
	
	public boolean hold(boolean isDetected, int keyCode, boolean isPressed) {
		
		if (isDetected && !isPressed ) {
			pressKeyTimer(keyCode, 0);
		} 
		
		if(isDetected && isPressed) {
			releaseKeyTimer(keyCode, 0, 0);
		}

		return isPressed;
	}

	/**
	 * 
	 * Presses a keyboard key and releases it after x ms; Works only when setting
	 * the start timer as result of the method; if isToggle is true - interval must
	 * be 1000
	 * 
	 * @param isDetected
	 *            - should the button be pressed:if rect has more than 50%
	 *            white-detected
	 * @param keyCode
	 *            - keyCode ex - KeyEvent.VK_ESCAPE
	 * @param startTimer
	 *            - has it been pressed and if yes - since when
	 * @param intervalMS
	 *            - how long should the key be pressed for
	 * @param isToggle
	 *            - is the button meant to be pressed as toggle - SHIFT in a game
	 *            for ex; 
	 *            //TODO REDESIGNED - toggle key logic is in keyController.pressHoldAndRelease
	 * 
	 * @return the time when the key was pressed - 0 if not pressed
	 */
	public long pressAndReleaseKey(boolean isDetected, int keyCode, long startTimer, int intervalMS, boolean isToggle) {

		if (isToggle) {
			if (isDetected && startTimer == 0) {
				startTimer = pressKeyTimer(keyCode, startTimer);
			}
		} else {
			if (isDetected) {// refreshing if isDetected is true
				startTimer = pressKeyTimer(keyCode, startTimer);
			}
		}

		startTimer = releaseKeyTimer(keyCode, startTimer, intervalMS);

		return startTimer;
	}

	private long pressKeyTimer(int keyCode, long startTimer) {
		// int code = KeyEvent.getExtendedKeyCodeForChar(keyCode);
		r.keyPress(keyCode);
		return startTimer = System.currentTimeMillis();

	}

	private long releaseKeyTimer(int keyCode, long startTimer, int intervalMS) {
		if (startTimer == 0) {// has not been pressed
			return 0;
		}

		long endTimer = System.currentTimeMillis() - startTimer;

		if (endTimer > intervalMS) {// not detecting it good enough for a 50 ms window
			// int code = KeyEvent.getExtendedKeyCodeForChar(keyCode);//for char
			r.keyRelease(keyCode);
			startTimer = 0;
		}

		return startTimer;
	}

	/**
	 * Toggle switch between pressing of 2 keys If boolean is true - press the 1st
	 * key, if it is false - press the 2nd<br>
	 * Currently releases the other button<br>
	 * 
	 * @param switchKey
	 *            does it need to be switched
	 * @param firstKey
	 * @param secondKey
	 * @return the keyCode of the key to be pressed
	 */
	public int switchKeyToBePressed(boolean switchKey, int firstKey, int secondKey) {
		int keyCode;

		if (switchKey) {
			keyCode = firstKey;
			r.keyRelease(secondKey);
		} else {
			keyCode = secondKey;
			r.keyRelease(firstKey);
		}

		return keyCode;
	}

	/**
	 * start a cooldown timer to block key switching
	 * @param isDetected
	 * @param startTimer
	 * @param timeLimit
	 * @return
	 */
	public long toggleSwitchKeyLimiter(boolean isDetected, long startTimer, int timeLimit) {
		if (isDetected && startTimer == 0) {// to not switch it 20 times during detection
			startTimer = System.currentTimeMillis();
		}
		if (startTimer != 0) {// ws has been detected
			long endTimer = System.currentTimeMillis() - startTimer;
			if (endTimer > timeLimit) {
				startTimer = 0;
			}
		}
		return startTimer;

	}
	
	/**
	 *
	 * @param listRoi
	 */
	public static void unpressAllRoiButtons(ArrayList<ROI> listRoi) {
		for (ROI roi : listRoi) {
			r.keyRelease(roi.getKey().getKeyCode());
		}
	}
	
}
