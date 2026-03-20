package br.mackenzie;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class FaseBaseScreen implements Screen{
    protected final Main game;
    protected final SpriteBatch spriteBatch;
    protected FitViewport viewport;
    
    public Player player; 

    public FaseBaseScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.spriteBatch;
    }

    public boolean isPlayerMoving() {
        if (player != null) {
            return player.isMoving(); 
        }
        return false; 
    }
    
    @Override
    public abstract void show(); 

    @Override
    public abstract void render(float delta);

    @Override
    public void resize(int width, int height) {
        if (viewport != null) {
            viewport.update(width, height, true);
        }
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {}
}
