package br.mackenzie;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class GameObject {
    protected Sprite sprite;
    protected Rectangle bounds;

    public GameObject(Texture texture, float x, float y, float width, float height){
        this.sprite = new Sprite(texture);
        this.sprite.setSize(width,height);
        this.sprite.setPosition(x,y);

        this.bounds = new Rectangle(x,y,width,height);
    }

    public abstract void update(float dt);

    public void draw(SpriteBatch batch){
        sprite.draw(batch);
    }

    public float getX(){
        return sprite.getX();
    }
    public float getY(){
        return sprite.getY();
    }
    public float getWidth(){
        return sprite.getWidth();
    }
    public float getHeight(){
        return sprite.getHeight();
    }

    public Rectangle getBounds(){
        // Reduz a área de colisão
        float paddingX = sprite.getWidth() * 0.10f; 
        float paddingY = sprite.getHeight() * 0.10f; 
        
        bounds.set(
            sprite.getX() + paddingX, 
            sprite.getY() + paddingY, 
            sprite.getWidth() - 2 * paddingX, 
            sprite.getHeight() - 2 * paddingY
        );
        return bounds;
    }

    public void setPosition(float x, float y){
        sprite.setPosition(x, y);
    }

    public void dispose() {}
}
