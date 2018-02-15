/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame_2;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.WindowConstants;

/**
 *
 * @author Nathan
 */
/**
 * The main class in the game. This class is responsible for creating the game windows, loading 
 * the game objects, running the game and checking game conditions.
 */
public class GameController 
{
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int FRAME_WIDTH = (int)screenSize.getWidth();
    private final int FRAME_HEIGHT = (int)screenSize.getHeight();
    private int counter;
    private PhysicsEngine myPhysicsEngine;
    private final  Set<Platform> platforms;
    private final Set<Platform> walls;
    private final Set<Zone> zones;
    private PlayerBall playerBall;
    private final Set<EvilBall> evil;
    private EndSpot endSpot;
    private final Set<Platform> drops;
    private Set<Platform> drawSet;
    private Set<MoveableObject> ballSet;
    private Camera view;
    private final JFrame gameWindow;
    private final JPanel cards;
    private final DrawingPanel myDrawingPanel;
    private final StartGame startGame;
    private final GameOver gameOver;
    private final LevelComplete levelComplete;
    private final CardLayout c1;
    private static final String STARTGAME = "StartGame";
    private static final String DRAWINGPANEL = "DrawingPanel";
    private static final String GAMEOVER = "GameOver";
    private static final String LEVELCOMPLETE = "LevelComplete";
    private ImageLoader il;
    private final Timer gameTime;
    private final Timer levelTime;
    int level = 1, injuredCount = 30, lives;
    
