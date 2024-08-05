package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.init.explosion.BaseExplosion;
import dev.xkmc.l2library.init.explosion.BaseExplosionContext;
import dev.xkmc.l2library.init.explosion.ExplosionHandler;
import dev.xkmc.l2library.init.explosion.ModExplosionContext;
import dev.xkmc.l2library.init.explosion.VanillaExplosionContext;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public record ExplodeArrowFeature(float radius, boolean hurt, boolean breakBlock) implements OnHitFeature {

    @Override
    public void onHitLivingEntity(GenericArrowEntity arrow, LivingEntity target, EntityHitResult hit) {
        this.explode(arrow, hit.m_82450_().x(), hit.m_82450_().y(), hit.m_82450_().z());
        arrow.m_146870_();
    }

    @Override
    public void onHitBlock(GenericArrowEntity arrow, BlockHitResult result) {
        this.explode(arrow, result.m_82450_().x, result.m_82450_().y, result.m_82450_().z);
        arrow.m_146870_();
    }

    private void explode(GenericArrowEntity arrow, double x, double y, double z) {
        boolean breaking = this.breakBlock();
        for (BowArrowFeature e : arrow.features.all()) {
            if (e == ExplosionBreakFeature.INS) {
                breaking = true;
                break;
            }
        }
        BaseExplosionContext base = new BaseExplosionContext(arrow.m_9236_(), x, y, z, this.radius);
        Explosion.BlockInteraction type = breaking ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
        VanillaExplosionContext mc = new VanillaExplosionContext(arrow, this.getSource(arrow), null, false, type);
        ModExplosionContext mod = entity -> this.onExplosionHurt(arrow, entity);
        ExplosionHandler.explode(new BaseExplosion(base, mc, mod));
    }

    private boolean onExplosionHurt(GenericArrowEntity arrow, Entity target) {
        if (target instanceof LivingEntity le) {
            if (arrow.m_19749_() instanceof Player pl) {
                le.setLastHurtByPlayer(pl);
            }
            arrow.doPostHurtEffects(le);
            return this.hurt;
        } else if (target instanceof ItemEntity) {
            return false;
        } else if (target instanceof ExperienceOrb) {
            return false;
        } else if (target instanceof HangingEntity) {
            return false;
        } else if (target instanceof Boat) {
            return false;
        } else {
            return target instanceof AbstractMinecart ? false : this.hurt;
        }
    }

    @Nullable
    private DamageSource getSource(GenericArrowEntity arrow) {
        Entity ent = arrow.m_19749_();
        return ent instanceof LivingEntity le ? le.m_9236_().damageSources().explosion(arrow, ent) : null;
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        if (this.hurt && this.breakBlock) {
            list.add(LangData.FEATURE_EXPLOSION_ALL.get(this.radius));
        }
        if (this.hurt && !this.breakBlock) {
            list.add(LangData.FEATURE_EXPLOSION_HURT.get(this.radius));
        }
        if (!this.hurt && !this.breakBlock) {
            list.add(LangData.FEATURE_EXPLOSION_NONE.get(this.radius));
        }
    }
}