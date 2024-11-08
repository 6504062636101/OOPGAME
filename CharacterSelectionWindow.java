package com.mycompany.gameproject;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CharacterSelectionWindow extends JPanel {

    private final Image backgroundImage;
    ImageIcon buttonIconknight = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/KnigtLOGO.png"));
    ImageIcon buttonIconmage = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/mageLOGO.png"));

    public CharacterSelectionWindow() {
        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/com/mycompany/gameproject/origbig.png")).getImage();

        // Load the custom font for the title label
        Font customFont = loadCustomFont("E:\\GameProject\\src\\main\\java\\com\\mycompany\\gameproject\\fonts\\PressStart2P.ttf", 24f);

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        JLabel titleLabel = new JLabel("Select Your Character", SwingConstants.CENTER);
        titleLabel.setFont(customFont);
        add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Create knight panel with button and label
        JPanel knightPanel = createCharacterPanel("Knight");

        // Create mage panel with button and label
        JPanel magePanel = createCharacterPanel("Mage");

        buttonPanel.setBackground(new Color(0, 0, 0, 0));
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 10));
        buttonPanel.add(knightPanel);
        buttonPanel.add(magePanel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Font loadCustomFont(String path, float size) {
        Font customFont = null;
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customFont;
    }

    private JPanel createCharacterPanel(String characterName) {
        JPanel characterPanel = new JPanel();
        characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.Y_AXIS));

        JLabel characterLabel = new JLabel();
        JButton characterButton;
        if (characterName.equals("Knight")) {
            characterButton = new JButton(buttonIconknight); // Knight button uses knight icon
        } else {
            characterButton = new JButton(buttonIconmage); // Mage button uses mage icon
        }

        characterButton.setContentAreaFilled(false);
        characterButton.setBorderPainted(false);
        characterButton.setFocusPainted(false);
        Dimension buttonSize = new Dimension(150, 50);
        characterButton.setPreferredSize(buttonSize);

        characterButton.setHorizontalTextPosition(SwingConstants.CENTER);
        characterButton.setVerticalTextPosition(SwingConstants.BOTTOM);

        characterButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, characterName + " Selected!");

            // Close the current window (Character Selection Window)
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            parentFrame.dispose();

            // Open the corresponding battle window
            if (characterName.equals("Knight")) {
                openKnightBattle();
            } else if (characterName.equals("Mage")) {
                openMageBattle();
            }
        });

        characterPanel.add(characterLabel);
        characterPanel.add(characterButton);

        return characterPanel;
    }

    // Open Knight Battle window
    private void openKnightBattle() {
        JFrame battleFrame = new JFrame("Knight Battle");
        battleFrame.setSize(800, 600);
        battleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the KnightBattle JPanel
        KnightBattle knightBattleWindow = new KnightBattle(2000);
        battleFrame.add(knightBattleWindow); // Add the JPanel to the JFrame

        battleFrame.setVisible(true);  // Make the JFrame visible
    }

    // Open Mage Battle window
    private void openMageBattle() {
        JFrame battleFrame = new JFrame("Mage Battle");
        battleFrame.setSize(800, 600);
        battleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the MageBattle JPanel
        MageBattle mageBattleWindow = new MageBattle(2000);
        battleFrame.add(mageBattleWindow); // Add the JPanel to the JFrame

        battleFrame.setVisible(true);  // Make the JFrame visible
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Character Selection");
        CharacterSelectionWindow panel = new CharacterSelectionWindow();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel); // Add JPanel to JFrame
        frame.pack(); // Automatically set size based on JPanel's preferred size
        frame.setVisible(true);
    }
}
