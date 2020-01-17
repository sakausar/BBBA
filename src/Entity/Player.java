package Entity;

import TileMap.*;

import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends MapObject
{

    // player stuff
    private int health;

    private int maxHealth;

    private int cookie;

    private int maxCookies;

    private boolean dead;

    private boolean flinching;

    private long flinchTimer;

    // cookies
    private boolean firing;

    private int fireCost;

    private int cookieDamage;

    private ArrayList<Cookie> cookies;

    // Slap
    private boolean slapping;

    private int slapDamage;

    private int slapRange;

    // gliding
    private boolean gliding;

    // animations
    private ArrayList<BufferedImage[]> sprites;

    private final int[] numFrames = { 2, 8, 1, 2, 4, 2, 5 };

    // animation actions
    private static final int IDLE = 0;

    private static final int WALKING = 1;

    private static final int JUMPING = 2;

    private static final int FALLING = 3;

    private static final int GLIDING = 4;

    private static final int COOKIE = 5;

    private static final int SLAPPING = 6;
    
    private HashMap<String, AudioPlayer> sfx;
    
    private boolean win = false;


    public Player( TileMap tm )
    {
        super( tm );

        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;

        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;

        facingRight = true;

        health = maxHealth = 5;
        cookie = maxCookies = 2500;

        fireCost = 200;
        cookieDamage = 5;
        cookies = new ArrayList<Cookie>();

        slapDamage = 8;
        slapRange = 40;

        // load sprites
        try
        {
            BufferedImage spritesheet = ImageIO
                .read( getClass().getResourceAsStream( "/Sprites/Player/final3-2.png" ) );

            sprites = new ArrayList<BufferedImage[]>();
            for ( int i = 0; i < 7; i++ )
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
        currentAction = IDLE;
        animation.setFrames( sprites.get( IDLE ) );
        animation.setDelay( 400 );
        
        sfx = new HashMap<String, AudioPlayer>();
        sfx.put( "jump", new AudioPlayer("/SFX/jump.mp3") );
        sfx.put( "slap", new AudioPlayer("/SFX/scratch.mp3") );


    }


    public int getHealth()
    {
        return health;
    }


    public int getMaxHealth()
    {
        return maxHealth;

    }


    public int getCookie()
    {
        return cookie;
    }


    public int getMaxCookie()
    {
        return maxCookies;

    }


    public void setFiring()
    {
        firing = true;
    }


    public void setSlapping()
    {
        slapping = true;
    }


    public void setGliding( boolean b )
    {
        gliding = b;
    }
    
    public boolean isDead()
    {
        return (this.health <= 0);
    }
    public boolean won()
    {

        return win;
    }


    public void checkAttack( ArrayList<Enemy> enemies )
    {

        for ( int i = 0; i < enemies.size(); i++ )
        {
            Enemy e = enemies.get( i );
            // slapping
            if ( slapping )
            {
                if ( facingRight )
                {
                    if ( e.getx() > x && e.getx() < x + slapRange && e.gety() > y - height / 2
                        && e.gety() < y + height / 2 )
                    {
                        e.hit( slapDamage );
                    }
                }
                else
                {
                    if ( e.getx() < x && e.getx() > x - slapRange && e.gety() > y - height / 2
                        && e.gety() < y + height / 2 )
                    {
                        e.hit( slapDamage );
                    }
                }
            }
            // fireballs
            for ( int j = 0; j < cookies.size(); j++ )
            {
                if ( cookies.get( j ).intersects( e ) )
                {
                    e.hit( cookieDamage );
                    cookies.get( j ).setHit();
                    break;
                }
            }

            // check enemy collision
            if ( intersects( e ) )
            {
                hit( e.getDamage() );
            }
        }

    }


    public void hit( int damage )
    {
        if(damage == 0)
        {
            win = true;
        }
        if ( flinching )
        {
            return;
        }
        health -= damage;
        if ( health < 0 )
        {
            health = 0;
        }
        if ( health == 0 )
        {
            dead = true;
        }
        flinching = true;
        flinchTimer = System.nanoTime();

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
        else
        {
            if ( dx > 0 )
            {
                dx -= stopSpeed;
                if ( dx < 0 )
                {
                    dx = 0;
                }
            }
            else if ( dx < 0 )
            {
                dx += stopSpeed;
                if ( dx > 0 )
                {
                    dx = 0;
                }
            }
        }

        // cannot attack while moving, except in air
        if ( ( currentAction == SLAPPING || currentAction == COOKIE ) && !( jumping || falling ) )
        {
            dx = 0;
        }

        // jumping
        if ( jumping && !falling )
        {
            sfx.get( "jump").play();
            dy = jumpStart;
            falling = true;
            
        }

        // falling
        if ( falling )
        {
            if ( dy > 0 && gliding )
                dy += fallSpeed * 0.1;
            else
                dy += fallSpeed;

            if ( dy > 0 )
            {
                jumping = false;
            }
            if ( dy < 0 && !jumping )
            {
                dy += stopJumpSpeed;
            }
            if ( dy > maxFallSpeed )
            {
                dy = maxFallSpeed;
            }
        }
        
        
    }


    public void update()
    {
        // update position
        getNextPosition();
        checkTileMapCollision();
        setPosition( xtemp, ytemp );

        // check attack has stopped
        if ( currentAction == SLAPPING )
        {
            if ( animation.hasPlayedOnce() )
            {
                slapping = false;
            }
        }
        if ( currentAction == COOKIE )
        {
            if ( animation.hasPlayedOnce() )
            {
                firing = false;
            }
        }

        // cookie attack

        cookie += 1;
        if ( cookie > maxCookies )
        {
            cookie = maxCookies;
        }
        if ( firing && currentAction != COOKIE )
        {
            if ( cookie > fireCost )
            {
                cookie -= fireCost;
                Cookie ck = new Cookie( tileMap, facingRight, false );
                ck.setPosition( x, y );
                cookies.add( ck );
            }
        }

        // update cookies
        for ( int i = 0; i < cookies.size(); i++ )
        {
            cookies.get( i ).update();
            if ( cookies.get( i ).shouldRemove() )
            {
                cookies.remove( i );
                i--;
            }
        }

        // check done flinching
        if ( flinching )
        {
            long elapsed = ( System.nanoTime() - flinchTimer ) / 1000000;
            if ( elapsed > 1000 )
            {
                flinching = false;
            }
        }

        // set animation
        if ( slapping )
        {
            if ( currentAction != SLAPPING )
            {
                sfx.get( "slap" ).play();
                currentAction = SLAPPING;
                animation.setFrames( sprites.get( SLAPPING ) );
                animation.setDelay( 40 );
                width = 60;
            }
        }
        else if ( firing )
        {
            if ( currentAction != COOKIE )
            {
                currentAction = COOKIE;
                animation.setFrames( sprites.get( COOKIE ) );
                animation.setDelay( 100 );
                width = 30;
            }
        }
        else if ( dy > 0 )
        {
            if ( gliding )
            {
                if ( currentAction != GLIDING )
                {
                    currentAction = GLIDING;
                    animation.setFrames( sprites.get( GLIDING ) );
                    animation.setDelay( 100 );
                    width = 30;
                }

            }
            else if ( currentAction != FALLING )
            {
                currentAction = FALLING;
                animation.setFrames( sprites.get( FALLING ) );
                animation.setDelay( 100 );
                width = 30;
            }
        }
        else if ( dy < 0 )
        {
            if ( currentAction != JUMPING )
            {
                currentAction = JUMPING;
                animation.setFrames( sprites.get( JUMPING ) );
                animation.setDelay( -1 );
                width = 30;
            }
        }
        else if ( left || right )
        {
            if ( currentAction != WALKING )
            {
                currentAction = WALKING;
                animation.setFrames( sprites.get( WALKING ) );
                animation.setDelay( 40 );
                width = 30;
            }
        }
        else
        {
            if ( currentAction != IDLE )
            {
                currentAction = IDLE;
                animation.setFrames( sprites.get( IDLE ) );
                animation.setDelay( 400 );
                width = 30;
            }
        }

        animation.update();

        // set direction
        if ( currentAction != SLAPPING && currentAction != COOKIE )
        {
            if ( right )
            {
                facingRight = true;

            }
            if ( left )
            {
                facingRight = false;
            }
        }
    }


    public void draw( Graphics2D g )
    {
        setMapPosition();

        // draw cookies
        for ( int i = 0; i < cookies.size(); i++ )
        {
            cookies.get( i ).draw( g );
        }

        // draw player
        if ( flinching )
        {
            long elapsed = ( System.nanoTime() - flinchTimer ) / 1000000;
            if ( elapsed / 100 % 2 == 0 )
            {
                return;
            }
        }

        super.draw( g );

    }
}
