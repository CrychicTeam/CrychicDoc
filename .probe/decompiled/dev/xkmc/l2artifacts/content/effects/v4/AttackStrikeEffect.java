package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class AttackStrikeEffect extends AbstractConditionalAttributeSetEffect<AttackStrikeData> {

    private final LinearFuncEntry duration;

    private final LinearFuncEntry count;

    public AttackStrikeEffect(LinearFuncEntry duration, LinearFuncEntry count, AttrSetEntry... entries) {
        super(entries);
        this.duration = duration;
        this.count = count;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            AttackStrikeData data = ConditionalData.HOLDER.get(player).getData(this.getKey());
            if (data != null && (double) data.count >= this.count.getFromRank(rank)) {
                this.addAttributes(player, ent, rank, data);
            }
        }
    }

    @Override
    public void playerHurtOpponentEvent(Player player, ArtifactSetConfig.Entry ent, int rank, AttackCache event) {
        if ((double) event.getStrength() > 0.99) {
            AttackStrikeData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
            data.update((int) this.duration.getFromRank(rank), rank);
            data.count++;
        }
    }

    @Override
    protected MutableComponent getConditionText(int rank) {
        int c = (int) Math.round(this.count.getFromRank(rank));
        double t = this.duration.getFromRank(rank) / 20.0;
        return Component.translatable(this.getDescriptionId() + ".desc", c, t);
    }

    protected AttackStrikeData getData() {
        return new AttackStrikeData();
    }
}