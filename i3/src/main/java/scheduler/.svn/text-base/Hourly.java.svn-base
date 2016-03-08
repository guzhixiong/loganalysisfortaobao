package scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hourly {

	private static final Logger LOG = LoggerFactory.getLogger(Hourly.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LOG.info("hello...");
			}
		}, new Date(), 1000 * 10);
	}

}
