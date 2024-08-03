package icyllis.modernui.mc.testforge.trash;

import java.util.Arrays;
import java.util.Comparator;
import javax.annotation.Nonnull;
import org.lwjgl.opengl.GL45C;

public final class GLVertexFormat {

    private final GLVertexAttrib[][] mAttributeSets;

    private int mVertexArray = 0;

    public GLVertexFormat(@Nonnull GLVertexAttrib... attribs) {
        int len = attribs.length;
        if (len == 0) {
            throw new IllegalArgumentException("No attribs");
        } else {
            Arrays.sort(attribs, Comparator.comparingInt(GLVertexAttrib::getBinding));
            this.mAttributeSets = new GLVertexAttrib[attribs[len - 1].getBinding() + 1][];
            int pos = 0;
            int binding = 0;
            for (int i = 0; i <= len; i++) {
                GLVertexAttrib attr = i < len ? attribs[i] : null;
                if (attr == null || binding != attr.getBinding()) {
                    GLVertexAttrib[] attributes = new GLVertexAttrib[i - pos];
                    int j = pos;
                    for (int k = 0; j < i; k++) {
                        attributes[k] = attribs[j];
                        j++;
                    }
                    this.mAttributeSets[binding] = attributes;
                    if (attr == null) {
                        break;
                    }
                    pos = i;
                    for (int jx = binding + 1; jx < attr.getBinding(); jx++) {
                        this.mAttributeSets[jx] = new GLVertexAttrib[0];
                    }
                    binding = attr.getBinding();
                }
            }
        }
    }

    public int getVertexArray() {
        if (this.mVertexArray == 0) {
            this.setFormat(this.mVertexArray = GL45C.glCreateVertexArrays());
        }
        return this.mVertexArray;
    }

    public void setFormat(int array) {
        int location = 0;
        for (GLVertexAttrib[] attributes : this.mAttributeSets) {
            int offset = 0;
            for (GLVertexAttrib attr : attributes) {
                offset = attr.setFormat(array, location, offset);
                location += attr.getLocationSize();
            }
        }
    }

    public int getMaxBinding() {
        return this.mAttributeSets.length - 1;
    }

    public void setVertexBuffer(int binding, int buffer, int offset) {
        GL45C.glVertexArrayVertexBuffer(this.getVertexArray(), binding, buffer, (long) offset, this.getBindingSize(binding));
    }

    public void setBindingDivisor(int binding, int divisor) {
        GL45C.glVertexArrayBindingDivisor(this.getVertexArray(), binding, divisor);
    }

    public void setIndexBuffer(int buffer) {
        GL45C.glVertexArrayElementBuffer(this.getVertexArray(), buffer);
    }

    public int getBindingSize(int binding) {
        int size = 0;
        for (GLVertexAttrib attributes : this.mAttributeSets[binding]) {
            size += attributes.getTotalSize();
        }
        return size;
    }

    public int hashCode() {
        return Arrays.deepHashCode(this.mAttributeSets);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return o != null && this.getClass() == o.getClass() ? Arrays.deepEquals(this.mAttributeSets, ((GLVertexFormat) o).mAttributeSets) : false;
        }
    }
}