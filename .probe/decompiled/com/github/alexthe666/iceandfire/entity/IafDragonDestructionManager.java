package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.api.event.DragonFireDamageWorldEvent;
import com.github.alexthe666.iceandfire.block.BlockCharedPath;
import com.github.alexthe666.iceandfire.block.BlockFallingReturningState;
import com.github.alexthe666.iceandfire.block.BlockReturningState;
import com.github.alexthe666.iceandfire.block.IDragonProof;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import com.github.alexthe666.iceandfire.entity.util.BlockLaunchExplosion;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.misc.IafDamageRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

public class IafDragonDestructionManager {

    public static void destroyAreaBreath(Level level, BlockPos center, EntityDragonBase dragon) {
        if (!MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(dragon, (double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()))) {
            int statusDuration;
            float damageScale;
            if (dragon.dragonType == DragonType.FIRE) {
                statusDuration = 5 + dragon.getDragonStage() * 5;
                damageScale = (float) IafConfig.dragonAttackDamageFire;
            } else if (dragon.dragonType == DragonType.ICE) {
                statusDuration = 50 * dragon.getDragonStage();
                damageScale = (float) IafConfig.dragonAttackDamageIce;
            } else {
                if (dragon.dragonType != DragonType.LIGHTNING) {
                    return;
                }
                statusDuration = 3;
                damageScale = (float) IafConfig.dragonAttackDamageLightning;
            }
            double damageRadius = 3.5;
            boolean canBreakBlocks = ForgeEventFactory.getMobGriefingEvent(level, dragon);
            if (dragon.getDragonStage() <= 3) {
                BlockPos.betweenClosedStream(center.offset(-1, -1, -1), center.offset(1, 1, 1)).forEach(position -> {
                    if (level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge) {
                        forge.onHitWithFlame();
                    } else {
                        if (canBreakBlocks && DragonUtils.canGrief(dragon) && dragon.m_217043_().nextBoolean()) {
                            attackBlock(level, dragon, position);
                        }
                    }
                });
            } else {
                int radius = dragon.getDragonStage() == 4 ? 2 : 3;
                int x = radius + level.random.nextInt(1);
                int y = radius + level.random.nextInt(1);
                int z = radius + level.random.nextInt(1);
                float f = (float) (x + y + z) * 0.333F + 0.5F;
                float ff = f * f;
                damageRadius = (double) (2.5F + f * 1.2F);
                BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(position -> {
                    if (level.getBlockEntity(position) instanceof TileEntityDragonforgeInput forge) {
                        forge.onHitWithFlame();
                    } else {
                        if (canBreakBlocks && center.m_123331_(position) <= (double) ff && DragonUtils.canGrief(dragon) && level.random.nextFloat() > (float) center.m_123331_(position) / ff) {
                            attackBlock(level, dragon, position);
                        }
                    }
                });
            }
            DamageSource damageSource = getDamageSource(dragon);
            float stageDamage = (float) dragon.getDragonStage() * damageScale;
            level.m_45976_(LivingEntity.class, new AABB((double) center.m_123341_() - damageRadius, (double) center.m_123342_() - damageRadius, (double) center.m_123343_() - damageRadius, (double) center.m_123341_() + damageRadius, (double) center.m_123342_() + damageRadius, (double) center.m_123343_() + damageRadius)).forEach(target -> {
                if (!DragonUtils.onSameTeam(dragon, target) && !dragon.m_7306_(target) && dragon.m_142582_(target)) {
                    target.hurt(damageSource, stageDamage);
                    applyDragonEffect(target, dragon, statusDuration);
                }
            });
        }
    }

