package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;

public class SimpleCASetEffect extends AbstractConditionalAttributeSetEffect<AttributeSetData> {

    private final Predicate<Player> pred;

    public SimpleCASetEffect(Predicate<Player> pred, AttrSetEntry... entries) {
        super(entries);
        this.pred = pred;
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            if (this.pred.test(player)) {
                AttributeSetData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
                data.update(2, rank);
                this.addAttributes(player, ent, rank, data);
            }
        }
    }

    @Override
    public AttributeSetData getData() {
        return new AttributeSetData();
    }
}