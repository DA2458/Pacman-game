import javax.swing.JFrame;
public class App 
{
    public static void main(String[] args) throws Exception 
    {
        //difining window size 
        int rowCnt = 21;
        int columunCnt = 19;
        int tileSize = 32; //pixel count
        int boardWidth = columunCnt * tileSize;
        int boardHeight = rowCnt* tileSize;

        //lines below creates a window, and makes it visible 
        JFrame frame  =  new JFrame("PacMan");
        frame.setSize(boardWidth, boardHeight);
        //centers window 
        frame.setLocationRelativeTo(null);
        //makes window static 
        frame.setResizable(false);
        //makes window close when you press the X
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

         /* initalizes a pacman game and adds it 
         * to the window and .pack resizes to get the 
         *full game into the window. 
         */
        PacMan pacmangame = new PacMan();
        frame.add(pacmangame);
        frame.pack();
        //NOTE: only set your window visible after getting all
        // components in
        pacmangame.requestFocus();
        frame.setVisible(true); 
    }
}
