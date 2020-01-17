package GameState;

import java.util.ArrayList;


public class GameStateManager
{
    
    private GameState[] gameStates;

    private int currentState;

    public static final int NUMGAMESTATES = 5;

    public static final int MENUSTATE = 0;

    public static final int LEVEL1STATE = 1;
    
    public static final int GAMEOVERSTATE = 2;
    
    public static final int HELPSTATE = 3;
    
    public static final int WINSTATE = 4;


    public GameStateManager()
    {

        gameStates = new GameState[NUMGAMESTATES];

        currentState = MENUSTATE;
        loadState( currentState );

    }


    private void loadState( int state )
    {
        if ( state == MENUSTATE )
        {
            gameStates[state] = new MenuState( this );
        }
        if ( state == LEVEL1STATE )
        {
            gameStates[state] = new Level1State( this );
        }
        if( state == GAMEOVERSTATE)
        {
            gameStates[state] = new GameOverState(this);
        }
        if(state == HELPSTATE)
        {
            gameStates[state] = new HelpState( this );
        }
        if(state == WINSTATE)
        {
            gameStates[state] = new WinState( this );
        }
    }


    private void unloadState( int state )
    {
        gameStates[state] = null;
    }


    public void setState( int state )
    {
        unloadState( currentState );
        currentState = state;
        loadState( currentState );

    }


    public void update()
    {
        if(gameStates[currentState] == null)
        {
            return;
        }
        gameStates[currentState].update();
    }


    public void draw( java.awt.Graphics2D g )
    {
        if(gameStates[currentState] == null)
        {
            return;
        }
        gameStates[currentState].draw( g );
    }


    public void keyPressed( int k )
    {
        gameStates[currentState].keyPressed( k );
    }


    public void keyReleased( int k )
    {
        gameStates[currentState].keyReleased( k );
    }

}
