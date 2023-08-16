package jade;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles all mouse-related events in the engine
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

    public static MouseListener get() {
        if (MouseListener.instance == null ) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

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
     * @param modifiers Modifiers are any buttons that are pressed in addition to the mouse button
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

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;

        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static boolean isMouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

    public static float getX() {
        return (float)get().xPos;
    }

    public static float getY() {
        return (float)get().yPos;
    }

    public static float getDx() {
        return (float)(get().lastX - get().xPos);
    }

    public static float getDy() {
        return (float)(get().lastY - get().yPos);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }


    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }
}
