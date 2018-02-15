/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mygame_2;

import java.awt.Image;
import java.awt.Point;
import java.util.*;
/**
 *
 * @author Nathan
 */
/**
 * The camera class sets the location of the player ball to the middle of the screen and
 * then moves game view around it. At each frame it takes all objects within a certain
 * distance of the player, resizes them and returns the resized and repositioned objects
 * to the game controller to draw.
 */
public class Camera
{

    public PlayerBall ball;
    private int xRatio;
    private int yRatio;
    private final Image platformImg, wallImg;

    public Camera(int width, int height, PlayerBall aBall, Image pImg, Image wImg)
    {
        xRatio = width/400;
        yRatio = height/250;
        ball = aBall;
        ball.setAbPos((int)(width * 0.5), (int)(height * 0.66));
        ball.setXRad((int)(ball.getRad() * xRatio));
        ball.setYRad((int)(ball.getRad() * yRatio));
        xRatio = width/400;
        yRatio = height/250;
        platformImg = pImg;
        wallImg = wImg;

    }

    public Set<Platform> change(Set<Platform> platforms)
    {
        Set<Platform> viewSet = new HashSet<Platform>();
        Point abPos = ball.getAbPos();
        int viewWidth, viewHeight, viewXPos, viewYPos;
        for (Platform p : platforms) {
            if (null != p.getType()) {
                switch (p.getType()) {
                    case PLATFORM:{
                        viewWidth = p.getWidth() * xRatio;
                        viewHeight = p.getHeight() * yRatio;
                        viewXPos = (p.getX() - ball.getX()) * xRatio + abPos.x;
                        viewYPos = (p.getY() - ball.getY()) * yRatio + abPos.y;
                        viewSet.add(new Platform(viewXPos, viewYPos, viewWidth, viewHeight, p.getType(), platformImg));
                        break;
                    }
                    case WALL:{
                        viewWidth = p.getWidth() * xRatio;
                        viewHeight = p.getHeight() * yRatio;
                        viewXPos = (p.getX() - ball.getX()) * xRatio + abPos.x;
                        viewYPos = (p.getY() - ball.getY()) * yRatio + abPos.y;
                        viewSet.add(new Platform(viewXPos, viewYPos, viewWidth, viewHeight, p.getType(), wallImg));
                        break;
                    }
                    case DROP:
                        break;
                    default:
                        break;
                }
            } else {
            }
        }
        return viewSet;
    }

    public void drawEnd(EndSpot end)
    {
        end.yRad = end.getRad() * yRatio;
        end.xRad = end.getRad() * xRatio;
        end.yPos = (end.getY() - ball.getY()) * yRatio + ball.getAbPos().y;
        end.xPos = (end.getX() - ball.getX()) * xRatio + ball.getAbPos().x;
    }

    public void drawEvil(EvilBall evil)
    {
        evil.setYRad(evil.getRad() * yRatio);
        evil.setXRad(evil.getRad() * xRatio);
        evil.setYPos(((int)evil.getRect().getY() - (int)ball.getRect().getY()) * yRatio + ball.getAbPos().y);
        evil.setXPos (((int)evil.getRect().getX() - (int)ball.getRect().getX()) * xRatio + ball.getAbPos().x);
    }
}

