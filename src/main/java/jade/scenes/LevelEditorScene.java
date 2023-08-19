package jade.scenes;

import jade.Scene;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private final String vertexShaderSource = "#version 330 core\n" +
            "\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    fColor = aColor;\n" +
            "\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private final String fragmentShaderSource = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private final float[] vertexArray = {
            // Position (XYZ)          // Color (RGBA)                           // Index (for element array)
             0.5f, -0.5f, 0.0f,        1.0f, 0.0f, 0.0f, 1.0f, // Bottom right   0
            -0.5f,  0.5f, 0.0f,        0.0f, 1.0f, 0.0f, 1.0f, // Top left       1
             0.5f,  0.5f, 0.0f,        1.0f, 0.0f, 1.0f, 1.0f, // Top right      2
            -0.5f, -0.5f, 0.0f,        1.0f, 1.0f, 0.0f, 1.0f, // Bottom left    3
    };

    /* !! MUST BE IN COUNTER-CLOCKWISE ORDER !! */
    private final int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3  // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // ============================================
        // Compile and link shaders
        // ============================================

        // ============================================
        // Load and compile the vertex shader
        // ============================================

        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexShaderSource);
        // Compile vertex shader
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);

            System.err.println("ERROR: 'defaultShader.glsl' - Vertex shader compilation failed.");
            System.err.println(glGetShaderInfoLog(vertexID, len));

            // Break out of the program
            assert false : "";
        }

        // ============================================
        // Load and compile the fragment shader
        // ============================================

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentShaderSource);
        // Compile vertex shader
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);

        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);

            System.err.println("ERROR: 'defaultShader.glsl' - Fragment shader compilation failed.");
            System.err.println(glGetShaderInfoLog(fragmentID, len));

            // Break out of the program
            assert false : "";
        }

        // ============================================
        // Link shaders
        // ============================================

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        // Check for linking errors
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);

        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);

            System.err.println("ERROR: 'defaultShader.glsl' - Linking shaders failed.");
            System.err.println(glGetProgramInfoLog(fragmentID, len));

            // Break out of the program
            assert false : "";
        }

        // ============================================
        // Generate
        // VAO (vertex array object),
        // VBO (vertex buffer object),
        // EBO (element buffer object)
        // and send to GPU
        // ============================================

        vaoID = glGenVertexArrays();
        // Every operation after this line happens to the VAO
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO and upload vertex buffer
        vboID = glGenBuffers();

        // Every operation after this line happens to the VBO
        glBindBuffer(GL_ARRAY_BUFFER, vboID);

        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();

        // Every operation after this line happens to the EBO
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);

        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers

        int positionSize = 3;
        int colorSize = 4;
        // 4 bytes for 1 float
        int floatSizeInBytes = 4;
        // Size of one vertex in bytes
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeInBytes;

        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // (positionSize * floatSizeInBytes) is the offset from the start of the vertex
        // In this case, we only have the position attribute before the color attribute
        // So we pass the position size in bytes. Example:
        // { 0.5f, -0.5f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f}
        // In this array, the position takes up the first three elements (positionSize)
        // so in order for GL to find the color we have to tell it where the color starts (the offset)
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeInBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(double dt) {
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable vertex attribute pointers (position and color)
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}
