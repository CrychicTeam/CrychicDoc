package icyllis.arc3d.engine;

import icyllis.arc3d.core.Matrix;
import icyllis.arc3d.core.Matrixc;
import icyllis.arc3d.core.Quad;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.Rect2fc;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.Rect2ic;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

public final class ClipStack extends Clip {

    public static final int OP_DIFFERENCE = 0;

    public static final int OP_INTERSECT = 1;

    public static final int STATE_EMPTY = 0;

    public static final int STATE_WIDE_OPEN = 1;

    public static final int STATE_DEVICE_RECT = 2;

    public static final int STATE_COMPLEX = 3;

    private final ArrayDeque<ClipStack.SaveRecord> mSaves = new ArrayDeque();

    private final ArrayDeque<ClipStack.ClipElement> mElements = new ArrayDeque();

    private final Collection<ClipStack.Element> mElementsView = Collections.unmodifiableCollection(this.mElements);

    private final Rect2i mDeviceBounds;

    private final boolean mMSAA;

    private final ClipStack.ClipElement mTmpElement = new ClipStack.ClipElement();

    private final Rect2f mTmpOuter = new Rect2f();

    private final ClipStack.Draw mTmpDraw = new ClipStack.Draw();

    private final ArrayList<ClipStack.Element> mElementsForMask = new ArrayList();

    public static final int CLIP_GEOMETRY_EMPTY = 0;

    public static final int CLIP_GEOMETRY_A_ONLY = 1;

    public static final int CLIP_GEOMETRY_B_ONLY = 2;

    public static final int CLIP_GEOMETRY_BOTH = 3;

    public ClipStack(Rect2ic deviceBounds, boolean msaa) {
        this.mDeviceBounds = new Rect2i(deviceBounds);
        this.mMSAA = msaa;
        this.mSaves.add(new ClipStack.SaveRecord(deviceBounds));
    }

    public int currentClipState() {
        return ((ClipStack.SaveRecord) this.mSaves.element()).mState;
    }

    public void save() {
        ((ClipStack.SaveRecord) this.mSaves.element()).pushSave();
    }

    public void restore() {
        ClipStack.SaveRecord current = (ClipStack.SaveRecord) this.mSaves.element();
        if (!current.popSave()) {
            current.removeElements(this.mElements);
            this.mSaves.pop();
            ((ClipStack.SaveRecord) this.mSaves.element()).restoreElements(this.mElements);
        }
    }

    @UnmodifiableView
    public Collection<ClipStack.Element> elements() {
        return this.mElementsView;
    }

    public void clipRect(@Nullable Matrixc viewMatrix, @Nonnull Rect2fc localRect, int clipOp) {
        this.clip(this.mTmpElement.init(localRect.left(), localRect.top(), localRect.right(), localRect.bottom(), viewMatrix, clipOp, false));
    }

    public void clipRect(@Nullable Matrixc viewMatrix, float left, float top, float right, float bottom, int clipOp) {
        this.clip(this.mTmpElement.init(left, top, right, bottom, viewMatrix, clipOp, false));
    }

    private void clip(ClipStack.ClipElement element) {
        if (((ClipStack.SaveRecord) this.mSaves.element()).mState != 0) {
            element.simplify(this.mDeviceBounds, this.mMSAA, this.mTmpOuter);
            if (!element.shape().isEmpty() || element.clipOp() != 0) {
                ClipStack.SaveRecord current = (ClipStack.SaveRecord) this.mSaves.element();
                boolean wasDeferred;
                if (current.canBeUpdated()) {
                    wasDeferred = false;
                } else {
                    boolean alive = current.popSave();
                    assert alive;
                    wasDeferred = true;
                    current = new ClipStack.SaveRecord(current, this.mElements.size());
                    this.mSaves.push(current);
                }
                int elementCount = this.mElements.size();
                if (!current.addElement(element, this.mElements) && wasDeferred) {
                    assert elementCount == this.mElements.size();
                    this.mSaves.pop();
                    ((ClipStack.SaveRecord) this.mSaves.element()).pushSave();
                }
            }
        }
    }

