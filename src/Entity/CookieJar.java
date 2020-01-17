package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.*;
import TileMap.TileMap;


public class CookieJar extends Enemy
{

    private BufferedImage[] sprites;


    public CookieJar( TileMap tm )
    {
        super( tm );

        moveSpeed = 0;
        maxSpeed = 0;
        fallSpeed = 0.002;
        maxFallSpeed = .0007;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 99999999;
        damage = 0;

        // load sprites
        try
        {
            BufferedImage spritesheet = ImageIO
                .read( getClass().getResourceAsStream( "/Sprites/Enemies/Cookies .png" ) );

            sprites = new BufferedImage[3];
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

        right = true;
        facingRight = true;
    }


    private void getNextPosition()
    {

        // falling
        if ( falling )
        {
            dy += fallSpeed;
        }

    }


    public void update()
    {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition( xtemp, ytemp );
        
        // update animation
        animation.update();

    }


    public void draw( Graphics2D g )
    {

        setMapPosition();
        super.draw( g );
    }
}
