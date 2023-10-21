package tennis;

public interface ScoreObserver {
    void updateDisplay(String score);

    void endGame();
}
