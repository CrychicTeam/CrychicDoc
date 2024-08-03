package org.violetmoon.quark.content.world.block.be;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.world.module.MonsterBoxModule;
import org.violetmoon.zeta.block.be.ZetaBlockEntity;

public class MonsterBoxBlockEntity extends ZetaBlockEntity {

    private int breakProgress;

    public MonsterBoxBlockEntity(BlockPos pos, BlockState state) {
        super(MonsterBoxModule.blockEntityType, pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, MonsterBoxBlockEntity be) {
        if (level.m_46791_() != Difficulty.PEACEFUL) {
            int x = pos.m_123341_();
            int y = pos.m_123342_();
            int z = pos.m_123343_();
            if (level.isClientSide) {
                level.addParticle(be.breakProgress == 0 ? ParticleTypes.FLAME : ParticleTypes.LARGE_SMOKE, (double) x + Math.random(), (double) y + Math.random(), (double) z + Math.random(), 0.0, 0.0, 0.0);
            }
            boolean doBreak = be.breakProgress > 0;
            if (!doBreak) {
                for (Player p : level.m_6907_()) {
                    if (p.m_20275_((double) x + 0.5, (double) y + 0.5, (double) z + 0.5) < MonsterBoxModule.activationRange * MonsterBoxModule.activationRange && !p.isSpectator()) {
                        doBreak = true;
                        break;
                    }
                }
            }
            if (doBreak) {
                if (be.breakProgress == 0) {
                    level.playSound(null, pos, QuarkSounds.BLOCK_MONSTER_BOX_GROWL, SoundSource.BLOCKS, 0.5F, 1.0F);
                }
                be.breakProgress++;
                if (be.breakProgress > 40) {
                    be.spawnMobs();
                    level.m_46796_(2001, pos, Block.getId(level.getBlockState(pos)));
                    level.removeBlock(pos, false);
                }
            }
        }
    }

    private void spawnMobs() {
        if (this.f_58857_ instanceof ServerLevel serverLevel) {
            BlockPos pos = this.m_58899_();
            LootTable loot = serverLevel.getServer().getLootData().m_278676_(MonsterBoxModule.MONSTER_BOX_SPAWNS_LOOT_TABLE);
            LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos)).withParameter(LootContextParams.BLOCK_STATE, this.m_58900_()).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withParameter(LootContextParams.BLOCK_ENTITY, this);
            LootParams ctx = builder.create(LootContextParamSets.BLOCK);
            int mobCount = MonsterBoxModule.minMobCount + this.f_58857_.random.nextInt(Math.max(MonsterBoxModule.maxMobCount - MonsterBoxModule.minMobCount + 1, 1));
            for (int i = 0; i < mobCount; i++) {
                loot.getRandomItemsRaw(ctx, stack -> {
                    Entity e = null;
                    if (stack.getItem() instanceof SpawnEggItem egg) {
                        EntityType<?> entitytype = egg.getType(stack.getTag());
                        e = entitytype.spawn(serverLevel, stack, null, pos, MobSpawnType.SPAWNER, true, true);
                        if (e != null) {
                            double motionMultiplier = 0.4;
                            e.setPos((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
                            double mx = ((double) this.f_58857_.random.nextFloat() - 0.5) * motionMultiplier;
                            double my = ((double) this.f_58857_.random.nextFloat() - 0.5) * motionMultiplier;
                            double mz = ((double) this.f_58857_.random.nextFloat() - 0.5) * motionMultiplier;
                            e.setDeltaMovement(mx, my, mz);
                            e.getPersistentData().putBoolean("quark:monster_box_spawned", true);
                        }
                    }
                });
            }
            serverLevel.getLevel().m_142346_(null, GameEvent.ENTITY_PLACE, pos);
        }
    }
}