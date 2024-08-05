package icyllis.arc3d.compiler;

public enum GLSLVersion {

    GLSL_300_ES("#version 300 es\n"),
    GLSL_310_ES("#version 310 es\n"),
    GLSL_330("#version 330 core\n"),
    GLSL_400("#version 400 core\n"),
    GLSL_420("#version 420 core\n"),
    GLSL_430("#version 430 core\n"),
    GLSL_440("#version 440 core\n"),
    GLSL_450("#version 450 core\n");

    public final String mVersionDecl;

    private GLSLVersion(String versionDecl) {
        this.mVersionDecl = versionDecl;
    }

    public boolean isAtLeast(GLSLVersion other) {
        return this.compareTo(other) >= 0;
    }

    public boolean isCoreProfile() {
        return this.compareTo(GLSL_330) >= 0;
    }

    public boolean isEsProfile() {
        return this.compareTo(GLSL_310_ES) <= 0;
    }
}