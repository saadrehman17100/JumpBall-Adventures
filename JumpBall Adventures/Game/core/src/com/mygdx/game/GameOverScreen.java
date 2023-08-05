package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private final MyGdxGame game; 

    public GameOverScreen(final MyGdxGame game,final int level) {
        this.game = game;

        batch = new SpriteBatch();
        font = new BitmapFont();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if(level == 3){
                    game.showLevelThree();
                }else if(level == 2){
                    game.showLevelTwo();
                }else{
                    game.showLevelOne();
                }
                return true;
            }
        });
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.draw(batch, "Game Over click to restart", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        batch.end();

        if (Gdx.input.justTouched()) {
            game.showLevelOne();
        }
    }

    @Override
    public void resize(int width, int height) {
        
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void hide() {
        
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
