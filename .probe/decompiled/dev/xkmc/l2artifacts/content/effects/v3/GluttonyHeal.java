package dev.xkmc.l2artifacts.content.effects.v3;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class GluttonyHeal extends PlayerOnlySetEffect {

    private final LinearFuncEntry value;

    public GluttonyHeal(LinearFuncEntry value) {
        super(0);
        this.value = value;
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        int l = (int) Math.round(this.value.getFromRank(rank));
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", l, l * 2));
    }

    @Override
    public void playerKillOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, LivingDeathEvent event) {
        if (!player.m_9236_().isClientSide()) {
            int val = (int) Math.round(this.value.getFromRank(rank));
            player.getFoodData().eat(val, 1.0F);
        }
    }
}