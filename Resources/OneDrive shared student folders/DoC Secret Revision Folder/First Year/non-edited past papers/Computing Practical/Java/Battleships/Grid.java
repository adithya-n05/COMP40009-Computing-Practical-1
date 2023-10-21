public class Grid {

  private static final int WIDTH = 10;
  private static final int HEIGHT = 10;

  private final Piece[][] grid = new Piece[HEIGHT][WIDTH];

  public Grid() {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        grid[i][j] = Piece.WATER;
      }
    }
  }

  public boolean canPlace(Coordinate c, int size, boolean isDown) {
    int row = c.getRow();
    int column = c.getColumn();
    if (isDown) {
      if (row + size > HEIGHT) {
        return false;
      }
      for (int i = 0; i < size; i++) {
        if (grid[row + i][column] != Piece.WATER) {
          return false;
        }
      }
    } else {
      if (column + size > WIDTH) {
        return false;
      }
      for (int i = 0; i < size; i++) {
        if (grid[row][column + i] != Piece.WATER) {
          return false;
        }
      }
    }
    return true;
  }

  public void placeShip(Coordinate c, int size, boolean isDown) {
    int row = c.getRow();
    int column = c.getColumn();
    if (isDown) {
      for (int i = 0; i < size; i++) {
        grid[row + i][column] = Piece.SHIP;
      }
    } else {
      for (int i = 0; i < size; i++) {
        grid[row][column + i] = Piece.SHIP;
      }
    }
  }

  public boolean wouldAttackSucceed(Coordinate c) {
    return grid[c.getRow()][c.getColumn()] == Piece.SHIP;
  }

  public void attackCell(Coordinate c) {
    int row = c.getRow();
    int column = c.getColumn();
    Piece piece = grid[row][column];
    if (piece == Piece.SHIP) {
      grid[row][column] = Piece.DAMAGED_SHIP;
    } else if (piece == Piece.WATER) {
      grid[row][column] = Piece.MISS;
    }
  }

  public boolean areAllSunk() {
    for (int i = 0; i < HEIGHT; i++) {
      for (int j = 0; j < WIDTH; j++) {
        if (grid[i][j] == Piece.SHIP) {
          return false;
        }
      }
    }
    return true;
  }

  public String toPlayerString() {
    Piece[][] grid = Util.deepClone(this.grid);
    Util.hideShips(grid);
    return renderGrid(grid);
  }

  @Override
  public String toString() {
    return renderGrid(grid);
  }

  private static String renderGrid(Piece[][] grid) {
    StringBuilder sb = new StringBuilder();
    sb.append(" 0123456789\n");
    for (int i = 0; i < grid.length; i++) {
      sb.append((char) ('A' + i));
      for (int j = 0; j < grid[i].length; j++) {
        if (grid[i][j] == null) {
          return "!";
        }
        switch (grid[i][j]) {
        case SHIP:
          sb.append('#');
          break;
        case DAMAGED_SHIP:
          sb.append('*');
          break;
        case MISS:
          sb.append('o');
          break;
        case WATER:
          sb.append('.');
          break;
        }
      }
      sb.append('\n');
    }
    return sb.toString();
  }

}