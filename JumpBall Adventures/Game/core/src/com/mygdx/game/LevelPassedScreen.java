package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelPassedScreen implements Screen {

    private SpriteBatch batch;
    private BitmapFont font;
    private final MyGdxGame game;
    private int level = 0;
    public LevelPassedScreen(final MyGdxGame game,final int level) {
        this.game = game;
        this.level = level;
        batch = new SpriteBatch();
        font = new BitmapFont();
        System.out.println(level);

        if(level == 4){
            System.out.println("Kevasdasdasd");
            Gdx.app.exit();
        }

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                
                if(level == 4){
                    game.showMainMenu();
                }

                if(level == 1) {
                    game.showLevelOne();
                }
                if(level  == 2){
                    game.showLevelTwo();
                }else{
                    game.showLevelThree();
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
        font.draw(batch, "Level " + (level-1) + " Completed", Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
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
