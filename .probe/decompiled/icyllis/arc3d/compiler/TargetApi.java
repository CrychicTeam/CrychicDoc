package icyllis.arc3d.compiler;

public enum TargetApi {

    OPENGL_3_3,
    OPENGL_ES_3_0,
    OPENGL_4_3,
    OPENGL_ES_3_1,
    OPENGL_4_5,
    VULKAN_1_0;

    public boolean isOpenGL() {
        return this == OPENGL_3_3 || this == OPENGL_4_3 || this == OPENGL_4_5;
    }

    public boolean isOpenGLES() {
        return this == OPENGL_ES_3_0 || this == OPENGL_ES_3_1;
    }

    public boolean isVulkan() {
        return this.compareTo(VULKAN_1_0) >= 0;
    }
}