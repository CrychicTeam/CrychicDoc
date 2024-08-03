package net.minecraft.client;

import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.FormattedText;

public class ComponentCollector {

    private final List<FormattedText> parts = Lists.newArrayList();

    public void append(FormattedText formattedText0) {
        this.parts.add(formattedText0);
    }

    @Nullable
    public FormattedText getResult() {
        if (this.parts.isEmpty()) {
            return null;
        } else {
            return this.parts.size() == 1 ? (FormattedText) this.parts.get(0) : FormattedText.composite(this.parts);
        }
    }

    public FormattedText getResultOrEmpty() {
        FormattedText $$0 = this.getResult();
        return $$0 != null ? $$0 : FormattedText.EMPTY;
    }

    public void reset() {
        this.parts.clear();
    }
}