package jade;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles all mouse-related events in the engine
 * Singleton
 */
public class MouseListener {
    private static MouseListener instance;

    private double scrollX, scrollY;
    private double xPos, yPos, lastY, lastX;

    // Create boolean array with the number of elements matching
    // the number of buttons on the users mouse
    private boolean mouseButtonPressed[] = new boolean[GLFW_MOUSE_BUTTON_LAST + 1];

    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0;
        this.scrollY = 0;

        this.xPos = 0;
        this.yPos = 0;

        this.lastX = 0;
        this.lastY = 0;
    }

    /**
     * Get the current mouse listener instance
     */
    public static MouseListener get() {
        if (MouseListener.instance == null ) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    /**
     * Called when the mouse position updates
     * @param window The current window
     * @param xPos The new xPos of the mouse
     * @param yPos The new yPos of the mouse
     */
    public static void mousePosCallback(long window, double xPos, double yPos) {
        // Save the previous X and Y before changing them
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        get().xPos = xPos;
        get().yPos = yPos;

        // This callback is only called when the mouse position changes
        // so if the button is still down when the mouse moves
        // then the user is dragging
        for(boolean isButtonPressed : get().mouseButtonPressed) {
            if(isButtonPressed) {
                get().isDragging = true;
            }
        }
    }

    /**
     * Called when a mouse button is pressed
     * @param window The current window
     * @param button The button that was pressed
     * @param action Pressed vs released
     * @param modifiers Any button that was pressed in addition to the mouse button (cmd, control, shift, etc.)
     */
    public static void mouseButtonCallback(long window, int button, int action, int modifiers) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                // Can't drag with a button that's not being pressed
                get().isDragging = false;
            }
        }
    }

    /**
     * Called when the mouse is scrolled
     * @param window The current window
     * @param xOffset The amount scrolled in the X axis
     * @param yOffset The amount scrolled in the Y axis
     */
    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    /**
     * Returns whether the provided button is down
     * @param button The button to check
     */
    public static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    /**
     * Get the current X position of the mouse
     */
    public static float getX() {
        return (float)get().xPos;
    }

    /**
     * Get the current Y position of the mouse
     */
    public static float getY() {
        return (float)get().yPos;
    }

    /**
     * Get the difference between the last mouse X position and the current mouse X position
     */
    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    /**
     * Get the difference between the last mouse X position and the current mouse X position
     */
    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    /**
     * Get the amount the mouse has been scrolled in the X axis
     */
    public static float getScrollX() {
        return (float)get().scrollX;
    }

    /**
     * Get the amount the mouse has been scrolled in the Y axis
     */
    public static float getScrollY() {
        return (float)get().scrollY;
    }

    /**
     * Get whether the mouse is being dragged
     */
    public static boolean isDragging() {
        return get().isDragging;
    }

    /*
      public static void endFrame() {
              get().scrollX = 0;
              get().scrollY = 0;

              get().lastX = get().xPos;
              get().lastY = get().yPos;
          }
     */
}
