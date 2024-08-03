package me.lucko.spark.lib.asm;

public abstract class ClassVisitor {

    protected final int api;

    protected ClassVisitor cv;

    public ClassVisitor(int api) {
        this(api, null);
    }

    public ClassVisitor(int api, ClassVisitor classVisitor) {
        if (api != 589824 && api != 524288 && api != 458752 && api != 393216 && api != 327680 && api != 262144 && api != 17432576) {
            throw new IllegalArgumentException("Unsupported api " + api);
        } else {
            if (api == 17432576) {
                Constants.checkAsmExperimental(this);
            }
            this.api = api;
            this.cv = classVisitor;
        }
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if (this.api < 524288 && (access & 65536) != 0) {
            throw new UnsupportedOperationException("Records requires ASM8");
        } else {
            if (this.cv != null) {
                this.cv.visit(version, access, name, signature, superName, interfaces);
            }
        }
    }

    public void visitSource(String source, String debug) {
        if (this.cv != null) {
            this.cv.visitSource(source, debug);
        }
    }

    public ModuleVisitor visitModule(String name, int access, String version) {
        if (this.api < 393216) {
            throw new UnsupportedOperationException("Module requires ASM6");
        } else {
            return this.cv != null ? this.cv.visitModule(name, access, version) : null;
        }
    }

    public void visitNestHost(String nestHost) {
        if (this.api < 458752) {
            throw new UnsupportedOperationException("NestHost requires ASM7");
        } else {
            if (this.cv != null) {
                this.cv.visitNestHost(nestHost);
            }
        }
    }

    public void visitOuterClass(String owner, String name, String descriptor) {
        if (this.cv != null) {
            this.cv.visitOuterClass(owner, name, descriptor);
        }
    }

    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return this.cv != null ? this.cv.visitAnnotation(descriptor, visible) : null;
    }

    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        if (this.api < 327680) {
            throw new UnsupportedOperationException("TypeAnnotation requires ASM5");
        } else {
            return this.cv != null ? this.cv.visitTypeAnnotation(typeRef, typePath, descriptor, visible) : null;
        }
    }

    public void visitAttribute(Attribute attribute) {
        if (this.cv != null) {
            this.cv.visitAttribute(attribute);
        }
    }

    public void visitNestMember(String nestMember) {
        if (this.api < 458752) {
            throw new UnsupportedOperationException("NestMember requires ASM7");
        } else {
            if (this.cv != null) {
                this.cv.visitNestMember(nestMember);
            }
        }
    }

    public void visitPermittedSubclass(String permittedSubclass) {
        if (this.api < 589824) {
            throw new UnsupportedOperationException("PermittedSubclasses requires ASM9");
        } else {
            if (this.cv != null) {
                this.cv.visitPermittedSubclass(permittedSubclass);
            }
        }
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        if (this.cv != null) {
            this.cv.visitInnerClass(name, outerName, innerName, access);
        }
    }

    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        if (this.api < 524288) {
            throw new UnsupportedOperationException("Record requires ASM8");
        } else {
            return this.cv != null ? this.cv.visitRecordComponent(name, descriptor, signature) : null;
        }
    }

    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return this.cv != null ? this.cv.visitField(access, name, descriptor, signature, value) : null;
    }

    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return this.cv != null ? this.cv.visitMethod(access, name, descriptor, signature, exceptions) : null;
    }

    public void visitEnd() {
        if (this.cv != null) {
            this.cv.visitEnd();
        }
    }
}