/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.awt.*;
import java.util.*;

/**
 *
 * @author Nathan
 */
/**
 * The enemies in the game. These objects use a rudimentary finite state engine
 * to track and follow the ball. Each Evil ball is aware of it's position, the
 * player's position and can use zones to determine if it needs to chase the player
 * or clime up or down the level.
 */
public class EvilBall extends MoveableObject
{
    PlayerBall myBall;
    private final Set<Platform> platforms, walls;
    private final Set<Platform> dropList;
    private int xPos, yPos, xRad, yRad;
    private final int startTime;
    private final Rectangle viewRect;
    //double temp;
    private BallState state;
    private Point target;
    private static Image img;

    public EvilBall(PlayerBall aBall, int xPosition, int yPosition, Color colour, 
            Set platformSet, Set drops, Set wallSet, int start, int max, Set<Zone> zoneSet, Image image)
    {
        super(xPosition, yPosition, colour, max, zoneSet);
        myBall = aBall;
        startTime = start;
        //the area that the evilball can 'see'.
        viewRect = new Rectangle(getX() - 110, getY() - 80, 220, 160);        
        platforms = platformSet;
        walls = wallSet;
        dropList = drops;
        state = BallState.FOLLOW;
        
        img = image;
    }


    //method to determine what the evilball should be doing
    public void updateEvilBallState()
    {
        //stops the ball trying to jump if it is in the air.
        if (!getOnAPlatform())
        {
            setUpPressed(false);
        }
                    
        if (myBall.getZone()!= null && getZone() != null)
        {
            if (getOnAPlatform())
            {
                setState();
            }
            //if myBall is in a different zone simply because it is jumping over the evil ball
            if (!myBall.getOnAPlatform() && myBall.getRect().intersects(viewRect)
                    && !myBall.getInjured())
            {
                state = BallState.FOLLOW;
            }
        }        
        //checks the satate of the evilball, helps it to decide what to do
        switch (getState()) 
        {
            case FOLLOW:
                //track the target
                if (getX() < myBall.getX())
                {
                    moveRight();
                }
                else if (getX() > myBall.getX())
                {
                    moveLeft();
                }
                //jump an obstructing wall
                Platform temp = wallApproaching(); //wallApproaching();
                if (temp != null)
                {                   
                    if (getDistance(temp) < getDistance(myBall))
                    {
                        setUpPressed(true);
                    }
                                        
                }
                //jump over an approaching drop unless the target is nearer
                temp = dropApproaching();
                if (temp != null)
                {
                    if (getDistance(temp) < getDistance(myBall))
                    {
                        setUpPressed(true);
                    }                    
                }
                //jump onto a platform in the same zone if the target is on it
                if (getY() > myBall.getY() + 45 && 
                        myBall.getRect().intersects(viewRect))
                {
                    setLeftPressed(false);
                    setRightPressed(false);
                    setUpPressed(true);
                }
                
                break;
            case DROP:
                //locate the nearest drop and track it
                Platform myDrop = nearestDrop();
                if (myDrop != null)
                {                
                    if (getX() < myDrop.getX())
                    {
                        moveRight();
                    }
                    if (getX() > myDrop.getX())
                    {
                        moveLeft();
                    }
                    //jump an obsturcting wall
                    if (wallApproaching() != null)
                    {
                        setUpPressed(true);
                    }
                }
                                
                break;
            case CLIME:
                //locate the nearest jump and track it
                Platform myJump = null;
                if (getOnAPlatform())
                {
                    myJump = nearestJump();
                }
                                
                if (myJump != null)
                {
                    if (getX() < myJump.getX())
                    {
                        moveRight();
                    }
                    if (getX() > myJump.getX())
                    {
                        moveLeft();
                    }
                    
                    if (getX() <= myJump.getX() + 3 && getX() >= myJump.getX() - 3)
                    {
                        setUpPressed(true);
                    }
                    //jump an obsturcting wall or approaching drop.
                    if (wallApproaching() != null || dropApproaching() != null)
                    {
                        setUpPressed(true);
                    }                    
                }
                
                break;
            case CELEBRATE:
                //causes the evilball to jump up and down if the player is hurt
                if (getOnAPlatform())
                {
                    setRightPressed(false);
                    setLeftPressed(false);
                    setUpPressed(true);
                }
                
                break;
            default:
                break;
        }
    }

    /** 
     * sets which state the evilball is in by comparing which zone it is in with which zone
     * the player is in.
     */ 
    private void setState()
    {
        int dif = (int)(getZone().getY() - myBall.getZone().getY());
        if (dif > 0)
        {
            state = BallState.CLIME;
        }
        else if (dif == 0)
        {
            state = BallState.FOLLOW;
        }
        else if (dif < 0)
        {
            state = BallState.DROP;
        }        
        
        if (myBall.getInjured())
        {
            state = BallState.CELEBRATE;
        }
    }
    
