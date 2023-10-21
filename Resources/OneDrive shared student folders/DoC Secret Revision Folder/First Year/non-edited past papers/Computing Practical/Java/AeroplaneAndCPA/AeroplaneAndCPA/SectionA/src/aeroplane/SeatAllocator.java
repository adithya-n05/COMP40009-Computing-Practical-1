package aeroplane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SeatAllocator {

	private Map<Seat, Passenger> allocation;

	private static final String CREW = "crew";
	private static final String BUSINESS = "business";
	private static final String ECONOMY = "economy";
	
	public SeatAllocator() {
		allocation = new HashMap<Seat, Passenger>();
	}

	@Override
	public String toString() {
		return allocation.toString();
	}
	
	private void allocateInRange(Passenger passenger,
			Seat first, Seat last) throws AeroplaneFullException {

	  boolean added = false;
	  boolean isLast = first.equals(last);
    Seat current = first;

    while ((current.hasNext() || current.equals(last)) && !added && !isLast) {
      if ((!allocation.containsKey(current))) {
        if (!current.isEmergencyExit() || passenger.isAdult()) {
          allocation.put(current, passenger);
          added = true;
        }
      }
      if (current.equals(last)) {
        isLast = true;
      } else {
        current = current.next();
      }
    }

    if (!added) {
      throw new AeroplaneFullException();
    }

	}

	private static String readStringValue(BufferedReader br) throws MalformedDataException, IOException {

		String result = br.readLine();
		
		if(result == null) {
			throw new MalformedDataException();
		}
		
		return result;
		
	}

	private static int readIntValue(BufferedReader br)
			throws MalformedDataException, IOException {
		try {
			return Integer.parseInt(readStringValue(br));
		} catch(NumberFormatException e) {
			throw new MalformedDataException();
		}
	}

	private static Luxury readLuxuryValue(BufferedReader br)
			throws MalformedDataException, IOException {
		try {
			return Luxury.valueOf(readStringValue(br));
		} catch(IllegalArgumentException e) {
			throw new MalformedDataException();
		}
	}

	
	public void allocate(String filename) throws IOException, AeroplaneFullException {
		
		BufferedReader br = new BufferedReader(new FileReader(filename));

		String line;
		while((line = br.readLine()) != null) {
			try {
				if(line.equals(CREW)) {
					allocateCrew(br);
				} else if(line.equals(BUSINESS)) {
					allocateBusiness(br);
				} else if(line.equals(ECONOMY)) {
					allocateEconomy(br);
				} else {
					throw new MalformedDataException();
				}
			} catch(MalformedDataException e) {
				System.out.println("Skipping malformed line of input");
			}
		}
		
	}
	
	private void allocateCrew(BufferedReader br) throws IOException, MalformedDataException, AeroplaneFullException {
		String firstName = readStringValue(br);
		String lastName = readStringValue(br);
		Passenger crew = new Crew(firstName, lastName);

		allocateInRange(crew, new Seat(Seat.CREW_ROW, Seat.SMALLEST_LETTER), new Seat(Seat.CREW_ROW, Seat.BIGGEST_LETTER));
		//       create a crew member using firstName and lastName
		//       call allocateInRange with appropriate arguments
	}

	private void allocateBusiness(BufferedReader br) throws IOException, MalformedDataException, AeroplaneFullException {
		String firstName = readStringValue(br);
		String lastName = readStringValue(br);
		int age = readIntValue(br);
		Luxury luxury = readLuxuryValue(br);

		Passenger businessP = new BusinessClass(firstName, lastName, age, luxury);

		allocateInRange(businessP, new Seat(Seat.BUSINESS_FIRST_ROW, Seat.SMALLEST_LETTER),
        new Seat(Seat.BUSINESS_LAST_ROW, Seat.BIGGEST_LETTER));
		//       create a business class passenger using firstName, lastName, age and luxury
		//       call allocateInRange with appropriate arguments
	}

	private void allocateEconomy(BufferedReader br) throws IOException, MalformedDataException, AeroplaneFullException {
		String firstName = readStringValue(br);
		String lastName = readStringValue(br);
		int age = readIntValue(br);

    Passenger economyP = new EconomyClass(firstName, lastName, age);

    allocateInRange(economyP, new Seat(Seat.ECONOMY_FIRST_ROW, Seat.SMALLEST_LETTER),
        new Seat(Seat.ECONOMY_LAST_ROW, Seat.BIGGEST_LETTER));

		//       create an economy class passenger using firstName, lastName and age
		//       call allocateInRange with appropriate arguments
	}

	public void upgrade() throws AeroplaneFullException {

	  Seat current = new Seat(Seat.BUSINESS_FIRST_ROW, Seat.SMALLEST_LETTER);
	  Seat last = new Seat(Seat.BUSINESS_LAST_ROW, Seat.BIGGEST_LETTER);
	  boolean isLast = false;

	  while ((current.hasNext() || current.equals(last)) && !isLast && (firstOcuppiedEconomySeat() != null)) {
	    if (!isOccupiedBusinessSeats(current)) {
	      Seat fSeat = firstOcuppiedEconomySeat();
        allocateInRange(allocation.get(fSeat), current, last);
        allocation.remove(fSeat);
      }
      if (current.equals(last)) {
	      isLast = true;
      } else {
        current = current.next();
      }
    }

  }

  private boolean isOccupiedBusinessSeats(Seat seat) {
	  return allocation.containsKey(seat);
  }

  private Seat firstOcuppiedEconomySeat() {

	  boolean isFound = false;
	  Seat current = new Seat(Seat.ECONOMY_FIRST_ROW, Seat.SMALLEST_LETTER);
	  Seat last = new Seat(Seat.ECONOMY_LAST_ROW, Seat.BIGGEST_LETTER);
	  boolean isLast = false;

	  while (!isFound && (current.hasNext() || current.equals(last)) && !isLast) {
      if (allocation.containsKey(current)) {
        return current;
      }
      if (current.equals(last)) {
       isLast = true;
      } else {
        current = current.next();
      }
    }

    return null;
  }

}
