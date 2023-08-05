package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;


public class MyGdxGame extends Game {
	private LevelOneScreen levelOneScreen;
	private GameOverScreen gameOverScreen;

	private MenuScreen menuScreen;

	@Override
	public void create() {
		gameOverScreen = new GameOverScreen(this,1);
		menuScreen = new MenuScreen(this);

		setScreen(menuScreen);
	}
	public void showGameOverScreen(int level) {
		setScreen(new GameOverScreen(this,level));
	}
	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose(){
		gameOverScreen.dispose();
		levelOneScreen.dispose();
	}


	public void showLevelOne() {
		setScreen(new LevelOneScreen(this));
	}

	public void restartLevelOne() {
		levelOneScreen = new LevelOneScreen(this);
		setScreen(levelOneScreen);
	}

	public void showMainMenu() {
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	public void showLevelPassedScreen(int level){
		setScreen(new LevelPassedScreen(this,level));
	}

	public void closeLevelOne() {
		levelOneScreen = null;
	}

	public void showLevelTwo() {
		setScreen(new LevelTwoScreen(this));
	}

	public void showLevelThree() {
		setScreen(new LevelThreeScreen(this));
	}
}
