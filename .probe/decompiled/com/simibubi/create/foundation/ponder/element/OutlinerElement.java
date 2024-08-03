package com.simibubi.create.foundation.ponder.element;

import com.simibubi.create.foundation.outliner.Outline;
import com.simibubi.create.foundation.outliner.Outliner;
import com.simibubi.create.foundation.ponder.PonderScene;
import java.util.function.Function;

public class OutlinerElement extends AnimatedSceneElement {

    private Function<Outliner, Outline.OutlineParams> outlinerCall;

    private int overrideColor;

    public OutlinerElement(Function<Outliner, Outline.OutlineParams> outlinerCall) {
        this.outlinerCall = outlinerCall;
        this.overrideColor = -1;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (!(this.fade.getValue() < 0.0625F)) {
            if (!(this.fade.getValue(0.0F) > this.fade.getValue(1.0F))) {
                Outline.OutlineParams params = (Outline.OutlineParams) this.outlinerCall.apply(scene.getOutliner());
                if (this.overrideColor != -1) {
                    params.colored(this.overrideColor);
                }
            }
        }
    }

    public void setColor(int overrideColor) {
        this.overrideColor = overrideColor;
    }
}