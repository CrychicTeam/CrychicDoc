package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PhysicalDamageEffect extends AttributeSetEffect {

    private final LinearFuncEntry factor;

    public PhysicalDamageEffect(AttrSetEntry entry, LinearFuncEntry factor) {
        super(entry);
        this.factor = factor;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        List<MutableComponent> ans = super.getDetailedDescription(rank);
        double val = this.factor.getFromRank(rank) * 100.0;
        ans.add(Component.translatable(this.getDescriptionId() + ".desc", (int) Math.round(val)));
        return ans;
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        LivingHurtEvent hurt = event.getLivingHurtEvent();
        assert hurt != null;
        if (hurt.getSource().is(L2DamageTypes.MAGIC)) {
            event.addHurtModifier(DamageModifier.multBase((float) this.factor.getFromRank(rank) - 1.0F));
        }
    }
}