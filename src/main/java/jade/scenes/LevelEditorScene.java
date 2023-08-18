package jade.scenes;

import jade.KeyListener;
import jade.Scene;
import jade.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private double timeToChangeScene = 2.0;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");
    }

    @Override
    public void update(double dt) {
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;

            Window.get().r -= (float) (dt * 5.0);
            Window.get().g -= (float) (dt * 5.0);
            Window.get().b -= (float) (dt * 5.0);
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
