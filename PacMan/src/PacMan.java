import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Random;

import javax.swing.*;


public class PacMan extends JPanel implements ActionListener, KeyListener
{

    
    //class for creating each individual square ont he grid 
    class Block
    {
        int x;
        int y;
        int width;
        int height;
        Image image;

        int startx;
        int starty;
        char direction = 'U';// U D L R 
        int velocityX = 0;
        int velocityY = 0;

        Block(Image image, int x, int y, int width, int height)
        {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startx = x;
            this.starty = y;
    

        }

        void updateDirection(char direction)
        {
            char prevDirection = this.direction;
            this.direction = direction;
            updateVelocity();
            this.x += velocityX;
            this.y += velocityY;
            for(Block wall : walls)
            {
                if(collision(this, wall))
                {
                    this.x -= velocityX;
                    this.y -= velocityY;
                    this.direction = prevDirection;
                    updateVelocity();
                }
            }
        }

        void updateVelocity()
        {
          if(this.direction == 'U')
          {
            this.velocityX = 0;
            this.velocityY = -tileSize/4;
          }
          if(this.direction == 'D')
          {
            this.velocityX = 0;
            this.velocityY = tileSize/4;
          }
          if(this.direction == 'L')
          {
            this.velocityX = -tileSize/4;
            this.velocityY = 0;
          }
          if(this.direction == 'R')
          {
            this.velocityX = tileSize/4;
            this.velocityY = 0;
          }


        }

        void reset()
        {
            this.x = startx;
            this.y = starty;
        }
    }
    private int rowCnt = 21;
    private int columunCnt = 19;
    private int tileSize = 32; //pixel count
    private int boardWidth = columunCnt * tileSize;
    private int boardHeight = rowCnt* tileSize;

    //image variables
    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacManUpImage;
    private Image pacManDownImage;
    private Image pacManLeftImage;
    private Image pacManRightImage;

    //hash sets storing every grid square
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman;

