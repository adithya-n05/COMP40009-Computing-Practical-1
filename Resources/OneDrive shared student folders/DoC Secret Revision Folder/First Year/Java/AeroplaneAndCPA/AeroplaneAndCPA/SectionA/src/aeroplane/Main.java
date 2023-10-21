package aeroplane;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {
	
	public static void main(String[] args) throws AeroplaneFullException {
		
		if(args.length != 1) {
			System.out.println("Program should be invoked with exactly one argument, the name of the data file");
			System.exit(1);
		}
		
		SeatAllocator allocator = new SeatAllocator();
		
		try {
			allocator.allocate(args[0]);
		} catch (IOException e) {
			System.out.println("An IO exception occurred");
			System.exit(1);
		} catch (AeroplaneFullException e) {
			System.out.println("Unable to allocate all passengers");
		}

		System.out.println(allocator);

		allocator.upgrade();

		Set<Passenger> passengers = new HashSet<>();
		passengers.add(new EconomyClass("Hang","Li Li", 17));
		passengers.add(new EconomyClass("Hang","Li Li", 17));
		passengers.add(new Crew("Hang","Li Li"));
    passengers.add(new Crew("Hang","Li Li"));
    passengers.add(new Crew("Hang","Ling Li"));
    passengers.add(new Crew("Hang","Li"));


		System.out.println(countAdults(passengers));
		
	}

	public static int countAdults(Set<? extends Passenger> passengers) {

		int count = 0;

		for (Passenger passenger : passengers) {
			if (passenger.isAdult()) {
				count++;
			}
		}

		return count;
	}
	
}
