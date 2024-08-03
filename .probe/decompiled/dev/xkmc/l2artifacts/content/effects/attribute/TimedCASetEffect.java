package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class TimedCASetEffect extends AbstractConditionalAttributeSetEffect<TimedCAData> {

    private final Predicate<Player> pred;

    private final LinearFuncEntry period;

    public TimedCASetEffect(Predicate<Player> pred, LinearFuncEntry period, AttrSetEntry... entries) {
        super(entries);
        this.pred = pred;
        this.period = period;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            TimedCAData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
            data.update(2, rank);
            if (!this.pred.test(player)) {
                data.time = 0;
                data.remove(player);
            } else {
                data.time++;
                if ((double) data.time > this.period.getFromRank(rank)) {
                    this.addAttributes(player, ent, rank, data);
                }
            }
        }
    }

    @Override
    protected MutableComponent getConditionText(int rank) {
        double time = (double) Math.round(this.period.getFromRank(rank)) / 20.0;
        return Component.translatable(this.getDescriptionId() + ".desc", time);
    }

    protected TimedCAData getData() {
        return new TimedCAData();
    }
}