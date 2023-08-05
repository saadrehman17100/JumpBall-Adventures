package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DesktopLauncher {
	public static void main(String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		// Set the desired screen width and height
		int screenWidth = 1920; // Replace with your desired screen width
		int screenHeight = 1080; // Replace with your desired screen height

		// Calculate the aspect ratio
		float aspectRatio = (float) screenWidth / screenHeight;

		// Set the window size while maintaining aspect ratio
		int windowWidth;
		int windowHeight;

		if (Lwjgl3ApplicationConfiguration.getDisplayMode().width / aspectRatio <= Lwjgl3ApplicationConfiguration.getDisplayMode().height) {
			windowWidth = Lwjgl3ApplicationConfiguration.getDisplayMode().width;
			windowHeight = (int) (windowWidth / aspectRatio);
		} else {
			windowHeight = Lwjgl3ApplicationConfiguration.getDisplayMode().height;
			windowWidth = (int) (windowHeight * aspectRatio);
		}

		// Set the windowed mode
		config.setWindowedMode(windowWidth, windowHeight);

		config.setForegroundFPS(60);
		config.setTitle("First");

		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
