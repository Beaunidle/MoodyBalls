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
 * Represents the different kinds of platform in the game. These are platforms, walls
 * and drops.
 */
public class Platform
{
    private final int WIDTH;
    private final int HEIGHT;
    private final int xPos;
    private final int yPos;
    private final Rectangle rect;
    private final Image img;
    private final PlatformType type;
    
    public Platform(int setX, int setY, int width, int height, PlatformType platformType, Image i)
    {        
        WIDTH = width;
        HEIGHT = height;
        type = platformType;
                
        xPos = setX;
        yPos = setY;
        rect = new Rectangle(xPos, yPos - 1, WIDTH, HEIGHT + 1);
        img = i;
    }

    public int getX()
    {
        return xPos;
    }

    public int getY()
    {
        return yPos;
    }

   public int getWidth()
    {
        return WIDTH;
    }

    public int getHeight()
    {
        return HEIGHT;
    }

    public PlatformType getType()
    {
        return type;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, xPos, yPos, WIDTH, HEIGHT, null);
    }

    @Override
    public String toString()
    {
        return (xPos + "," + yPos + "," + WIDTH + "," + HEIGHT + "," + type);
    }

}
