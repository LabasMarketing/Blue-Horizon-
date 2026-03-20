package br.mackenzie;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
    public SpriteBatch spriteBatch;
    private static int maxFaseLiberada = 1;


    public static int getMaxFaseLiberada() {
        return maxFaseLiberada;
    }

    public static void setMaxFaseLiberada(int nivel) {
        // Só atualiza se o novo nível for maior que o atual
        if (nivel > maxFaseLiberada) {
            maxFaseLiberada = nivel;
        }
    }

    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); 

        setScreen(new MenuScreen(this)); 
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (spriteBatch != null) {
            spriteBatch.dispose();
        }
    }
}