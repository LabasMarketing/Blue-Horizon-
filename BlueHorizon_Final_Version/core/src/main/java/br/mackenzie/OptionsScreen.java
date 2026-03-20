package br.mackenzie;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class OptionsScreen implements Screen {
    private final Main game;
    private final SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Texture menuBackground;

    private float x_painel;
    private float y_painel;

    private Texture background;

    public OptionsScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.spriteBatch;
    }

    @Override
    public void show() {
        viewport = new FitViewport(8, 5);
        menuBackground = new Texture("Menu/Fundo.png");
        background = new Texture("Menu/OpcaoOptions/Settings.png"); 
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        x_painel = (viewport.getWorldWidth() / 2) - (8f / 2);
        y_painel = (viewport.getWorldHeight() / 2) - (4f / 2);

        spriteBatch.begin();
        spriteBatch.draw(menuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.end();

        // Lógica de cliques
        if (com.badlogic.gdx.Gdx.input.justTouched()) {
            
            Vector2 touchPoint = new Vector2(com.badlogic.gdx.Gdx.input.getX(), com.badlogic.gdx.Gdx.input.getY());
            viewport.unproject(touchPoint); 

            float inputX = touchPoint.x;
            float inputY = touchPoint.y;

            if (inputX >= 2f && inputX <= 2f + 0.7f &&
                inputY >= 4.05f && inputY <= 4.05f + 0.3f) {
                
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    @Override
    public void dispose() {
        background.dispose();
    }
}