    @Override
    public void getConservativeBounds(Rect2i out) {
        ClipStack.SaveRecord current = (ClipStack.SaveRecord) this.mSaves.element();
        if (current.mState == 0) {
            out.setEmpty();
        } else if (current.mState == 1) {
            out.set(this.mDeviceBounds);
        } else if (current.op() == 0) {
            subtract(this.mDeviceBounds, current.mInnerBounds, out, true);
        } else {
            assert this.mDeviceBounds.contains(current.outerBounds());
            out.set(current.mOuterBounds);
        }
    }

    @Override
    public int apply(SurfaceDrawContext sdc, boolean aa, ClipResult out, Rect2f bounds) {
        ClipStack.Draw draw = this.mTmpDraw.init(bounds, aa);
        if (!draw.mBounds.intersect(this.mDeviceBounds)) {
            return 2;
        } else {
            ClipStack.SaveRecord save = (ClipStack.SaveRecord) this.mSaves.element();
            if (save.mState == 0) {
                return 2;
            } else if (save.mState == 1) {
                return 1;
            } else {
                switch(getClipGeometry(save, draw)) {
                    case 0:
                        return 2;
                    case 1:
                        assert false;
                        break;
                    case 2:
                        return 1;
                    case 3:
                        assert save.mState == 2 || save.mState == 3;
                }
                Rect2i scissorBounds;
                if (save.op() == 1) {
                    scissorBounds = new Rect2i(save.outerBounds());
                } else {
                    assert save.op() == 0;
                    Rect2i diff = new Rect2i();
                    if (Rect2i.subtract(draw.outerBounds(), save.innerBounds(), diff)) {
                        scissorBounds = diff;
                    } else {
                        scissorBounds = new Rect2i(draw.outerBounds());
                    }
                }
                boolean scissorIsNeeded = false;
                List<ClipStack.Element> elementsForMask = this.mElementsForMask;
                int i = this.mElements.size();
                for (ClipStack.ClipElement e : this.mElements) {
                    if (--i >= save.oldestElementIndex()) {
                        if (e.isInvalid()) {
                            continue;
                        }
                        switch(getClipGeometry(e, draw)) {
                            case 0:
                                elementsForMask.clear();
                                return 2;
                            case 1:
                                assert false;
                                break;
                            case 2:
                            default:
                                continue;
                            case 3:
                        }
                        scissorIsNeeded = true;
                        boolean fullyApplied = false;
                        if (e.op() == 1) {
                            fullyApplied = e.innerBounds() == e.outerBounds() || e.innerBounds().contains(scissorBounds);
                        }
                        if (!fullyApplied) {
                            elementsForMask.add(e);
                        }
                        continue;
                    }
                    break;
                }
                if (!scissorIsNeeded) {
                    assert elementsForMask.isEmpty();
                    return 1;
                } else {
                    if (save.op() == 1 && !elementsForMask.isEmpty()) {
                        boolean res = scissorBounds.intersect(draw.outerBounds());
                        assert res;
                    }
                    if (!scissorBounds.contains(draw.outerBounds())) {
                        out.addScissor(scissorBounds, bounds);
                    }
                    if (!elementsForMask.isEmpty()) {
                        elementsForMask.clear();
                    }
                    assert out.hasClip();
                    return 0;
                }
            }
        }
    }

    public static int getClipGeometry(ClipStack.Geometry A, ClipStack.Geometry B) {
        if (A.op() == 1) {
            if (B.op() == 1) {
                if (!Rect2i.intersects(A.outerBounds(), B.outerBounds())) {
                    return 0;
                }
                if (B.contains(A)) {
                    return 1;
                }
                if (A.contains(B)) {
                    return 2;
                }
                return 3;
            }
            if (B.op() == 0) {
                if (!Rect2i.intersects(A.outerBounds(), B.outerBounds())) {
                    return 1;
                }
                if (B.contains(A)) {
                    return 0;
                }
                return 3;
            }
        }
        if (A.op() == 0) {
            if (B.op() == 1) {
                if (!Rect2i.intersects(B.outerBounds(), A.outerBounds())) {
                    return 2;
                }
                if (A.contains(B)) {
                    return 0;
                }
                return 3;
            }
            if (B.op() == 0) {
                if (A.contains(B)) {
                    return 1;
                }
                if (B.contains(A)) {
                    return 2;
                }
                return 3;
            }
        }
        throw new IllegalStateException();
    }

