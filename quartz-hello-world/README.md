# quartz-hello-world

Simple clusterable hello world project.

Program arguments:

* Running time in ms. Mandatory.
* -maxSleepTime, max. sleeping time for job. Optional, defaults to 666ms.
* -cron, cron expression between ''. Optional.
* -config, alterante properties file. Optional.
* -rescheduleWhenJobFound, reschedule jobs. Optional.
* -deleteJobsBefore, delete jobs before start. Optional.
* -unscheduleJobs, unschedule jobs at the end. Optional.
* -shutdownScheduler, shutdown scheduler. Does no wait for jobs to end. Optional.

Sample command lines: 

* java -jar target/quartz-hello-world-1.0-jar-with-dependencies.jar 30000  -maxSleepTime 10000 -cron '0/5 * * * * ? *' -deleteJobsBefore
* java -jar target/quartz-hello-world-1.0-jar-with-dependencies.jar 90000
 java -cp /c/Programs/hsqldb/2.4.1/hsqldb/lib/hsqldb.jar org.hsqldb.Server -database.0 /c/Users/benitodarder/Development/databases/quartz-hello-world/quartz-hello-world -dbname.0 quartz


