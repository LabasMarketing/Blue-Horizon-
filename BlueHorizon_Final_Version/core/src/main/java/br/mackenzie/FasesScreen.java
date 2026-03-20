package br.mackenzie;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class FasesScreen implements Screen{
    private final Main game;
    private final SpriteBatch spriteBatch;
    private FitViewport viewport;

    private Texture menuBackground;
    private Texture faseAtualTexture;
    private Texture fase1Texture;
    private Texture fase2Texture;
    private Texture fase3Texture;

    private float x_painel;
    private float y_painel;
    private float width_painel;
    private float height_painel;

    private float x_voltar;
    private float y_voltar;
    private float width_voltar;
    private float height_voltar;

    int maxFaseLiberada;

    public FasesScreen(Main game) {
        this.game = game;
        this.spriteBatch = game.spriteBatch;
    }

    @Override
    public void show() {
        menuBackground = new Texture("Menu/Fundo.png");
        fase1Texture = new Texture("Menu/OpcaoPlay/Img_1.png");
        fase2Texture = new Texture("Menu/OpcaoPlay/Img_2.png");
        fase3Texture = new Texture("Menu/OpcaoPlay/Img_3.png");

        viewport = new FitViewport(8, 5); 
        viewport.apply();

        width_painel = 8f;
        height_painel = 4f;

        x_painel = (viewport.getWorldWidth() / 2) - (width_painel / 2);
        y_painel = (viewport.getWorldHeight() / 2) - (height_painel / 2);

        x_voltar = 1.02f;
        y_voltar = 3.57f;
        width_voltar = 0.6f;
        height_voltar = 0.25f;

        maxFaseLiberada = Main.getMaxFaseLiberada();

        switch (maxFaseLiberada) {
            case 1:
                faseAtualTexture = fase1Texture; 
                break;
            case 2:
                faseAtualTexture = fase2Texture; 
                break;
            default:
                faseAtualTexture = fase3Texture; 
                break;
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
        // Desenha o fundo ocupando toda a tela
        spriteBatch.draw(menuBackground, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        spriteBatch.draw(faseAtualTexture, x_painel, y_painel, width_painel, height_painel);

        spriteBatch.end();

        // Lógica de cliques
        if (com.badlogic.gdx.Gdx.input.justTouched()) {
            
            // Converte as coordenadas da tela (pixels) para o mundo (float)
            Vector2 touchPoint = new Vector2(com.badlogic.gdx.Gdx.input.getX(), com.badlogic.gdx.Gdx.input.getY());
            viewport.unproject(touchPoint); 

            float inputX = touchPoint.x;
            float inputY = touchPoint.y;

            if (inputX >= 1f && inputX <= 1f + 1.7f &&
                inputY >= 1.5f && inputY <= 1.5f + 1.4f) {
                
                MenuScreen.stopMusica();
                game.setScreen(new Fase1Screen(game));
            }

            if (inputX >= 3.1f && inputX <= 3.1f + 1.7f &&
                inputY >= 1.5f && inputY <= 1.5f + 1.4f && maxFaseLiberada == 2) {
                
                MenuScreen.stopMusica();
                game.setScreen(new Fase2Screen(game));
            }

            if (inputX >= 5.2f && inputX <= 5.2f + 1.7f &&
                inputY >= 1.5f && inputY <= 1.5f + 1.4f && maxFaseLiberada == 3) {
                
                game.setScreen(new Fase3Screen(game));
            }

            if (inputX >= x_voltar && inputX <= x_voltar + width_voltar &&
                inputY >= y_voltar && inputY <= y_voltar + height_voltar) {
                
                game.setScreen(new MenuScreen(game));
            }
        }
    }

    @Override public void resize(int width, int height) { 
        viewport.update(width, height, true); 
    }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() {
        
    }
    @Override
    public void dispose() {
        if (menuBackground != null) menuBackground.dispose();
        if (fase1Texture != null) fase1Texture.dispose();
    }
}
