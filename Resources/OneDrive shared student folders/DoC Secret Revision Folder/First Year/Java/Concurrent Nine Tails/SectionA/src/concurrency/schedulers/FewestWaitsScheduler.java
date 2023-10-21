package concurrency.schedulers;

import concurrency.ConcurrentProgram;
import concurrency.DeadlockException;
import concurrency.statements.WaitStmt;
import java.util.Comparator;
import java.util.Set;

public class FewestWaitsScheduler extends AbstractScheduler {

  @Override
  public int chooseThread(ConcurrentProgram program) throws DeadlockException {
    checkDeadlock(program);
    Set<Integer> enabledThreadIds = program.getEnabledThreadIds();
    return enabledThreadIds.stream().min(Comparator.comparing(
        id -> program.remainingStatements(id).stream()
            .filter(s -> s instanceof WaitStmt).count())).orElse(0);
  }

}