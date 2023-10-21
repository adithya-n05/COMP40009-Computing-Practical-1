package Q3;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class BoxOfficeTest {

  static final Show LION_KING =
      new Show("The Lion King", 44.00);

  static final Show HAMILTON =
      new Show("Hamilton", 80.00);

  static final Customer SALLY = new Customer("Sally Davies");
  static final Customer TOM = new Customer("Thomas Williams");

  // write your tests here

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  Theatre theatre = context.mock(Theatre.class);
  Payments payments = context.mock(Payments.class);
  Waitlist waitlist = context.mock(Waitlist.class);

  private final BoxOffice office = new BoxOffice(theatre, payments, waitlist);


  @Test
  public void canBookAvailableShow(){

    context.checking(new Expectations(){{
      oneOf(theatre).checkAvailable(LION_KING, 4); will(returnValue(true));
      oneOf(payments).pay(LION_KING.price()*4, SALLY);
      oneOf(theatre).confirm(LION_KING, 4);

    }});

    office.bookTickets(LION_KING, 4, SALLY);

  }

  @Test
  public void bookingUnavailableShowAddsToWaitlist(){

    context.checking(new Expectations(){{
      oneOf(theatre).checkAvailable(HAMILTON, 2); will(returnValue(false));
      oneOf(waitlist).add(TOM, HAMILTON, 2);

    }});

    office.bookTickets(HAMILTON, 2, TOM);

  }

  @Test
  public void returningTicketsChecksWaitlist(){

    context.checking(new Expectations(){{
      oneOf(waitlist).anyoneWaiting(HAMILTON, 4);
      oneOf(payments).pay(HAMILTON.price()*4, TOM);

    }});

    office.returnTickets(HAMILTON,4);
    office.bookTickets(TOM, HAMILTON, 4);

  }



}
