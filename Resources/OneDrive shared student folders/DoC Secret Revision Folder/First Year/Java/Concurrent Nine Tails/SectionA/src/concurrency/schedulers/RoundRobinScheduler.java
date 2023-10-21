package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;
import java.util.Set;

public class RoundRobinScheduler extends AbstractScheduler {

  private boolean first;
  private int t;

  public RoundRobinScheduler() {
    first = true;
  }

  @Override
  public int chooseThread(ConcurrentProgram program) throws DeadlockException {
    checkDeadlock(program);
    Set<Integer> enabledThreadIds = program.getEnabledThreadIds();
    int smallest = enabledThreadIds.stream().min(Integer::compareTo).orElse(0);
    if (first) {
      t = smallest;
      first = false;
    } else {
      t = enabledThreadIds.stream()
          .filter(m -> m > t).min(Integer::compareTo).orElse(smallest);
    }
    return t;
  }

}