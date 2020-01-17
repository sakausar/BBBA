package GameState;

import TileMap.*;

import Entity.*;
import Entity.Enemies.*;
import Main.GamePanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Audio.AudioPlayer;


public class Level1State extends GameState
{

    private TileMap tileMap;

    private Background bg;

    private Player player;

    public ArrayList<Enemy> enemies;

    private ArrayList<Explosion> explosions;

    private HUD hud;

    private HUD eHud;

    private AudioPlayer bgMusic;

    private AudioPlayer win = new AudioPlayer( "/SFX/yay.mp3" );

    private int play1 = 0;


    public Level1State( GameStateManager gsm )
    {
        this.gsm = gsm;
        init();
    }


    public void init()
    {

        tileMap = new TileMap( 30 );
        tileMap.loadTiles( "/Tilesets/t2.png" );
        tileMap.loadMap( "/Maps/testmap.map" );
        tileMap.setPosition( 0, 0 );
        tileMap.setTween( 1 );

        bg = new Background( "/Backgrounds/bgTitlescreen.jpg", 0.1 );

        player = new Player( tileMap );
        player.setPosition( 100, 100 );

        populateEnemies();

        explosions = new ArrayList<Explosion>();

        hud = new HUD( player );

        bgMusic = new AudioPlayer( "/Music/bestMusic.mp3" );
        bgMusic.play();
    }


    private void populateEnemies()
    {
        enemies = new ArrayList<Enemy>();
        MilkWalker s;
        Point[] points = new Point[] { new Point( 225, 100 ), new Point( 185, 100 ),
            new Point( 200, 100 ), new Point( 860, 200 ), new Point( 1525, 200 ),
            new Point( 1680, 200 ), new Point( 1800, 200 ) };
        for ( int i = 0; i < points.length; i++ )
        {
            s = new MilkWalker( tileMap );
            s.setPosition( points[i].x, points[i].y );
            enemies.add( s );
        }
        FlyingMilk p;
        Point[] FlyingMilkpoints = new Point[] { new Point( 2000, 75 ), new Point( 2100, 110 ),
            new Point( 2200, 100 ) };
        for ( int i = 0; i < FlyingMilkpoints.length; i++ )
        {
            p = new FlyingMilk( tileMap, 160 );
            p.setPosition( FlyingMilkpoints[i].x, FlyingMilkpoints[i].y );
            enemies.add( p );
        }

    }


    public void update()
    {
        BigBoiFulk b = new BigBoiFulk( tileMap );
        // update player
        player.update();
        if ( player.isDead() )
        {
            bgMusic.close();
            gsm.setState( 2 );
            return;

        }
        if ( player.gety() > 225 )
        {
            bgMusic.close();
            gsm.setState( 2 );
            return;
        }
        if ( player.getx() > 2925 && play1 == 0 )
        {

            b.setPosition( 3100, 200 );
            enemies.add( b );
            tileMap.loadMap( "/Maps/Bossmap.map" );
            eHud = new HUD( b );
            play1++;

        }
        if ( player.won() )
        {
            bgMusic.close();
            win.play();
            gsm.setState( 4 );
        }

        tileMap.setPosition( GamePanel.WIDTH / 2 - player.getx(),
            GamePanel.HEIGHT / 2 - player.gety() );

        // set background scrolling
        bg.setPosition( tileMap.getx(), tileMap.gety() );

        // attack enemies
        player.checkAttack( enemies );

        // attack players

        b.checkPlayerAttack( player );

        // update all enemies
        for ( int i = 0; i < enemies.size(); i++ )
        {
            Enemy e = enemies.get( i );
            e.update();
            if ( e.isDead() )
            {
                if ( player.getx() > 2800 && play1 == 1 )
                {
                    CookieJar c = new CookieJar( tileMap );
                    c.setPosition( 3100, 50 );
                    enemies.add( c );
                    play1++;
                }

                enemies.remove( i );
                i--;
                explosions.add( new Explosion( e.getx(), e.gety() ) );
            }

        }
        // update all explosions
        for ( int i = 0; i < explosions.size(); i++ )
        {
            explosions.get( i ).update();
            if ( explosions.get( i ).shouldRemove() )
            {
                explosions.remove( i );
                i--;
            }
        }

    }


    public void draw( Graphics2D g )
    {

        // clear bg
        bg.draw( g );

        // draw tilemap
        tileMap.draw( g );

        // draw player
        player.draw( g );

        // draw enemies
        for ( int i = 0; i < enemies.size(); i++ )
        {
            enemies.get( i ).draw( g );

        }

        // draw explosions
        for ( int i = 0; i < explosions.size(); i++ )
        {
            explosions.get( i ).setMapPostion( (int)tileMap.getx(), (int)tileMap.gety() );
            explosions.get( i ).draw( g );
        }

        // draw hud;
        hud.draw( g );
        if ( eHud != null )
        {
            eHud.draw( g );
        }

    }


    public void keyPressed( int k )
    {
        if ( k == KeyEvent.VK_LEFT )
        {
            player.setLeft( true );
        }

        if ( k == KeyEvent.VK_RIGHT )
        {
            player.setRight( true );
        }

        if ( k == KeyEvent.VK_UP )
        {
            player.setUp( true );
        }

        if ( k == KeyEvent.VK_DOWN )
        {
            player.setDown( true );
        }

        if ( k == KeyEvent.VK_W )
        {
            player.setJumping( true );
        }

        if ( k == KeyEvent.VK_E )
        {
            player.setGliding( true );
        }

        if ( k == KeyEvent.VK_R )
        {
            player.setSlapping();
        }

        if ( k == KeyEvent.VK_F )
        {
            player.setFiring();
        }

        if ( k == KeyEvent.VK_ESCAPE )
        {
            gsm.setState( 0 );
            bgMusic.close();
        }

    }


    public void keyReleased( int k )
    {
        if ( k == KeyEvent.VK_LEFT )
        {
            player.setLeft( false );
        }

        if ( k == KeyEvent.VK_RIGHT )
        {
            player.setRight( false );
        }

        if ( k == KeyEvent.VK_UP )
        {
            player.setUp( false );
        }

        if ( k == KeyEvent.VK_DOWN )
        {
            player.setDown( false );
        }

        if ( k == KeyEvent.VK_W )
        {
            player.setJumping( false );
        }

        if ( k == KeyEvent.VK_E )
        {
            player.setGliding( false );
        }
    }

}
