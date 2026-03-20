package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;

public class PowerUp extends GameObject {
    private final FaseBaseScreen game;
    

    public PowerUp(Texture texture, float x, float y, float width, float height, FaseBaseScreen game) {
        super(texture, x, y, width, height);
        this.game = game;
    }

    @Override
    public void update(float dt) {
        // Movimento
        sprite.translateX(-2f * dt);
    }
}
