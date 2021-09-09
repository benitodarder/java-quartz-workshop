package local.tin.tests.quartz.hello.world;

import java.util.Date;
import java.util.Random;
import org.apache.log4j.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author benitodarder
 */
public class HelloWorldJob implements Job {

    public static final String MAX_SLEEP_TIME = "MAX_SLEEP_TIME";
    public static final int DEFAULT_MAX_SLEEP_TIME = 666;
    private static final Logger LOGGER = Logger.getLogger(HelloWorldJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Random rand = new Random();
        int sleepTime = rand.nextInt(DEFAULT_MAX_SLEEP_TIME);
        if (context.getJobDetail().getJobDataMap().get(MAX_SLEEP_TIME) != null) {
            sleepTime = rand.nextInt(Integer.parseInt((String) context.getJobDetail().getJobDataMap().get(MAX_SLEEP_TIME)));
        }

        LOGGER.info("Hello World! - " + new Date() + ", I'll sleep for: " + sleepTime + "ms");
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ie) {
            LOGGER.warn("Who dares to interrupt me?!?!? ", ie);
        }
        LOGGER.info("Hello World! - " + new Date() + ", I slept for: " + sleepTime + "ms");
    }

}
