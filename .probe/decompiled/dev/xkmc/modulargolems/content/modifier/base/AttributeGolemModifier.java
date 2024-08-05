package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;

public class AttributeGolemModifier extends GolemModifier {

    public final AttributeGolemModifier.AttrEntry[] entries;

    public AttributeGolemModifier(int max, AttributeGolemModifier.AttrEntry... entries) {
        super(StatFilterType.MASS, max);
        this.entries = entries;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        List<MutableComponent> ans = new ArrayList();
        for (AttributeGolemModifier.AttrEntry ent : this.entries) {
            ans.add(((GolemStatType) ent.type.get()).getAdderTooltip(ent.getValue(v)));
        }
        return ans;
    }

    public static record AttrEntry(Supplier<GolemStatType> type, DoubleSupplier value) {

        public double getValue(int level) {
            return this.value.getAsDouble() * (double) level;
        }
    }
}