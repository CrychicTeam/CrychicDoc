package dev.xkmc.l2artifacts.content.effects.persistent;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.core.PersistentDataSetEffect;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractConditionalPersistentSetEffect<T extends PeriodicData> extends PersistentDataSetEffect<T> {

    private final LinearFuncEntry period;

    public AbstractConditionalPersistentSetEffect(LinearFuncEntry period) {
        super(0);
        this.period = period;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            T data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
            data.update(2, rank);
            if (!this.test(player, ent, rank, data)) {
                data.tick_count = 0;
            } else {
                data.tick_count++;
                if ((double) data.tick_count >= this.period.getFromRank(rank)) {
                    data.tick_count = (int) ((double) data.tick_count - this.period.getFromRank(rank));
                    this.perform(player, ent, rank, data);
                }
            }
        }
    }

    protected abstract boolean test(Player var1, ArtifactSetConfig.Entry var2, int var3, T var4);

    protected abstract void perform(Player var1, ArtifactSetConfig.Entry var2, int var3, T var4);

    public abstract T getData(ArtifactSetConfig.Entry var1);
}