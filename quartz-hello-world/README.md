# quartz-hello-world

Simple clusterable hello world project.

Quartz 2.3.0, quartz-2.3.0-distribution.tar.gz, did not include the /docs/dbTables, so quartz-2.2.3-distribution.tar.gz was used.

HSQLDB schema does not work directly and BINARY type must be changed to BLOB.

Program arguments:

* Running time in ms. Mandatory.
* -maxSleepTime, max. sleeping time for job. Optional, defaults to 666ms.
* -cron, cron expression between ''. Optional.
* -config, alterante properties file. Optional.
* -rescheduleWhenJobFound, reschedule jobs. Optional.
* -deleteJobsBefore, delete jobs before start. Optional.
* -unscheduleJobs, unschedule jobs at the end. Optional.
* -shutdownScheduler, shutdown scheduler. Does no wait for jobs to end. Optional.

