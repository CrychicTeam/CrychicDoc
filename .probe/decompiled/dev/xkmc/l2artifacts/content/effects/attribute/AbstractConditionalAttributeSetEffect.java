package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class AbstractConditionalAttributeSetEffect<T extends AttributeSetData> extends PersistentDataSetEffect<T> {

    private final AttrSetEntry[] entries;

    public AbstractConditionalAttributeSetEffect(AttrSetEntry... entries) {
        super(entries.length);
        this.entries = entries;
    }

    protected void addAttributes(Player player, ArtifactSetConfig.Entry ent, int rank, T data) {
        for (int i = 0; i < this.entries.length; i++) {
            AttrSetEntry entry = this.entries[i];
            AttributeInstance ins = player.m_21051_((Attribute) entry.attr().get());
            if (ins != null) {
                UUID id = ent.id[i];
                if (ins.getModifier(id) == null) {
                    double val = entry.getValue(rank);
                    ins.addTransientModifier(new AttributeModifier(id, ent.getName(), val, entry.op()));
                }
            }
        }
    }

    protected abstract T getData();

    public T getData(ArtifactSetConfig.Entry ent) {
        T ans = this.getData();
        for (int i = 0; i < this.entries.length; i++) {
            AttrSetEntry entry = this.entries[i];
            UUID id = ent.id[i];
            ans.list.add(new AttributeSetData.AttributePair((Attribute) entry.attr().get(), id));
        }
        return ans;
    }

    protected MutableComponent getConditionText(int rank) {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        List<MutableComponent> ans = new ArrayList();
        ans.add(this.getConditionText(rank));
        for (AttrSetEntry ent : this.entries) {
            double val = ent.getValue(rank);
            String sign = val > 0.0 ? "attribute.modifier.plus." : "attribute.modifier.take.";
            ans.add(Component.translatable(sign + (ent.usePercent() ? 1 : 0), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(ent.usePercent() ? val * 100.0 : val)), Component.translatable(((Attribute) ent.attr().get()).getDescriptionId())));
        }
        return ans;
    }
}