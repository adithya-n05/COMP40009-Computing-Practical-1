package aeroplane;

import java.util.NoSuchElementException;

public class Seat {

  private int row;
  private char letter;

  public Seat(int row, char letter) throws MalformedDataException {
    if (row < 1 || row > 50 || letter < 'A' || letter > 'F') {
      throw new MalformedDataException();
    }
    this.row = row;
    this.letter = letter;
  }

  public boolean isEmergencyExit() {
    return row == 1 || row == 10 || row == 30;
  }

  public boolean hasNext() {
    return row < 50 || letter < 'F';
  }

  public Seat next() throws MalformedDataException {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return letter >= 'F'
        ? new Seat(row + 1, 'A')
        : new Seat(row, (char) (letter + 1));
  }

  public int getRow() {
    return row;
  }

  public char getLetter() {
    return letter;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Seat)) {
      return false;
    }
    Seat seat = (Seat) object;
    return row == seat.row && letter == seat.letter;
  }

  @Override
  public int hashCode() {
    return (int) (Math.pow(row, 2) * Math.pow(3, letter));
  }

  @Override
  public String toString() {
    return row + "" + letter;
  }

}