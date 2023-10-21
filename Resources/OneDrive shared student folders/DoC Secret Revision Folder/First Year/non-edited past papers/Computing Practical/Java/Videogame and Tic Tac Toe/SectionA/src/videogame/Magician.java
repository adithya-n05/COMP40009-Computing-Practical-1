package videogame;

public class Magician extends Entity implements SpellCaster {

  public Magician(String name, int lifePoints) {
    super(name, lifePoints);
  }

  @Override
  public int getStrength() {
    return 2 * lifePoints;
  }

  @Override
  protected int propagateDamage(int damageAmount) {
    assert damageAmount >= 0;
    int min = Math.min(damageAmount, lifePoints);
    lifePoints -= min;
    return min;
  }

  @Override
  public int minimumStrikeToDestroy() {
    return lifePoints;
  }

  @Override
  public String toString() {
    return name + "(" + lifePoints + ")";
  }

}