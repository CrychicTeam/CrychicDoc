package dev.xkmc.l2complements.content.effect.skill;

import dev.xkmc.l2complements.init.data.DamageTypeGen;
import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2complements.init.registrate.LCParticle;
import dev.xkmc.l2library.base.effects.api.ClientRenderEffect;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.FirstPlayerRenderEffect;
import dev.xkmc.l2library.util.Proxy;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EmeraldPopeEffect extends MobEffect implements FirstPlayerRenderEffect, ClientRenderEffect {

    public EmeraldPopeEffect(MobEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public void applyEffectTick(LivingEntity self, int level) {
        if (!self.m_9236_().isClientSide()) {
            int radius = (level + 1) * LCConfig.COMMON.emeraldBaseRange.get();
            AttributeInstance atk = self.getAttribute(Attributes.ATTACK_DAMAGE);
            int damage = (int) (LCConfig.COMMON.emeraldDamageFactor.get() * (atk == null ? 1.0 : atk.getValue()));
            DamageSource source = new DamageSource(DamageTypeGen.forKey(self.m_9236_(), DamageTypeGen.EMERALD), null, self);
            for (Entity e : self.m_9236_().m_45933_(self, new AABB(self.m_20183_()).inflate((double) radius))) {
                if (e instanceof Enemy && !e.isAlliedTo(self) && ((LivingEntity) e).hurtTime == 0 && e.position().distanceToSqr(self.m_20182_()) < (double) (radius * radius)) {
                    double dist = e.position().distanceTo(self.m_20182_());
                    if (dist > 0.1) {
                        ((LivingEntity) e).knockback(0.4F, e.position().x - self.m_20182_().x, e.position().z - self.m_20182_().z);
                    }
                    e.hurt(source, (float) damage);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int tick, int lv) {
        return tick % 10 == 0;
    }

    @Override
    public void render(LivingEntity entity, int lv, Consumer<DelayedEntityRender> consumer) {
        if (entity != Proxy.getClientPlayer()) {
            renderEffect(lv, entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onClientLevelRender(AbstractClientPlayer player, MobEffectInstance ins) {
        renderEffect(ins.getAmplifier(), player);
    }

    private static void renderEffect(int lv, Entity entity) {
        if (!Minecraft.getInstance().isPaused()) {
            int r = (lv + 1) * LCConfig.COMMON.emeraldBaseRange.get();
            int count = (1 + lv) * (1 + lv) * 4;
            for (int i = 0; i < count; i++) {
                addParticle(entity.level(), entity.position(), r);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void addParticle(Level w, Vec3 vec, int r) {
        float tpi = (float) (Math.PI * 2);
        Vec3 v0 = new Vec3(0.0, (double) r, 0.0);
        Vec3 v1 = v0.xRot(tpi / 3.0F).yRot((float) (Math.random() * (double) tpi));
        float a0 = (float) (Math.random() * (double) tpi);
        float b0 = (float) Math.acos(2.0 * Math.random() - 1.0);
        v0 = v0.xRot(a0).yRot(b0);
        v1 = v1.xRot(a0).yRot(b0);
        w.addAlwaysVisibleParticle((ParticleOptions) LCParticle.EMERALD.get(), vec.x + v0.x, vec.y + v0.y, vec.z + v0.z, vec.x + v1.x, vec.y + v1.y, vec.z + v1.z);
    }
}