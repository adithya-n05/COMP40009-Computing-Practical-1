package aeroplane;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Seat {

	private String rowLetter;
	private int row;
	private char letter;
	public final static Set<Integer> EMERGENCY_EXIT_ROWS = new HashSet<>();
	public final static int TOTAL_NUMBER_OF_ROWS = 50;
  public final static char BIGGEST_LETTER = 'F';
  public final static char SMALLEST_LETTER = 'A';
  public final static int CREW_ROW = 1;
  public final static int BUSINESS_FIRST_ROW = 2;
  public final static int BUSINESS_LAST_ROW = 15;
  public final static int ECONOMY_FIRST_ROW = 16;
  public final static int ECONOMY_LAST_ROW = 50;

  public Seat(int row, char letter) {
    assert (letter >= SMALLEST_LETTER && letter <= BIGGEST_LETTER): "Error";
    this.rowLetter = "" + row + letter;
    this.letter = letter;
    this.row = row;
    EMERGENCY_EXIT_ROWS.addAll(Arrays.asList(1, 30, 10));
  }

  public boolean isEmergencyExit() {
    return EMERGENCY_EXIT_ROWS.contains(row);
  }

  public boolean hasNext() {
    return ((row < TOTAL_NUMBER_OF_ROWS) || (row == TOTAL_NUMBER_OF_ROWS) && (letter < BIGGEST_LETTER));
  }

  public Seat next() {

    if (hasNext()) {
      if (letter == BIGGEST_LETTER) {
        return new Seat(row + 1, 'A');
      } else {
        return new Seat(row, (char) (letter + 1));
      }
    } else {
      throw new java.util.NoSuchElementException();
    }

  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Seat)) {
      return false;
    } else {
      Seat otherSeat = (Seat) other;
      return (row == otherSeat.row) && (letter == otherSeat.letter);
    }
  }

  @Override
  public int hashCode() {
    return (int) Math.pow(row, (int) letter);
  }


}
