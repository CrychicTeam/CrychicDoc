package net.minecraft.world.entity.animal.horse;

import javax.annotation.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SkeletonTrapGoal extends Goal {

    private final SkeletonHorse horse;

    public SkeletonTrapGoal(SkeletonHorse skeletonHorse0) {
        this.horse = skeletonHorse0;
    }

    @Override
    public boolean canUse() {
        return this.horse.m_9236_().m_45914_(this.horse.m_20185_(), this.horse.m_20186_(), this.horse.m_20189_(), 10.0);
    }

    @Override
    public void tick() {
        ServerLevel $$0 = (ServerLevel) this.horse.m_9236_();
        DifficultyInstance $$1 = $$0.m_6436_(this.horse.m_20183_());
        this.horse.setTrap(false);
        this.horse.m_30651_(true);
        this.horse.m_146762_(0);
        LightningBolt $$2 = EntityType.LIGHTNING_BOLT.create($$0);
        if ($$2 != null) {
            $$2.m_6027_(this.horse.m_20185_(), this.horse.m_20186_(), this.horse.m_20189_());
            $$2.setVisualOnly(true);
            $$0.addFreshEntity($$2);
            Skeleton $$3 = this.createSkeleton($$1, this.horse);
            if ($$3 != null) {
                $$3.m_20329_(this.horse);
                $$0.m_47205_($$3);
                for (int $$4 = 0; $$4 < 3; $$4++) {
                    AbstractHorse $$5 = this.createHorse($$1);
                    if ($$5 != null) {
                        Skeleton $$6 = this.createSkeleton($$1, $$5);
                        if ($$6 != null) {
                            $$6.m_20329_($$5);
                            $$5.m_5997_(this.horse.m_217043_().triangle(0.0, 1.1485), 0.0, this.horse.m_217043_().triangle(0.0, 1.1485));
                            $$0.m_47205_($$5);
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private AbstractHorse createHorse(DifficultyInstance difficultyInstance0) {
        SkeletonHorse $$1 = EntityType.SKELETON_HORSE.create(this.horse.m_9236_());
        if ($$1 != null) {
            $$1.m_6518_((ServerLevel) this.horse.m_9236_(), difficultyInstance0, MobSpawnType.TRIGGERED, null, null);
            $$1.m_6034_(this.horse.m_20185_(), this.horse.m_20186_(), this.horse.m_20189_());
            $$1.f_19802_ = 60;
            $$1.m_21530_();
            $$1.m_30651_(true);
            $$1.m_146762_(0);
        }
        return $$1;
    }

    @Nullable
    private Skeleton createSkeleton(DifficultyInstance difficultyInstance0, AbstractHorse abstractHorse1) {
        Skeleton $$2 = EntityType.SKELETON.create(abstractHorse1.m_9236_());
        if ($$2 != null) {
            $$2.m_6518_((ServerLevel) abstractHorse1.m_9236_(), difficultyInstance0, MobSpawnType.TRIGGERED, null, null);
            $$2.m_6034_(abstractHorse1.m_20185_(), abstractHorse1.m_20186_(), abstractHorse1.m_20189_());
            $$2.f_19802_ = 60;
            $$2.m_21530_();
            if ($$2.m_6844_(EquipmentSlot.HEAD).isEmpty()) {
                $$2.m_8061_(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            }
            $$2.m_8061_(EquipmentSlot.MAINHAND, EnchantmentHelper.enchantItem($$2.m_217043_(), this.disenchant($$2.m_21205_()), (int) (5.0F + difficultyInstance0.getSpecialMultiplier() * (float) $$2.m_217043_().nextInt(18)), false));
            $$2.m_8061_(EquipmentSlot.HEAD, EnchantmentHelper.enchantItem($$2.m_217043_(), this.disenchant($$2.m_6844_(EquipmentSlot.HEAD)), (int) (5.0F + difficultyInstance0.getSpecialMultiplier() * (float) $$2.m_217043_().nextInt(18)), false));
        }
        return $$2;
    }

    private ItemStack disenchant(ItemStack itemStack0) {
        itemStack0.removeTagKey("Enchantments");
        return itemStack0;
    }
}