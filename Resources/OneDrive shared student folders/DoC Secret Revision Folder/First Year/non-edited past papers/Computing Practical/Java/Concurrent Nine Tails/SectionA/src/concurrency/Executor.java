package concurrency;

import concurrency.schedulers.Scheduler;
import java.util.LinkedList;
import java.util.List;

public class Executor {

  private ConcurrentProgram program;
  private Scheduler scheduler;

  public Executor(ConcurrentProgram program, Scheduler scheduler) {
    this.program = program;
    this.scheduler = scheduler;
  }

  /**
   * Executes program with respect to scheduler
   *
   * @return the final state and history of execution
   */
  public String execute() {
    List<Integer> history = new LinkedList<>();
    boolean deadlockOccurred = false;
    while (!(program.isTerminated() || deadlockOccurred)) {
      try {
        int id = scheduler.chooseThread(program);
        program.step(id);
        history.add(id);
      } catch (DeadlockException e) {
        deadlockOccurred = true;
      }
    }
    StringBuilder result = new StringBuilder();
    result.append("Final state: ").append(program).append("\n")
        .append("History: ").append(history).append("\n")
        .append("Termination status: ")
        .append(deadlockOccurred ? "deadlock" : "graceful").append("\n");
    return result.toString();
  }

  public ConcurrentProgram getProgram() {
    return program;
  }

  @Override
  public boolean equals(Object that) {
    return that instanceof Executor && program.toString()
        .equals(((Executor) that).getProgram().toString());
  }

}