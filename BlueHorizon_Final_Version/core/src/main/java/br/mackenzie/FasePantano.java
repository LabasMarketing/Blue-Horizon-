package br.mackenzie;

public class FasePantano extends FaseBase{
    public FasePantano(FaseBaseScreen game) {
        super("Imagens_Fase2/Fundo_fase2.png", "Sons_Fase2/GameMusic.mp3", game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
