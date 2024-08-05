package dev.xkmc.l2artifacts.content.effects.v4;

import dev.xkmc.l2artifacts.content.config.ArtifactSetConfig;
import dev.xkmc.l2artifacts.content.effects.attribute.AbstractConditionalAttributeSetEffect;
import dev.xkmc.l2artifacts.content.effects.attribute.AttrSetEntry;
import dev.xkmc.l2library.capability.conditionals.ConditionalData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;

public class LongShooterPersistentEffect extends AbstractConditionalAttributeSetEffect<LongShooterPersistentData> {

    public LongShooterPersistentEffect(AttrSetEntry... entries) {
        super(entries);
    }

    @Override
    public void tick(Player player, ArtifactSetConfig.Entry ent, int rank, boolean enabled) {
        if (enabled) {
            if (player.f_19797_ % 10 == 0) {
                if (player.m_9236_().getEntities(EntityTypeTest.forClass(Monster.class), new AABB(player.m_20318_(0.0F), player.m_20318_(0.0F)).inflate(6.0), EntitySelector.NO_SPECTATORS).isEmpty()) {
                    LongShooterPersistentData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
                    data.update(11, rank);
                    this.addAttributes(player, ent, rank, data);
                    data.old = true;
                } else if (!player.m_9236_().getEntities(EntityTypeTest.forClass(Monster.class), new AABB(player.m_20318_(0.0F), player.m_20318_(0.0F)).inflate(6.0), EntitySelector.NO_SPECTATORS).isEmpty()) {
                    LongShooterPersistentData data = ConditionalData.HOLDER.get(player).getOrCreateData(this, ent);
                    if (data.old) {
                        data.update(40, rank);
                        player.m_7292_(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1));
                        data.old = false;
                    }
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetailedDescription(int rank) {
        List<MutableComponent> ans = new ArrayList();
        ans.add(this.getConditionText(rank));
        return ans;
    }

    public LongShooterPersistentData getData() {
        return new LongShooterPersistentData();
    }

    @Override
    protected MutableComponent getConditionText(int rank) {
        return Component.translatable(this.getDescriptionId() + ".desc");
    }
}