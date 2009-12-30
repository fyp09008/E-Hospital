/**
 * 
 */
package hku.hk.cs.javacard;

import org.apache.log4j.Logger;

import com.ibm.jc.*;

/**
 * @author Dan
 * 
 */
public class JavaCardManager {

	private static Logger logger = Logger.getLogger(JavaCardManager.class);

	public static final int JCM_REMOTE = 0;

	public static final int JCM_PCSC = 1;

	private JCard card;

	private JCApplet applet;

	/**
	 * 
	 */
	public JavaCardManager(int spec, String param, String aid) {
		//logger.info("Starting Smartcard Terminal ... ");
		JCTerminal terminal = null;
		switch (spec) {
		case JCM_REMOTE:
			terminal = JCTerminal.getInstance("Remote", param);
			logger.debug("Remote Terminal connected ...");
			break;
		case JCM_PCSC:
			terminal = JCTerminal.getInstance("PCSC", param);
			logger.debug("PCSC Terminal [" + param + "] connected ...");
			break;
		}

		//logger.info("Opening terminal ... ");

		terminal.open();
		byte[] atr = terminal.waitForCard(500);
		if (atr != null) {
			logger.debug("ATR:" + JCInfo.dataToString(atr));
		}

		//logger.info("Get card ...");
		card = new JCard(terminal, null, 500);

		//logger.info("Select card manager ...");
		CardManager cardManager = new CardManager(card, CardManager.daid);
		cardManager.select();

		//logger.info("Authenticate to card manager ...");
		cardManager.initializeUpdate(255, 0, OPApplet.SCP_UNDEFINED);
		cardManager.externalAuthenticate(OPApplet.APDU_CLR);

		// synchronize state with on-card card manager
		cardManager.update();

		JCInfo info = JCInfo.INFO;
		logger.debug("CardManager AID : "
				+ JCInfo.dataToString(cardManager.getAID()));
		logger.debug("CardManager state : "
				+ info.toString("card.status", (byte) cardManager.getState()));

		if (aid != null) {
			// Select Applet
			selectApplet(JavaCardHelper.string2bytes(aid));
		}
	}

	public void selectApplet(byte[] aid) {
		applet = new JCApplet(card, aid);
		applet.select();
	}

	public JCard getCard() {
		return card;
	}

	public JCApplet getApplet() {
		return applet;
	}
}
