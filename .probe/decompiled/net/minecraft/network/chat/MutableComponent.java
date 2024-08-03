package net.minecraft.network.chat;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.locale.Language;
import net.minecraft.util.FormattedCharSequence;

public class MutableComponent implements Component {

    private final ComponentContents contents;

    private final List<Component> siblings;

    private Style style;

    private FormattedCharSequence visualOrderText = FormattedCharSequence.EMPTY;

    @Nullable
    private Language decomposedWith;

    MutableComponent(ComponentContents componentContents0, List<Component> listComponent1, Style style2) {
        this.contents = componentContents0;
        this.siblings = listComponent1;
        this.style = style2;
    }

    public static MutableComponent create(ComponentContents componentContents0) {
        return new MutableComponent(componentContents0, Lists.newArrayList(), Style.EMPTY);
    }

    @Override
    public ComponentContents getContents() {
        return this.contents;
    }

    @Override
    public List<Component> getSiblings() {
        return this.siblings;
    }

    public MutableComponent setStyle(Style style0) {
        this.style = style0;
        return this;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    public MutableComponent append(String string0) {
        return this.append(Component.literal(string0));
    }

    public MutableComponent append(Component component0) {
        this.siblings.add(component0);
        return this;
    }

    public MutableComponent withStyle(UnaryOperator<Style> unaryOperatorStyle0) {
        this.setStyle((Style) unaryOperatorStyle0.apply(this.getStyle()));
        return this;
    }

    public MutableComponent withStyle(Style style0) {
        this.setStyle(style0.applyTo(this.getStyle()));
        return this;
    }

    public MutableComponent withStyle(ChatFormatting... chatFormatting0) {
        this.setStyle(this.getStyle().applyFormats(chatFormatting0));
        return this;
    }

    public MutableComponent withStyle(ChatFormatting chatFormatting0) {
        this.setStyle(this.getStyle().applyFormat(chatFormatting0));
        return this;
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        Language $$0 = Language.getInstance();
        if (this.decomposedWith != $$0) {
            this.visualOrderText = $$0.getVisualOrder(this);
            this.decomposedWith = $$0;
        }
        return this.visualOrderText;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof MutableComponent $$1) ? false : this.contents.equals($$1.contents) && this.style.equals($$1.style) && this.siblings.equals($$1.siblings);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.contents, this.style, this.siblings });
    }

    public String toString() {
        StringBuilder $$0 = new StringBuilder(this.contents.toString());
        boolean $$1 = !this.style.isEmpty();
        boolean $$2 = !this.siblings.isEmpty();
        if ($$1 || $$2) {
            $$0.append('[');
            if ($$1) {
                $$0.append("style=");
                $$0.append(this.style);
            }
            if ($$1 && $$2) {
                $$0.append(", ");
            }
            if ($$2) {
                $$0.append("siblings=");
                $$0.append(this.siblings);
            }
            $$0.append(']');
        }
        return $$0.toString();
    }
}