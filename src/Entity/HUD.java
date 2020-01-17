package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class HUD
{

    private Player player;
    
    private Enemy enemy;

    private BufferedImage image;

    private Font font;
    
    private boolean checkEnemy = false;


    public HUD( Player p )
    {
        player = p;
        try
        {
            image = ImageIO.read( getClass().getResourceAsStream( "/HUD/hud.gif" ) );
            font = new Font("Arial", Font.PLAIN, 14);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    public HUD(Enemy en)
    {
        checkEnemy = true;
        enemy = en;
        try
        {
            image = ImageIO.read( getClass().getResourceAsStream( "/HUD/bHealth.png" ) );
            font = new Font("Arial", Font.PLAIN, 14);
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g)
    {
        
        if(!checkEnemy)
        {
            g.drawImage( image, 0, 10, null);
            g.setFont( font );
            g.setColor( Color.WHITE );
            g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 30, 25);
            g.drawString(player.getCookie()/100 + "/" + player.getMaxCookie()/100, 30, 45);
        }
        else
        {
                g.drawImage( image, 100, -30, null );
                g.setFont( font );
                g.setColor( Color.WHITE );
                g.drawString( enemy.health + "/" + enemy.maxHealth, 150, 30 );           
        }
       
    }

}
