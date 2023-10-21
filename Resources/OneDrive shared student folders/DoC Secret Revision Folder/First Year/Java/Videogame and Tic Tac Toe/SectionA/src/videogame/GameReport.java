package videogame;

public class GameReport {

  public static void main(String[] args) {
    TransportUnit t1 = new TransportUnit("Falkor", 2500);
    TransportUnit t2 = new TransportUnit("Shadowfax", 350);
    Entity m1 = new Magician("Ged", 300);
    Entity m2 = new Magician("Dumbledore", 200);
    Entity m3 = new Magician("Saruman", 600);
    Entity m4 = new Magician("Harry", 18);
    Entity m5 = new Magician("Gandalf", 950);
    t1.add(m1);
    t1.add(m2);
    t1.add(m3);
    t2.add(m4);
    t2.add(m5);
    t1.add(t2);
    System.out.println(t1);
    Magician m6 = new Magician("Gargamel", 200);
    t1.applySpell(m6);
    System.out.println(t1);
    Magician m7 = new Magician("Magician", t1.minimumStrikeToDestroy());
    t1.applySpell(m7);
    System.out.println(t1);
    assert !t1.isAlive() && !t2.isAlive() && !m1.isAlive() && !m2.isAlive()
        && !m3.isAlive() && !m4.isAlive() && !m5.isAlive();
    assert m6.isAlive() && m7.isAlive();
  }

}