    public GameController(String title)
    {
        platforms = new HashSet();
        walls = new HashSet();
        drops = new HashSet();
        zones = new HashSet();
        evil = new HashSet();
        drawSet = new HashSet();
        drawSet = Collections.synchronizedSet(drawSet);
        ballSet = new HashSet();
        ballSet = Collections.synchronizedSet(ballSet);
        myPhysicsEngine = new PhysicsEngine();
        lives = 5;
        il = new ImageLoader();       
        gameWindow = new JFrame(title);
        gameWindow.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        gameWindow.setVisible(true);
        gameWindow.setResizable(false);
        gameWindow.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cards = new JPanel(new CardLayout());
        startGame = new StartGame(getAvailableWidth(), getAvailableHeight());
        startGame.addAL(new StartButton());
        gameOver = new GameOver(getAvailableWidth(), getAvailableHeight());
        gameOver.addAL(new EndButton());
        levelComplete = new LevelComplete(getAvailableWidth(), getAvailableHeight());
        myDrawingPanel = new DrawingPanel(getAvailableWidth(), getAvailableHeight(), il.getBackGround());
        gameWindow.addKeyListener(new Mover());
        gameWindow.add(cards);
        cards.add(startGame, STARTGAME);
        cards.add(myDrawingPanel, DRAWINGPANEL);
        cards.add(gameOver, GAMEOVER);
        cards.add(levelComplete, LEVELCOMPLETE);
         
        c1 = (CardLayout)(cards.getLayout());
        
        gameTime = new Timer(40, new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                update();
            }
        });
        
        levelTime = new Timer(1500, new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                loadNextLevel();
            }
        });
        
        
        try
        {
            createPlatforms(level);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        view = new Camera(getAvailableWidth(), getAvailableHeight(), playerBall, il.getPlatformImage(), il.getWallImage());
    }
    
    private int getAvailableWidth()
    {
        return gameWindow.getWidth() - gameWindow.getInsets().left - gameWindow.getInsets().right;
    }

    //note you may also need to adjust for menu height
    private int getAvailableHeight()
    {
        return gameWindow.getHeight() - gameWindow.getInsets().top - gameWindow.getInsets().bottom;
    }

    //starts the game running once start button has been pressed on startGame screen.
    public void startTheGame()
    {   
        gameWindow.requestFocus();
        counter = 0;
        c1.show(cards,DRAWINGPANEL);
        gameTime.start();
    }
    
    /**
     * This method is responsible for running the game. At each tick of the gameTime this method is run.
     * It checks the game state, checks for any moves from the play and AI controls, passes these onto
     * the physics engine, collects objects for the camera to draw and selects the relevant screen when 
     * it needs to.
     */    
    public boolean update()
    {
        Rectangle es = new Rectangle(endSpot.getX(), endSpot.getY(), endSpot.getRad(), endSpot.getRad());

        if (getLives() == 0)
        {
            gameTime.stop();
            c1.show(cards, GAMEOVER);
            return false;
        }
        if (playerBall.getRect().intersects(es))
        {
            gameTime.stop();
            if (level == 10)
            {                
                c1.show(cards, GAMEOVER);
                return false;
            }
            levelComplete.setLevel(level);
            levelCompleted();
        }
        for (EvilBall e : evil)
        {
            if (playerBall.getRect().intersects(e.getRect()) && !playerBall.getInjured())
            {
                playerBall.setInjured(true);
                loseLife();
            }
        }
                
        if (playerBall.getInjured())
        {
            if (injuredCount == 0)
            {
                playerBall.setInjured(false);
                injuredCount = 30;
            }
            else
            {
                injuredCount--;
            }
        }

        counter ++;
        updatePictureState();
                                  
        drawSet = view.change(playerBall.getLocalPlatforms());
        ballSet = getBallSet();
        if (endSpot.getX() > playerBall.getX() - 400 && endSpot.getX() < playerBall.getX() + 400
                     && endSpot.getY() < playerBall.getY() + 200 && endSpot.getY() > playerBall.getY() - 250)
        {
            view.drawEnd(endSpot);
            myDrawingPanel.draw(drawSet, ballSet, endSpot, lives);
        }
        else
        {
            myDrawingPanel.draw(drawSet, ballSet, lives);
        }                            
        return true;                  
    }
    
    //loads the level complete screen and starts the levelTime timer.
    private void levelCompleted()
    {
        c1.show(cards, LEVELCOMPLETE);
        levelTime.start();
    }
    
    //loads the next level
    private void loadNextLevel()
    {
        level ++;
        counter = 0;
        try
        {
            createPlatforms(level);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("e" + level);
        }            
        view = new Camera(getAvailableWidth(), getAvailableHeight(), playerBall, il.getPlatformImage(), il.getWallImage());                
        c1.show(cards, DRAWINGPANEL);
        levelTime.stop();
        gameTime.start();
    }
    
    public void setLives(int life)
    {
        lives = life;
    }
    
    public int getLives()
    {
        return lives;
    }
    
    public void loseLife()
    {
        lives--;
    }
    
    //passes all movement information to the physics engine and updates the positon of all moveable objects
    public void updatePictureState()
    {
        playerBall.resetLocalArea();
        getSurroundings(playerBall);
                
        myPhysicsEngine.updatePhysicsState(playerBall);
        for (EvilBall e : evil)
        {
            if (counter > e.getStart() && playerBall != null)
            {
                e.resetLocalArea();
                getSurroundings(e);
                e.updateEvilBallState();
                myPhysicsEngine.updatePhysicsState(e);
            }
        }
    }
    
    //returns the set of balls within the players view.
    private Set<MoveableObject> getBallSet()
    {
        HashSet<MoveableObject> ballsToAdd = new HashSet();
        ballsToAdd.add(playerBall);
        
        for(EvilBall e : evil)
        {
            if (counter > e.getStart())
            {
                if (e.getRect().getX() > playerBall.getX() - 400 && e.getX() < playerBall.getX() + 400
                      && e.getY() < playerBall.getY() + 200 && e.getY() > playerBall.getY() - 250)
                {
                    view.drawEvil(e);
                    ballsToAdd.add(e);
                }
            }
        }
        return ballsToAdd;
    }
    
    //returns the set of platforms and walls within the player's view.
    public synchronized void getSurroundings(MoveableObject m)
    {
        Rectangle viewRect = new Rectangle(m.getX() - 400, m.getY() - 250, 800, 500);
        for(Platform a: platforms)
        {            
            if(a.getRect().intersects(viewRect))
            {
                m.addPlatform(a);
            }                    
        }
        for(Platform w: walls)
        {
            if(w.getRect().intersects(viewRect))
            {
                m.addPlatform(w);
            }
        }
    }
    
    //pause and unpause the game
    public void pause()
    {
        if (gameTime.isRunning())
        {
            gameTime.stop();
        }
        else
        {
            gameTime.start();
        }
    }
    
    //loads all the objects for the specified level
    private void createPlatforms(int level) throws FileNotFoundException
            {
                platforms.clear();
                walls.clear();
                drops.clear();
                zones.clear();
                evil.clear();
                File input = new File("./src/myGame_2_levels/" + level + ".txt");
                
                Scanner can = new Scanner(new BufferedReader(new FileReader(input))).useDelimiter(",");
                while (can.hasNextLine())
                {
                    int xp = can.nextInt();
                    int yp = can.nextInt();
                    int w = can.nextInt();
                    int h = can.nextInt();
                    String pt = can.next();
                    if (pt.equals("PLATFORM"))
                    {
                        platforms.add(new Platform(xp, yp, w, h, PlatformType.PLATFORM, il.getPlatformImage()));
                    }
                    if (pt.equals("WALL"))
                    {
                        walls.add(new Platform(xp, yp, w, h, PlatformType.WALL, il.getWallImage()));
                    }
                    if (pt.equals("WALLFILLER"))
                    {
                        walls.add(new Platform(xp, yp, w, h, PlatformType.WALL, il.getWallImage()));
                    }
                    if (pt.equals("DROP"))
                    {
                        drops.add(new Platform(xp, yp, w, h, PlatformType.DROP, null));
                    }
                    if (pt.equals("PLAYER"))
                    {
                        playerBall = new PlayerBall(xp, yp, Color.black, w, zones);
                    }
                    if (pt.equals("END"))
                    {
                        endSpot = new EndSpot(xp, yp);
                    }
                    if (pt.equals("EVIL"))
                    {
                        evil.add(new EvilBall(playerBall, xp, yp, Color.red, platforms, drops, walls, w, h, zones, il.getEvilImage()));                      
                    }
                    if (pt.equals("ZONE"))
                    {
                        zones.add(new Zone(xp, yp, w));
                    }
                    can.nextLine();
                }
                can.close();                
            }
    
    //resets the game to the beginning state.
    public void resetGame()
    {
        level = 1;
        setLives(5);
        try
        {
            createPlatforms(level);
        }
        catch (FileNotFoundException e)
        {
            System.out.println(e);
        }
        view = new Camera(getAvailableWidth(), getAvailableHeight(), playerBall, il.getPlatformImage(), il.getWallImage());
        c1.show(cards, STARTGAME);
    }
    
    //registers all key events for the game and takes appropriate actions
    public class Mover extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int event = e.getKeyCode();

            if (event == KeyEvent.VK_LEFT)
            {
                playerBall.setLeftPressed(true);
            }
            if (event == KeyEvent.VK_RIGHT) 
            {
                playerBall.setRightPressed(true);
            }
            if (event == KeyEvent.VK_UP || event == KeyEvent.VK_SPACE)
            {
                playerBall.setUpPressed(true); 
            }
            if (event == KeyEvent.VK_P)
            {
                pause();
            }
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
            int event = e.getKeyCode();
            if(event == KeyEvent.VK_LEFT)
            {
                playerBall.setLeftPressed(false);
            }
            if(event == KeyEvent.VK_RIGHT)
            {
                playerBall.setRightPressed(false);
            }
            if(event == KeyEvent.VK_UP || event == KeyEvent.VK_SPACE)
            {
                playerBall.setUpPressed(false);
            }
        }
    }
    
    // actions for the buttons on the startGame screen.
    private class StartButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object buttonPressed = e.getSource();
            
            if(buttonPressed.equals(StartGame.start))
            {
                startTheGame();
            }
            if(buttonPressed.equals(StartGame.exit))
            {
                System.exit(0);
            }
        }
    }
    
    //actions for the buttons on the endGame screen.
    private class EndButton implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            Object buttonPressed = e.getSource();
            
            if(buttonPressed.equals(GameOver.restart))
            {
                resetGame();
            }
            if(buttonPressed.equals(GameOver.exit))
            {
                System.exit(0);
            }
        }
    }
}