    static void subtract(Rect2ic a, Rect2ic b, Rect2i out, boolean exact) {
        Rect2i diff = new Rect2i();
        if (!Rect2i.subtract(a, b, diff) && exact) {
            out.set(a);
        } else {
            out.set(diff);
        }
    }

    static class ClipElement extends ClipStack.Element implements ClipStack.Geometry {

        final Matrix mInverseViewMatrix = new Matrix();

        final Rect2i mInnerBounds = new Rect2i();

        final Rect2i mOuterBounds = new Rect2i();

        int mInvalidatedByIndex = -1;

        public ClipElement() {
        }

        public ClipElement(Rect2fc rect, Matrixc viewMatrix, int clipOp, boolean aa) {
            super(rect, viewMatrix, clipOp, aa);
            if (!viewMatrix.invert(this.mInverseViewMatrix)) {
                this.mRect.setEmpty();
            }
        }

        public ClipElement(ClipStack.ClipElement e) {
            super(e.shape(), e.viewMatrix(), e.clipOp(), e.aa());
            this.mInverseViewMatrix.set(e.mInverseViewMatrix);
            this.mInnerBounds.set(e.mInnerBounds);
            this.mOuterBounds.set(e.mOuterBounds);
            this.mInvalidatedByIndex = e.mInvalidatedByIndex;
        }

        public ClipStack.ClipElement init(float left, float top, float right, float bottom, Matrixc viewMatrix, int clipOp, boolean aa) {
            this.mRect.set(left, top, right, bottom);
            if (viewMatrix != null) {
                this.mViewMatrix.set(viewMatrix);
            } else {
                this.mViewMatrix.setIdentity();
            }
            this.mClipOp = clipOp;
            this.mAA = aa;
            if (viewMatrix != null) {
                if (!viewMatrix.invert(this.mInverseViewMatrix)) {
                    this.mRect.setEmpty();
                    this.mInverseViewMatrix.setIdentity();
                }
            } else {
                this.mInverseViewMatrix.setIdentity();
            }
            this.mInnerBounds.setEmpty();
            this.mOuterBounds.setEmpty();
            this.mInvalidatedByIndex = -1;
            return this;
        }

        public void set(ClipStack.ClipElement e) {
            this.mRect.set(e.shape());
            this.mViewMatrix.set(e.viewMatrix());
            this.mClipOp = e.clipOp();
            this.mAA = e.aa();
            this.mInverseViewMatrix.set(e.mInverseViewMatrix);
            this.mInnerBounds.set(e.mInnerBounds);
            this.mOuterBounds.set(e.mOuterBounds);
            this.mInvalidatedByIndex = e.mInvalidatedByIndex;
        }

        public boolean isInvalid() {
            return this.mInvalidatedByIndex >= 0;
        }

        public void markInvalid(ClipStack.SaveRecord current) {
            assert !this.isInvalid();
            this.mInvalidatedByIndex = current.firstActiveElementIndex();
        }

        public void restoreValid(ClipStack.SaveRecord current) {
            if (current.firstActiveElementIndex() < this.mInvalidatedByIndex) {
                this.mInvalidatedByIndex = -1;
            }
        }

