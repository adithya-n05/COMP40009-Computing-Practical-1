public class GraphEdge {

  private Task task;

  private GraphNode start;
  private GraphNode end;

  public GraphEdge(Task task) {
    this.task = task;
  }

  public String getTaskName() {
    return task.getName();
  }

  public int getTaskDuration() {
    return task.getDuration();
  }

  public GraphNode getStartPoint() {
    return start;
  }

  public void setStartPoint(GraphNode start) {
    this.start = start;
  }

  public GraphNode getEndPoint() {
    return end;
  }

  public void setEndPoint(GraphNode end) {
    this.end = end;
  }

}