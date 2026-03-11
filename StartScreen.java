package Warcaby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class StartScreen extends JFrame {
    private final String title = "Warcaby";


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StartScreen().setVisible(true));
    }

    public StartScreen() {
        setTitle(title);
        setSize(400, 250);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Warcaby", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton startButton = new JButton("Zagraj");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener((ActionEvent e) -> {
            new WarcabyBoard().setVisible(true);
            dispose();
        });

        JButton exitButton = new JButton("Wyjdź");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.addActionListener((ActionEvent e) -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);
    }
}