        public boolean combine(ClipStack.ClipElement other, ClipStack.SaveRecord current) {
            if (other.mClipOp == 1 && this.mClipOp == 1) {
                boolean shapeUpdated = false;
                if (Matrix.equals(this.mViewMatrix, other.mInverseViewMatrix)) {
                    if (!this.mRect.intersect(other.mRect)) {
                        this.mRect.setEmpty();
                        this.markInvalid(current);
                        return true;
                    }
                    shapeUpdated = true;
                }
                if (!shapeUpdated) {
                    return false;
                } else {
                    assert this.mClipOp == 1 && other.mClipOp == 1;
                    boolean res = this.mOuterBounds.intersect(other.mOuterBounds);
                    assert res;
                    if (!this.mInnerBounds.intersect(other.mInnerBounds)) {
                        this.mInnerBounds.setEmpty();
                    }
                    return true;
                }
            } else {
                return false;
            }
        }

        public void updateForElement(ClipStack.ClipElement added, ClipStack.SaveRecord current) {
            if (!this.isInvalid()) {
                switch(ClipStack.getClipGeometry(this, added)) {
                    case 0:
                        this.markInvalid(current);
                        added.markInvalid(current);
                        break;
                    case 1:
                        added.markInvalid(current);
                        break;
                    case 2:
                        this.markInvalid(current);
                        break;
                    case 3:
                        if (added.combine(this, current)) {
                            this.markInvalid(current);
                        }
                }
            }
        }

        @Override
        public int op() {
            return this.mClipOp;
        }

        public Rect2ic innerBounds() {
            return this.mInnerBounds;
        }

        @Override
        public Rect2ic outerBounds() {
            return this.mOuterBounds;
        }

        @Override
        public boolean contains(ClipStack.Geometry g) {
            if (g instanceof ClipStack.Draw d) {
                return this.contains(d);
            } else {
                return g instanceof ClipStack.SaveRecord s ? this.contains(s) : this.contains((ClipStack.ClipElement) g);
            }
        }

        public boolean contains(ClipStack.Draw d) {
            if (this.mInnerBounds.contains(d.outerBounds())) {
                return true;
            } else {
                Rect2fc queryBounds = (Rect2fc) (d.mAA ? d.bounds() : d.mTmpBounds);
                return rect_contains_rect(this.mRect, this.mViewMatrix, this.mInverseViewMatrix, queryBounds, Matrix.identity(), false);
            }
        }

        public boolean contains(ClipStack.SaveRecord s) {
            return this.mInnerBounds.contains(s.mOuterBounds) ? true : rect_contains_rect(this.mRect, this.mViewMatrix, this.mInverseViewMatrix, new Rect2f(s.mOuterBounds), Matrix.identity(), false);
        }

        public boolean contains(ClipStack.ClipElement e) {
            if (this.mInnerBounds.contains(e.mOuterBounds)) {
                return true;
            } else {
                boolean mixedAA = this.mAA != e.mAA;
                return rect_contains_rect(this.mRect, this.mViewMatrix, this.mInverseViewMatrix, e.mRect, e.mViewMatrix, mixedAA);
            }
        }