    //returns the state the evilball is currently in.
    private BallState getState()
    {
        return state;
    }
    
    //returns the time the evilball should appear in the level.
    public int getStart()
    {
        return startTime;
    }

    //returns the distance between the evilball and a particular platform or wall.
    public int getDistance(Platform p)
    {
        if (getX() < p.getX())
        {
            return p.getX() - getX() + getRad();
        }
        else 
        {
            return getX() - p.getX() + p.getWidth();
        }
    }
    
    //returns the distance between the evilball and the player.
    public int getDistance(MoveableObject b)
    {
        if (getX() < b.getX())
        {
            return b.getX() - getX() + getRad();
        }
        else
        {
            return getX() - b.getX() + b.getRad();
        }
    }
    
    //finds the nearest drop to the evilball.
    public Platform nearestDrop()
    {
        Platform nearestDrop = null;
        Set<Platform> drops = new HashSet();
        Rectangle thisZone = getZone();
        if (thisZone == null)
        {
            return null;
        }
        for (Platform d : dropList)
        {
            if ( d.getY() == thisZone.getY() + thisZone.getHeight())
            {
                drops.add(d);
            }

        }
                
        int testValue = 50000;
        for (Platform p : drops)
        {
            if (this.getDistance(p) < testValue)
            {
                nearestDrop = p;
                testValue = this.getDistance(p);
            }
        }
        return nearestDrop;
    }
    
    //finds the nearest drop in the zone above the evilball. This tells it where to jump.
    public Platform nearestJump()
    {
        Platform nearestJump = null;
        Set<Platform>jumps = new HashSet();
        Rectangle thisZone = getZone();
        if (thisZone == null)
        {
            return null;
        }
        for (Platform d : dropList)
        {
            if (d.getY() == thisZone.getY() - 20)
            {
                jumps.add(d);
            }
        }
        
        int testValue = 50000;
        for (Platform p : jumps)
        {
            if (this.getDistance(p) < testValue)
            {
                nearestJump = p;
                testValue = this.getDistance(p);
            }
        }
        return nearestJump;
    }
    
    //returns a wall if there is one obstructing the evilball.
    public Platform wallApproaching()
    {
        for (Platform a : walls)
        {
            if (a.getRect().intersects(viewRect))
            {
                if (((movingRight() && getDistance(a) < 60
                       && getX() < a.getX() && getY() > a.getY()
                       && getY() < a.getY() + a.getHeight() && getYMoment() == 0.0)
                   ||
                   (movingLeft() && getDistance(a) < 60
                       && getX() > a.getX() + a.getWidth() && getY() > a.getY()
                       && getY() < a.getY() + a.getHeight()&& getYMoment() == 0.0))
                   )
                {                   
                    return a;
                }
            }                             
        }
        return null;
    }
    
    //returns a drop if the evilball needs to jump over it.
    public Platform dropApproaching()
    {        
        for (Platform a : dropList)
        {
            if (a.getRect().intersects(viewRect))
            {
                if (((movingRight() && getDistance(a) < 25
                       && getX() < a.getX() && getY() < a.getY() 
                       && getY() > a.getY() - getRad() - 1 && getYMoment() == 0.0)
                   ||
                   (movingLeft() && getDistance(a) < 25
                       && getX() > a.getX()  && getY() < a.getY() 
                       && getY() > a.getY() - getRad() - 1 && getYMoment() == 0.0))
                   )
                {      
                    return a;
                }
            }                             
        }
        return null;
    }
    
    //returns true if evilball is moving left.
    private boolean movingLeft()
    {
        return getXMoment() < 0;
    }
    
    //retruns true if evilball is moving right.
    private boolean movingRight()
    {
        return getXMoment() > 0;
    }
    
    private void moveLeft()
    {
        setLeftPressed(true);
        setRightPressed(false);
    }
    
    private void moveRight()
    {
        setRightPressed(true);
        setLeftPressed(false);
    }
    
    public void setYRad(int y)
    {
        yRad = y;
    }
    
    public void setXRad(int x)
    {
        xRad = x;
    }
    
    public void setYPos(int y)
    {
        yPos = y;
    }
    
    public void setXPos(int x)
    {
        xPos = x;
    }
    
    @Override
    public void updatePictureState()
    {
        rect.translate((int)xMoment, (int)yMoment);
        viewRect.translate((int)xMoment, (int)yMoment);
    }
    
    @Override
    public void draw(Graphics g)
    {
        g.setColor(color);
        g.drawImage(img, xPos, yPos, xRad, yRad,null);
    }
}
