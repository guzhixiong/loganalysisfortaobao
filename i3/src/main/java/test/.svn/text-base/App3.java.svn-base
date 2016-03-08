package test;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App3 {

	private static final Logger log = LoggerFactory.getLogger(App3.class);

	public static void main(String[] args) throws InterruptedException {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDetail job = new JobDetail("job1", "group1", HelloJob.class);

			Date t = TriggerUtils.getEvenMinuteDate(new Date());
			SimpleTrigger trigger = new SimpleTrigger("trigger1", "group1", t);

			scheduler.scheduleJob(job, trigger);

			scheduler.start();

			Thread.sleep(1000 * 60 * 60);

			scheduler.shutdown();

		} catch (SchedulerException se) {
			se.printStackTrace();
		}

	}

	public static class HelloJob implements Job {

		@Override
		public void execute(JobExecutionContext ctx)
				throws JobExecutionException {
			log.info("Hello World! - " + new Date());
		}

	}

}
