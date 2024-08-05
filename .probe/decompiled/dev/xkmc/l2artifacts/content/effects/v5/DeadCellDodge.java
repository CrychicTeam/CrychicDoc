package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class DeadCellDodge extends PlayerOnlySetEffect {

    private final LinearFuncEntry chance;

    public DeadCellDodge(LinearFuncEntry chance) {
        super(0);
        this.chance = chance;
    }

    @Override
    public boolean playerAttackedCancel(Player player, ArtifactSetConfig.Entry ent, int rank, DamageSource source, AttackCache cache) {
        return (source.is(L2DamageTypes.DIRECT) || source.is(DamageTypeTags.IS_PROJECTILE)) && player.m_6144_() && player.m_217043_().nextDouble() < this.chance.getFromRank(rank);
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.chance.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val));
    }
}