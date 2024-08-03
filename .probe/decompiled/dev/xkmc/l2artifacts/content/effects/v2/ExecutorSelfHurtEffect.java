package dev.xkmc.l2artifacts.content.effects.v2;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.damage.DamageTypeRoot;
import dev.xkmc.l2damagetracker.contents.damage.DefaultDamageState;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ExecutorSelfHurtEffect extends AttributeSetEffect {

    private final LinearFuncEntry factor;

    public ExecutorSelfHurtEffect(AttrSetEntry entry, LinearFuncEntry factor) {
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
    public void playerKillOpponentEvent(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
        double damage = (double) event.getEntity().getMaxHealth() * this.factor.getFromRank(rank);
        Holder<DamageType> type = DamageTypeRoot.of(DamageTypes.PLAYER_ATTACK).enable(DefaultDamageState.BYPASS_ARMOR).enable(DefaultDamageState.BYPASS_MAGIC).getHolder(le.m_9236_());
        le.hurt(new DamageSource(type, le), (float) damage);
    }
}