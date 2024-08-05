package net.minecraft.client.gui.layouts;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.util.Mth;

public class FrameLayout extends AbstractLayout {

    private final List<FrameLayout.ChildContainer> children = new ArrayList();

    private int minWidth;

    private int minHeight;

    private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults().align(0.5F, 0.5F);

    public FrameLayout() {
        this(0, 0, 0, 0);
    }

    public FrameLayout(int int0, int int1) {
        this(0, 0, int0, int1);
    }

    public FrameLayout(int int0, int int1, int int2, int int3) {
        super(int0, int1, int2, int3);
        this.setMinDimensions(int2, int3);
    }

    public FrameLayout setMinDimensions(int int0, int int1) {
        return this.setMinWidth(int0).setMinHeight(int1);
    }

    public FrameLayout setMinHeight(int int0) {
        this.minHeight = int0;
        return this;
    }

    public FrameLayout setMinWidth(int int0) {
        this.minWidth = int0;
        return this;
    }

    public LayoutSettings newChildLayoutSettings() {
        return this.defaultChildLayoutSettings.copy();
    }

    public LayoutSettings defaultChildLayoutSetting() {
        return this.defaultChildLayoutSettings;
    }

    @Override
    public void arrangeElements() {
        super.m_264036_();
        int $$0 = this.minWidth;
        int $$1 = this.minHeight;
        for (FrameLayout.ChildContainer $$2 : this.children) {
            $$0 = Math.max($$0, $$2.m_264477_());
            $$1 = Math.max($$1, $$2.m_264608_());
        }
        for (FrameLayout.ChildContainer $$3 : this.children) {
            $$3.m_264032_(this.m_252754_(), $$0);
            $$3.m_264572_(this.m_252907_(), $$1);
        }
        this.f_263674_ = $$0;
        this.f_263818_ = $$1;
    }

    public <T extends LayoutElement> T addChild(T t0) {
        return this.addChild(t0, this.newChildLayoutSettings());
    }

    public <T extends LayoutElement> T addChild(T t0, LayoutSettings layoutSettings1) {
        this.children.add(new FrameLayout.ChildContainer(t0, layoutSettings1));
        return t0;
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumerLayoutElement0) {
        this.children.forEach(p_265653_ -> consumerLayoutElement0.accept(p_265653_.f_263728_));
    }

    public static void centerInRectangle(LayoutElement layoutElement0, int int1, int int2, int int3, int int4) {
        alignInRectangle(layoutElement0, int1, int2, int3, int4, 0.5F, 0.5F);
    }

    public static void centerInRectangle(LayoutElement layoutElement0, ScreenRectangle screenRectangle1) {
        centerInRectangle(layoutElement0, screenRectangle1.position().x(), screenRectangle1.position().y(), screenRectangle1.width(), screenRectangle1.height());
    }

    public static void alignInRectangle(LayoutElement layoutElement0, ScreenRectangle screenRectangle1, float float2, float float3) {
        alignInRectangle(layoutElement0, screenRectangle1.left(), screenRectangle1.top(), screenRectangle1.width(), screenRectangle1.height(), float2, float3);
    }

    public static void alignInRectangle(LayoutElement layoutElement0, int int1, int int2, int int3, int int4, float float5, float float6) {
        alignInDimension(int1, int3, layoutElement0.getWidth(), layoutElement0::m_252865_, float5);
        alignInDimension(int2, int4, layoutElement0.getHeight(), layoutElement0::m_253211_, float6);
    }

    public static void alignInDimension(int int0, int int1, int int2, Consumer<Integer> consumerInteger3, float float4) {
        int $$5 = (int) Mth.lerp(float4, 0.0F, (float) (int1 - int2));
        consumerInteger3.accept(int0 + $$5);
    }

    static class ChildContainer extends AbstractLayout.AbstractChildWrapper {

        protected ChildContainer(LayoutElement layoutElement0, LayoutSettings layoutSettings1) {
            super(layoutElement0, layoutSettings1);
        }
    }
}