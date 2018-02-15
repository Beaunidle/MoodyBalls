/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import static java.awt.Font.SERIF;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Nathan
 */
//LevelComplete screen to be displayed between levels.
public class LevelComplete extends JPanel 
{
    private final JTextArea wellDone;
    private int level;
        

    public LevelComplete(int width, int height)
    {
        setSize(width, height);
        setLayout(new FlowLayout());
        setBackground(Color.GREEN);
            
        wellDone = new JTextArea("Level complete");
        wellDone.setFont(new Font(SERIF, 2, 34));
        wellDone.setBackground(Color.GREEN);
        wellDone.setBounds(getWidth()/2-150, 200, 300, 50);
        wellDone.setLayout(new FlowLayout());
        add(wellDone);
        
    }            
        
    public void setLevel(int number)
    {
        level = number;
        wellDone.setText("Level " + level + " complete. Loading level " + (level + 1));
    }
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
}
