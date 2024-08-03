package icyllis.arc3d.core;

import java.util.ArrayList;
import javax.annotation.Nullable;

public final class MarkerStack {

    private final ArrayList<MarkerStack.Rec> mStack = new ArrayList();

    public void setMarker(int id, Matrix4 mat, int boundary) {
        for (int i = this.mStack.size() - 1; i >= 0; i--) {
            MarkerStack.Rec it = (MarkerStack.Rec) this.mStack.get(i);
            if (it.mBoundary != boundary) {
                break;
            }
            if (it.mID == id) {
                it.setMatrix(mat);
                return;
            }
        }
        this.mStack.add(new MarkerStack.Rec(id, mat, boundary));
    }

    public boolean findMarker(int id, Matrix4 out) {
        Matrix4 mat = this.findMarker(id);
        if (mat != null) {
            out.set(mat);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public Matrix4 findMarker(int id) {
        for (int i = this.mStack.size() - 1; i >= 0; i--) {
            MarkerStack.Rec it = (MarkerStack.Rec) this.mStack.get(i);
            if (it.mID == id) {
                return it.mMatrix;
            }
        }
        return null;
    }

    public boolean findMarkerInverse(int id, Matrix4 out) {
        Matrix4 mat = this.findMarkerInverse(id);
        if (mat != null) {
            out.set(mat);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public Matrix4 findMarkerInverse(int id) {
        for (int i = this.mStack.size() - 1; i >= 0; i--) {
            MarkerStack.Rec it = (MarkerStack.Rec) this.mStack.get(i);
            if (it.mID == id) {
                return it.mMatrixInverse;
            }
        }
        return null;
    }

    public void restore(int boundary) {
        for (int i = this.mStack.size() - 1; i >= 0; i--) {
            MarkerStack.Rec it = (MarkerStack.Rec) this.mStack.get(i);
            if (it.mBoundary != boundary) {
                break;
            }
            this.mStack.remove(i);
        }
    }

    private static final class Rec {

        int mID;

        final Matrix4 mMatrix = new Matrix4();

        final Matrix4 mMatrixInverse = new Matrix4();

        int mBoundary;

        Rec(int id, Matrix4 mat, int boundary) {
            this.mID = id;
            this.setMatrix(mat);
            this.mBoundary = boundary;
        }

        void setMatrix(Matrix4 mat) {
            this.mMatrix.set(mat);
            if (!mat.invert(this.mMatrixInverse)) {
                throw new IllegalStateException();
            }
        }
    }
}