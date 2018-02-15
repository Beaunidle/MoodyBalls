/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author Nathan
 */
/**
 * This class draws the game window
 */
public class DrawingPanel extends JPanel
{
    private final Image img;
    private Set<Platform> drawSet;
    private Set<MoveableObject> balls;
    private EndSpot end;
    private final int width, height;
    private int lives;
    
    public DrawingPanel(int width1, int height1, Image image)
    {                
        width = width1;
        height = height1;
        setSize(width, height);
        setFocusable(true);
        this.setBackground(Color.CYAN);
        
        img = image;                     
        drawSet = new HashSet();
        balls = new HashSet();        
    }
    
    //draws all objects within the game window when the end spot is not in view.
    public void draw(Set<Platform> platforms, Set<MoveableObject> moveables, int lives1)
    {
        lives = lives1;
        drawSet.clear();
        drawSet = platforms;
        balls.clear();
        balls = moveables;
        end = null;
        repaint();
        
    }
    
    //draws all objects within the game window when the end spot is in view.
    public void draw(Set<Platform> platforms, Set<MoveableObject> moveables, EndSpot e, int lives1)
    {
        lives = lives1;
        drawSet.clear();
        drawSet = platforms;
        balls.clear();
        balls = moveables;
        end = e;
        repaint();
    }

    //draws the game window
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), getRootPane());
        for (Platform a : drawSet)
        {
            a.draw(g);
        }
        for (MoveableObject b : balls)
        {
            b.draw(g);
        }         
        if (end != null)
        {
            end.draw(g);
        }
        for (int i = lives; i > 0; i--)
        {
            g.setColor(Color.RED);
            g.fillOval(width - i*25, 20 + 1*10, 20, 20);
        }
    }            
}
