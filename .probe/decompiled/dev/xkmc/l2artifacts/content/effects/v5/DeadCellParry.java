package dev.xkmc.l2artifacts.content.effects.v5;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;

public class DeadCellParry extends PlayerOnlySetEffect {

    private final LinearFuncEntry reflect;

    public DeadCellParry(LinearFuncEntry reflect) {
        super(0);
        this.reflect = reflect;
    }

    @Override
    public void playerShieldBlock(Player player, ArtifactSetConfig.Entry entry, int rank, ShieldBlockEvent event) {
        if (event.getDamageSource().is(L2DamageTypes.DIRECT) && event.getDamageSource().getDirectEntity() instanceof LivingEntity le) {
            float dmg = (float) this.reflect.getFromRank(rank) * event.getBlockedDamage();
            GeneralEventHandler.schedule(() -> le.hurt(player.m_269291_().thorns(player), dmg));
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int val = (int) Math.round(this.reflect.getFromRank(rank) * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val));
    }
}