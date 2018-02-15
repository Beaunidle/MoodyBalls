/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.awt.*;

/**
 *
 * @author Nathan
 */
/**
 * The endspot is the goal in each level. If the player reaches this then the level 
 * is complete.
 */
public class EndSpot
{
    public Point pos;
    public int xPos, yPos, xRad = 10, yRad = 10;
    public Rectangle rect;

    public EndSpot(int xPosition, int yPosition)
    {
        pos = new Point();
        pos.x = xPosition;        
        pos.y = yPosition;
        xPos = pos.x;
        yPos = pos.y;
        rect = new Rectangle(pos.x, pos.y, xRad, yRad);
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.white);
        g.fillOval(xPos, yPos, xRad, yRad);
    }

    public Point getPos()
    {
        return pos;
    }

    public int getX()
    {
        return pos.x;
    }

    public int getY()
    {
        return pos.y;
    }

    public int getRad()
    {
        return 20;
    }
    
    public Rectangle getRect()
    {
        return rect;
    }
}
