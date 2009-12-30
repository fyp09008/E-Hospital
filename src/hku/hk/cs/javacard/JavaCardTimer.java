/**
 * 
 */
package hku.hk.cs.javacard;

/**
 * @author Dan
 * 
 */
public class JavaCardTimer {
	private long startTick = 0;
	private long duration = 0;
	private long total = 0;
	private int delay = 0;

	public JavaCardTimer() {
		reset();
	}

	public JavaCardTimer(int delay) {
		reset();
		this.delay = delay;
	}

	public void reset() {
		startTick = System.currentTimeMillis();
		duration = 0;
	}

	public void start() {
		reset();
	}

	public void stop() {
		duration = System.currentTimeMillis() - startTick;

		total += duration - delay;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public long getDurationValue() {
		return duration;
	}

	public long getTotalDurationValue() {
		return total;
	}

	public String getDuration() {
		return formatDuration(duration);
	}

	public String getTotalDuration() {
		return formatDuration(total);
	}

	public static String formatDuration(long duration) {
		// Validate
		if (duration > 0L) {
			// -- Declare variables
			String strDays = "";
			String strHours = "";
			String strMinutes = "";
			String strSeconds = "";
			String strMillisecs = "";
			String strReturn = "";
			long lRest;

			// -- Find values
			// -- -- Days
			strDays = String.valueOf(duration / 86400000L);
			lRest = duration % 86400000L;
			// -- -- Hours
			strHours = String.valueOf(lRest / 3600000L);
			lRest %= 3600000L;
			// -- -- Minutes
			strMinutes = String.valueOf(lRest / 60000L);
			lRest %= 60000L;
			// -- -- Seconds
			strSeconds = String.valueOf(lRest / 1000L);
			lRest %= 1000L;
			// -- -- Milliseconds
			strMillisecs = String.valueOf(lRest);

			// -- Format return
			// -- -- Days
			if (new Integer(strDays).intValue() != 0) {
				strReturn += strDays + "day ";
			}
			// -- -- Hours
			if (new Integer(strHours).intValue() != 0) {
				strReturn += strHours + "hr ";
			}
			// -- -- Minutes
			if (new Integer(strMinutes).intValue() != 0) {
				strReturn += strMinutes + "min ";
			}
			// -- -- Seconds
			if (new Integer(strSeconds).intValue() != 0) {
				strReturn += strSeconds + "sec ";
			}
			// -- -- Milliseconds
			if (new Integer(strMillisecs).intValue() != 0) {
				strReturn += strMillisecs + "ms";
			}

			return strReturn;
		} else if (duration == 0L) {

			return "0ms";
		} else {
			return "-1";
		}
	}
}
