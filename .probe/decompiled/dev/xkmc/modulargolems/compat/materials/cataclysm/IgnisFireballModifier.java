package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.projectile.Ignis_Abyss_Fireball_Entity;
import com.github.L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class IgnisFireballModifier extends GolemModifier {

    private static final int[] TIME = new int[] { 45, 61, 77, 93, 109 };

    private static final int[] ANGLE = new int[] { -5, -2, 0, 2, 5 };

    public IgnisFireballModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new IgnisFireballAttackGoal(entity, lv));
    }

    public static void addFireball(LivingEntity user, int lv) {
        user.m_9236_().playLocalSound(user.m_20185_(), user.m_20186_(), user.m_20189_(), SoundEvents.EVOKER_PREPARE_SUMMON, user.m_5720_(), 5.0F, 1.4F + user.getRandom().nextFloat() * 0.1F, false);
        int index = user.getRandom().nextInt(5);
        lv = Mth.clamp(lv, 0, 2);
        for (int i = 2 - lv; i < 3 + lv; i++) {
            shootFireball(user, new Vec3((double) ANGLE[i], 3.0, 0.0), TIME[i], index == i);
        }
    }

    private static void shootFireball(LivingEntity user, Vec3 shotAt, int timer, boolean abyss) {
        shotAt = shotAt.yRot(-user.m_146908_() * (float) (Math.PI / 180.0));
        Projectile shot;
        if (abyss) {
            Ignis_Abyss_Fireball_Entity bullet = new Ignis_Abyss_Fireball_Entity(user.m_9236_(), user);
            bullet.setUp(timer);
            shot = bullet;
        } else {
            Ignis_Fireball_Entity bullet = new Ignis_Fireball_Entity(user.m_9236_(), user);
            bullet.setUp(timer);
            if (user.getHealth() < user.getMaxHealth() / 2.0F) {
                bullet.setSoul(true);
            }
            shot = bullet;
        }
        float rot = user.yBodyRot * (float) (Math.PI / 180.0);
        double width = (double) (user.m_20205_() + 1.0F) * 0.15;
        shot.m_6034_(user.m_20185_() - width * (double) Mth.sin(rot), user.m_20186_() + 1.0, user.m_20189_() + width * (double) Mth.cos(rot));
        double d0 = shotAt.x;
        double d1 = shotAt.y;
        double d2 = shotAt.z;
        float f = Mth.sqrt((float) (d0 * d0 + d2 * d2)) * 0.35F;
        shot.shoot(d0, d1 + (double) f, d2, 0.25F, 3.0F);
        user.m_9236_().m_7967_(shot);
    }
}