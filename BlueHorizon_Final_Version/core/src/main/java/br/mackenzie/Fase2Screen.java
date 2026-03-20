package br.mackenzie;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Fase2Screen extends FaseBaseScreen{

    public Fase2Screen(Main game) {
        super(game);
    }
        
    // Animação (padrão)
    private Animation<TextureRegion> animacaoCorrida;
    private float stateTimeCorrida; 
    private TextureRegion frameAtual;
    private Texture[] playerTexturesToDispose; 

    // Animação (shield)
    private Animation<TextureRegion> animacaoShield;
    private float stateTimeShield; 
    private TextureRegion frameAtualShield;
    private Texture[] playerTexturesToDisposeShield;
    Texture playerTextureInitialShield;

    // Fases
    FaseBase fasePantano;
    FaseBase faseAtual;
    private Texture fundoChegadaTexture;
    private boolean pertoDoFinal = false;
    private boolean esperandoCicloParalaxe = false;
    private boolean movimentoFinalPlayer = false;

    // Estados de Vitória
    public enum GameState {
        RUNNING,        // Jogo normal, rolando
        VICTORY_SCREEN  // Tela de vitória, estática
    }
    private GameState gameState = GameState.RUNNING;
    private Texture vitoriaImagemTexture;
    private Sound vitoriaSound;
    private Sound torcidaSound;
    private float vitoriaTimer = 0f;
    private final float duracao_vitoria = 16.0f;

    // Inimigos
    private Texture lixoTexture;
    private Texture pneuTexture;
    private Texture jacareTexture;
    private Array<Inimigo> inimigos;
    private float inimigoTimer;

    // Power-Ups
    //Pato
    private Texture patoTexture;
    private Texture patoTimerTexture;
    private Texture playerPatoTexture;
    private float patoTimer = 0f;
    private boolean patoAtivo = false;
    //Shield
    private Texture shieldTexture;
    private Texture shieldTimerTexture;
    private float shieldTimer = 0f;
    private boolean shieldAtivo = false;

    private Array<PowerUp> powerUps;
    private float powerUpTimer;

    //Extra
    private Texture patoShieldPlayerTexture;

    // Vida
    private float qtdVidas = 3;
    private Texture heartTexture;

    // Som 
    Sound patoSound;
    Sound shieldSound;
    Sound damageSound;
    Sound gameOverSound;

    // Pause
    private boolean paused;
    private Texture pauseButtonTexture;
    private Texture playButtonTexture;

    private final float x_pause = 0.15f;
    private final float y_pause = 4.69f;
    private final float width_pause = 0.25f;
    private final float height_pause = 0.23f;

    // Game Over (morrer)
    private boolean gameOver = false;
    private float fadeScreen = 0f;
    private float gameOverTimer = 0f;


    // UI
    private Stage uiStage;
    private BitmapFont fonte; 
    private Texture menu_pause;

    // Barra de Progresso
    private Texture barraAzulClaroTexture;
    private Texture barraAzulEscuroTexture;
    private Texture IconePlayerTexture;
    private Texture chegadaTexture;

    private float progressTime = 0f;
    private float progressRate = 1.0f;
    // Duração total da fase em "segundos de ação" 
    private final float MAX_PROGRESS_TIME = 180f; // 300f
    
    @Override
    public void show() {
        // Inicializa tela
        viewport = new FitViewport(8, 5);

        // Inimigos
        lixoTexture = new Texture("Imagens_Fase2/Lixo.png");
        pneuTexture = new Texture("Imagens_Fase2/Pneu.png");
        jacareTexture = new Texture("Imagens_Fase2/Imagem1_jacare.png");
        inimigos = new Array<>();
        
        // Power-ups
        //Pato
        patoTexture = new Texture("Imagens_Fase2/Pato.png");
        patoTimerTexture = new Texture("Imagens_Fase2/Imagens_UI/Pato_PU.png");
        playerPatoTexture = new Texture("Imagens_Personagem/Imagem_bonus_commit.png");
        //Shield
        shieldTexture = new Texture("Imagens_Fase2/EnergyShield.png");
        shieldTimerTexture = new Texture("Imagens_Fase2/Imagens_UI/Shield_PU.png");
        powerUps = new Array<>();
        //Extra
        patoShieldPlayerTexture = new Texture("Imagens_Fase2/Imagens_player_shield/Imagem_bonus_shield.png");

        // Vida
        heartTexture = new Texture("Imagens_Fase2/heart-life.png");

        // Som 
        patoSound = Gdx.audio.newSound(Gdx.files.internal("Sons/PatoQuack.mp3"));
        shieldSound = Gdx.audio.newSound(Gdx.files.internal("Sons/ShieldActivate.mp3"));
        damageSound = Gdx.audio.newSound(Gdx.files.internal("Sons/damage.mp3"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sons/game-over.mp3"));
        vitoriaSound = Gdx.audio.newSound(Gdx.files.internal("Sons/game-victory.mp3"));
        torcidaSound = Gdx.audio.newSound(Gdx.files.internal("Sons/SomTorcida.mp3"));
        // Pause
        pauseButtonTexture = new Texture("Imagens_Fase2/Imagens_UI/Pause_button.png");
        playButtonTexture = new Texture("Imagens_Fase2/Imagens_UI/Play_button.png");
        menu_pause = new Texture("Imagens_Fase2/Imagens_UI/Menu_fase.png");


        // Player
        //Carregamento dos frames nadador (padrão)
        TextureRegion[] framesNado = new TextureRegion[8];
        playerTexturesToDispose = new Texture[8]; 
        playerTexturesToDisposeShield = new Texture[8];

        //Carregamento dos frames nadador (shield)
        TextureRegion[] framesShield = new TextureRegion[8];
        
        for (int i = 0; i < 8; i++) {
            //Usando for para pegar as imagens do nadador
            Texture t1 = new Texture("Imagens_Personagem/Imagem_" + (i + 1) + "_commit.png");
            Texture t2 = new Texture("Imagens_Fase2/Imagens_player_shield/Imagem" + (i + 1) + "Shield.png");
            framesNado[i] = new TextureRegion(t1);
            framesShield[i] = new TextureRegion(t2);
            playerTexturesToDispose[i] = t1;
            playerTexturesToDisposeShield[i] = t2;
        }
        
        animacaoCorrida = new Animation<>(0.2f, framesNado); // 0.2s de duração por frame
        animacaoShield = new Animation<>(0.2f, framesShield);
        
        //Estado Inicial(padrão)
        stateTimeCorrida = 0f;
        frameAtual = animacaoCorrida.getKeyFrames()[0]; //Definir o primeiro frame como inicial

        //Estado Inicial(shield)
        stateTimeShield = 0f; 
        frameAtualShield = animacaoShield.getKeyFrames()[0];

        //Player configurado (padrão)
        Texture playerTextureInitial = playerTexturesToDispose[0];
        float playerWidth = 2f; 
        float playerHeight = playerWidth * (playerTextureInitial.getHeight() / (float) playerTextureInitial.getWidth());
        player = new Player(playerTextureInitial, -10f, 0.9f, playerWidth, playerHeight, viewport);

        // UI
        uiStage = new Stage();
        fonte = new BitmapFont();

        // Barra de Progresso
        barraAzulEscuroTexture = new Texture("Imagens_Fase2/Barra_de_progresso/barra_azul_escuro.png"); 
        barraAzulClaroTexture = new Texture("Imagens_Fase2/Barra_de_progresso/barra_azul_claro.png");
        IconePlayerTexture = new Texture("Imagens_Fase2/Barra_de_progresso/player_barra.png");
        chegadaTexture = new Texture("Imagens_Fase2/Barra_de_progresso/chegada.png");

        progressTime = 0f;

        // Fase
        fasePantano = new FasePantano(this);
        faseAtual = fasePantano;
        fundoChegadaTexture = new Texture("Imagens_Fase2/Fundo_fase2_chegada.png");
        vitoriaImagemTexture = new Texture("Imagens_Fase2/vitoria.png");
        faseAtual.music.play();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    @Override
    public void render(float dt) {
        dt = Gdx.graphics.getDeltaTime();
        inputsDoSistema();

        ScreenUtils.clear(0, 0, 0, 1);

        if(!paused){
            switch (gameState) {
            case RUNNING:
                updateGameObjects(dt);
                colisoes();
                spawnInimigos(dt);
                spawnPowerUps(dt);

                // Controle dos timers dos power-ups
                if (patoTimer > 0) {
                    patoTimer -= dt;
                    if (patoTimer <= 0) {
                        patoAtivo = false;
                        player.clearPowerUpSprite();
                        faseAtual.setParalaxVelocity(0.75f);
                        progressRate = 1.0f;
                    }
                }

                if (shieldTimer > 0) {
                    shieldTimer -= dt;
                    if (shieldTimer <= 0) {
                        shieldAtivo = false; // Perde imunidade
                    }
                }
                break;
            case VICTORY_SCREEN:
                updateVictoryScreen(dt);
                break;
            }
        }
        switch (gameState) {
            case RUNNING:
                drawGameObjects();
                drawUI();
                break;
            case VICTORY_SCREEN:
                drawVictoryScreen();
                break;
        }

        if (paused) {

            float uiW = uiStage.getViewport().getWorldWidth();
            float uiH = uiStage.getViewport().getWorldHeight();

            final float Menu_Width = uiW * 0.30f;
            final float Menu_Height = Menu_Width * (menu_pause.getHeight() / (float) menu_pause.getWidth());

            float imageX = (uiW - Menu_Width) / 2;
            float imageY = (uiH - Menu_Height) / 2;

            spriteBatch.setProjectionMatrix(uiStage.getCamera().combined);
            spriteBatch.begin();
        
            spriteBatch.draw(menu_pause, imageX, imageY, Menu_Width, Menu_Height);
            
            spriteBatch.end();

            float x_quit = 3.7f;
            float y_quit = 2.05f;
            float width_quit = 0.6f;
            float height_quit = 0.3f;

            float x_resume = 3.7f;
            float y_resume = 2.45f;
            float width_resume = 0.6f;
            float height_resume = 0.3f;


            // LÓGICA DE CLIQUE 
            if (com.badlogic.gdx.Gdx.input.justTouched()) {
                Vector2 touchPoint = new Vector2(com.badlogic.gdx.Gdx.input.getX(), com.badlogic.gdx.Gdx.input.getY());
                viewport.unproject(touchPoint); 

                float inputX = touchPoint.x;
                float inputY = touchPoint.y;

                // Verificação de clique no botão QUIT 
                if (inputX >= x_quit && inputX <= x_quit + width_quit &&
                    inputY >= y_quit && inputY <= y_quit + height_quit) {
                    
                    // Troca de tela para Menu de Fases
                    game.setScreen(new FasesScreen(game));
                }

                // Verificação de clique no botão RESUME 
                if (inputX >= x_resume && inputX <= x_resume + width_resume &&
                    inputY >= y_resume && inputY <= y_resume + height_resume) {
                    
                    // O jogo deixa de estar pausado
                    paused = !paused;
                    if(paused){
                        faseAtual.music.pause();
                    } else {
                        faseAtual.music.play();
                    }  
                }

                // Verificação de clique no botão de PAUSE 
                if (inputX >= x_pause && inputX <= x_pause + width_pause &&
                    inputY >= y_pause && inputY <= y_pause + height_pause) {
                    
                    // O jogo deixa de estar pausado
                    paused = !paused;
                    if(paused){
                        faseAtual.music.pause();
                    } else {
                        faseAtual.music.play();
                    }  
                }
            }
        }

        if (gameOver) {
            faseAtual.music.pause();
            gameOverTimer += dt;
            fadeScreen = Math.min(1f, fadeScreen + dt * 2f); 

                if (fadeScreen > 1f) fadeScreen = 1f;

            // Desenha a tela preta com efeito de escurecendo crescentemente
            ScreenUtils.clear(0, 0, 0, fadeScreen);
            spriteBatch.setProjectionMatrix(uiStage.getCamera().combined);
            spriteBatch.begin();

            // Desenha o texto "GAME OVER"
            fonte.getData().setScale(2.5f);
            fonte.setColor(1, 1, 1, fadeScreen);
            fonte.draw(spriteBatch, "GAME OVER",
            uiStage.getViewport().getWorldWidth() / 2f,
            uiStage.getViewport().getWorldHeight() / 2f,
            0, Align.center, false);
            spriteBatch.end();

            // Após 0.5s, volta pro menu
            if (gameOverTimer >= 3f) {
                game.setScreen(new MenuScreen(game));
            }
        }

    }

    private void updateGameObjects(float dt) {
        // Player
        if (movimentoFinalPlayer) {
        // Se o player está se movendo
        if (player.isMoving()) { 
            // Move o player para a direita em uma velocidade constante
            player.setX(player.getX() + 1.5f * dt); 
        }
    }
        //Atualiza O player 
        player.update(dt);

        // Lógica da Barra de progresso 
        // Se o player está se movendo para frente
        if (player.isMoving()) { 
            progressTime += dt * progressRate;
        }

        // Troca de Fundo no final
        float porcentagem_atual = progressTime / MAX_PROGRESS_TIME;
        final float porcentagem_final = 0.96f;

        if (porcentagem_atual >= porcentagem_final && !pertoDoFinal) {
            pertoDoFinal = true;
            esperandoCicloParalaxe = true;
        }

        // 2. VERIFICAÇÃO DE ESPERA
        if (esperandoCicloParalaxe) {
            
            float currentOffset = faseAtual.backgroundOffsetX;
            float worldWidth = viewport.getWorldWidth();
            
            // Calcula o quanto falta para o próximo ciclo
            float distanceIntoCurrentCycle = currentOffset % worldWidth;
            
            if (distanceIntoCurrentCycle < 0.1f) { 
                
                faseAtual.setBackground(fundoChegadaTexture);
                esperandoCicloParalaxe = false; 

                faseAtual.setParalaxVelocity(0.0f);

                movimentoFinalPlayer = true;
                
                faseAtual.backgroundOffsetX = 0;

                inimigos.clear();   // Remove todos os inimigos da tela na hora
                powerUps.clear();   // Remove todos os powerups da tela na hora
            }
        }

        // Verifica a vitória
        if (progressTime >= MAX_PROGRESS_TIME && gameState == GameState.RUNNING) {
            gameState = GameState.VICTORY_SCREEN;
            vitoriaTimer = 0f;
            faseAtual.music.pause();
            vitoriaSound.play();
            torcidaSound.play();

        }

        // Lógica da Animação: Usa a variável de estado 'shieldAtivo' para escolher a animação
        if (shieldAtivo) { // Se o Shield estiver ativo, atualiza a animação do Shield
            if (player.isMoving()) {
                stateTimeShield += dt; // Atualiza o tempo do Shield
                frameAtualShield = animacaoShield.getKeyFrame(stateTimeShield, true); 
            } else {
                stateTimeShield = 0f;
                frameAtualShield = animacaoShield.getKeyFrames()[0];
            }
        } else { // Caso contrário (Shield inativo), atualiza a animação Padrão
            if (player.isMoving()) {
                stateTimeCorrida += dt;
                frameAtual = animacaoCorrida.getKeyFrame(stateTimeCorrida, true); 
            } else {
                //Se parado, mostra o frame inicial
                stateTimeCorrida = 0f;
                frameAtual = animacaoCorrida.getKeyFrame(stateTimeCorrida); 
            }
        }

        faseAtual.update();

        // Inimigos
        for (int i = inimigos.size - 1; i >= 0; i--) {
            Inimigo ini = inimigos.get(i);
            ini.update(dt);
            if (ini.getBounds().x < -ini.getBounds().width) {
                inimigos.removeIndex(i);
            }
        }

        // Power-Ups
        for (int i = powerUps.size - 1; i >= 0; i--) {
            PowerUp pu = powerUps.get(i);
            pu.update(dt);

            if (pu.getBounds().x < -pu.getBounds().width) {
                powerUps.removeIndex(i);
            }
        }
    }

    private void updateVictoryScreen(float dt) {
        vitoriaTimer += dt;

        // O tempo acabou, voltar para o menu de fases
        if (vitoriaTimer >= duracao_vitoria) {
            
            Main.setMaxFaseLiberada(3);
            game.setScreen(new FasesScreen(game)); 
            
            dispose(); 
        }
    }

    private void spawnInimigos(float dt){
        // Lógica inimigos
        if (pertoDoFinal) { 
            return;
        }

        inimigoTimer += dt;
        if (inimigoTimer > 1f) {
            inimigoTimer = 0;
            float worldWidth = viewport.getWorldWidth();

            Inimigo newInimigo = null;
            int chanceInimigo = MathUtils.random(0, 6);

            int slotY = MathUtils.random(0, 2); 
            float spawnY; 

            switch (slotY) {
                case 0:
                    spawnY = 1.7f; //Raia Alta
                    break;
                case 1:
                    spawnY = 0.9f; //Raia Média
                    break;
                default:
                    spawnY = 0.1f; //Raia Baixa
                    break;
            }

            switch (chanceInimigo) {
                case 0:
                    newInimigo = new Inimigo(pneuTexture, worldWidth, spawnY, 0.5f, 0.5f,this);
                    break;
                case 1:
                    newInimigo = new Inimigo(lixoTexture, worldWidth, spawnY, 0.5f, 0.5f,this);
                    break;
                case 2:
                    newInimigo = new Inimigo(jacareTexture, worldWidth, spawnY, 0.7f, 0.6f,this);
                    break;
                default:
                    break;
            }
            if (newInimigo != null) {
                inimigos.add(newInimigo);
            }
        }
    }
    private void spawnPowerUps(float dt){
        // Lógica power-ups
        if (pertoDoFinal) { 
            return;
        }

        powerUpTimer += dt;
        if (powerUpTimer > 3f) { 
            powerUpTimer = 0;

            float worldWidth = viewport.getWorldWidth();
            float puWidth = 0.75f;
            float puHeight = puWidth * (patoTexture.getHeight() / (float) patoTexture.getWidth());

            PowerUp powerUp = null;
            int chancePowerUp = MathUtils.random(0, 6);
            float spawnY; 
            int slotY = MathUtils.random(0, 2);

            switch (slotY) {
                case 0:
                    spawnY = 1.7f; //Raia Alta
                    break;
                case 1:
                    spawnY = 0.9f; //Raia Média
                    break;
                default:
                    spawnY = 0.1f; //Raia Baixa
                    break;
            }

            switch (chancePowerUp) {
                case 0:
                    powerUp = new Speed(patoTexture, worldWidth, spawnY, puWidth, puHeight, patoSound,this);
                    break;
                case 1:
                    powerUp = new Shield(shieldTexture, worldWidth, spawnY, puWidth, puHeight, shieldSound,this);
                    break;
            }
            if (powerUp != null) {
                powerUps.add(powerUp);
            }
        }
    }
    private void colisoes(){
        // Player vs Inimigos
        
        for (int i = inimigos.size - 1; i >= 0; i--) {
            Inimigo ini = inimigos.get(i);
            if (player.getBounds().overlaps(ini.getBounds())) {
                inimigos.removeIndex(i);
                damageSound.play();

                // Lógica por trás de se você toma dano perde escudo na hr
                if(shieldAtivo){
                    shieldAtivo = false;
                    shieldTimer = 0.01f;   // Assim eu garanto que nao haja mais shield

                    if(patoAtivo){
                        player.setPowerUpSprite(playerPatoTexture);
                    }
                } else{
                    qtdVidas = qtdVidas-1;
                    // Aqui fica legal implementar efeito sonoro perdendo vida de X na tela
                }
            }
        }

        // Verifica se o jogador perdeu todas as vidas
        if (qtdVidas <= 0 && !gameOver) {
            // Para a música da fase
            damageSound.pause();
            gameOverSound.play();
            gameOver = true;
            fadeScreen = 0f;
            gameOverTimer = 0f;
        }

        // Player vs Power-Ups
        for (int i = powerUps.size - 1; i >= 0; i--) {
            PowerUp pu = powerUps.get(i);
            if (pu.getBounds().x < -pu.getBounds().width) {
                powerUps.removeIndex(i);
                continue; 
            }

            if (player.getBounds().overlaps(pu.getBounds())) {
                if (pu instanceof Speed) {
                    patoTimer = 15f;
                    patoAtivo = true;
                    patoSound.play();
                    player.setPowerUpSprite(playerPatoTexture);
                    faseAtual.setParalaxVelocity(1.5f);
                    progressRate = 2.0f;

                    // Dura 15 segundos
                } else if (pu instanceof Shield) {
                    shieldAtivo = true;  // nave fica imune
                    shieldTimer = 15f;  // 15 segundos de duração
                    shieldSound.play();
                    player.setPowerUpSprite(playerTextureInitialShield);
                }
                powerUps.removeIndex(i);
            }
        }
    }
    

    private void drawGameObjects() {
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();
    
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();
        
        // Desenho do fundo paralaxe 
        float x1 = faseAtual.backgroundOffsetX % worldWidth;
        if (x1 > 0){
            x1 -= worldWidth; // Garante a rolagem contínua 

        }

        spriteBatch.draw(faseAtual.background, x1, 0, worldWidth, worldHeight); 
        spriteBatch.draw(faseAtual.background, x1 + worldWidth, 0, worldWidth, worldHeight);
        
        // Lógica desenhar a trocar de sprites nos usos dos power_ups
        if (patoAtivo && shieldAtivo) { 
            spriteBatch.draw(patoShieldPlayerTexture, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        } 
        else if (shieldAtivo) {
            spriteBatch.draw(frameAtualShield, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        } 
        else if (patoAtivo) { 
            spriteBatch.draw(player.getPowerUpSprite(), player.getX(), player.getY(), player.getWidth(), player.getHeight());
        } 
        else { 
            spriteBatch.draw(frameAtual, player.getX(), player.getY(), player.getWidth(), player.getHeight());
        }
        
        // Desenho dos inimigos e power-ups 
        for (Inimigo ini : inimigos) {
            ini.draw(spriteBatch);
        }

        for(PowerUp powerUp : powerUps){
            powerUp.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    private void drawVictoryScreen() {
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();
        
        // Desenha a imagem preenchendo toda a viewport do mundo
        float w = viewport.getWorldWidth();
        float h = viewport.getWorldHeight();
        
        // Desenha a imagem nas coordenadas (0, 0) com a largura e altura do mundo
        spriteBatch.draw(vitoriaImagemTexture, 0, 0, w, h);
        
        spriteBatch.end();
    }

    private void drawUI() {
        uiStage.act();
        uiStage.draw();

        // Pega tamanho do "mundo" do UI Stage
        float uiWidth = uiStage.getViewport().getWorldWidth();
        float uiHeight = uiStage.getViewport().getWorldHeight();

        spriteBatch.setProjectionMatrix(uiStage.getCamera().combined);

        spriteBatch.begin();

        // Dimensões dos ícones de power-up
        final float Icone_Width = 190f;
        final float Icone_Height = 100f;
        final float Padding = 10f;
        final float Spacing_H = 5f; 

        // Posição Y é a mesma para ambos
        float y = uiHeight - Icone_Height - Padding - 50f;

        // Posição X dos slots
        float xSlotDireita = uiWidth - Icone_Width - Padding + 30f;
        float xSlotEsquerda = xSlotDireita - Icone_Width - Spacing_H + 60f;


        // Verifica se ambos estão ativos
        if (patoTimer > 0 && shieldTimer > 0) {
            
            // Pato 
            spriteBatch.draw(patoTimerTexture, xSlotDireita, y, Icone_Width, Icone_Height);
            String timeTextPato = "" + (int) patoTimer;
            fonte.draw(spriteBatch, timeTextPato, xSlotDireita + Icone_Width / 2f, y - 5f, 0, Align.center, false);

            // Shield 
            spriteBatch.draw(shieldTimerTexture, xSlotEsquerda, y, Icone_Width, Icone_Height);
            String timeTextShield = "" + (int) shieldTimer;
            fonte.draw(spriteBatch, timeTextShield, xSlotEsquerda + Icone_Width / 2f, y - 5f, 0, Align.center, false);

        // Verifica se apenas o Pato está ativo 
        } else if (patoTimer > 0) {
            
            // Pato 
            spriteBatch.draw(patoTimerTexture, xSlotDireita, y, Icone_Width, Icone_Height);
            String timeTextPato = "" + (int) patoTimer;
            fonte.draw(spriteBatch, timeTextPato, xSlotDireita + Icone_Width / 2f, y - 5f, 0, Align.center, false);

        // Verifica se apenas o Shield está ativo
        } else if (shieldTimer > 0) {
            
            // Shield 
            spriteBatch.draw(shieldTimerTexture, xSlotDireita, y, Icone_Width, Icone_Height);
            String timeTextShield = "" + (int) shieldTimer;
            fonte.draw(spriteBatch, timeTextShield, xSlotDireita + Icone_Width / 2f, y - 5f, 0, Align.center, false);
        }

        // Desenho do Botão de Pause com coordenadas de UI
        final float Pause_Width = 130f; 
        final float Pause_Height = 85f;
        final float Pause_Padding = 1f;

        // Coordenadas em pixels para o canto superior esquerdo
        float pauseX = Pause_Padding; 
        float pauseY = uiHeight - Pause_Height - Pause_Padding;

        Texture currentButtonTexture = paused ? playButtonTexture : pauseButtonTexture;

        spriteBatch.draw(currentButtonTexture, pauseX, pauseY, Pause_Width, Pause_Height);

        // Desenho da Barra de progresso

        final float BAR_W = uiWidth * 0.6f;
        final float BAR_H = uiHeight * 0.02f; 
        final float BAR_PADDING_Y = 30f; 

        float barX = (uiWidth - BAR_W) / 2;
        float barY = uiHeight - BAR_H - BAR_PADDING_Y; 

        // Calcular o Progresso
        float progressRatio = progressTime / MAX_PROGRESS_TIME;
        progressRatio = Math.min(progressRatio, 1.0f); // Limita a 100%
        float fillWidth = BAR_W * progressRatio;

        // Desenhar o Fundo da Barra
        spriteBatch.draw(barraAzulEscuroTexture, barX, barY, BAR_W, BAR_H);

        // Desenhar o Preenchimento
        spriteBatch.draw(barraAzulClaroTexture, barX, barY, fillWidth, BAR_H);

        // Desenhar Linha de Chegada 
        final float CHEGADA_WIDTH = 40f; 
        final float CHEGADA_HEIGHT = BAR_H * 1.5f; 

        float chegadaX = barX + BAR_W - (CHEGADA_WIDTH / 2f);
        float chegadaY = barY - (CHEGADA_HEIGHT - BAR_H) / 2f;

        spriteBatch.draw(chegadaTexture, chegadaX, chegadaY + 2f, CHEGADA_WIDTH, CHEGADA_HEIGHT);

        // 1. Converte a proporção (0.0 a 1.0) para porcentagem (0 a 100)
        int percentage = (int) (progressRatio * 100);
        String percentageText = percentage + "%";

        // 2. Define a posição (Ex: Centralizado acima da barra)
        float textX = barX + BAR_W / 2f; 
        float textY = barY + BAR_H + 15f; // 15f unidades acima da barra

        // 3. Desenha o texto (Usando sua fonte existente)
        // font.draw(spriteBatch, String, float x, float y, float targetWidth, int align, boolean wrap)
        fonte.draw(spriteBatch, percentageText, textX, textY, 0, Align.center, false);


        // Desenhar o Ícone do Jogador na Barra
        final float PLAYER_ICON_SIZE = BAR_H * 1.5f; 

        // Posição X e Y
        float playerIconX = barX + fillWidth - (PLAYER_ICON_SIZE / 2);
        float playerIconY = barY - (PLAYER_ICON_SIZE - BAR_H) / 2; 

        // Desenha quantidade vidas como coracoes no canto direito em cima
        float heartWidth = 50f;
        float heartHeight = 40f;
        float spacing = 10f;

        float totalWidth = (heartWidth + spacing) * 3 - spacing;
        float heartsStartX = uiStage.getViewport().getWorldWidth() - totalWidth - 15f;
        float heartsY = uiStage.getViewport().getWorldHeight() - heartHeight - 25f;

        for (int i = 0; i < 3; i++) {
            float x = heartsStartX + (heartWidth + spacing) * i;

            if (i < qtdVidas) {
                spriteBatch.draw(heartTexture, x, heartsY, heartWidth, heartHeight);
            } else {
                spriteBatch.setColor(1f, 1f, 1f, 0.3f);
                spriteBatch.draw(heartTexture, x, heartsY, heartWidth, heartHeight);
                spriteBatch.setColor(1f, 1f, 1f, 1f);
            }
        }
        spriteBatch.draw(IconePlayerTexture, playerIconX, playerIconY, PLAYER_ICON_SIZE, PLAYER_ICON_SIZE);


        spriteBatch.end();
    }

    @Override
    public void dispose() { 
    
        for (Texture t : playerTexturesToDispose) {
            if (t != null) {
                t.dispose();
            }
        }
        for (Texture t : playerTexturesToDisposeShield) {
            if (t != null) {
                t.dispose();
            }
        }
        pneuTexture.dispose();
        lixoTexture.dispose();
        jacareTexture.dispose();
        patoTexture.dispose();
        patoTimerTexture.dispose();
        playerPatoTexture.dispose();
        shieldTexture.dispose();
        shieldTimerTexture.dispose();
        pauseButtonTexture.dispose();
        playButtonTexture.dispose();
        patoSound.dispose();
        shieldSound.dispose();
        damageSound.dispose();
        fonte.dispose();
        faseAtual.dispose();
        fundoChegadaTexture.dispose();
        heartTexture.dispose();
    }

    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { faseAtual.music.pause(); }

    private void inputsDoSistema(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
            if(paused){
                faseAtual.music.pause();
            } else {
                faseAtual.music.play();
            }
        }
    }
}
