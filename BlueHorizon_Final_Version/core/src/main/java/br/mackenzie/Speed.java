package br.mackenzie;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Speed extends PowerUp{
    public Speed(Texture texture, float x, float y, float width, float height, Sound som, FaseBaseScreen game) {
        super(texture, x, y, width, height, game);
    }
}
