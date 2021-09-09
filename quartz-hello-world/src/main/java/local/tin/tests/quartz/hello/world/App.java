package local.tin.tests.quartz.hello.world;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;
import org.apache.log4j.Logger;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import static org.quartz.JobKey.jobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author benitodarder
 */
public class App {

    private static final String PARAMETER_CRON = "-cron";
    private static final String DEFAULT_VALUE_NOT_PRESENT = "";
    private static final String PARAMETER_CONFIG = "-config";
    private static final String DEFAULT_PROPERTIES_FILE = "quartz.properties";
    private static final String PARAMETER_RESCHEDULE = "-rescheduleWhenJobFound";
    private static final String PARAMETR_DELETE_JOBS_BEFORE = "-deleteJobsBefore";
    private static final String PARAMETER_UNSCHEDULE_JOBS = "-unscheduleJobs";
    private static final String SHUTDOWN_SCHEDULER = "-shutdownScheduler";
    private static final String PARAMETER_MAX_SLEEP_TIME = "-maxSleepTime";
    private static final String USAGE_STRING = "Usage: java -jar quartz-hello-world.jar" + System.lineSeparator()
            + "<Running time in ms. Mandatory>" + System.lineSeparator()
            + PARAMETER_MAX_SLEEP_TIME + " <Max. sleeping time for job. Optional, defaults to " + HelloWorldJob.DEFAULT_MAX_SLEEP_TIME + "ms>" + System.lineSeparator()
            + PARAMETER_CRON + " <Cron expression between ''. Optional>" + System.lineSeparator()
            + PARAMETER_CONFIG + " <Alternate properties file. Optional>" + System.lineSeparator()
            + PARAMETER_RESCHEDULE + " <Reschedule jobs. Optional>" + System.lineSeparator()
            + PARAMETR_DELETE_JOBS_BEFORE + " <Delete jobs before start. Optional>" + System.lineSeparator()
            + PARAMETER_UNSCHEDULE_JOBS +  " <Unschedule jobs at the end. Optional>" + System.lineSeparator()
            + SHUTDOWN_SCHEDULER + " <Shutdown scheduler. Does no wait for jobs to end. Optional>";

    private static final Logger LOGGER = Logger.getLogger(App.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SchedulerException {
        boolean rescheduleWhenJobFound = false;
        boolean deleteJobsBefore = false;
        boolean unscheduleJobs = false;
        String cronExpression = DEFAULT_VALUE_NOT_PRESENT;
        boolean shutdownScheduler = false;
        String maxSleepTime = String.valueOf(HelloWorldJob.DEFAULT_MAX_SLEEP_TIME);
        Scheduler scheduler = null;
        try {
            if (args.length < 1) {
                LOGGER.error(USAGE_STRING);
            } else {
                LOGGER.info("There we go...");
                Properties properties = null;
                for (int i = 1; i < args.length; i++) {
                    switch (args[i]) {
                        case PARAMETER_CRON:
                            i++;
                            cronExpression = args[i];
                            break;
                        case PARAMETER_CONFIG:
                            i++;
                            properties = getPropertiesFile(args[i]);
                            break;
                        case PARAMETER_RESCHEDULE:
                            rescheduleWhenJobFound = true;
                            break;
                        case PARAMETR_DELETE_JOBS_BEFORE:
                            deleteJobsBefore = true;
                            break;
                        case PARAMETER_UNSCHEDULE_JOBS:
                            unscheduleJobs = true;
                            break;
                        case SHUTDOWN_SCHEDULER:
                            shutdownScheduler = true;
                            break;
                        case PARAMETER_MAX_SLEEP_TIME:
                            i++;
                            maxSleepTime = args[i];
                            break;
                    }

                }
                if (properties == null) {
                    properties = getPropertiesFile(App.class, DEFAULT_PROPERTIES_FILE);
                }
                System.getProperties().putAll(properties);
                SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                scheduler = schedulerFactory.getScheduler();
                CronTrigger cronTrigger = null;
                if (!DEFAULT_VALUE_NOT_PRESENT.equals(cronExpression)) {

                    JobDetail joDetail = newJob(HelloWorldJob.class)
                            .withIdentity("job1", "group1")
                            .build();
                    joDetail.getJobDataMap().put(HelloWorldJob.MAX_SLEEP_TIME, maxSleepTime);
                    cronTrigger = newTrigger()
                            .withIdentity("trigger1", "group1")
                            .withSchedule(cronSchedule(cronExpression))
                            .build();
                    if (deleteJobsBefore) {
                        JobKey jobKey = jobKey("job1", "group1");
                        if (scheduler.checkExists(jobKey)) {
                            LOGGER.info("Job found with key: " + jobKey);
                            scheduler.deleteJob(jobKey);
                            LOGGER.info("Delete job found with key: " + jobKey);
                        }
                    }
                    Date scheduledDate;
                    if (scheduler.checkExists(joDetail.getKey()) && !rescheduleWhenJobFound) {
                        LOGGER.info("Job found... Exiting..");

                    } else {
                        if (scheduler.checkExists(joDetail.getKey()) && rescheduleWhenJobFound) {
                            LOGGER.info("Job found... ");
                            scheduledDate = scheduler.rescheduleJob(cronTrigger.getKey(), cronTrigger);
                            LOGGER.info("Job next rescheduled execution: " + scheduledDate);
                        } else {
                            LOGGER.info("Job not found... Scheduling...");
                            scheduledDate = scheduler.scheduleJob(joDetail, cronTrigger);
                            LOGGER.info("Job next scheduled execution: " + scheduledDate);
                        }
                    }
                }
                LOGGER.info("Scheduler about to start...");
                if (!scheduler.isStarted()) {
                    scheduler.start();
                    LOGGER.info("Scheduler started...");
                } else {
                    LOGGER.info("Scheduler already started...");
                }

                long t0 = System.currentTimeMillis();
                LOGGER.info("About to sleep for: " + args[0] + "ms");
                Thread.sleep(Long.parseLong(args[0]));
                LOGGER.info("Slept for: " + (System.currentTimeMillis() - t0) + "ms");
                if (unscheduleJobs && cronTrigger != null) {
                    LOGGER.info("Unscheduling jobs for key: " + cronTrigger.getKey());
                    scheduler.unscheduleJob(cronTrigger.getKey());
                    LOGGER.info("Unscheduled jobs for key: " + cronTrigger.getKey());

                }

                LOGGER.info("That's all folks!");
            }

        } catch (IOException | SchedulerException | InterruptedException | NumberFormatException e) {
            LOGGER.error(e);
        } finally {
            if (scheduler != null && scheduler.isStarted() && shutdownScheduler) {
                LOGGER.info("Scheduler about to shutdown...");
                scheduler.shutdown(false);
                LOGGER.info("Scheduler 'shutdowned'...");
            }
            System.exit(0);
        }

    }

    private static Properties getPropertiesFile(Class<?> klass, String fileName) throws IOException {
        InputStreamReader fileInputStream = new InputStreamReader(klass.getResourceAsStream(fileName));
        Properties properties = new Properties();
        properties.load(fileInputStream);
        return properties;
    }

    private static Properties getPropertiesFile(String filePath) throws IOException {
        try ( InputStream fileInputStream = new FileInputStream(filePath)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }

    }
}
