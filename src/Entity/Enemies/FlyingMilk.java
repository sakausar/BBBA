package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class FlyingMilk extends Enemy
{
    private BufferedImage[] sprites;
    
    private int webLength;
    

    public FlyingMilk( TileMap tm, int webLength )
    {
        super( tm );
        falling = false;
        this.webLength = webLength; 
        
        moveSpeed = 0.3;
        maxSpeed = 1.5;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 2;
        damage = 2;
        
        try
        {
            BufferedImage spritesheet = ImageIO
                .read( getClass().getResourceAsStream( "/Sprites/Enemies/flyingMilk.png" ) );

            sprites = new BufferedImage[2];
            for ( int i = 0; i < sprites.length; i++ )
            {
                sprites[i] = spritesheet.getSubimage( i * width, 0, width, height );
            }
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames( sprites );
        animation.setDelay( 300 );
        
        setDown(true);
    }
    
    private void getNextPosition()
    {
        if(down)
        {
            dy -= moveSpeed;
            if(dy < -maxSpeed)
            {
                dy = -maxSpeed;
                
            }
            
            
        }
        else if(up)
        {
            dy += moveSpeed;
            if(dy > maxSpeed)
            {
                dy = maxSpeed;
            }
            
        }
    }
    public void update()
    {
     // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition( xtemp, ytemp );
        if(this.gety() == webLength)
        {
            dy = 0;
            up = true;
            down = false;
        }
        
        if ( down && dy == 0)
        {

            down = false;
            up = true;
        }
        else if(up && dy == 0)
        {
            up = false;
            down = true;
        }
        

        // check flinching
        if ( flinching )
        {
            long elapsed = ( System.nanoTime() - flinchTimer / 1000000 );
            if ( elapsed > 400 )
            {
                flinching = false;
            }
        }
        
        animation.update();
    }
    
    public void draw( Graphics2D g )
    {
        setMapPosition();
        super.draw( g );
    }

}
