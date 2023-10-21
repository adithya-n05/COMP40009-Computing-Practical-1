import java.util.Random;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    Grid grid;
    if (args.length == 1 && args[0].equals("random")) {
      grid = makeRandomGrid();
    } else {
      grid = makeInitialGrid();
    }
    int i = 0;
    while (!grid.areAllSunk()) {
      System.out.println(grid.toPlayerString());
      System.out.println("Enter an attack:");
      Coordinate coord = Util.parseCoordinate(input.next());
      grid.attackCell(coord);
      if (grid.wouldAttackSucceed(coord)) {
        System.out.println("Direct Hit!");
      }
      i++;
    }
    System.out.println(i + " attack attempts were required to sink all ships.");
    System.out.println(grid);
  }

  private static Grid makeInitialGrid() {
    Grid grid = new Grid();
    String[] coords = { "A7", "B1", "B4", "D3", "F7", "H1", "H4" };
    int[] sizes = { 2, 4, 1, 3, 1, 2, 5 };
    boolean[] isDowns = { false, true, true, false, false, true, false };
    for (int i = 0; i < coords.length; i++) {
      Coordinate c = Util.parseCoordinate(coords[i]);
      grid.placeShip(c, sizes[i], isDowns[i]);
    }
    return grid;
  }

  private static Grid makeRandomGrid() {
    Random rnd = new Random();
    Grid grid = new Grid();
    int max = 10;
    int[] sizes = { 1, 1, 2, 2, 3, 4, 5 };
    for (int pos = 0; pos < sizes.length; pos++) {
      int size = sizes[pos];
      boolean isDown = rnd.nextBoolean();
      Coordinate c = new Coordinate(rnd.nextInt(max), rnd.nextInt(max));
      if (grid.canPlace(c, size, isDown)) {
        grid.placeShip(c, size, isDown);
      } else {
        pos--;
      }
    }
    return grid;
  }

}