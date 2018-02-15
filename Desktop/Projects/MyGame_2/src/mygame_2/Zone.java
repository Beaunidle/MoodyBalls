/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.Rectangle;
/**
 *
 * @author Nathan
 */
//Zones are used by the evilBall to help it decide what it needs to do.
public class Zone 
{
    private int xPos, yPos, size;
    private Rectangle rect;
    
    public Zone(int x, int y, int sizes)
    {
        xPos = x;
        yPos = y;
        size = sizes;
        rect = new Rectangle(xPos, yPos, size, size);
    }
    
    public Rectangle getRect()
    {
        return rect;
    }
}
