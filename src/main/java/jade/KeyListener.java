package jade;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;

    // Create boolean array with the number of elements matching
    // the number of keys on the users keyboard
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1];

    private KeyListener() {

    }

    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    /**
     * @param action Whether the key was pressed or released
     * @param modifiers Modifiers are any buttons that are pressed in addition to the mouse button
     */
    public static void keyCallback(long window, int key, int scanCode, int action, int modifiers) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
