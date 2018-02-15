/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author Nathan
 */
//Parent class for all types of ball in the game.
public class MoveableObject 
{
    private final int RADIUS = 10;
    //public Point pos;
    public double xMoment = 0, yMoment = 0;
    public Color color;
    private int jumpCount;
    private boolean jump, leftPressed, rightPressed, upPressed, platformToLeft, 
            platformToRight, onAPlatform;
    public Rectangle rect;
    private int maxSpeed;
    private final Set<Platform> localPlatforms;
    private final Set<MoveableObject> localObjects;
    private final Set<Zone> zones;


    public MoveableObject(int xPos, int yPos, Color colour, int max, Set<Zone> zoneSet)
    {
        color = colour;
        maxSpeed = max;
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        platformToLeft = false;
        platformToRight = false;
        onAPlatform = false;
        rect = new Rectangle(xPos, yPos, RADIUS, RADIUS);
        localPlatforms = new HashSet<Platform>();
        localObjects = new HashSet<MoveableObject>();
        zones = zoneSet;
    }

    public final int getX()
    {
        return (int)rect.getX();
    }

    public final void setX(int x)
    {
        rect.x = x;
    }

    public final int getY()
    {
        return (int)rect.getY();
    }

    public final void setY(int y)
    {
        rect.y = y;
    }

    public double getXMoment()
    {
        return xMoment;
    }

    public void setXMoment(double x)
    {
        xMoment = x;
    }

    public double getYMoment()
    {
        return yMoment;
    }

    public void setYMoment(double y)
    {
        yMoment = y;
    }
    
    public void setLeftPressed(boolean set)
    {
        leftPressed = set;
    }

    public void setRightPressed(boolean set)
    {
        rightPressed = set;
    }

    public void setUpPressed(boolean set)
    {
        upPressed = set;
    }

    public Rectangle getRect()
    {
        return rect;
    }

    public int getRad()
    {
        return RADIUS;
    }


    public void setMaxSpeed(int speed)
    {
        maxSpeed = speed;
    }

    public int getMaxSpeed()
    {
        return maxSpeed;
    }
    public boolean getPlatformToLeft()
    {
        return platformToLeft;
    }

    public boolean getPlatformToRight()
    {
        return platformToRight;
    }

    public void setPlatformToLeft(boolean test)
    {
        platformToLeft = test;
    }
    
    public void setPlatformToRight(boolean test)
    {
        platformToRight = test;
    }

    public boolean getOnAPlatform()
    {
        return onAPlatform;
    }
    
    public void setOnAPlatform(boolean test)
    {
        onAPlatform = test;
    }
    
    public void jump()
    {
       
        if (leftPressed == true && xMoment > -3)
        {
            xMoment = 0;
            xMoment = xMoment - 6;
        }
        if (rightPressed == true && xMoment < 3)
        {
            xMoment = 0;
            xMoment = xMoment + 6;
        }
        yMoment = -12;
       
    }

    public void draw(Graphics g)
    {
        g.setColor(color);
        g.fillOval(rect.x, rect.y, RADIUS, RADIUS);
    }


    public void updatePictureState()
    {
        //pos.translate((int)xMoment, (int)yMoment);
        rect.translate((int)xMoment, (int)yMoment);
    }

    public boolean getLeftPressed()
    {
        return leftPressed;
    }
    
    public boolean getRightPressed()
    {
        return rightPressed;
    }
    
    public boolean getUpPressed()
    {
        return upPressed;
    }        
    
    public void addPlatform(Platform platform)
    {
        localPlatforms.add(platform);
    }
    
    public void addObject (MoveableObject object)
    {
        localObjects.add(object);
    }
    
    public Set getLocalPlatforms()
    {
        return localPlatforms;
    }
    
    public Set getLoacalObjects()
    {
        return localObjects;
    }
    
    public void resetLocalArea()
    {
        localPlatforms.clear();
        localObjects.clear();
    }
    
    public Rectangle getZone()
    {
        for (Zone z : zones)
        {
            if (z.getRect().contains(new Point(10, getY())))
            {
                return z.getRect();
            }
        }
        return null;
    }
}
