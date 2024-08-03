package dev.xkmc.l2artifacts.content.effects.v1;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PlayerOnlySetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SaintRestoration extends PlayerOnlySetEffect {

    private final LinearFuncEntry val;

    public SaintRestoration(LinearFuncEntry val) {
        super(0);
        this.val = val;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            int period = (int) this.val.getFromRank(rank);
            if (player.f_19797_ % period == 0 && player.m_21205_().isEmpty()) {
                if (player.m_21223_() < player.m_21233_()) {
                    player.m_5634_(1.0F);
                } else {
                    List<Player> list = player.m_9236_().m_45976_(Player.class, player.m_20191_().inflate(32.0));
                    Player min = null;
                    double health = 1000000.0;
                    for (Player p : list) {
                        if (p.m_6084_() && (double) p.m_21223_() < health) {
                            min = p;
                            health = (double) p.m_21223_();
                        }
                    }
                    if (min != null) {
                        min.m_5634_(1.0F);
                    }
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        double period = this.val.getFromRank(rank) / 20.0;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(period)));
    }
}