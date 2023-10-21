package aeroplane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SeatAllocator {

  private static final String CREW = "crew";
  private static final String BUSINESS = "business";
  private static final String ECONOMY = "economy";
  private Map<Seat, Passenger> allocation;

  public SeatAllocator() {
    allocation = new HashMap<>();
  }

  private static String readStringValue(BufferedReader br)
      throws MalformedDataException, IOException {
    String result = br.readLine();
    if (result == null) {
      throw new MalformedDataException();
    }
    return result;
  }

  private static int readIntValue(BufferedReader br)
      throws MalformedDataException, IOException {
    try {
      return Integer.parseInt(readStringValue(br));
    } catch (NumberFormatException e) {
      throw new MalformedDataException();
    }
  }

  private static Luxury readLuxuryValue(BufferedReader br)
      throws MalformedDataException, IOException {
    try {
      return Luxury.valueOf(readStringValue(br));
    } catch (IllegalArgumentException e) {
      throw new MalformedDataException();
    }
  }

  @Override
  public String toString() {
    return allocation.toString();
  }

  private void allocateInRange(Passenger passenger,
      Seat first, Seat last)
      throws AeroplaneFullException, MalformedDataException {
    Seat seat = first;
    while (allocation.containsKey(seat)
        || !passenger.isAdult() && seat.isEmergencyExit()) {
      if (seat.equals(last) || !seat.hasNext()) {
        throw new AeroplaneFullException();
      }
      seat = seat.next();
    }
    allocation.put(seat, passenger);
  }

  public void allocate(String filename)
      throws IOException, AeroplaneFullException {
    BufferedReader br = new BufferedReader(new FileReader(filename));
    String line;
    while ((line = br.readLine()) != null) {
      try {
        if (line.equals(CREW)) {
          allocateCrew(br);
        } else if (line.equals(BUSINESS)) {
          allocateBusiness(br);
        } else if (line.equals(ECONOMY)) {
          allocateEconomy(br);
        } else {
          throw new MalformedDataException();
        }
      } catch (MalformedDataException e) {
        System.out.println("Skipping malformed line of input");
      }
    }
  }

  private void allocateCrew(BufferedReader br)
      throws IOException, MalformedDataException, AeroplaneFullException {
    String firstName = readStringValue(br);
    String lastName = readStringValue(br);
    allocateInRange(
        new CrewMember(firstName, lastName),
        new Seat(1, 'A'), new Seat(1, 'F')
    );
  }

  private void allocateBusiness(BufferedReader br)
      throws IOException, MalformedDataException, AeroplaneFullException {
    String firstName = readStringValue(br);
    String lastName = readStringValue(br);
    int age = readIntValue(br);
    Luxury luxury = readLuxuryValue(br);
    allocateInRange(
        new BusinessClassPassenger(firstName, lastName, age, luxury),
        new Seat(2, 'A'), new Seat(15, 'F')
    );
  }

  private void allocateEconomy(BufferedReader br)
      throws IOException, MalformedDataException, AeroplaneFullException {
    String firstName = readStringValue(br);
    String lastName = readStringValue(br);
    int age = readIntValue(br);
    allocateInRange(
        new EconomyClassPassenger(firstName, lastName, age),
        new Seat(16, 'A'), new Seat(50, 'F')
    );
  }

  public void upgrade() throws MalformedDataException {
    List<Seat> seats = allocation.keySet().stream()
        .filter(seat -> seat.getRow() > 15 && seat.getRow() <= 50)
        .collect(Collectors.toList());
    seats.sort(
        Comparator.comparing(Seat::getRow).thenComparing(Seat::getLetter)
    );
    for (Seat seat : seats) {
      try {
        allocateInRange(allocation.get(seat),
            new Seat(2, 'A'), new Seat(15, 'F'));
      } catch (AeroplaneFullException exception) {
        break;
      }
      allocation.remove(seat);
    }
  }

}