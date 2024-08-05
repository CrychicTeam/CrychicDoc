package software.bernie.geckolib.core.animatable.model;

import java.util.List;
import software.bernie.geckolib.core.state.BoneSnapshot;

public interface CoreGeoBone {

    String getName();

    CoreGeoBone getParent();

    float getRotX();

    float getRotY();

    float getRotZ();

    float getPosX();

    float getPosY();

    float getPosZ();

    float getScaleX();

    float getScaleY();

    float getScaleZ();

    void setRotX(float var1);

    void setRotY(float var1);

    void setRotZ(float var1);

    default void updateRotation(float xRot, float yRot, float zRot) {
        this.setRotX(xRot);
        this.setRotY(yRot);
        this.setRotZ(zRot);
    }

    void setPosX(float var1);

    void setPosY(float var1);

    void setPosZ(float var1);

    default void updatePosition(float posX, float posY, float posZ) {
        this.setPosX(posX);
        this.setPosY(posY);
        this.setPosZ(posZ);
    }

    void setScaleX(float var1);

    void setScaleY(float var1);

    void setScaleZ(float var1);

    default void updateScale(float scaleX, float scaleY, float scaleZ) {
        this.setScaleX(scaleX);
        this.setScaleY(scaleY);
        this.setScaleZ(scaleZ);
    }

    void setPivotX(float var1);

    void setPivotY(float var1);

    void setPivotZ(float var1);

    default void updatePivot(float pivotX, float pivotY, float pivotZ) {
        this.setPivotX(pivotX);
        this.setPivotY(pivotY);
        this.setPivotZ(pivotZ);
    }

    float getPivotX();

    float getPivotY();

    float getPivotZ();

    boolean isHidden();

    boolean isHidingChildren();

    void setHidden(boolean var1);

    void setChildrenHidden(boolean var1);

    void saveInitialSnapshot();

    void markScaleAsChanged();

    void markRotationAsChanged();

    void markPositionAsChanged();

    boolean hasScaleChanged();

    boolean hasRotationChanged();

    boolean hasPositionChanged();

    void resetStateChanges();

    BoneSnapshot getInitialSnapshot();

    List<? extends CoreGeoBone> getChildBones();

    default BoneSnapshot saveSnapshot() {
        return new BoneSnapshot(this);
    }
}