        static boolean rect_contains_rect(Rect2fc a, Matrixc aToDevice, Matrixc deviceToA, Rect2fc b, Matrixc bToDevice, boolean mixedAAMode) {
            if (!mixedAAMode && Matrix.equals(aToDevice, bToDevice)) {
                return a.contains(b);
            } else if (bToDevice.isIdentity() && aToDevice.isAxisAligned()) {
                Rect2f bInA = new Rect2f(b);
                if (mixedAAMode) {
                    bInA.inset(-0.5F, -0.5F);
                }
                boolean res = deviceToA.mapRect(bInA);
                assert res;
                return a.contains(bInA);
            } else {
                Quad deviceQuad = new Quad(b, bToDevice);
                if (!(deviceQuad.w0() < 5.0E-5F) && !(deviceQuad.w1() < 5.0E-5F) && !(deviceQuad.w2() < 5.0E-5F) && !(deviceQuad.w3() < 5.0E-5F)) {
                    float[] p = new float[2];
                    for (int i = 0; i < 4; i++) {
                        deviceQuad.point(i, p);
                        deviceToA.mapPoint(p);
                        if (!a.contains(p[0], p[1])) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        public void simplify(Rect2ic deviceBounds, boolean msaa, Rect2f outer) {
            this.mRect.sort();
            if (!this.mRect.isEmpty()) {
                boolean axisAligned = this.mViewMatrix.mapRect(this.mRect, outer);
                if (!outer.intersect(deviceBounds)) {
                    this.mRect.setEmpty();
                } else {
                    if (msaa && !axisAligned) {
                        this.mAA = true;
                    }
                    Clip.getPixelBounds(outer, this.mAA, true, this.mOuterBounds);
                    if (axisAligned) {
                        this.mRect.set(outer);
                        this.mViewMatrix.setIdentity();
                        this.mInverseViewMatrix.setIdentity();
                        if (!this.mAA && outer.width() >= 1.0F && outer.height() >= 1.0F) {
                            outer.round(this.mOuterBounds);
                            this.mInnerBounds.set(this.mOuterBounds);
                        } else {
                            Clip.getPixelBounds(outer, this.mAA, false, this.mInnerBounds);
                        }
                    }
                    if (this.mOuterBounds.isEmpty()) {
                        this.mRect.setEmpty();
                    }
                }
            }
        }

        private int clipType() {
            if (this.mRect.isEmpty()) {
                return 0;
            } else {
                return this.mClipOp == 1 && this.mViewMatrix.isIdentity() ? 2 : 3;
            }
        }
    }

    static class Draw implements ClipStack.Geometry {

        final Rect2f mOriginalBounds = new Rect2f();

        final Rect2i mBounds = new Rect2i();

        final Rect2f mTmpBounds = new Rect2f();

        boolean mAA;

        public ClipStack.Draw init(Rect2fc drawBounds, boolean aa) {
            Clip.getPixelBounds(drawBounds, aa, true, this.mBounds);
            this.mAA = aa;
            this.mOriginalBounds.set(drawBounds);
            this.mOriginalBounds.inset(0.001F, 0.001F);
            if (this.mOriginalBounds.isEmpty()) {
                this.mOriginalBounds.set(drawBounds);
            }
            this.mTmpBounds.set(this.mBounds);
            return this;
        }

        @Override
        public int op() {
            return 1;
        }

        @Override
        public Rect2ic outerBounds() {
            return this.mBounds;
        }

        @Override
        public boolean contains(ClipStack.Geometry other) {
            assert other instanceof ClipStack.SaveRecord || other instanceof ClipStack.ClipElement;
            return false;
        }

        public Rect2fc bounds() {
            return this.mOriginalBounds;
        }
    }

    public static class Element {

        final Rect2f mRect;

        final Matrix mViewMatrix;

        int mClipOp;

        boolean mAA;

        Element() {
            this.mRect = new Rect2f();
            this.mViewMatrix = new Matrix();
        }

        Element(Rect2fc rect, Matrixc viewMatrix, int clipOp, boolean aa) {
            this.mRect = new Rect2f(rect);
            this.mViewMatrix = new Matrix(viewMatrix);
            this.mClipOp = clipOp;
            this.mAA = aa;
        }

        public Rect2fc shape() {
            return this.mRect;
        }

        public Matrixc viewMatrix() {
            return this.mViewMatrix;
        }

        public int clipOp() {
            return this.mClipOp;
        }

        public boolean aa() {
            return this.mAA;
        }

        public String toString() {
            return "Element{mRect=" + this.mRect + ", mViewMatrix=" + this.mViewMatrix + ", mClipOp=" + (this.mClipOp == 1 ? "Intersect" : "Difference") + ", mAA=" + this.mAA + "}";
        }
    }

    public interface Geometry {

        int op();

        Rect2ic outerBounds();

        boolean contains(ClipStack.Geometry var1);
    }

    static final class SaveRecord implements ClipStack.Geometry {

        private final Rect2i mInnerBounds;

        private final Rect2i mOuterBounds;

        final int mStartingElementIndex;

        int mOldestValidIndex;

        private int mDeferredSaveCount;

        private int mState;

        private int mOp;

        SaveRecord(Rect2ic deviceBounds) {
            this.mInnerBounds = new Rect2i(deviceBounds);
            this.mOuterBounds = new Rect2i(deviceBounds);
            this.mStartingElementIndex = 0;
            this.mOldestValidIndex = 0;
            this.mState = 1;
            this.mOp = 1;
        }

        SaveRecord(ClipStack.SaveRecord prior, int startingElementIndex) {
            this.mInnerBounds = new Rect2i(prior.mInnerBounds);
            this.mOuterBounds = new Rect2i(prior.mOuterBounds);
            this.mStartingElementIndex = startingElementIndex;
            this.mOldestValidIndex = prior.mOldestValidIndex;
            this.mState = prior.mState;
            this.mOp = prior.mOp;
            assert startingElementIndex >= prior.mStartingElementIndex;
        }

        @Override
        public int op() {
            return this.mOp;
        }

        @Override
        public Rect2ic outerBounds() {
            return this.mOuterBounds;
        }

        public Rect2ic innerBounds() {
            return this.mInnerBounds;
        }

        @Override
        public boolean contains(ClipStack.Geometry g) {
            return g instanceof ClipStack.Draw draw ? this.contains(draw) : this.contains((ClipStack.ClipElement) g);
        }

        public boolean contains(ClipStack.Draw draw) {
            return this.mInnerBounds.contains(draw.outerBounds());
        }

        public boolean contains(ClipStack.ClipElement element) {
            return this.mInnerBounds.contains(element.outerBounds());
        }

        public int firstActiveElementIndex() {
            return this.mStartingElementIndex;
        }

        public int oldestElementIndex() {
            return this.mOldestValidIndex;
        }

        public boolean canBeUpdated() {
            return this.mDeferredSaveCount == 0;
        }

        public void pushSave() {
            assert this.mDeferredSaveCount >= 0;
            this.mDeferredSaveCount++;
        }

        public boolean popSave() {
            this.mDeferredSaveCount--;
            assert this.mDeferredSaveCount >= -1;
            return this.mDeferredSaveCount >= 0;
        }

        public boolean addElement(ClipStack.ClipElement toAdd, ArrayDeque<ClipStack.ClipElement> elements) {
            assert (toAdd.shape().isEmpty() || !toAdd.mOuterBounds.isEmpty()) && (toAdd.mInnerBounds.isEmpty() || toAdd.mOuterBounds.contains(toAdd.mInnerBounds));
            assert this.canBeUpdated();
            if (this.mState == 0) {
                return false;
            } else if (!toAdd.shape().isEmpty()) {
                switch(ClipStack.getClipGeometry(this, toAdd)) {
                    case 0:
                        this.mState = 0;
                        return true;
                    case 1:
                        return false;
                    case 2:
                        this.replaceWithElement(toAdd, elements);
                        return true;
                    case 3:
                    default:
                        if (this.mState == 1) {
                            this.replaceWithElement(toAdd, elements);
                            return true;
                        } else {
                            if (this.mOp == 1) {
                                if (toAdd.op() == 1) {
                                    boolean res = this.mOuterBounds.intersect(toAdd.outerBounds());
                                    assert res;
                                    if (!this.mInnerBounds.intersect(toAdd.innerBounds())) {
                                        this.mInnerBounds.setEmpty();
                                    }
                                } else {
                                    ClipStack.subtract(this.mOuterBounds, toAdd.innerBounds(), this.mOuterBounds, true);
                                    ClipStack.subtract(this.mInnerBounds, toAdd.outerBounds(), this.mInnerBounds, false);
                                }
                            } else if (toAdd.op() == 1) {
                                Rect2i oldOuter = new Rect2i(this.mOuterBounds);
                                ClipStack.subtract(toAdd.outerBounds(), this.mInnerBounds, this.mOuterBounds, true);
                                ClipStack.subtract(toAdd.innerBounds(), oldOuter, this.mInnerBounds, false);
                            } else {
                                this.mOuterBounds.join(toAdd.outerBounds());
                                if (toAdd.innerBounds().width() * toAdd.innerBounds().height() > this.mInnerBounds.width() * this.mInnerBounds.height()) {
                                    this.mInnerBounds.set(toAdd.innerBounds());
                                }
                            }
                            assert !this.mOuterBounds.isEmpty() && (this.mInnerBounds.isEmpty() || this.mOuterBounds.contains(this.mInnerBounds));
                            return this.appendElement(toAdd, elements);
                        }
                }
            } else {
                assert toAdd.clipOp() == 1;
                this.mState = 0;
                return true;
            }
        }

        private boolean appendElement(ClipStack.ClipElement toAdd, ArrayDeque<ClipStack.ClipElement> elements) {
            int i = elements.size() - 1;
            int youngestValid = this.mStartingElementIndex - 1;
            int oldestValid = elements.size();
            ClipStack.ClipElement oldestActiveInvalid = null;
            int oldestActiveInvalidIndex = elements.size();
            for (ClipStack.ClipElement existing : elements) {
                if (i < this.mOldestValidIndex) {
                    break;
                }
                existing.updateForElement(toAdd, this);
                if (toAdd.isInvalid()) {
                    if (existing.isInvalid()) {
                        this.mState = 0;
                        return true;
                    }
                    return false;
                }
                if (existing.isInvalid()) {
                    if (i >= this.mStartingElementIndex) {
                        oldestActiveInvalid = existing;
                        oldestActiveInvalidIndex = i;
                    }
                } else {
                    oldestValid = i;
                    if (i > youngestValid) {
                        youngestValid = i;
                    }
                }
                i--;
            }
            assert oldestValid == elements.size() || oldestValid >= this.mOldestValidIndex && oldestValid < elements.size();
            assert youngestValid == this.mStartingElementIndex - 1 || youngestValid >= this.mStartingElementIndex && youngestValid < elements.size();
            assert oldestActiveInvalid == null || oldestActiveInvalidIndex >= this.mStartingElementIndex && oldestActiveInvalidIndex < elements.size();
            assert oldestValid >= this.mOldestValidIndex;
            this.mOldestValidIndex = Math.min(oldestValid, oldestActiveInvalidIndex);
            this.mState = oldestValid == elements.size() ? toAdd.clipType() : 3;
            if (this.mOp == 0 && toAdd.op() == 1) {
                this.mOp = 1;
            }
            int targetCount = youngestValid + 1;
            if (oldestActiveInvalid == null || oldestActiveInvalidIndex >= targetCount) {
                targetCount++;
                oldestActiveInvalid = null;
            }
            while (elements.size() > targetCount) {
                assert oldestActiveInvalid != elements.peek();
                elements.pop();
            }
            if (oldestActiveInvalid != null) {
                oldestActiveInvalid.set(toAdd);
            } else if (elements.size() < targetCount) {
                elements.push(new ClipStack.ClipElement(toAdd));
            } else {
                ((ClipStack.ClipElement) elements.element()).set(toAdd);
            }
            return true;
        }

        private void replaceWithElement(ClipStack.ClipElement toAdd, ArrayDeque<ClipStack.ClipElement> elements) {
            this.mInnerBounds.set(toAdd.mInnerBounds);
            this.mOuterBounds.set(toAdd.mOuterBounds);
            this.mOp = toAdd.clipOp();
            this.mState = toAdd.clipType();
            int targetCount = this.mStartingElementIndex + 1;
            while (elements.size() > targetCount) {
                elements.pop();
            }
            if (elements.size() < targetCount) {
                elements.push(new ClipStack.ClipElement(toAdd));
            } else {
                ((ClipStack.ClipElement) elements.element()).set(toAdd);
            }
        }

        public void removeElements(ArrayDeque<ClipStack.ClipElement> elements) {
            while (elements.size() > this.mStartingElementIndex) {
                elements.pop();
            }
        }

        public void restoreElements(ArrayDeque<ClipStack.ClipElement> elements) {
            int i = elements.size() - 1;
            for (ClipStack.ClipElement e : elements) {
                if (i < this.mOldestValidIndex) {
                    break;
                }
                e.restoreValid(this);
                i--;
            }
        }
    }
}