package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.SetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class DamoclesSword extends SetEffect {

    private final LinearFuncEntry amplify;

    public DamoclesSword(LinearFuncEntry amplify) {
        super(0);
        this.amplify = amplify;
    }

    @Override
    public void tick(LivingEntity le, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled && !le.m_9236_().isClientSide()) {
            if (le.getHealth() > 0.0F && le.getHealth() < le.getMaxHealth() / 2.0F && le.hurtTime == 0 && (!(le instanceof Player pl) || ConditionalData.HOLDER.get(pl).tickSinceDeath > 60)) {
                le.hurt(le.m_9236_().damageSources().fellOutOfWorld(), le.getMaxHealth());
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double amplify = this.amplify.getFromRank(rank);
        int amount = (int) Math.round(amplify * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", amount));
    }

    @Override
    public void playerHurtOpponentEvent(LivingEntity player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if (!(player.getHealth() < player.getMaxHealth())) {
            event.addHurtModifier(DamageModifier.multBase((float) this.amplify.getFromRank(rank)));
        }
    }
}