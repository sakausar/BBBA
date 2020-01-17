package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Cookie;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;


public class BigBoiFulk extends Enemy
{
    private ArrayList<BufferedImage[]> sprites;

    private final int[] numFrames = { 2, 8 };

    private int cookie;

    private int maxCookies;

    private boolean firing = true;

    private int fireCost;

    private int cookieDamage;

    private ArrayList<Cookie> cookies;


    public BigBoiFulk( TileMap tm )
    {
        super( tm );

        moveSpeed = 1;
        maxSpeed = 3;
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        health = maxHealth = 1010;
        damage = 3;

        cookie = maxCookies = 1000000000;

        fireCost = 0;
        cookieDamage = 1;
        cookies = new ArrayList<Cookie>();

        // load sprites
        try
        {
            BufferedImage spritesheet = ImageIO
                .read( getClass().getResourceAsStream( "/Sprites/Enemies/player.png" ) );

            sprites = new ArrayList<BufferedImage[]>();
            for ( int i = 0; i < 2; i++ )
            {
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for ( int j = 0; j < numFrames[i]; j++ )
                {
                    if ( i != 6 )
                    {
                        bi[j] = spritesheet.getSubimage( j * width, i * height, width, height );
                    }
                    else
                    {
                        bi[j] = spritesheet
                            .getSubimage( j * width * 2, i * height, width * 2, height );
                    }

                }
                sprites.add( bi );
            }

        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }

        animation = new Animation();
        currentAction = 0;
        animation.setFrames( sprites.get( 0 ) );
        animation.setDelay( 400 );

        right = true;
        facingRight = true;
    }


    public void checkPlayerAttack( Player p )
    {
        
        for ( int j = 0; j < cookies.size(); j++ )
        {
            System.out.print( "test" );
            if ( p.getx() == cookies.get( j ).getx() && p.gety() == cookies.get( j ).gety() )
            {
                
                p.hit( cookieDamage );
                cookies.get( j ).setHit();
                break;
            }
        }
    }


    private void getNextPosition()
    {

        // movement
        if ( left )
        {
            dx -= moveSpeed;
            if ( dx < -maxSpeed )
            {
                dx = -maxSpeed;
            }
        }
        else if ( right )
        {
            dx += moveSpeed;
            if ( dx > maxSpeed )
            {
                dx = maxSpeed;
            }
        }

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

        // check flinching
        if ( flinching )
        {
            long elapsed = ( System.nanoTime() - flinchTimer / 1000000 );
            if ( elapsed > 400 )
            {
                flinching = false;
            }
        }

        // hits wall
        if ( right && dx == 0 )
        {
            right = false;
            left = true;
            facingRight = false;
        }
        else if ( left && dx == 0 )
        {
            left = false;
            right = true;
            facingRight = true;

        }
        // cookie attack
        if ( Math.random() > .9 )
        {
            if ( cookie > maxCookies )
            {
                cookie = maxCookies;
            }
            if ( firing )
            {
                if ( cookie > fireCost )
                {

                    cookie -= fireCost;
                    Cookie ck = new Cookie( tileMap, facingRight, true );
                    ck.setPosition( x, y );
                    cookies.add( ck );
                    width = 30;
                }
            }
        }
        for ( int i = 0; i < cookies.size(); i++ )
        {
            cookies.get( i ).update();
            if ( cookies.get( i ).shouldRemove() )
            {
                cookies.remove( i );
                i--;
            }
        }

        if ( left || right )
        {
            if ( currentAction != 1 )
            {
                currentAction = 1;
                animation.setFrames( sprites.get( 1 ) );
                animation.setDelay( 40 );
                width = 30;
            }
        }
        if ( health < 0 )
        {
            dead = true;
        }

        // update animation
        animation.update();
    }


    public void draw( Graphics2D g )
    {

        // if ( notOnScreen() )
        // {
        // return;
        // }
        // draw cookies
//        for ( int i = 0; i < cookies.size(); i++ )
//        {
//            cookies.get( i ).draw( g );
//        }
        setMapPosition();
        super.draw( g );
    }
}
