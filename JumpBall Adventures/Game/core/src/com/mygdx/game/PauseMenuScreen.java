package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PauseMenuScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private final MyGdxGame game; 

    public PauseMenuScreen(final MyGdxGame game) {
        this.game = game;

        batch = new SpriteBatch();
        font = new BitmapFont();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.showLevelOne();
                    return true;
                }
                return false;
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
        font.draw(batch, "Paused", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        batch.end();
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
