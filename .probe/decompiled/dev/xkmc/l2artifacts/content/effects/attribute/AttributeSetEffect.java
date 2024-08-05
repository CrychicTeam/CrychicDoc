package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class AttributeSetEffect extends SetEffect {

    private final AttrSetEntry[] entries;

    public AttributeSetEffect(AttrSetEntry... entries) {
        super(entries.length);
        this.entries = entries;
    }

    @Override
    public void update(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        for (int i = 0; i < this.entries.length; i++) {
            AttrSetEntry entry = this.entries[i];
            double val = entry.getValue(rank);
            AttributeInstance ins = player.getAttribute((Attribute) entry.attr().get());
            if (ins != null) {
                ins.removeModifier(ent.id[i]);
                if (enabled) {
                    ins.addTransientModifier(new AttributeModifier(ent.id[i], ent.getName(), val, entry.op()));
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        List<MutableComponent> ans = new ArrayList();
        for (AttrSetEntry ent : this.entries) {
            double val = ent.getValue(rank);
            String sign = val > 0.0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
            ans.add(Component.translatable(sign + (ent.usePercent() ? 1 : 0), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(ent.usePercent() ? val * 100.0 : val)), Component.translatable(((Attribute) ent.attr().get()).getDescriptionId())));
        }
        return ans;
    }
}