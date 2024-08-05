package icyllis.arc3d.core;

public interface RefCounted {

    @SharedPtr
    static <T extends RefCounted> T move(@SharedPtr T sp) {
        if (sp != null) {
            sp.unref();
        }
        return null;
    }

    @SharedPtr
    static <T extends RefCounted> T move(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        return that;
    }

    @SharedPtr
    static <T extends RefCounted> T create(@SharedPtr T that) {
        if (that != null) {
            that.ref();
        }
        return that;
    }

    @SharedPtr
    static <T extends RefCounted> T create(@SharedPtr T sp, @SharedPtr T that) {
        if (sp != null) {
            sp.unref();
        }
        if (that != null) {
            that.ref();
        }
        return that;
    }

    void ref();

    void unref();
}