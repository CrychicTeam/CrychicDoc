package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.special.BaseRangedAttackGoal;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import vazkii.botania.common.entity.ManaBurstEntity;

public class ManaBurstAttackGoal extends BaseRangedAttackGoal {

    public ManaBurstAttackGoal(AbstractGolemEntity<?, ?> golem, int lv) {
        super(40, 4, 48, golem, lv);
    }

    @Override
    protected void performAttack(LivingEntity target) {
        int manaCost = MGConfig.COMMON.manaBurstCost.get() * this.lv;
        BotUtils bot = new BotUtils(this.golem);
        if (bot.getMana() >= manaCost) {
            bot.consumeMana(manaCost);
            ManaBurstEntity burst = this.getBurst(this.golem);
            this.golem.m_9236_().m_7967_(burst);
        }
    }

    public ManaBurstEntity getBurst(LivingEntity golem) {
        ManaBurstEntity burst = new GolemManaBurstEntity(golem, this.lv);
        Vec3 pos = new Vec3(golem.m_20185_(), golem.m_20188_() - 0.1F, golem.m_20189_());
        burst.m_146884_(pos);
        Vec3 forward = golem.m_20156_();
        if (golem instanceof Mob mob) {
            LivingEntity target = mob.getTarget();
            if (target != null) {
                forward = new Vec3(target.m_20185_(), target.m_20227_(0.5), target.m_20189_()).subtract(pos).normalize();
            }
        }
        double d0 = forward.horizontalDistance();
        burst.m_146922_((float) (180.0 - Mth.atan2(forward.x, forward.z) * 180.0F / (float) Math.PI));
        burst.m_146926_((float) (Mth.atan2(forward.y, d0) * 180.0F / (float) Math.PI));
        burst.m_20256_(ManaBurstEntity.calculateBurstVelocity(burst.m_146909_(), burst.m_146908_()));
        float motionModifier = 7.0F;
        int manaCost = MGConfig.COMMON.manaBurstCost.get() * this.lv;
        burst.setColor(2162464);
        burst.setMana(manaCost);
        burst.setStartingMana(manaCost);
        burst.setMinManaLoss(40);
        burst.setManaLossPerTick((float) manaCost / 40.0F);
        burst.setGravity(0.0F);
        burst.m_20256_(burst.m_20184_().scale((double) motionModifier));
        return burst;
    }
}