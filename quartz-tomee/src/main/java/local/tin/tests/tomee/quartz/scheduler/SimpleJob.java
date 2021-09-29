package local.tin.tests.tomee.quartz.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class SimpleJob {

  private static final Logger LOGGER = LogManager.getLogger();

  @Resource
  private SessionContext sessionContext;


  // METHODS

  @PostConstruct
  void init() {

     LOGGER.info("SimpleJob initilized!!");
  }

  // poner tiempo concreto
  @Schedule(second="30", minute = "*", hour = "*")
  public void scheduledMethod() {

     LOGGER.info("Running scheduled task in: " + System.getProperty("simple.job.identifier"));

  }


}
