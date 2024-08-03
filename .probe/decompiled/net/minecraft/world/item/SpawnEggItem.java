package net.minecraft.world.item;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpawnEggItem extends Item {

    private static final Map<EntityType<? extends Mob>, SpawnEggItem> BY_ID = Maps.newIdentityHashMap();

    private final int backgroundColor;

    private final int highlightColor;

    private final EntityType<?> defaultType;

    public SpawnEggItem(EntityType<? extends Mob> entityTypeExtendsMob0, int int1, int int2, Item.Properties itemProperties3) {
        super(itemProperties3);
        this.defaultType = entityTypeExtendsMob0;
        this.backgroundColor = int1;
        this.highlightColor = int2;
        BY_ID.put(entityTypeExtendsMob0, this);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        if (!($$1 instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack $$2 = useOnContext0.getItemInHand();
            BlockPos $$3 = useOnContext0.getClickedPos();
            Direction $$4 = useOnContext0.getClickedFace();
            BlockState $$5 = $$1.getBlockState($$3);
            if ($$5.m_60713_(Blocks.SPAWNER)) {
                BlockEntity $$6 = $$1.getBlockEntity($$3);
                if ($$6 instanceof SpawnerBlockEntity $$7) {
                    EntityType<?> $$8 = this.getType($$2.getTag());
                    $$7.setEntityId($$8, $$1.getRandom());
                    $$6.setChanged();
                    $$1.sendBlockUpdated($$3, $$5, $$5, 3);
                    $$1.m_142346_(useOnContext0.getPlayer(), GameEvent.BLOCK_CHANGE, $$3);
                    $$2.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
            BlockPos $$9;
            if ($$5.m_60812_($$1, $$3).isEmpty()) {
                $$9 = $$3;
            } else {
                $$9 = $$3.relative($$4);
            }
            EntityType<?> $$11 = this.getType($$2.getTag());
            if ($$11.spawn((ServerLevel) $$1, $$2, useOnContext0.getPlayer(), $$9, MobSpawnType.SPAWN_EGG, true, !Objects.equals($$3, $$9) && $$4 == Direction.UP) != null) {
                $$2.shrink(1);
                $$1.m_142346_(useOnContext0.getPlayer(), GameEvent.ENTITY_PLACE, $$3);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        ItemStack $$3 = player1.m_21120_(interactionHand2);
        BlockHitResult $$4 = m_41435_(level0, player1, ClipContext.Fluid.SOURCE_ONLY);
        if ($$4.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass($$3);
        } else if (!(level0 instanceof ServerLevel)) {
            return InteractionResultHolder.success($$3);
        } else {
            BlockPos $$6 = $$4.getBlockPos();
            if (!(level0.getBlockState($$6).m_60734_() instanceof LiquidBlock)) {
                return InteractionResultHolder.pass($$3);
            } else if (level0.mayInteract(player1, $$6) && player1.mayUseItemAt($$6, $$4.getDirection(), $$3)) {
                EntityType<?> $$7 = this.getType($$3.getTag());
                Entity $$8 = $$7.spawn((ServerLevel) level0, $$3, player1, $$6, MobSpawnType.SPAWN_EGG, false, false);
                if ($$8 == null) {
                    return InteractionResultHolder.pass($$3);
                } else {
                    if (!player1.getAbilities().instabuild) {
                        $$3.shrink(1);
                    }
                    player1.awardStat(Stats.ITEM_USED.get(this));
                    level0.m_220400_(player1, GameEvent.ENTITY_PLACE, $$8.position());
                    return InteractionResultHolder.consume($$3);
                }
            } else {
                return InteractionResultHolder.fail($$3);
            }
        }
    }

    public boolean spawnsEntity(@Nullable CompoundTag compoundTag0, EntityType<?> entityType1) {
        return Objects.equals(this.getType(compoundTag0), entityType1);
    }

    public int getColor(int int0) {
        return int0 == 0 ? this.backgroundColor : this.highlightColor;
    }

    @Nullable
    public static SpawnEggItem byId(@Nullable EntityType<?> entityType0) {
        return (SpawnEggItem) BY_ID.get(entityType0);
    }

    public static Iterable<SpawnEggItem> eggs() {
        return Iterables.unmodifiableIterable(BY_ID.values());
    }

    public EntityType<?> getType(@Nullable CompoundTag compoundTag0) {
        if (compoundTag0 != null && compoundTag0.contains("EntityTag", 10)) {
            CompoundTag $$1 = compoundTag0.getCompound("EntityTag");
            if ($$1.contains("id", 8)) {
                return (EntityType<?>) EntityType.byString($$1.getString("id")).orElse(this.defaultType);
            }
        }
        return this.defaultType;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.defaultType.requiredFeatures();
    }

    public Optional<Mob> spawnOffspringFromSpawnEgg(Player player0, Mob mob1, EntityType<? extends Mob> entityTypeExtendsMob2, ServerLevel serverLevel3, Vec3 vec4, ItemStack itemStack5) {
        if (!this.spawnsEntity(itemStack5.getTag(), entityTypeExtendsMob2)) {
            return Optional.empty();
        } else {
            Mob $$6;
            if (mob1 instanceof AgeableMob) {
                $$6 = ((AgeableMob) mob1).getBreedOffspring(serverLevel3, (AgeableMob) mob1);
            } else {
                $$6 = entityTypeExtendsMob2.create(serverLevel3);
            }
            if ($$6 == null) {
                return Optional.empty();
            } else {
                $$6.setBaby(true);
                if (!$$6.m_6162_()) {
                    return Optional.empty();
                } else {
                    $$6.m_7678_(vec4.x(), vec4.y(), vec4.z(), 0.0F, 0.0F);
                    serverLevel3.m_47205_($$6);
                    if (itemStack5.hasCustomHoverName()) {
                        $$6.m_6593_(itemStack5.getHoverName());
                    }
                    if (!player0.getAbilities().instabuild) {
                        itemStack5.shrink(1);
                    }
                    return Optional.of($$6);
                }
            }
        }
    }
}