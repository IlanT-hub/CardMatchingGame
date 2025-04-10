import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardMatchingGame {
    private static final String IMAGE_DIR = "src/card/";
    private static final String CARD_BACK = IMAGE_DIR + "b1fv.png";
    private static final int PAIRS = 5;
    private static final int TOTAL_CARDS = PAIRS * 2;
    private static final int ROWS = 2;
    private static final int COLS = 5;

    private List<JButton> cardButtons;
    private List<String> cardImages;

    private JButton firstCard = null;
    private JButton secondCard = null;
    private boolean canClick = true;

    private int tries = 0;
    private int matchedPairs = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CardMatchingGame::new);
    }

    public CardMatchingGame() {
        JFrame frame = new JFrame("Card Matching Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new GridLayout(ROWS, COLS));
        cardButtons = new ArrayList<>();
        cardImages = generateCardImages();

        for (int i = 0; i < TOTAL_CARDS; i++) {
            JButton card = createCardButton(i);
            cardButtons.add(card);
            frame.add(card);
        }
        frame.setVisible(true);
    }

    private List<String> generateCardImages() {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < PAIRS; i++) {
            String imagePath = IMAGE_DIR + i + ".png";
            images.add(imagePath);
            images.add(imagePath);
        }
        Collections.shuffle(images);
        return images;
    }

    private JButton createCardButton(int index) {
        JButton button = new JButton();
        setCardBack(button);
        button.putClientProperty("index", index);
        button.addActionListener(this::handleCardClick);
        return button;
    }

    private void handleCardClick(ActionEvent e) {
        if (!canClick) return;

        JButton clickedCard = (JButton) e.getSource();
        int index = (int) clickedCard.getClientProperty("index");

        if (clickedCard == firstCard) return;

        setCardFront(clickedCard, cardImages.get(index));

        if (firstCard == null) {
            firstCard = clickedCard;
        } else {
            secondCard = clickedCard;
            canClick = false;
            tries++;
            checkForMatch();
        }
    }

    private void checkForMatch() {
        String firstImage = (String) firstCard.getClientProperty("image");
        String secondImage = (String) secondCard.getClientProperty("image");

        if (firstImage.equals(secondImage)) {
            firstCard.setEnabled(false);
            secondCard.setEnabled(false);
            matchedPairs++;

            if (matchedPairs == PAIRS) {
                showCompletionMessage();
            }

            resetTurn();
        } else {

            Timer timer = new Timer(1000, e -> {
                setCardBack(firstCard);
                setCardBack(secondCard);
                resetTurn();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void resetTurn() {
        firstCard = null;
        secondCard = null;
        canClick = true;
    }

    private void setCardBack(JButton button) {
        ImageIcon backImage = new ImageIcon(CARD_BACK);
        Image scaledBack = backImage.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledBack));
        button.putClientProperty("image", null);
    }

    private void setCardFront(JButton button, String imagePath) {
        ImageIcon cardImage = new ImageIcon(imagePath);
        Image scaledImage = cardImage.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImage));
        button.putClientProperty("image", imagePath);
    }

    private void showCompletionMessage() {
        JOptionPane.showMessageDialog(null,
                "Congratulations! You completed Ilan's card game in " + tries + " tries!",
                "Game Completed",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
