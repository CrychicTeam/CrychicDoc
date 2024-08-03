package dev.xkmc.modulargolems.compat.materials.l2complements;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class EnderTeleportModifier extends GolemModifier {

    private static final String KEY = "modulargolems:teleport";

    public EnderTeleportModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int seconds = MGConfig.COMMON.teleportCooldown.get() / 20;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", seconds).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(10, new EnderTeleportGoal(entity));
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (!event.getSource().is(L2DamageTypes.MAGIC)) {
            if (event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
                event.setCanceled(true);
            }
            if (mayTeleport(entity)) {
                for (int i = 0; i < 16; i++) {
                    if (teleport(entity)) {
                        event.setCanceled(true);
                        resetCooldown(entity);
                        return;
                    }
                }
            }
        }
    }

    public static boolean mayTeleport(AbstractGolemEntity<?, ?> entity) {
        long time = entity.getPersistentData().getLong("modulargolems:teleport");
        long current = entity.m_9236_().getGameTime();
        return current >= time + (long) MGConfig.COMMON.teleportCooldown.get().intValue();
    }

    public static void resetCooldown(AbstractGolemEntity<?, ?> entity) {
        entity.getPersistentData().putLong("modulargolems:teleport", entity.m_9236_().getGameTime());
    }

    public static boolean teleportTowards(AbstractGolemEntity<?, ?> entity, Entity pTarget) {
        return teleport(entity, pTarget.getX(), pTarget.getY(), pTarget.getZ());
    }

    private static boolean teleport(AbstractGolemEntity<?, ?> entity) {
        int r = MGConfig.COMMON.teleportRadius.get();
        if (!entity.m_9236_().isClientSide() && entity.m_6084_()) {
            double d0 = entity.m_20185_() + (entity.m_217043_().nextDouble() - 0.5) * (double) r * 2.0;
            double d1 = entity.m_20186_() + (double) (entity.m_217043_().nextInt(r * 2) - r);
            double d2 = entity.m_20189_() + (entity.m_217043_().nextDouble() - 0.5) * (double) r * 2.0;
            return teleport(entity, d0, d1, d2);
        } else {
            return false;
        }
    }

    public static boolean teleport(AbstractGolemEntity<?, ?> entity, double pX, double pY, double pZ) {
        BlockPos.MutableBlockPos ipos = new BlockPos.MutableBlockPos(pX, pY, pZ);
        while (ipos.m_123342_() > entity.m_9236_().m_141937_() && !entity.m_9236_().getBlockState(ipos).m_280555_()) {
            ipos.move(Direction.DOWN);
        }
        BlockState blockstate = entity.m_9236_().getBlockState(ipos);
        boolean flag = blockstate.m_280555_();
        boolean flag1 = blockstate.m_60819_().is(FluidTags.WATER);
        if (flag && !flag1) {
            EntityTeleportEvent.EnderEntity event = ForgeEventFactory.onEnderTeleport(entity, pX, pY, pZ);
            if (event.isCanceled()) {
                return false;
            } else {
                Vec3 vec3 = entity.m_20182_();
                boolean flag2 = entity.m_20984_(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
                if (flag2) {
                    entity.m_9236_().m_214171_(GameEvent.TELEPORT, vec3, GameEvent.Context.of(entity));
                    if (!entity.m_20067_()) {
                        entity.m_9236_().playSound(null, entity.f_19854_, entity.f_19855_, entity.f_19856_, SoundEvents.ENDERMAN_TELEPORT, entity.m_5720_(), 1.0F, 1.0F);
                        entity.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
                    }
                }
                return flag2;
            }
        } else {
            return false;
        }
    }
}