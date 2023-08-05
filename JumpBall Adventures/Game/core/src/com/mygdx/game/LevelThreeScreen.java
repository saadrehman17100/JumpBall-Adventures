package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelThreeScreen implements Screen {
    private MyGdxGame game;
    private LevelThree levelThree;
    private Texture backgroundTexture;
    private SpriteBatch spriteBatch;
    public LevelThreeScreen(MyGdxGame game) {
        this.game = game;
        levelThree = new LevelThree(this.game);
        backgroundTexture = new Texture("background.jpg"); 
        spriteBatch = new SpriteBatch();

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        levelThree.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.begin();
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch.end();

        levelThree.render();
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
        levelThree.dispose();
        backgroundTexture.dispose(); 
        spriteBatch.dispose();
    }
}
