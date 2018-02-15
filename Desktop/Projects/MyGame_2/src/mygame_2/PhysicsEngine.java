/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.util.*;

/**
 *
 * @author Nathan
 */
//This class is responsible for handling all the physics in the game
public class PhysicsEngine
{
    
    private Set<Platform> obstructions;

    public PhysicsEngine()
    {
        obstructions = new HashSet<Platform>();
        
    }

    public void updatePhysicsState(MoveableObject myBall)
        {
            obstructions = myBall.getLocalPlatforms();
            
            //set onAPlatform to true for ball if it is on a platform. False otherwise
            myBall.setOnAPlatform(onaPlatform(myBall));

            //set platformToRight to true for ball if it has a platform immediately to the right. False otherwise
            myBall.setPlatformToRight(platformToRight(myBall));

            //set platformToLeft to true for ball if it has a platform immediately to the left. False otherwise
            myBall.setPlatformToLeft(platformToLeft(myBall));
            
            
            
            //makes ball fall if not ont a platform, to a maximum speed of 8
            if (!myBall.getOnAPlatform() && myBall.getYMoment() < 9)
            {
                myBall.setYMoment(myBall.getYMoment() + 1);
                if (myBall.getXMoment() > 0)
                {
                    myBall.setXMoment(myBall.getXMoment() - (int)0.5);                    
                }
                if (myBall.getXMoment() < 0)
                {
                    myBall.setXMoment(myBall.getXMoment() + (int)0.5);
                }
            }
            
            
            //stop a rising ball if it hits a platform
            if (platformAbove(myBall))
            {
                myBall.setYMoment(0);
            }            
            
            //moment when falling ball hits a platform. Have not implemented a bounce
            //as this effects subsequent jumps until bounce is finished
            if (myBall.getOnAPlatform() && myBall.yMoment > 0)
            {
                myBall.setYMoment(0);
            }

            //moment when ball rolls into platform to the right
            if (myBall.getPlatformToRight() && (myBall.getXMoment() > 0))
            {
                    myBall.setXMoment(0 - (myBall.getXMoment() * 0.75));
                    
            }

            //moment when ball rolls into platform to the left
            if (myBall.getPlatformToLeft() && (myBall.getXMoment() < 0))
            {
                    myBall.setXMoment(0 - (int)(myBall.getXMoment() * 0.75));
                    
            }

            //the next two methods slow the ball down after a direction key is released
            if (myBall.getXMoment() < 0 && myBall.getLeftPressed() == false)
            {
                myBall.setXMoment(myBall.getXMoment() + 0.5);
            }
            if (myBall.getXMoment() > 0 &&  myBall.getRightPressed() == false)
            {
                myBall.setXMoment(myBall.getXMoment() - 0.5);
            }

            //increases the ball's left moment if the left key is pressed.
            if (myBall.getLeftPressed() == true && 
                    myBall.getXMoment() > -myBall.getMaxSpeed() && !myBall.getPlatformToLeft())
            {
                if (myBall.getOnAPlatform())
                {
                    myBall.setXMoment(myBall.getXMoment() - 2);
                }
                else
                {
                    myBall.setXMoment(myBall.getXMoment() - 0.5);
                }
                
            }
            
            //increases the ball's right moment if the right key is pressed.
            if ( myBall.getRightPressed() == true && 
                    myBall.getXMoment() < myBall.getMaxSpeed() && !myBall.getPlatformToRight())
            {
                if (myBall.getOnAPlatform())
                {
                    myBall.setXMoment(myBall.getXMoment() + 2);
                }
                else
                {
                    myBall.setXMoment(myBall.getXMoment() + 0.5);
                }
            }
                        
            //performs a jump if the ball is on a platform and up is pressed.
            if (myBall.getOnAPlatform() && myBall.getUpPressed() == true && myBall.getYMoment() == 0)
            {               
                myBall.jump();
            }

            myBall.updatePictureState();
        }

    //checks if the ball is on a platform
    public boolean onaPlatform(MoveableObject myBall)
        {
            
            for(Platform a: obstructions)
            {
                if(myBall.getRect().intersects(a.getRect()) && myBall.getY() < a.getY() + 3 )
                {
                    if (myBall.getYMoment() >= 0)
                    {
                        myBall.setY(a.getY() - myBall.getRad() );
                        return true;
                    }                    
                }
            }
            return false;
        }

    //checks if the ball is touching a platform above it.
    public boolean platformAbove(MoveableObject myBall)
        {
            for(Platform a: obstructions)
            {
                if(myBall.getRect().intersects(a.getRect()) && myBall.getY() > a.getY())
                {
                    if (myBall.getYMoment() < 0)
                    {
                        myBall.setY(a.getY() + a.getHeight());
                        return true;
                    }
                }
            }
            return false;
        }

    //checks if there is a platform immediately to the ball's left
    public boolean platformToLeft(MoveableObject myBall)
        {

            for(Platform a: obstructions)
            {
                if (myBall.getX() <= a.getX() + a.getWidth() && myBall.getX() + myBall.getRad() > a.getX() + a.getWidth())
                {
                    if(myBall.getY()  < a.getY() + a.getHeight() && myBall.getY() + 10 > a.getY())
                    {
                        myBall.setX(a.getX() + a.getWidth());
                        return true;
                    }
                }
            }
            return false;
        }

    //checks if there is a platform immediately to the ball's right.
    public boolean platformToRight(MoveableObject myBall)
        {
            for (Platform a: obstructions)
            {
                if (myBall.getX() + myBall.getRad() >= a.getX() && myBall.getX() < a.getX())
                {
                    if(myBall.getY()  < a.getY() + a.getHeight() && myBall.getY() + 10 > a.getY())
                    {
                        myBall.setX(a.getX() - myBall.getRad());
                        return true;
                    }
                }
            }
            return false;
        }

}
