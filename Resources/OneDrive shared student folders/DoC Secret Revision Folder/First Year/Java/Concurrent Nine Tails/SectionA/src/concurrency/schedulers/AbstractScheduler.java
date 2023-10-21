package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;

abstract class AbstractScheduler implements Scheduler {

  void checkDeadlock(ConcurrentProgram program) throws DeadlockException {
    if (program.getEnabledThreadIds().isEmpty()) {
      throw new DeadlockException();
    }
  }

}