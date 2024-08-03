package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2artifacts.content.effects.attribute.AttributeSetData;
import dev.xkmc.l2artifacts.init.registrate.items.LAItem4;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public class LongShooterEffect extends AbstractConditionalAttributeSetEffect<AttributeSetData> {

    public LongShooterEffect(AttrSetEntry... entries) {
        super(entries);
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            if (player.f_19797_ % 10 == 0) {
                if (player.m_9236_().getEntities(EntityTypeTest.forClass(Monster.class), new AABB(player.m_20318_(0.0F), player.m_20318_(0.0F)).inflate(8.0), EntitySelector.NO_SPECTATORS).isEmpty() && !ConditionalData.HOLDER.get(player).hasData(((LongShooterPersistentEffect) LAItem4.EFF_LONGSHOOTER_4.get()).getKey())) {
                    AttributeSetData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
                    data.update(11, rank);
                    this.addAttributes(player, ent, rank, data);
                }
            }
        }
    }

    @Override
    public AttributeSetData getData() {
        return new AttributeSetData();
    }

    @Override
    protected MutableComponent getConditionText(int rank) {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
}