    Timer gameLoop;
    char[] directions = {'U', 'D', 'L', 'R'};
    Random random = new Random();
    int score = 0;
    int lives = 3;
    boolean gameOver = false;


    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };


    PacMan()
    {
        //creates the size of the jframe and sets its background to black
        setPreferredSize(new Dimension(boardWidth,boardHeight));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);


        //loads images
        //NOTE: "./" tells to look in the same folder
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();
        pacManUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacManDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacManLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacManRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap();
        /*1000 ms in 1 second
         * the 50 denoted below is 50ms
         * 100/50 = 20
         * 20 frames per second
         */
        for(Block ghost : ghosts)
        {
            char newDirection = directions[random.nextInt(4)];
            ghost.updateDirection(newDirection);
        }
        gameLoop = new Timer(50,this);
        gameLoop.start();
       



    }

    //loads objects onto the map 
    public void loadMap()
    {
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        for(int r = 0; r< rowCnt; r++)
        {
            for (int c =0; c< columunCnt; c++)
            {
                char ch = tileMap[r].charAt(c);
                int x = c*tileSize;
                int y = r*tileSize;

                if (ch == 'X') //create wall
                {
                    Block wall = new Block(wallImage,x,y,tileSize,tileSize);
                    walls.add(wall);
                }
                else if(ch == 'P') // create pacman
                {
                    pacman = new Block(pacManLeftImage,x,y,tileSize,tileSize);
                }
                else if(ch == 'b') // blue ghost
                {
                    Block ghost = new Block(blueGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(ch == 'o') // orange ghost
                {
                    Block ghost = new Block(orangeGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(ch == 'p') // pink ghost
                {
                    Block ghost = new Block(pinkGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(ch== 'r') // red ghost
                {
                    Block ghost = new Block(redGhostImage,x,y,tileSize,tileSize);
                    ghosts.add(ghost);
                }
                else if(ch == ' ') // food
                {
                    Block food = new Block(null,x+14,y+14,4,4);
                    foods.add(food);  
                }
            }
        }

    }

    //draws the objects onto the window
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);

    }

    //draws the objects onto the window
    public void draw(Graphics g)
    {
        g.drawImage(pacman.image,pacman.x,pacman.y,pacman.width,pacman.height,null);

        for(Block ghost : ghosts)
        {
            g.drawImage(ghost.image,ghost.x,ghost.y,ghost.width,ghost.height,null);
        }

        for( Block wall : walls)
        {
        g.drawImage(wall.image,wall.x,wall.y,wall.width,wall.height,null);

        }

        //sets color for fill
        g.setColor(Color.WHITE);
        for(Block food : foods)
        {
            //fills a rectangle shape with specified dimensions
            g.fillRect(food.x,food.y,food.width, food.height);
        }

        //score 
        g.setFont(new Font("Arial", Font.PLAIN,18));
        if(gameOver)
        {
            g.drawString("Game Over: " + String.valueOf(score) + "\n Press space to play again", tileSize/2, tileSize/2);
        }
        else
        {
            g.drawString("x:" + String.valueOf(lives) + " Score: " + String.valueOf(score), tileSize/2, tileSize/2 );
        }
    }

    

    public void move()
    {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;

        if(pacman.x <= 0)
        {
            pacman.x = boardWidth - (int)(0.9*tileSize);
        }
        else if(pacman.x + pacman.width >= boardWidth)
        {
            pacman.x = 0 + (int)(0.9*tileSize);
        }

        //Pacman colisions
        for(Block wall : walls)
        {
            if (collision(pacman, wall))
            {
                pacman.x -= pacman.velocityX;
                pacman.y -= pacman.velocityY;
                break;
            }
        }

            //ghost colisions
        for(Block ghost : ghosts)
        {
            if(ghost.y == tileSize*9 && ghost.direction != 'U' && ghost.direction != 'D')
            {
                ghost.updateDirection(directions[random.nextInt(2)]);
            }
            ghost.x += ghost.velocityX;
            ghost.y += ghost.velocityY;
            for(Block wall : walls)
            {
                if(collision(ghost, wall))
                {
                    ghost.x -= ghost.velocityX;
                    ghost.y -= ghost.velocityY;
                    ghost.updateDirection(directions[random.nextInt(4)]);
                }
            }

            if(collision(pacman, ghost))
            {
                lives -= 1;
                if(lives == 0)
                {
                    gameOver = true;
                    return;
                }
                resetPositions();
            }

            if(ghost.x <= 0)
            {
                ghost.x = boardWidth -(int)(0.9*tileSize);
                
            }
            else if (ghost.x + ghost.width >= boardWidth)
            {
                ghost.x = 0 + (int)(0.9*tileSize);
            }
            
        }

        //food colisions
        Block foodEaten = null;
        for(Block food : foods)
        {
            if( collision(pacman, food))
            {
                foodEaten = food;
                score += 10;
                break;
            }
        }
        foods.remove(foodEaten);

        if(foods.isEmpty())
        {
            loadMap();
            resetPositions();
            
        }

    }

    public boolean collision(Block a, Block b)
    {
        return a.x < b.x + b.width &&
        a.x + a.width > b.x &&
        a.y < b.height + b.y &&
        a.y + a.height > b.y;
    }

    public void resetPositions()
    {
        pacman.reset();
        pacman.velocityX = 0;
        pacman.velocityY = 0;
        for(Block ghost : ghosts)
        {
            ghost.reset();
            ghost.direction = directions[random.nextInt(4)];
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) 
    {
        move();
        repaint();
        if(gameOver)
        {
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) 
    {
        if (gameOver && (e.getKeyCode() == KeyEvent.VK_SPACE))
        {
            loadMap();
            resetPositions();
            lives = 3;
            score = 0;
            gameOver =false;
            gameLoop.start();
        }
        //System.out.println("Key Event: " + e.getKeyCode());
        //System.out.println(pacman.direction);
        if(e.getKeyCode() == KeyEvent.VK_UP)
        {
            pacman.updateDirection('U');
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            pacman.updateDirection('D');
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            pacman.updateDirection('L');
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            pacman.updateDirection('R');
        }

        if(pacman.direction == 'U')
        {
            pacman.image = pacManUpImage;
        }
        else if(pacman.direction == 'D')
        {
            pacman.image = pacManDownImage;
        }
        else if(pacman.direction == 'L')
        {
            pacman.image = pacManLeftImage;
        }
        else if(pacman.direction == 'R')
        {
            pacman.image = pacManRightImage;
        }

    }

}
