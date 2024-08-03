package net.minecraft.client.gui.layouts;

import com.mojang.math.Divisor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class LinearLayout extends AbstractLayout {

    private final LinearLayout.Orientation orientation;

    private final List<LinearLayout.ChildContainer> children = new ArrayList();

    private final LayoutSettings defaultChildLayoutSettings = LayoutSettings.defaults();

    public LinearLayout(int int0, int int1, LinearLayout.Orientation linearLayoutOrientation2) {
        this(0, 0, int0, int1, linearLayoutOrientation2);
    }

    public LinearLayout(int int0, int int1, int int2, int int3, LinearLayout.Orientation linearLayoutOrientation4) {
        super(int0, int1, int2, int3);
        this.orientation = linearLayoutOrientation4;
    }

    @Override
    public void arrangeElements() {
        super.m_264036_();
        if (!this.children.isEmpty()) {
            int $$0 = 0;
            int $$1 = this.orientation.getSecondaryLength(this);
            for (LinearLayout.ChildContainer $$2 : this.children) {
                $$0 += this.orientation.getPrimaryLength($$2);
                $$1 = Math.max($$1, this.orientation.getSecondaryLength($$2));
            }
            int $$3 = this.orientation.getPrimaryLength(this) - $$0;
            int $$4 = this.orientation.getPrimaryPosition(this);
            Iterator<LinearLayout.ChildContainer> $$5 = this.children.iterator();
            LinearLayout.ChildContainer $$6 = (LinearLayout.ChildContainer) $$5.next();
            this.orientation.setPrimaryPosition($$6, $$4);
            $$4 += this.orientation.getPrimaryLength($$6);
            if (this.children.size() >= 2) {
                Divisor $$7 = new Divisor($$3, this.children.size() - 1);
                while ($$7.hasNext()) {
                    $$4 += $$7.nextInt();
                    LinearLayout.ChildContainer $$8 = (LinearLayout.ChildContainer) $$5.next();
                    this.orientation.setPrimaryPosition($$8, $$4);
                    $$4 += this.orientation.getPrimaryLength($$8);
                }
            }
            int $$9 = this.orientation.getSecondaryPosition(this);
            for (LinearLayout.ChildContainer $$10 : this.children) {
                this.orientation.setSecondaryPosition($$10, $$9, $$1);
            }
            switch(this.orientation) {
                case HORIZONTAL:
                    this.f_263818_ = $$1;
                    break;
                case VERTICAL:
                    this.f_263674_ = $$1;
            }
        }
    }

    @Override
    public void visitChildren(Consumer<LayoutElement> consumerLayoutElement0) {
        this.children.forEach(p_265178_ -> consumerLayoutElement0.accept(p_265178_.f_263728_));
    }

    public LayoutSettings newChildLayoutSettings() {
        return this.defaultChildLayoutSettings.copy();
    }

    public LayoutSettings defaultChildLayoutSetting() {
        return this.defaultChildLayoutSettings;
    }

    public <T extends LayoutElement> T addChild(T t0) {
        return this.addChild(t0, this.newChildLayoutSettings());
    }

    public <T extends LayoutElement> T addChild(T t0, LayoutSettings layoutSettings1) {
        this.children.add(new LinearLayout.ChildContainer(t0, layoutSettings1));
        return t0;
    }

    static class ChildContainer extends AbstractLayout.AbstractChildWrapper {

        protected ChildContainer(LayoutElement layoutElement0, LayoutSettings layoutSettings1) {
            super(layoutElement0, layoutSettings1);
        }
    }

    public static enum Orientation {

        HORIZONTAL, VERTICAL;

        int getPrimaryLength(LayoutElement p_265322_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265322_.getWidth();
                case VERTICAL ->
                    p_265322_.getHeight();
            };
        }

        int getPrimaryLength(LinearLayout.ChildContainer p_265173_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265173_.m_264477_();
                case VERTICAL ->
                    p_265173_.m_264608_();
            };
        }

        int getSecondaryLength(LayoutElement p_265570_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265570_.getHeight();
                case VERTICAL ->
                    p_265570_.getWidth();
            };
        }

        int getSecondaryLength(LinearLayout.ChildContainer p_265345_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265345_.m_264608_();
                case VERTICAL ->
                    p_265345_.m_264477_();
            };
        }

        void setPrimaryPosition(LinearLayout.ChildContainer p_265660_, int p_265194_) {
            switch(this) {
                case HORIZONTAL:
                    p_265660_.m_264032_(p_265194_, p_265660_.m_264477_());
                    break;
                case VERTICAL:
                    p_265660_.m_264572_(p_265194_, p_265660_.m_264608_());
            }
        }

        void setSecondaryPosition(LinearLayout.ChildContainer p_265536_, int p_265313_, int p_265295_) {
            switch(this) {
                case HORIZONTAL:
                    p_265536_.m_264572_(p_265313_, p_265295_);
                    break;
                case VERTICAL:
                    p_265536_.m_264032_(p_265313_, p_265295_);
            }
        }

        int getPrimaryPosition(LayoutElement p_265209_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265209_.getX();
                case VERTICAL ->
                    p_265209_.getY();
            };
        }

        int getSecondaryPosition(LayoutElement p_265676_) {
            return switch(this) {
                case HORIZONTAL ->
                    p_265676_.getY();
                case VERTICAL ->
                    p_265676_.getX();
            };
        }
    }
}