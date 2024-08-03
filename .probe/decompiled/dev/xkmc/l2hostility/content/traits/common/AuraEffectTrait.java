package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;

public class AuraEffectTrait extends MobTrait {

    private final Supplier<MobEffect> eff;

    public AuraEffectTrait(Supplier<MobEffect> eff) {
        super(() -> ((MobEffect) eff.get()).getColor());
        this.eff = eff;
    }

    protected boolean canApply(LivingEntity e) {
        return CurioCompat.hasItemInCurio(e, (Item) LHItems.RING_REFLECTION.get()) ? false : !CurioCompat.hasItemInCurio(e, (Item) LHItems.ABRAHADABRA.get());
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        int range = ((ForgeConfigSpec.IntValue) LHConfig.COMMON.range.get(this.getRegistryName().getPath())).get();
        if (!mob.m_9236_().isClientSide() && mob.f_19797_ % 5 == 0) {
            AABB box = mob.m_20191_().inflate((double) range);
            for (LivingEntity e : mob.m_9236_().m_45976_(LivingEntity.class, box)) {
                if ((!(e instanceof Player pl) || !pl.getAbilities().instabuild) && !(e.m_20270_(mob) > (float) range) && this.canApply(e)) {
                    EffectUtil.refreshEffect(e, new MobEffectInstance((MobEffect) this.eff.get(), 40, level - 1, true, true), EffectUtil.AddReason.FORCE, mob);
                }
            }
        }
        if (mob.m_9236_().isClientSide()) {
            Vec3 center = mob.m_20182_();
            float tpi = (float) (Math.PI * 2);
            Vec3 v0 = new Vec3(0.0, (double) range, 0.0);
            v0 = v0.xRot(tpi / 4.0F).yRot(mob.getRandom().nextFloat() * tpi);
            int k = ((MobEffect) this.eff.get()).getColor();
            mob.m_9236_().addAlwaysVisibleParticle(ParticleTypes.EFFECT, center.x + v0.x, center.y + v0.y + 0.5, center.z + v0.z, (double) (k >> 16 & 0xFF) / 255.0, (double) (k >> 8 & 0xFF) / 255.0, (double) (k & 0xFF) / 255.0);
        }
    }
}