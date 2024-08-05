package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.phys.Vec3;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlazeBurnerBlockItem extends BlockItem {

    private final boolean capturedBlaze;

    public static BlazeBurnerBlockItem empty(Item.Properties properties) {
        return new BlazeBurnerBlockItem((Block) AllBlocks.BLAZE_BURNER.get(), properties, false);
    }

    public static BlazeBurnerBlockItem withBlaze(Block block, Item.Properties properties) {
        return new BlazeBurnerBlockItem(block, properties, true);
    }

    @Override
    public void registerBlocks(Map<Block, Item> p_195946_1_, Item p_195946_2_) {
        if (this.hasCapturedBlaze()) {
            super.registerBlocks(p_195946_1_, p_195946_2_);
        }
    }

    private BlazeBurnerBlockItem(Block block, Item.Properties properties, boolean capturedBlaze) {
        super(block, properties);
        this.capturedBlaze = capturedBlaze;
    }

    @Override
    public String getDescriptionId() {
        return this.hasCapturedBlaze() ? super.getDescriptionId() : "item.create." + RegisteredObjects.getKeyOrThrow(this).getPath();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (this.hasCapturedBlaze()) {
            return super.useOn(context);
        } else {
            Level world = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockEntity be = world.getBlockEntity(pos);
            Player player = context.getPlayer();
            if (!(be instanceof SpawnerBlockEntity)) {
                return super.useOn(context);
            } else {
                BaseSpawner spawner = ((SpawnerBlockEntity) be).getSpawner();
                List<SpawnData> possibleSpawns = spawner.spawnPotentials.m_146338_().stream().map(WeightedEntry.Wrapper::m_146310_).toList();
                if (possibleSpawns.isEmpty()) {
                    possibleSpawns = new ArrayList();
                    possibleSpawns.add(spawner.nextSpawnData);
                }
                for (SpawnData e : possibleSpawns) {
                    Optional<EntityType<?>> optionalEntity = EntityType.by(e.entityToSpawn());
                    if (!optionalEntity.isEmpty() && AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches((EntityType<?>) optionalEntity.get())) {
                        this.spawnCaptureEffects(world, VecHelper.getCenterOf(pos));
                        if (!world.isClientSide && player != null) {
                            this.giveBurnerItemTo(player, context.getItemInHand(), context.getHand());
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
                return super.useOn(context);
            }
        }
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack heldItem, Player player, LivingEntity entity, InteractionHand hand) {
        if (this.hasCapturedBlaze()) {
            return InteractionResult.PASS;
        } else if (!AllTags.AllEntityTags.BLAZE_BURNER_CAPTURABLE.matches(entity)) {
            return InteractionResult.PASS;
        } else {
            Level world = player.m_9236_();
            this.spawnCaptureEffects(world, entity.m_20182_());
            if (world.isClientSide) {
                return InteractionResult.FAIL;
            } else {
                this.giveBurnerItemTo(player, heldItem, hand);
                entity.m_146870_();
                return InteractionResult.FAIL;
            }
        }
    }

    protected void giveBurnerItemTo(Player player, ItemStack heldItem, InteractionHand hand) {
        ItemStack filled = AllBlocks.BLAZE_BURNER.asStack();
        if (!player.isCreative()) {
            heldItem.shrink(1);
        }
        if (heldItem.isEmpty()) {
            player.m_21008_(hand, filled);
        } else {
            player.getInventory().placeItemBackInInventory(filled);
        }
    }

    private void spawnCaptureEffects(Level world, Vec3 vec) {
        if (!world.isClientSide) {
            BlockPos soundPos = BlockPos.containing(vec);
            world.playSound(null, soundPos, SoundEvents.BLAZE_HURT, SoundSource.HOSTILE, 0.25F, 0.75F);
            world.playSound(null, soundPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.HOSTILE, 0.5F, 0.75F);
        } else {
            for (int i = 0; i < 40; i++) {
                Vec3 motion = VecHelper.offsetRandomly(Vec3.ZERO, world.random, 0.125F);
                world.addParticle(ParticleTypes.FLAME, vec.x, vec.y, vec.z, motion.x, motion.y, motion.z);
                Vec3 circle = motion.multiply(1.0, 0.0, 1.0).normalize().scale(0.5);
                world.addParticle(ParticleTypes.SMOKE, circle.x, vec.y, circle.z, 0.0, -0.125, 0.0);
            }
        }
    }

    public boolean hasCapturedBlaze() {
        return this.capturedBlaze;
    }
}