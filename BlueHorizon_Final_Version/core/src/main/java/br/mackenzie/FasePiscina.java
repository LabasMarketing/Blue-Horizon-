package br.mackenzie;

public class FasePiscina extends FaseBase {
    public FasePiscina(FaseBaseScreen game) {
        super("Imagens_Fase1/Fundo_fase1.png", "Sons_Fase1/GameMusic.mp3", game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
