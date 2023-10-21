package tennis;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;

public class TennisModelTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    ScoreObserver observer = context.mock((ScoreObserver.class));

    TennisModel model =  new TennisModel(observer);

    @Test
    public void scoreIsUpdatedIfPlayer1Scores(){

        context.checking(new Expectations() {{
            oneOf(observer).updateDisplay(with(containsString("15 - Love")));
        }});

        model.playerOneWinsPoint();
    }

    @Test
    public void scoreIsUpdatedIfPlayer2Scores(){

        context.checking(new Expectations() {{
            oneOf(observer).updateDisplay(with(containsString("Love - 15")));
        }});

        model.playerTwoWinsPoint();
    }

    @Test
    public void gameEndsIfPlayer1Scores4Times(){

        context.checking(new Expectations() {{
            ignoring(observer).updateDisplay(with(any(String.class)));
            oneOf(observer).endGame();
        }});

        model.playerOneWinsPoint();
        model.playerOneWinsPoint();
        model.playerOneWinsPoint();
        model.playerOneWinsPoint();

    }

    @Test
    public void scoreDisplaysAllIfBothPlayersEqual(){

        context.checking(new Expectations() {{
            oneOf(observer).updateDisplay(with(any(String.class)));
            oneOf(observer).updateDisplay(with(containsString("15 all")));
        }});

        model.playerOneWinsPoint();
        model.playerTwoWinsPoint();
    }

}