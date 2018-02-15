/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Nathan
 */
//Loads all the images that the game uses.
public class ImageLoader 
{
    private Image platform;
    private Image wall;
    private Image evilBall;
    private Image backGround;
    
    public ImageLoader()
    {
        try
        {
            platform = ImageIO.read(this.getClass().getResource("/platform.jpg"));
        }
        catch (Exception e)
        {
            System.out.println("Platform not found");
        }
        
        try
        {
            wall = ImageIO.read(this.getClass().getResource("/wall2.jpg"));
        }
        catch(Exception e)
        {
            System.out.println("Wall not found");
        }
        
        try
        {
            evilBall = ImageIO.read(this.getClass().getResource("/EvilBall.png"));
        }
        catch (Exception e)
        {
            System.out.println("Evil not found");
        }
        
        try
        {
            backGround = ImageIO.read(this.getClass().getResource("/blueSky.jpg"));
        }
        catch (Exception e)
        {
            System.out.println("Background not found");
        }
    }
    
    public Image getPlatformImage()
    {
        return platform;
    }
    
    public Image getWallImage()
    {
        return wall;
    }
    
    public Image getEvilImage()
    {
        return evilBall;
    }
    
    public Image getBackGround()
    {
        return backGround;
    }
}
