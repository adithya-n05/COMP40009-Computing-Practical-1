package tennis;

import javax.swing.*;

public class TennisScorer implements ScoreObserver {

  private JTextField scoreDisplay;
  private TennisModel model = new TennisModel(this);
    private JButton playerOneScores;
    private JButton playerTwoScores;

    public static void main(String[] args) {
    new TennisScorer().display();
  }

  @Override
  public void updateDisplay(String score) {
    scoreDisplay.setText(score);
  }

  @Override
  public void endGame(){
      playerOneScores.setEnabled(false);
      playerTwoScores.setEnabled(false);
  }

  private void display() {

    JFrame window = new JFrame("Tennis");
    window.setSize(400, 150);

      playerOneScores = new JButton("Player One Scores");
      playerTwoScores = new JButton("Player Two Scores");

    scoreDisplay = new JTextField(20);
    scoreDisplay.setHorizontalAlignment(JTextField.CENTER);
    scoreDisplay.setEditable(false);

    playerOneScores.addActionListener(
            e -> {
              model.playerOneWinsPoint();
            });

    playerTwoScores.addActionListener(
            e -> {
              model.playerTwoWinsPoint();
            });

    JPanel panel = new JPanel();
    panel.add(playerOneScores);
    panel.add(playerTwoScores);
    panel.add(scoreDisplay);

    window.add(panel);

    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    window.setVisible(true);

  }

}