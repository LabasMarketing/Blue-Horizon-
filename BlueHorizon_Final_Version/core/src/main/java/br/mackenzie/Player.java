package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player extends GameObject{
    private final Viewport viewport;
    private boolean isMoving = false;
    private Texture powerUpSprite = null;
    private final Sound somTrocaRaia = Gdx.audio.newSound(Gdx.files.internal("Sons/swim.mp3"));

    private final float Posicao_Fixa_x = -10f; // Posição X do nadador
    private final float Raia_Alta_y = 1.35f; // Posição Y da Raia Alta
    private final float Raia_Media_y = 0.6f; // Posição Y da Raia Média
    private final float Raia_Baixa_y = -0.2f; // Posição Y da Raia Baixa
    private float targetY;

    public Player(Texture texture, float x, float y, float width, float height, Viewport viewport) {
        super(texture, x, y, width, height);
        this.viewport = viewport;
        this.targetY = Raia_Media_y; 
        sprite.setY(targetY);
        sprite.setX(Posicao_Fixa_x);
    }

   @Override
    public void update(float dt){
        float worldWidth = viewport.getWorldWidth();
        float playerWidth = sprite.getWidth();
        float worldHeight = viewport.getWorldHeight();
        float playerHeight = sprite.getHeight();
        
        isMoving = false; 

        // NADADOR
        // Para frente e para trás
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            isMoving = true; 
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            isMoving = true; 
        }
        // Para cima e para baixo
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)){
            // Tenta pular para a raia mais alta
            somTrocaRaia.play();
            if (targetY == Raia_Baixa_y) {
                targetY = Raia_Media_y;
            } else if (targetY == Raia_Media_y) {
                targetY = Raia_Alta_y;
            }
        } else if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)){
            // Tenta pular para a raia mais baixa
            somTrocaRaia.play();
            if (targetY == Raia_Alta_y) {
                targetY = Raia_Media_y;
            } else if (targetY == Raia_Media_y) {
                targetY = Raia_Baixa_y;
            }
        }

        sprite.setY(targetY);

        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - playerWidth));
        sprite.setY(MathUtils.clamp(sprite.getY(), -1, worldHeight - playerHeight));

    }

    public void setX(float newX) {
        sprite.setX(newX);
    }

    public void clearPowerUpSprite() {
        this.powerUpSprite = null;
    }

    public void setPowerUpSprite(Texture texture) {
        this.powerUpSprite = texture;
    }

    public Texture getPowerUpSprite() {
        return powerUpSprite;
    }

    public boolean isMoving() {
        return isMoving;
    }
}