package com.mna.guide.sections;

import com.google.gson.JsonObject;
import com.mna.guide.interfaces.IEntrySection;
import com.mna.tools.TextConsumer;
import java.util.Collection;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TitleSection extends TextSection {

    public TitleSection() {
        this.CENTER = true;
        this.SCALE_FACTOR = 1.5F;
    }

    @Override
    public Collection<IEntrySection> parse(JsonObject element, int startY, int maxHeight, int maxWidth, int page) {
        if (startY >= maxHeight - 30) {
            page++;
            startY = 10;
        }
        return super.parse(element, startY, maxHeight, maxWidth, page);
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (FormattedCharSequence p : this.lines) {
            TextConsumer tc = new TextConsumer();
            p.accept(tc);
            sb.append(tc.getString() + " ");
        }
        return sb.toString();
    }
}