package br.mackenzie;

public class FaseEspaco extends FaseBase{
    public FaseEspaco(FaseBaseScreen game) {
        super("Imagens_Fase3/Fundo_fase3.png", "Sons_Fase3/GameMusic.mp3", game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
