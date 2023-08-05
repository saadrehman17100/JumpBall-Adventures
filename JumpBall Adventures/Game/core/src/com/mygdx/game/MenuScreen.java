package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {
    private final MyGdxGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont menuFont;

    public MenuScreen(final MyGdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        batch = new SpriteBatch();

        
        titleFont = new BitmapFont();
        titleFont.getData().setScale(10);

        
        menuFont = new BitmapFont();
        menuFont.getData().setScale(2);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                game.restartLevelOne();
                return true;
            }
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        handleInput();

        
        Gdx.gl.glClearColor(0.6f, 0.8f, 1.0f, 1f); 
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        
        String title = "2D Ball";
        GlyphLayout titleLayout = new GlyphLayout(titleFont, title);
        float titleX = (Gdx.graphics.getWidth() - titleLayout.width) / 2;
        float titleY = Gdx.graphics.getHeight() * 0.9f;
        titleFont.draw(batch, title, titleX, titleY);

        
        float optionY = Gdx.graphics.getHeight() * 0.7f;
        float optionSpacing = 50;
        menuFont.draw(batch, "Play Game", Gdx.graphics.getWidth() * 0.4f, optionY);
        optionY -= optionSpacing;
        menuFont.draw(batch, "Exit Game", Gdx.graphics.getWidth() * 0.4f, optionY);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            
            if (touchX > Gdx.graphics.getWidth() * 0.4f && touchX < Gdx.graphics.getWidth() * 0.6f
                    && touchY > Gdx.graphics.getHeight() * 0.5f && touchY < Gdx.graphics.getHeight() * 0.6f) {

                System.out.println("here");
                game.setScreen(new LevelOneScreen(game)); 
            }

            
            if (touchX > Gdx.graphics.getWidth() * 0.4f && touchX < Gdx.graphics.getWidth() * 0.6f
                    && touchY > Gdx.graphics.getHeight() * 0.4f && touchY < Gdx.graphics.getHeight() * 0.5f) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        titleFont.dispose();
        menuFont.dispose();
    }
}
