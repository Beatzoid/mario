package jade;

import static org.lwjgl.glfw.GLFW.*;

public class KeyListener {
    private static KeyListener instance;

    // Create boolean array with the number of elements matching
    // the number of keys on the users keyboard
    private boolean keyPressed[] = new boolean[GLFW_KEY_LAST + 1];

    private KeyListener() { }

    /**
     * Get the current KeyListener instance
     */
    public static KeyListener get() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    /**
     * Called when a key is pressed
     * @param window The current window
     * @param key The key that was pressed
     * @param scanCode A unique, platform-specific code for every key
     * @param action Whether the key was pressed or released
     * @param modifiers Any key that was pressed in addition to the passed key (control, cmd, shift, etc.)
     */
    public static void keyCallback(long window, int key, int scanCode, int action, int modifiers) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    /**
     * Get whether the provided key is pressed
     * @param keyCode The key to check
     */
    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
