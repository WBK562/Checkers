package Warcaby;

import javax.swing.*;
import java.awt.*;

public class EndScreen extends JFrame {
    private final String endTitle = "Koniec gry!";

    public EndScreen(String winnerName, WarcabyBoard oldBoard) {
        setTitle(endTitle);
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel message = new JLabel("Wygrał gracz: " + winnerName, SwingConstants.CENTER);
        message.setFont(new Font("Arial", Font.BOLD, 20));

        JButton exitButton = new JButton("Zakończ");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 16));
        exitButton.addActionListener(e -> System.exit(0));

        JButton againButton = new JButton("Rewanż");
        againButton.setFont(new Font("Arial", Font.PLAIN, 16));
        againButton.addActionListener(e -> {
            oldBoard.dispose();
            new WarcabyBoard().setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(againButton);
        buttonPanel.add(exitButton);

        setLayout(new BorderLayout());
        add(message, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