    public static void destroyAreaCharge(Level level, BlockPos center, EntityDragonBase dragon) {
        if (dragon != null) {
            if (!MinecraftForge.EVENT_BUS.post(new DragonFireDamageWorldEvent(dragon, (double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_()))) {
                int x = 2;
                int y = 2;
                int z = 2;
                boolean canBreakBlocks = DragonUtils.canGrief(dragon) && ForgeEventFactory.getMobGriefingEvent(level, dragon);
                if (canBreakBlocks) {
                    if (dragon.getDragonStage() <= 3) {
                        BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(position -> {
                            BlockState state = level.getBlockState(position);
                            if (!(state.m_60734_() instanceof IDragonProof)) {
                                if ((double) (dragon.m_217043_().nextFloat() * 3.0F) > center.m_123331_(position) && DragonUtils.canDragonBreak(state, dragon)) {
                                    level.m_46961_(position, false);
                                }
                                if (dragon.m_217043_().nextBoolean()) {
                                    attackBlock(level, dragon, position, state);
                                }
                            }
                        });
                    } else {
                        int radius = dragon.getDragonStage() == 4 ? 2 : 3;
                        x = radius + level.random.nextInt(2);
                        y = radius + level.random.nextInt(2);
                        z = radius + level.random.nextInt(2);
                        float f = (float) (x + y + z) * 0.333F + 0.5F;
                        float ff = f * f;
                        destroyBlocks(level, center, x, y, z, (double) ff, dragon);
                        BlockPos.betweenClosedStream(center.offset(-(++x), -(++y), -(++z)), center.offset(x, y, z)).forEach(position -> {
                            if (center.m_123331_(position) <= (double) ff) {
                                attackBlock(level, dragon, position);
                            }
                        });
                    }
                }
                int statusDuration;
                if (dragon.dragonType == DragonType.FIRE) {
                    statusDuration = 15;
                } else if (dragon.dragonType == DragonType.ICE) {
                    statusDuration = 400;
                } else {
                    if (dragon.dragonType != DragonType.LIGHTNING) {
                        return;
                    }
                    statusDuration = 9;
                }
                float stageDamage = (float) Math.max(1, dragon.getDragonStage() - 1) * 2.0F;
                DamageSource damageSource = getDamageSource(dragon);
                level.m_45976_(LivingEntity.class, new AABB((double) center.m_123341_() - (double) x, (double) center.m_123342_() - (double) y, (double) center.m_123343_() - (double) z, (double) center.m_123341_() + (double) x, (double) center.m_123342_() + (double) y, (double) center.m_123343_() + (double) z)).forEach(target -> {
                    if (!dragon.isAlliedTo(target) && !dragon.m_7306_(target) && dragon.m_142582_(target)) {
                        target.hurt(damageSource, stageDamage);
                        applyDragonEffect(target, dragon, statusDuration);
                    }
                });
                if (IafConfig.explosiveDragonBreath) {
                    causeExplosion(level, center, dragon, damageSource, dragon.getDragonStage());
                }
            }
        }
    }

    private static DamageSource getDamageSource(EntityDragonBase dragon) {
        Player player = dragon.getRidingPlayer();
        if (dragon.dragonType == DragonType.FIRE) {
            return (DamageSource) (player != null ? IafDamageRegistry.causeIndirectDragonFireDamage(dragon, player) : IafDamageRegistry.causeDragonFireDamage(dragon));
        } else if (dragon.dragonType == DragonType.ICE) {
            return (DamageSource) (player != null ? IafDamageRegistry.causeIndirectDragonIceDamage(dragon, player) : IafDamageRegistry.causeDragonIceDamage(dragon));
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            return (DamageSource) (player != null ? IafDamageRegistry.causeIndirectDragonLightningDamage(dragon, player) : IafDamageRegistry.causeDragonLightningDamage(dragon));
        } else {
            return dragon.m_9236_().damageSources().mobAttack(dragon);
        }
    }

    private static void attackBlock(Level level, EntityDragonBase dragon, BlockPos position, BlockState state) {
        if (!(state.m_60734_() instanceof IDragonProof) && DragonUtils.canDragonBreak(state, dragon)) {
            BlockState transformed;
            if (dragon.dragonType == DragonType.FIRE) {
                transformed = transformBlockFire(state);
            } else if (dragon.dragonType == DragonType.ICE) {
                transformed = transformBlockIce(state);
            } else {
                if (dragon.dragonType != DragonType.LIGHTNING) {
                    return;
                }
                transformed = transformBlockLightning(state);
            }
            if (!transformed.m_60713_(state.m_60734_())) {
                level.setBlockAndUpdate(position, transformed);
            }
            Block elementalBlock;
            boolean doPlaceBlock;
            if (dragon.dragonType == DragonType.FIRE) {
                elementalBlock = Blocks.FIRE;
                doPlaceBlock = dragon.m_217043_().nextBoolean();
            } else {
                if (dragon.dragonType != DragonType.ICE) {
                    return;
                }
                elementalBlock = IafBlockRegistry.DRAGON_ICE_SPIKES.get();
                doPlaceBlock = dragon.m_217043_().nextInt(9) == 0;
            }
            BlockState stateAbove = level.getBlockState(position.above());
            if (doPlaceBlock && transformed.m_280296_() && stateAbove.m_60819_().isEmpty() && !stateAbove.m_60815_() && state.m_60815_() && DragonUtils.canDragonBreak(stateAbove, dragon)) {
                level.setBlockAndUpdate(position.above(), elementalBlock.defaultBlockState());
            }
        }
    }

    private static void attackBlock(Level level, EntityDragonBase dragon, BlockPos position) {
        attackBlock(level, dragon, position, level.getBlockState(position));
    }

    private static void applyDragonEffect(LivingEntity target, EntityDragonBase dragon, int statusDuration) {
        if (dragon.dragonType == DragonType.FIRE) {
            target.m_20254_(statusDuration);
        } else if (dragon.dragonType == DragonType.ICE) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, statusDuration));
        } else if (dragon.dragonType == DragonType.LIGHTNING) {
            double x = dragon.m_20185_() - target.m_20185_();
            double y = dragon.m_20189_() - target.m_20189_();
            target.knockback((double) statusDuration / 10.0, x, y);
        }
    }

    private static void causeExplosion(Level world, BlockPos center, EntityDragonBase destroyer, DamageSource source, int stage) {
        Explosion.BlockInteraction mode = ForgeEventFactory.getMobGriefingEvent(world, destroyer) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.KEEP;
        BlockLaunchExplosion explosion = new BlockLaunchExplosion(world, destroyer, source, (double) center.m_123341_(), (double) center.m_123342_(), (double) center.m_123343_(), (float) Math.min(2, stage - 2), mode);
        explosion.m_46061_();
        explosion.finalizeExplosion(true);
    }

    private static void destroyBlocks(Level world, BlockPos center, int x, int y, int z, double radius2, Entity destroyer) {
        BlockPos.betweenClosedStream(center.offset(-x, -y, -z), center.offset(x, y, z)).forEach(pos -> {
            if (center.m_123331_(pos) <= radius2) {
                BlockState state = world.getBlockState(pos);
                if (state.m_60734_() instanceof IDragonProof) {
                    return;
                }
                if ((double) (world.random.nextFloat() * 3.0F) > (double) ((float) center.m_123331_(pos)) / radius2 && DragonUtils.canDragonBreak(state, destroyer)) {
                    world.m_46961_(pos, false);
                }
            }
        });
    }

    public static BlockState transformBlockFire(BlockState in) {
        if (in.m_60734_() instanceof SpreadingSnowyDirtBlock) {
            return (BlockState) IafBlockRegistry.CHARRED_GRASS.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        } else if (in.m_60713_(Blocks.DIRT)) {
            return (BlockState) IafBlockRegistry.CHARRED_DIRT.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        } else if (in.m_204336_(BlockTags.SAND) && in.m_60734_() == Blocks.GRAVEL) {
            return (BlockState) IafBlockRegistry.CHARRED_GRAVEL.get().defaultBlockState().m_61124_(BlockFallingReturningState.REVERTS, true);
        } else if (!in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) || in.m_60734_() != Blocks.COBBLESTONE && !in.m_60734_().getDescriptionId().contains("cobblestone")) {
            if (in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) && in.m_60734_() != IafBlockRegistry.CHARRED_COBBLESTONE.get()) {
                return (BlockState) IafBlockRegistry.CHARRED_STONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
            } else if (in.m_60734_() == Blocks.DIRT_PATH) {
                return (BlockState) IafBlockRegistry.CHARRED_DIRT_PATH.get().defaultBlockState().m_61124_(BlockCharedPath.REVERTS, true);
            } else if (in.m_204336_(BlockTags.LOGS) || in.m_204336_(BlockTags.PLANKS)) {
                return IafBlockRegistry.ASH.get().defaultBlockState();
            } else {
                return !in.m_204336_(BlockTags.LEAVES) && !in.m_204336_(BlockTags.FLOWERS) && !in.m_204336_(BlockTags.CROPS) && in.m_60734_() != Blocks.SNOW ? in : Blocks.AIR.defaultBlockState();
            }
        } else {
            return (BlockState) IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        }
    }

    public static BlockState transformBlockIce(BlockState in) {
        if (in.m_60734_() instanceof SpreadingSnowyDirtBlock) {
            return (BlockState) IafBlockRegistry.FROZEN_GRASS.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        } else if ((!in.m_204336_(BlockTags.DIRT) || in.m_60734_() != Blocks.DIRT) && !in.m_204336_(BlockTags.SNOW)) {
            if (in.m_204336_(BlockTags.SAND) && in.m_60734_() == Blocks.GRAVEL) {
                return (BlockState) IafBlockRegistry.FROZEN_GRAVEL.get().defaultBlockState().m_61124_(BlockFallingReturningState.REVERTS, true);
            } else if (in.m_204336_(BlockTags.SAND) && in.m_60734_() != Blocks.GRAVEL) {
                return in;
            } else if (!in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) || in.m_60734_() != Blocks.COBBLESTONE && !in.m_60734_().getDescriptionId().contains("cobblestone")) {
                if (in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) && in.m_60734_() != IafBlockRegistry.FROZEN_COBBLESTONE.get()) {
                    return (BlockState) IafBlockRegistry.FROZEN_STONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
                } else if (in.m_60734_() == Blocks.DIRT_PATH) {
                    return (BlockState) IafBlockRegistry.FROZEN_DIRT_PATH.get().defaultBlockState().m_61124_(BlockCharedPath.REVERTS, true);
                } else if (in.m_204336_(BlockTags.LOGS) || in.m_204336_(BlockTags.PLANKS)) {
                    return IafBlockRegistry.FROZEN_SPLINTERS.get().defaultBlockState();
                } else if (in.m_60713_(Blocks.WATER)) {
                    return Blocks.ICE.defaultBlockState();
                } else {
                    return !in.m_204336_(BlockTags.LEAVES) && !in.m_204336_(BlockTags.FLOWERS) && !in.m_204336_(BlockTags.CROPS) && in.m_60734_() != Blocks.SNOW ? in : Blocks.AIR.defaultBlockState();
                }
            } else {
                return (BlockState) IafBlockRegistry.FROZEN_COBBLESTONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
            }
        } else {
            return (BlockState) IafBlockRegistry.FROZEN_DIRT.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        }
    }

    public static BlockState transformBlockLightning(BlockState in) {
        if (in.m_60734_() instanceof SpreadingSnowyDirtBlock) {
            return (BlockState) IafBlockRegistry.CRACKLED_GRASS.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        } else if (in.m_204336_(BlockTags.DIRT) && in.m_60734_() == Blocks.DIRT) {
            return (BlockState) IafBlockRegistry.CRACKLED_DIRT.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        } else if (in.m_204336_(BlockTags.SAND) && in.m_60734_() == Blocks.GRAVEL) {
            return (BlockState) IafBlockRegistry.CRACKLED_GRAVEL.get().defaultBlockState().m_61124_(BlockFallingReturningState.REVERTS, true);
        } else if (!in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) || in.m_60734_() != Blocks.COBBLESTONE && !in.m_60734_().getDescriptionId().contains("cobblestone")) {
            if (in.m_204336_(BlockTags.BASE_STONE_OVERWORLD) && in.m_60734_() != IafBlockRegistry.CRACKLED_COBBLESTONE.get()) {
                return (BlockState) IafBlockRegistry.CRACKLED_STONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
            } else if (in.m_60734_() == Blocks.DIRT_PATH) {
                return (BlockState) IafBlockRegistry.CRACKLED_DIRT_PATH.get().defaultBlockState().m_61124_(BlockCharedPath.REVERTS, true);
            } else if (in.m_204336_(BlockTags.LOGS) || in.m_204336_(BlockTags.PLANKS)) {
                return IafBlockRegistry.ASH.get().defaultBlockState();
            } else {
                return !in.m_204336_(BlockTags.LEAVES) && !in.m_204336_(BlockTags.FLOWERS) && !in.m_204336_(BlockTags.CROPS) && in.m_60734_() != Blocks.SNOW ? in : Blocks.AIR.defaultBlockState();
            }
        } else {
            return (BlockState) IafBlockRegistry.CRACKLED_COBBLESTONE.get().defaultBlockState().m_61124_(BlockReturningState.REVERTS, true);
        }
    }
}