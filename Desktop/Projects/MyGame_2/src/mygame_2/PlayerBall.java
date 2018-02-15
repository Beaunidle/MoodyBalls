/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.awt.*;
import java.util.Set;

/**
 *
 * @author Nathan
 */
/**
 * This is the object that the player in the game controls.
 */
    public class PlayerBall extends MoveableObject
    {
        private final Point absolutePos;
        private int xRad;
        private int yRad;
        private boolean injured = false;
        Color lastColour;

    public PlayerBall(int xPos, int yPos, Color colour, int max, Set<Zone> zoneSet)
    {
        super(xPos, yPos, colour, max, zoneSet);
        absolutePos = new Point(0, 0);
        lastColour = colour;
    }

    //This is the position used by the camera to draw the player.
    public void setAbPos(int x, int y)
    {
        absolutePos.setLocation(x, y);
    }
    
    public Point getAbPos()
    {
        return absolutePos;
    }
    
    public void setXRad(int n)
    {
        xRad = n;
    }

    public void setYRad(int n)
    {
        yRad = n;
    }
    
    public boolean getInjured()
    {
        return injured;
    }
    
    public void setInjured(boolean isInjured)
    {
        injured = isInjured;
    }
    
    @Override
    public void updatePictureState()
    {
        rect.translate((int)xMoment, (int)yMoment);
    }
    
    @Override
    public void draw(Graphics g)
    {
        if(!injured)
        {
            g.setColor(color);
            g.fillOval(absolutePos.x, absolutePos.y, xRad, yRad);
        }
        else
        {
            if (lastColour.equals(color))
            {
                lastColour = Color.CYAN;
            }
            else
            {
                lastColour = color;
            }
            g.setColor(lastColour);
            g.fillOval(absolutePos.x, absolutePos.y, xRad, yRad);
        }
    }
}
