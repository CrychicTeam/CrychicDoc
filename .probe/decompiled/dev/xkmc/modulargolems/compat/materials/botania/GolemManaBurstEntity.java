package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.ManaBurstEntity;

public class GolemManaBurstEntity extends ManaBurstEntity {

    private final int lv;

    public GolemManaBurstEntity(LivingEntity user, int lv) {
        super(BotaniaEntities.MANA_BURST, user.m_9236_());
        this.m_5602_(user);
        this.setBurstSourceCoords(NO_SOURCE);
        this.lv = lv;
    }

    protected void onHitEntity(@NotNull EntityHitResult hit) {
        if (this.m_6084_() && !this.m_9236_().isClientSide) {
            Entity owner = this.m_19749_();
            double dmg = 7.0;
            if (owner instanceof LivingEntity le) {
                dmg = le.getAttributeValue(Attributes.ATTACK_DAMAGE) * (double) this.lv * MGConfig.COMMON.manaBurstDamage.get();
            }
            hit.getEntity().hurt(this.m_269291_().indirectMagic(this, this.m_19749_()), (float) dmg);
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_146870_();
        }
    }
}