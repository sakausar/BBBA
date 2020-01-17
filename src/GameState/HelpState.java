package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Entity.Player;
import TileMap.Background;

public class HelpState extends GameState
{
    
    
    private Background bg;

    private int currentChoice = 0;

    private String[] options = { "Start", "Menu ", "Quit", "" };

    private Color titleColor;

    private Font titleFont;
    
    private Font insFont;

    private Font font;

    private Player player;

    public HelpState(GameStateManager gsm)
    {
        this.gsm = gsm;

        try
        {

            bg = new Background( "/Backgrounds/bgTitlescreen.jpg", 1 );

            titleColor = new Color( 128, 0, 0 );
            titleFont = new Font( "Century Gothic", Font.PLAIN, 28 );
            
            insFont = new Font("Arial", Font.PLAIN, 12);

            font = new Font( "Arial", Font.PLAIN, 12 );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    public void init()
    {
        

    }


    public void update()
    {
        bg.update();

    }


    public void draw( Graphics2D g )
    {
        bg.draw( g );
        
        // draw title
        g.setColor( titleColor );
        g.setFont( titleFont );
        g.drawString( "How To Play", 80, 30 );
        g.setFont( insFont );
        g.setColor( Color.CYAN );
        g.drawString( "1. Use the Arrow Keys to move Big Boi", 60, 70 );
        g.drawString( "2. Use the 'W' key to make Big Boi jump", 60, 90 );
        g.drawString( "3. Use the 'W' + 'E' keys to make Big Boi glide", 60, 110 );
        g.drawString( "4. Use the 'R' key to shoot cookies", 60, 130 );
        g.drawString( "5. Use the 'F' key to slap the baddies", 60, 150 );

        // draw menu options
        g.setFont( font );
        for ( int i = 0; i < options.length; i++ )
        {
            if ( i == currentChoice )
            {
                g.setColor( Color.GREEN );
            }
            else
            {
                g.setColor( Color.RED );
            }
            g.drawString( options[i], 145, 165 + i * 15 );
        }


    }


    private void select()
    {
        if ( currentChoice == 0 )
        {
            gsm.setState( GameStateManager.LEVEL1STATE );
        }
        if ( currentChoice == 1 )
        {
            gsm.setState( GameStateManager.MENUSTATE );
        }
        if ( currentChoice == 2 )
        {
            System.exit( 0 );
        }
        if( currentChoice == 3)
        {
            gsm.setState( GameStateManager.LEVEL1STATE );
        }

    }
// Not this 
    
/**
 * But this format
 */

    public void keyPressed( int k )
    {
        if ( k == KeyEvent.VK_ENTER )
        {
            select();
        }
        if ( k == KeyEvent.VK_UP )
        {
            currentChoice--;
            if ( currentChoice == -1 )
            {
                currentChoice = options.length - 1;
            }
        }
        if ( k == KeyEvent.VK_DOWN )
        {
            currentChoice++;
            if ( currentChoice == options.length )
            {
                currentChoice = 0;
            }
        }

    }


    public void keyReleased( int k )
    {
        // TODO Auto-generated method stub

    }

}
