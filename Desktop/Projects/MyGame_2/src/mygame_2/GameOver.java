/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Nathan
 */
//Game over screen for the game
public class GameOver extends JPanel
{
    
        private final JPanel textPanel;
        private final JPanel buttons;
        private final JTextArea gameOver;
        public static JButton restart, exit;


        public GameOver(int width, int height)
        {
            setSize(width, height);
            setLayout(new GridLayout(2,1));

            textPanel = new JPanel();
            textPanel.setSize(width, height / 2);
            textPanel.setBackground(Color.red);
            textPanel.setLayout(new BorderLayout());
            gameOver = new JTextArea("Game over. Final score: " +
                        "\nPlay again?");
            textPanel.add(gameOver, BorderLayout.CENTER);
            add(textPanel);

            buttons = new JPanel();
            buttons.setSize(width, height / 2);
            buttons.setBackground(Color.red);
            restart = new JButton("Restart");
            exit = new JButton("Exit");
            buttons.add(restart);
            buttons.add(exit);
            add(buttons);
        }

        public void addAL(ActionListener al)
        {
            restart.addActionListener(al);
            exit.addActionListener(al);
        }
}
