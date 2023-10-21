package tennis;

public class TennisModel {

    private final ScoreObserver scorer;

    public TennisModel(ScoreObserver scorer){
        this.scorer = scorer;
    }
    private int playerOneScore = 0;
    private int playerTwoScore = 0;

    private final String[] scoreNames = {"Love", "15", "30", "40"};

    private String score() {

        if (playerOneScore > 2 && playerTwoScore > 2) {
            int difference = playerOneScore - playerTwoScore;
            switch (difference) {
                case 0:
                    return "Deuce";
                case 1:
                    return "Advantage Player 1";
                case -1:
                    return "Advantage Player 2";
                case 2:
                    scorer.endGame();
                    return "Game Player 1";
                case -2:
                    scorer.endGame();
                    return "Game Player 2";
            }
        }

        if (playerOneScore > 3) {
            scorer.endGame();
            return "Game Player 1";
        }
        if (playerTwoScore > 3) {
            scorer.endGame();
            return "Game Player 2";
        }
        if (playerOneScore == playerTwoScore) {
            return scoreNames[playerOneScore] + " all";
        }
        return scoreNames[playerOneScore] + " - " + scoreNames[playerTwoScore];

    }

    public void playerOneWinsPoint() {
        playerOneScore++;
        scorer.updateDisplay(score());
    }

    public void playerTwoWinsPoint() {
        playerTwoScore++;
        scorer.updateDisplay(score());
    }
}
