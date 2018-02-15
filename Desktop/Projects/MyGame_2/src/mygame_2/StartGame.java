/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import static java.awt.Font.SERIF;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Nathan
 */
//The title screen for the game.
public class StartGame extends JPanel 
{
    
        private final JPanel textPanel;
        private final JPanel buttons;
        private final JTextArea startTheGame;
        private final JTextPane instructions;
        public static JButton start, exit;
        

        public StartGame(int width, int height)
        {
            setSize(width, height);
            setLayout(new GridLayout(3,1));
            setBackground(Color.GREEN);

            textPanel = new JPanel();
            textPanel.setSize(width, height / 2);
            textPanel.setBackground(Color.GREEN);
            textPanel.setLayout(new FlowLayout());
            
            startTheGame = new JTextArea("MOODYBALLS!");
            startTheGame.setFont(new Font(SERIF, 2, 106));
            //startTheGame.setSize(textPanel.getWidth()/4, textPanel.getHeight()/4);
            startTheGame.setBackground(Color.GREEN);
            //startTheGame.setLineWrap(true);
            //startTheGame.setLocation(200, 500);
            startTheGame.setBounds(textPanel.getWidth()/2-200, 200, 300, 50);
            //startTheGame.setLayout(new BorderLayout());
            textPanel.add(startTheGame, BorderLayout.SOUTH);

            add(textPanel, BorderLayout.NORTH);

            buttons = new JPanel();
            buttons.setLayout(new FlowLayout());
            buttons.setSize(width, height / 2);
            buttons.setBackground(Color.red);
            start = new JButton("Start");
            exit = new JButton("Exit");
            buttons.add(start);
            buttons.add(exit);
            add(buttons, BorderLayout.CENTER);
            
            instructions = new JTextPane();
            
            SimpleAttributeSet attribs = new SimpleAttributeSet();  
            StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);
            StyleConstants.setFontSize(attribs, 24);
            instructions.setParagraphAttributes(attribs,true);
            String text = ("How to play:\n"
                    + "Escape the moody orange balls and find the portal to progress to the next level\n"
                    + "Use arrow keys to move left and right and space bar to jump\n"
                    + "Complete all ten levels to beat the game\n");
            instructions.setText(text);
            //startTheGame.setSize(textPanel.getWidth()/4, textPanel.getHeight()/4);
            instructions.setBackground(Color.yellow);
            //startTheGame.setLineWrap(true);
            //startTheGame.setLocation(200, 500);
            instructions.setBounds(textPanel.getWidth()/2-200, 200, 300, 50);
            //startTheGame.setLayout(new BorderLayout());
            add(instructions, BorderLayout.SOUTH);
        }            
        
        public void addAL(ActionListener al)
        {
            start.addActionListener(al);
            exit.addActionListener(al);
        }
}

