package net.mehvahdjukaar.supplementaries.integration.create;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BambooSpikesBlock;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BambooSpikesBlockTile;
import net.mehvahdjukaar.supplementaries.integration.CreateCompat;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BambooSpikesBehavior implements MovementBehaviour {

    private static final BambooSpikesBlockTile DUMMY = new BambooSpikesBlockTile(BlockPos.ZERO, ((Block) ModRegistry.BAMBOO_SPIKES.get()).defaultBlockState());

    public boolean isSameDir(MovementContext context) {
        return VecHelper.isVecPointingTowards(context.relativeMotion, (Direction) context.state.m_61143_(BambooSpikesBlock.FACING));
    }

    @Override
    public boolean renderAsNormalBlockEntity() {
        return true;
    }

    @Override
    public void tick(MovementContext context) {
        this.damageEntities(context);
    }

    public void damageEntities(MovementContext context) {
        Level world = context.world;
        Vec3 pos = context.position;
        DamageSource damageSource = BambooSpikesBlock.getDamageSource(world);
        label67: for (Entity entity : world.m_45976_(Entity.class, new AABB(pos.add(-0.5, -0.5, -0.5), pos.add(0.5, 0.5, 0.5)))) {
            if (!(entity instanceof ItemEntity) && !(entity instanceof AbstractContraptionEntity) && (!(entity instanceof Player player) || !player.isCreative())) {
                if (entity instanceof AbstractMinecart) {
                    for (Entity passenger : entity.getIndirectPassengers()) {
                        if (CreateCompat.isContraption(context, passenger)) {
                            continue label67;
                        }
                    }
                }
                if (entity.isAlive() && entity instanceof LivingEntity livingEntity && !world.isClientSide) {
                    double pow = 5.0 * Math.pow(context.relativeMotion.length(), 0.4) + 1.0;
                    float damage = !this.isSameDir(context) ? 1.0F : (float) Mth.clamp(pow, 2.0, 6.0);
                    entity.hurt(damageSource, damage);
                    this.doTileStuff(context, world, livingEntity);
                }
                if (world.isClientSide == (entity instanceof Player)) {
                    Vec3 motionBoost = context.motion.add(0.0, context.motion.length() / 4.0, 0.0);
                    int maxBoost = 4;
                    if (motionBoost.length() > (double) maxBoost) {
                        motionBoost = motionBoost.subtract(motionBoost.normalize().scale(motionBoost.length() - (double) maxBoost));
                    }
                    entity.setDeltaMovement(entity.getDeltaMovement().add(motionBoost));
                    entity.hurtMarked = true;
                }
            }
        }
    }

    private void doTileStuff(MovementContext context, @NotNull Level world, LivingEntity le) {
        CompoundTag com = context.blockEntityData;
        if (com != null) {
            long lastTicked = com.getLong("LastTicked");
            if (!this.isOnCooldown(world, lastTicked)) {
                DUMMY.load(com);
                if (DUMMY.interactWithEntity(le, world)) {
                    CreateCompat.changeState(context, (BlockState) context.state.m_61124_(BambooSpikesBlock.TIPPED, false));
                }
                com = DUMMY.m_187480_();
                lastTicked = world.getGameTime();
                com.putLong("LastTicked", lastTicked);
                context.blockEntityData = com;
            }
        }
    }

    public boolean isOnCooldown(Level world, long lastTicked) {
        return world.getGameTime() - lastTicked < 20L;
    }
}