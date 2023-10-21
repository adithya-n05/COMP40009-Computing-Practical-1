package videogame;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TransportUnit extends Entity {

  private Set<Entity> entities;

  public TransportUnit(String name, int lifePoints) {
    super(name, lifePoints);
    entities = new HashSet<>();
  }

  @Override
  protected int propagateDamage(int damageAmount) {
    assert damageAmount >= 0;
    int min = Math.min(damageAmount, lifePoints);
    lifePoints -= min;
    return min + entities.stream()
        .mapToInt(entity -> entity.propagateDamage(damageAmount / 2)).sum();
  }

  @Override
  public int minimumStrikeToDestroy() {
    return Math.max(lifePoints, entities.stream()
        .mapToInt(entity -> 2 * entity.lifePoints).max().orElse(0));
  }

  public void add(Entity entity) {
    entities.add(entity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(name);
    sb.append("(");
    sb.append(lifePoints);
    sb.append(") transporting: [");
    Iterator<Entity> iterator = entities.iterator();
    while (iterator.hasNext()) {
      sb.append(iterator.next());
      if (iterator.hasNext()) {
        sb.append(", ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

}