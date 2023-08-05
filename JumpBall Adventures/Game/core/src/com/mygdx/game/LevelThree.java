package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class LevelThree {
    private final OrthographicCamera camera;
    private final SpriteBatch batch;
    private final Texture boxTexture, grassTexture, obstacleTexture, platformTexture;
    private final Rectangle box;
    private final Rectangle[] grass;

    private final Texture backgroundTexture;
    private int lives = 3;

    private boolean isJumping;
    private float jumpVelocity;
    private final float gravity;
    private final Sound jumpSound, colSound, newLevelSound;
    private Music backgroundMusic;

    private boolean isPaused;
    private final MyGdxGame game;
    PauseMenuScreen pauseMenuScreen;

    
    private Rectangle[] movingObstacles;
    private Rectangle[] jumpingObstacles;
    private Rectangle[] platforms;
    private float obstacleSpeed;
    private final float obstacleMovementDistance = 100; 
    private boolean isMovingRight = true;
    private boolean[] isObstacleJumping;

    private boolean isBoxJumping; 
    private float boxJumpVelocity; 
    private float[] obstacleJumpVelocity;
    private Texture heartTexture;
    private Rectangle[] hearts;

    public LevelThree(final MyGdxGame game) {
        this.game = game;
        backgroundTexture = new Texture("background3.jpg");
        pauseMenuScreen = new PauseMenuScreen(game); 
        isPaused = false;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.mp3")); 
        colSound = Gdx.audio.newSound(Gdx.files.internal("col.mp3"));
        newLevelSound = Gdx.audio.newSound(Gdx.files.internal("new-level.mp3"));
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
        platformTexture = new Texture("platform.png"); 
        movingObstacles = new Rectangle[7];
        jumpingObstacles = new Rectangle[2];
        platforms = new Rectangle[7];
        isObstacleJumping = new boolean[2];
        obstacleJumpVelocity = new float[2];

        float obstacleWidth = 64; 
        float obstacleHeight = 64; 
        float obstacleY = 68; 
        float platformWidth = 128;
        float platformHeight = 32;
        float platformY = 150;

        jumpingObstacles[0] = new Rectangle(600, 0, obstacleWidth, 32);
        jumpingObstacles[1] = new Rectangle(1000, 0, obstacleWidth, 32);

        movingObstacles[0] = new Rectangle(400, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[1] = new Rectangle(600, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[2] = new Rectangle(800, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[3] = new Rectangle(1000, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[4] = new Rectangle(1200, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[5] = new Rectangle(1400, obstacleY, obstacleWidth, obstacleHeight);
        movingObstacles[6] = new Rectangle(1600, obstacleY, obstacleWidth, obstacleHeight);



        platforms[0] = new Rectangle(300, platformY, platformWidth, platformHeight);
        platforms[1] = new Rectangle(450, 300, platformWidth, platformHeight);
        platforms[2] = new Rectangle(600, 450, platformWidth, platformHeight);
        platforms[3] = new Rectangle(800, 450, platformWidth, platformHeight);
        platforms[5] = new Rectangle(1000, 450, platformWidth, platformHeight);
        platforms[6] = new Rectangle(1200, 350, platformWidth, platformHeight);
        platforms[4] = new Rectangle(1400, platformY, platformWidth, platformHeight);

        heartTexture = new Texture("live.png");

        hearts = new Rectangle[lives];
        hearts[0] = new Rectangle(0,1000,64,64);
        hearts[1] = new Rectangle(100,1000,64,64);
        hearts[2] = new Rectangle(200,1000,64,64);
        
        obstacleSpeed = 100; 

        isJumping = false;
        jumpVelocity = 0;
        gravity = -1000; 
        isBoxJumping = false; 
        boxJumpVelocity = 0; 
        for (int i = 0; i < 2; i++) {
            isObstacleJumping[i] = false; 
            obstacleJumpVelocity[i] = 0; 
        }
    }

    public void update(float deltaTime) {
        if (!isPaused) {
            
            handleInput(deltaTime);
            applyPhysics(deltaTime);
            checkCollisions();
            checkLevelPassed();
            updateMovingObstacles(deltaTime);
            updateVerticalMovingObstacles(deltaTime);

            
            for (int i = 0; i < jumpingObstacles.length; i++) {
                if (!isObstacleJumping[i]) {
                    isObstacleJumping[i] = true;
                    obstacleJumpVelocity[i] = 500; 
                }

                obstacleJumpVelocity[i] += gravity * deltaTime;
                jumpingObstacles[i].y += obstacleJumpVelocity[i] * deltaTime;

                
                if (jumpingObstacles[i].y <= 0) {
                    jumpingObstacles[i].y = 0;
                    isObstacleJumping[i] = false;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            isPaused = !isPaused; 
            if (isPaused) {
                
                game.setScreen(pauseMenuScreen);
            }
        }
    }

    private void updateVerticalMovingObstacles(float deltaTime) {
        float movementDelta = obstacleSpeed * deltaTime;

        
        for (Rectangle obstacle : jumpingObstacles) {
            if (!isObstacleJumping[getObstacleIndex(obstacle)]) {
                isObstacleJumping[getObstacleIndex(obstacle)] = true;
                obstacleJumpVelocity[getObstacleIndex(obstacle)] = 900;
            }

            obstacleJumpVelocity[getObstacleIndex(obstacle)] += gravity * deltaTime;
            obstacle.y += obstacleJumpVelocity[getObstacleIndex(obstacle)] * deltaTime;

            
            if (obstacle.y <= 0) {
                obstacle.y = 0;
                isObstacleJumping[getObstacleIndex(obstacle)] = false;
            }
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

        
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isBoxJumping) {
            isBoxJumping = true;
            boxJumpVelocity = 500; 
            playJumpSound();
        }
    }

    private void applyPhysics(float deltaTime) {
        if (isBoxJumping) {
            boxJumpVelocity += gravity * deltaTime;
            box.y += boxJumpVelocity * deltaTime;

            
            if (box.y <= 0) {
                box.y = 0;
                isBoxJumping = false;
            }
        } else {
            
            float newY = box.y + (gravity * deltaTime);
            boolean onPlatform = false;

            
            for (Rectangle platform : platforms) {
                if (box.overlaps(platform) && newY < platform.y + platform.height) {
                    onPlatform = true;
                    break;
                }
            }

            if (onPlatform) {
                
                isBoxJumping = false;
                boxJumpVelocity = 0;
                box.y = newY;
            } else {
                
                boxJumpVelocity += gravity * deltaTime;
                box.y += boxJumpVelocity * deltaTime;

                
                if (box.y <= 0) {
                    box.y = 0;
                    isBoxJumping = false;
                }
            }
        }
    }


    private void checkCollisions() {
        
        for (Rectangle grassTile : grass) {
            if (box.overlaps(grassTile)) {
                
                box.y = grassTile.y + grassTile.height;
                isBoxJumping = false;
            }
        }

        
        for (Rectangle platform : platforms) {
            if (box.overlaps(platform)) {
                
                box.y = platform.y + platform.height;
                isBoxJumping = false;
            }
        }

        
        float obstacleGrassBuffer = 5f; 
        for (Rectangle obstacle : jumpingObstacles) {
            if (obstacle.overlaps(box)) {
                System.out.println("Game Over");
                if(lives > 0){
                    lives --;
                    if (lives < hearts.length) {
                        hearts[lives] = null;
                    }
                    resetLevel();
                }else{
                    playGameoverSound();
                    resetLevel();
                    isPaused = false;
                    ((MyGdxGame) Gdx.app.getApplicationListener()).showGameOverScreen(3);
                }
                updateHeartPositions();
            }

            
            for (Rectangle grassTile : grass) {
                if (obstacle.overlaps(grassTile)) {
                    
                    obstacle.y = grassTile.y + grassTile.height + obstacleGrassBuffer;
                    isObstacleJumping[getObstacleIndex(obstacle)] = false;
                }
            }
        }

        
        for (Rectangle obstacle : movingObstacles) {
            if (box.overlaps(obstacle)) {
                System.out.println("Game Over");
                if(lives > 0){
                    lives --;
                    if (lives < hearts.length) {
                        hearts[lives] = null;
                    }
                    resetLevel();
                }else{
                    playGameoverSound();
                    resetLevel();
                    isPaused = false;
                    ((MyGdxGame) Gdx.app.getApplicationListener()).showGameOverScreen(3);
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


    private void updateMovingObstacles(float deltaTime) {
        float movementDelta = obstacleSpeed * deltaTime;

        
        for (Rectangle obstacle : movingObstacles) {
            if (isMovingRight) {
                obstacle.x += movementDelta;
                if (obstacle.x + obstacle.width > Gdx.graphics.getWidth()) {
                    
                    isMovingRight = false;
                    float overshoot = obstacle.x + obstacle.width - Gdx.graphics.getWidth();
                    obstacle.x = Gdx.graphics.getWidth() - obstacle.width - overshoot;
                }
            } else {
                obstacle.x -= movementDelta;
                if (obstacle.x < 0) {
                    
                    isMovingRight = true;
                    obstacle.x = -obstacle.x;
                }
            }
        }
    }

    private void checkLevelPassed() {
        if (box.x + box.width == Gdx.graphics.getWidth()) {
            newLevelSound.play();
            ((MyGdxGame) Gdx.app.getApplicationListener()).showLevelPassedScreen(4);
            System.out.println("Level Passed!"); 
        }
    }

    private void resetLevel() {
        box.x = 0;
        box.y = 68;
        isBoxJumping = false;
        boxJumpVelocity = 0;
    }

    private int getObstacleIndex(Rectangle obstacle) {
        for (int i = 0; i < jumpingObstacles.length; i++) {
            if (obstacle == jumpingObstacles[i]) {
                return i;
            }
        }
        return -1;
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

        
        for (Rectangle obstacle : movingObstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y);
        }

        for (Rectangle obstacle : jumpingObstacles) {
            batch.draw(obstacleTexture, obstacle.x, obstacle.y);
        }

        
        for (Rectangle platform : platforms) {
            batch.draw(platformTexture, platform.x, platform.y);
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
        platformTexture.dispose();
        jumpSound.dispose();
        colSound.dispose();
        newLevelSound.dispose();
        backgroundMusic.dispose();
        batch.dispose();
        backgroundTexture.dispose();
    }
}
