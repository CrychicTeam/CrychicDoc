package icyllis.arc3d.compiler;

public enum ShaderKind {

    BASE,
    VERTEX,
    FRAGMENT,
    COMPUTE,
    SUBROUTINE,
    SUBROUTINE_SHADER,
    SUBROUTINE_COLOR_FILTER,
    SUBROUTINE_BLENDER,
    PRIVATE_SUBROUTINE_SHADER,
    PRIVATE_SUBROUTINE_COLOR_FILTER,
    PRIVATE_SUBROUTINE_BLENDER;

    public boolean isVertex() {
        return this == VERTEX;
    }

    public boolean isFragment() {
        return this == FRAGMENT;
    }

    public boolean isCompute() {
        return this == COMPUTE;
    }

    public boolean isAnySubroutine() {
        return this.compareTo(SUBROUTINE) >= 0;
    }
}