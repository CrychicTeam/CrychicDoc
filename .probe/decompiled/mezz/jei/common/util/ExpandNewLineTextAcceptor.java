package mezz.jei.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public class ExpandNewLineTextAcceptor implements FormattedText.StyledContentConsumer<Void> {

    private final List<FormattedText> lines = new ArrayList();

    @Nullable
    private MutableComponent lastComponent;

    @Override
    public Optional<Void> accept(Style style, String line) {
        String[] descriptionLineExpanded = line.split("\\\\n");
        for (int i = 0; i < descriptionLineExpanded.length; i++) {
            String s = descriptionLineExpanded[i];
            if (s.isEmpty()) {
                if (i == 0 && this.lastComponent != null) {
                    this.lines.add(this.lastComponent);
                    this.lastComponent = null;
                } else {
                    this.lines.add(Component.f_130760_);
                }
            } else {
                MutableComponent textComponent = Component.literal(s);
                textComponent.setStyle(style);
                if (this.lastComponent != null) {
                    if (i == 0) {
                        if (!this.lastComponent.getStyle().isEmpty() && !this.lastComponent.getStyle().equals(style)) {
                            this.lastComponent = Component.literal("").append(this.lastComponent);
                        }
                        this.lastComponent.append(textComponent);
                        continue;
                    }
                    this.lines.add(this.lastComponent);
                    this.lastComponent = null;
                }
                if (i == descriptionLineExpanded.length - 1) {
                    this.lastComponent = textComponent;
                } else {
                    this.lines.add(textComponent);
                }
            }
        }
        return Optional.empty();
    }

    public void addLinesTo(List<FormattedText> descriptionLinesExpanded) {
        descriptionLinesExpanded.addAll(this.lines);
        if (this.lastComponent != null) {
            descriptionLinesExpanded.add(this.lastComponent);
        }
    }
}