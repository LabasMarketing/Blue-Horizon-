package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class MenuScreen implements Screen{
    private final Main game;
    private final SpriteBatch spriteBatch;
    private FitViewport viewport;
    
    // Recursos do Menu
    private Texture menuBackground;
    private Texture startButtonTexture;
    private Texture optionsButtonTexture;
    private Texture creditsButtonTexture;
    private Texture exitButtonTexture;
    private Texture tituloTexture;
    private static Music musicaMenu;

    // Dimensões e Posição do Botão
    private final float buttonWidth = 2.0f;
    private final float buttonHeight = 1.0f;

    private float buttonX;
    private float buttonY;

    public MenuScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.spriteBatch;
    }

    @Override
    public void show() {
        // Inicialização das Imagens
        menuBackground = new Texture("Menu/Fundo.png");
        tituloTexture = new Texture("Menu/Titulo.png");
        startButtonTexture = new Texture("Menu/Play.png"); 
        optionsButtonTexture = new Texture("Menu/Options.png");
        creditsButtonTexture = new Texture("Menu/Credits.png");
        exitButtonTexture = new Texture("Menu/Exit.png");

         if (musicaMenu == null) {
             musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("Menu/menu-music.mp3"));
             musicaMenu.setLooping(true);
        }

        viewport = new FitViewport(8, 5); 
        viewport.apply();
        
        // Posição dos botões
        buttonX = (viewport.getWorldWidth() / 2) - (buttonWidth / 2);
        buttonY = (viewport.getWorldHeight() / 2) - (buttonHeight / 2);

        if (!musicaMenu.isPlaying()) {
            musicaMenu.play();
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        // Desenha o fundo e botões
        spriteBatch.draw(menuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(tituloTexture, buttonX, buttonY + 1.4f, buttonWidth, buttonHeight);
        spriteBatch.draw(startButtonTexture, buttonX, buttonY + 0.7f, buttonWidth, buttonHeight);
        spriteBatch.draw(optionsButtonTexture, buttonX, buttonY, buttonWidth, buttonHeight);
        spriteBatch.draw(creditsButtonTexture, buttonX, buttonY - 0.7f, buttonWidth, buttonHeight);
        spriteBatch.draw(exitButtonTexture, buttonX, buttonY - 1.4f, buttonWidth, buttonHeight);

        spriteBatch.end();

        // Verifica toques
        if (Gdx.input.justTouched()) {
            Vector2 touchPoint = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPoint);

            float inputX = touchPoint.x;
            float inputY = touchPoint.y;

            // Botão PLAY
            if (isButtonClicked(inputX, inputY, buttonY + 0.7f)) {
                game.setScreen(new FasesScreen(game));
            }

            // Botão OPTIONS
            if (isButtonClicked(inputX, inputY, buttonY)) {
                game.setScreen(new OptionsScreen(game)); // 
            }

            // Botão CREDITS
            if (isButtonClicked(inputX, inputY, buttonY - 0.7f)) {
                game.setScreen(new CreditsScreen(game)); 
            }

            // Botão EXIT
            if (isButtonClicked(inputX, inputY, buttonY - 1.4f)) {
                Gdx.app.exit();
            }
        }
    }

    private boolean isButtonClicked(float inputX, float inputY, float btnY) {
        return inputX >= buttonX && inputX <= buttonX + buttonWidth &&
               inputY >= btnY && inputY <= btnY + buttonHeight;
    }
    public static void stopMusica(){
        if (musicaMenu != null && musicaMenu.isPlaying()) {
            musicaMenu.stop(); 
        }
    }

    @Override
    public void dispose() {
        if (menuBackground != null) menuBackground.dispose();
        if (startButtonTexture != null) startButtonTexture.dispose();
        if (optionsButtonTexture != null) optionsButtonTexture.dispose();
        if (creditsButtonTexture != null) creditsButtonTexture.dispose();
        if (exitButtonTexture != null) exitButtonTexture.dispose();

    }
    
    @Override public void resize(int width, int height) { 
        viewport.update(width, height, true); 
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { 
        if (musicaMenu != null) {
            musicaMenu.pause();
        }
    }
}
