package me.lucko.spark.lib.asm;

public abstract class AnnotationVisitor {

    protected final int api;

    protected AnnotationVisitor av;

    public AnnotationVisitor(int api) {
        this(api, null);
    }

    public AnnotationVisitor(int api, AnnotationVisitor annotationVisitor) {
        if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576) {
            throw new IllegalArgumentException("Unsupported api " + api);
        } else {
            if (api == 17432576) {
                Constants.checkAsmExperimental(this);
            }
            this.api = api;
            this.av = annotationVisitor;
        }
    }

    public void visit(String name, Object value) {
        if (this.av != null) {
            this.av.visit(name, value);
        }
    }

    public void visitEnum(String name, String descriptor, String value) {
        if (this.av != null) {
            this.av.visitEnum(name, descriptor, value);
        }
    }

    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        return this.av != null ? this.av.visitAnnotation(name, descriptor) : null;
    }

    public AnnotationVisitor visitArray(String name) {
        return this.av != null ? this.av.visitArray(name) : null;
    }

    public void visitEnd() {
        if (this.av != null) {
            this.av.visitEnd();
        }
    }
}