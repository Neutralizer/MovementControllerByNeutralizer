package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import interfacePanel.panel.MainPanel;

public class PersonalLoggerFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainPanel.class);

	public static Logger getLogger() {
		return LOGGER;
	}

}
