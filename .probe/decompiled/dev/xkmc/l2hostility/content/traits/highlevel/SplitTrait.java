package dev.xkmc.l2hostility.content.traits.highlevel;

import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.logic.InheritContext;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.events.MiscHandlers;
import net.minecraft.ChatFormatting;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class SplitTrait extends MobTrait {

    public SplitTrait(ChatFormatting format) {
        super(format);
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return !(le instanceof Slime) && super.allow(le, difficulty, maxModLv);
    }

    @Override
    public void onDeath(int lv, LivingEntity entity, LivingDeathEvent event) {
        if (!entity.m_9236_().isClientSide()) {
            if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
                this.add(entity);
                this.add(entity);
            }
        }
    }

    private void add(LivingEntity entity) {
        Level level = entity.m_9236_();
        LivingEntity e0 = (LivingEntity) entity.m_6095_().create(level);
        assert e0 != null;
        MiscHandlers.copyCap(entity, e0);
        e0.m_20359_(entity);
        level.m_7967_(e0);
    }

    @Override
    public int inherited(MobTraitCap cap, int rank, InheritContext ctx) {
        cap.lv /= 2;
        cap.noDrop = true;
        return rank - 1;
    }
}