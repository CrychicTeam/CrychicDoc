package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class SaintReduction extends PlayerOnlySetEffect {

    private final LinearFuncEntry atk;

    private final LinearFuncEntry def;

    public SaintReduction(LinearFuncEntry atk, LinearFuncEntry def) {
        super(0);
        this.atk = atk;
        this.def = def;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int damage = (int) Math.round(100.0 * (1.0 - this.atk.getFromRank(rank)));
        int reduction = (int) Math.round(100.0 * (1.0 - this.def.getFromRank(rank)));
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", damage, reduction));
    }

    @Override
    public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        event.addHurtModifier(DamageModifier.multBase((float) (-this.atk.getFromRank(rank))));
    }

    @Override
    public void playerReduceDamage(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        float amp = (float) (1.0 - this.def.getFromRank(rank));
        cache.addDealtModifier(DamageModifier.multTotal(amp));
    }
}