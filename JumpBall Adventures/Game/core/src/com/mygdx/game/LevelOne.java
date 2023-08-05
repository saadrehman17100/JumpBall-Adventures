package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class LevelOne {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Texture boxTexture, grassTexture, obstacleTexture;
    private final Rectangle box;
    private final Rectangle[] grass;
    private final Rectangle[] obstacles;

    private final Texture backgroundTexture;
    private final Texture heartTexture;

    private  boolean isJumping;
    private  float jumpVelocity;
    private final float gravity;
    private final Sound jumpSound,colSound,newLevelSound;
    private Music backgroundMusic;

    private boolean isPaused;
    private final MyGdxGame game;
    PauseMenuScreen pauseMenuScreen;
    private Rectangle[] hearts;
    private int lives = 3;

    public LevelOne(final  MyGdxGame game) {
        this.game  = game;
        backgroundTexture = new Texture("background.jpg");
        pauseMenuScreen = new PauseMenuScreen(game);
        isPaused = false;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
        colSound = Gdx.audio.newSound(Gdx.files.internal("col.mp3"));
        newLevelSound  = Gdx.audio.newSound(Gdx.files.internal("new-level.mp3"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true); 
        backgroundMusic.setVolume(0.5f); 
        backgroundMusic.play(); 

        batch = new SpriteBatch();

        boxTexture = new Texture("box.png"); 
        box = new Rectangle(0, 68, 32, 32); 

        grassTexture = new Texture("grass.png"); 
        grass = new Rectangle[35]; 

        for (int i = 0; i < grass.length; i++) {
            float grassWidth = 64; 
            float grassHeight = 64; 
            float grassX = i * 60; 
            float grassY = 0; 
            grass[i] = new Rectangle(grassX, grassY, grassWidth, grassHeight);
        }

        
        obstacleTexture = new Texture("obstacle.png");
        obstacles = new Rectangle[13];

        for (int i = 0; i < obstacles.length; i++) {
            float obstacleWidth = 64; 
            float obstacleHeight = 64; 
            float obstacleX = 150 + i * 150; 
            float obstacleY = 68;
            obstacles[i] = new Rectangle(obstacleX,obstacleY,obstacleWidth,obstacleHeight);
        }

        heartTexture = new Texture("live.png");

        hearts = new Rectangle[lives];
        hearts[0] = new Rectangle(0,1000,64,64);
        hearts[1] = new Rectangle(100,1000,64,64);
        hearts[2] = new Rectangle(200,1000,64,64);

        isJumping = false;
        jumpVelocity = 0;
        gravity = -1000;
    }

    public void update(float deltaTime) {
        if (!isPaused) {

            handleInput(deltaTime);
            applyPhysics(deltaTime);
            checkCollisions();
            checkLevelPassed();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused;
            if (isPaused) {
                game.setScreen(pauseMenuScreen);
            }
        }



    }

    public void resetLevel() {
        box.x = 0;
        box.y = 68;
        isJumping = false;
        jumpVelocity = 0;
    }

    private void checkLevelPassed() {
        if (box.x + box.width == Gdx.graphics.getWidth()) {
            newLevelSound.play();
            ((MyGdxGame) Gdx.app.getApplicationListener()).showLevelPassedScreen(2);
            System.out.println("Level Passed!");
        }
    }

    private void handleInput(float deltaTime) {
        float speed = 300f;
        float screenWidth = Gdx.graphics.getWidth();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            box.x -= speed * deltaTime;
            if (box.x < 0) {
                box.x = 0;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            box.x += speed * deltaTime;
            if (box.x + box.width > screenWidth) {
                box.x = screenWidth - box.width;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isJumping) {
            isJumping = true;
            jumpVelocity = 500;
            playJumpSound();

        }


    }

    private void applyPhysics(float deltaTime) {

        if (isJumping) {
            jumpVelocity += gravity * deltaTime;
            box.y += jumpVelocity * deltaTime;

            if (box.y <= 0) {
                box.y = 0;
                isJumping = false;
            }
        }
    }

    private void checkCollisions() {
        for (Rectangle grassTile : grass) {
            if (box.overlaps(grassTile)) {
                box.y = grassTile.y + grassTile.height;
                isJumping = false;
            }
        }

        for (Rectangle obstacle : obstacles) {
            if (box.overlaps(obstacle)) {
                if(lives > 0){
                    lives --;
                    if (lives < hearts.length) {
                        hearts[lives] = null;
                    }
                    resetLevel();
                }else{
                    playGameoverSound();
                    isPaused = false;
                    ((MyGdxGame) Gdx.app.getApplicationListener()).showGameOverScreen(1);
                }
                updateHeartPositions();


            }
        }
    }

    private void updateHeartPositions() {
        float heartX = 0;
        float heartY = 1000;

        for (int i = 0; i < hearts.length; i++) {
            if (hearts[i] != null) {
                hearts[i].setPosition(heartX, heartY);
                heartX += hearts[i].width + 5; // Adjust the heart spacing if needed
            }
        }
    }

    private void playGameoverSound() {
        colSound.play();
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for (Rectangle grassTile : grass) {
            batch.draw(grassTexture, grassTile.x, grassTile.y);
        }

        for (Rectangle obstacle : obstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y);
        }

        for(Rectangle h:hearts){
            if(h!=null){
                batch.draw(heartTexture,h.x,h.y);
            }
        }

        batch.draw(boxTexture, box.x, box.y);

        batch.end();
    }

    private void playJumpSound() {

        jumpSound.play();
    }
    public void dispose() {
        boxTexture.dispose();
        grassTexture.dispose();
        obstacleTexture.dispose();
        jumpSound.dispose();
        batch.dispose();
        backgroundTexture.dispose();

    }
}
