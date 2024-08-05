package net.mehvahdjukaar.supplementaries.common.items;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.ICatchableMob;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.BucketHelper;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.CapturedMobHandler;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ReputationEventHandler;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMobContainerItem extends BlockItem {

    private final float mobContainerHeight;

    private final float mobContainerWidth;

    private final boolean isAquarium;

    protected AbstractMobContainerItem(Block block, Item.Properties properties, float width, float height, boolean aquarium) {
        super(block, properties);
        this.mobContainerWidth = width;
        this.mobContainerHeight = height;
        this.isAquarium = aquarium;
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    public boolean isAquarium() {
        return this.isAquarium;
    }

    public float getMobContainerHeight() {
        return this.mobContainerHeight;
    }

    public float getMobContainerWidth() {
        return this.mobContainerWidth;
    }

    protected boolean canFitEntity(Entity e) {
        float margin = 0.125F;
        float h = e.getBbHeight() - margin;
        float w = e.getBbWidth() - margin;
        return w < this.mobContainerWidth && h < this.mobContainerHeight;
    }

    public void playCatchSound(Player player) {
    }

    public void playFailSound(Player player) {
    }

    public void playReleaseSound(Level world, Vec3 v) {
    }

    public int getMaxStackSize(ItemStack stack) {
        return this.isFull(stack) ? 1 : 64;
    }

    public boolean isFull(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains("BlockEntityTag");
    }

    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (this.isFull(stack)) {
            return false;
        } else {
            InteractionHand hand = player.m_7655_();
            return hand == InteractionHand.OFF_HAND ? false : this.doInteract(stack, player, entity, player.m_7655_()).consumesAction();
        }
    }

    private <T extends Entity> boolean canCatch(T entity, Player player) {
        if (entity.isAlive() && !(entity instanceof Player)) {
            if (entity instanceof LivingEntity living) {
                if (living.isDeadOrDying()) {
                    return false;
                }
                String name = Utils.getID(entity.getType()).toString();
                if (entity.getType().is(ModTags.CAPTURE_BLACKLIST)) {
                    return false;
                }
                if ((Boolean) CommonConfigs.Functional.CAGE_ALL_MOBS.get() || CapturedMobHandler.isCommandMob(name)) {
                    return true;
                }
                if (entity instanceof TamableAnimal pet && (!pet.isTame() || !pet.isOwnedBy(player))) {
                    return false;
                }
                int p = (Integer) CommonConfigs.Functional.CAGE_HEALTH_THRESHOLD.get();
                if (p != 100 && living.getHealth() > living.getMaxHealth() * ((float) p / 100.0F)) {
                    return false;
                }
            }
            if (ForgeHelper.isMultipartEntity(entity)) {
                return false;
            } else {
                ICatchableMob cap = CapturedMobHandler.getCatchableMobCapOrDefault(entity);
                return cap.canBeCaughtWithItem(entity, this, player);
            }
        } else {
            return false;
        }
    }

    public abstract boolean canItemCatch(Entity var1);

    public ItemStack saveEntityInItem(Entity entity, ItemStack currentStack, ItemStack bucketStack) {
        ItemStack returnStack = new ItemStack(this);
        if (currentStack.hasCustomHoverName()) {
            returnStack.setHoverName(currentStack.getHoverName());
        }
        CompoundTag cmp = MobContainer.createMobHolderItemTag(entity, this.getMobContainerWidth(), this.getMobContainerHeight(), bucketStack, this.isAquarium);
        if (cmp != null) {
            returnStack.addTagElement("BlockEntityTag", cmp);
        }
        return returnStack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        CompoundTag com = stack.getTagElement("BlockEntityTag");
        Player player = context.getPlayer();
        if (!context.getPlayer().m_6144_() && com != null) {
            boolean success = false;
            Level world = context.getLevel();
            Vec3 v = context.getClickLocation();
            if (com.contains("BucketHolder")) {
                ItemStack bucketStack = ItemStack.of(com.getCompound("BucketHolder").getCompound("Bucket"));
                if (bucketStack.getItem() instanceof BucketItem bi) {
                    bi.checkExtraContent(player, world, bucketStack, context.getClickedPos());
                    success = true;
                }
            } else if (com.contains("MobHolder")) {
                CompoundTag nbt = com.getCompound("MobHolder");
                Entity entity = EntityType.loadEntityRecursive(nbt.getCompound("EntityData"), world, o -> o);
                if (entity != null) {
                    success = true;
                    if (!world.isClientSide) {
                        if (!player.isCreative() && entity instanceof NeutralMob ang && !entity.getType().is(ModTags.NON_ANGERABLE)) {
                            ang.forgetCurrentTargetAndRefreshUniversalAnger();
                            ang.setPersistentAngerTarget(player.m_20148_());
                            ang.setLastHurtByMob(player);
                        }
                        entity.absMoveTo(v.x(), v.y(), v.z(), context.getRotation(), 0.0F);
                        if ((Boolean) CommonConfigs.Functional.CAGE_PERSISTENT_MOBS.get() && entity instanceof Mob mob) {
                            mob.setPersistenceRequired();
                        }
                        UUID temp = entity.getUUID();
                        if (nbt.contains("UUID")) {
                            UUID id = nbt.getUUID("UUID");
                            entity.setUUID(id);
                        }
                        if (!world.m_7967_(entity)) {
                            entity.setUUID(temp);
                            success = world.m_7967_(entity);
                            if (!success) {
                                Supplementaries.LOGGER.warn("Failed to release caged mob");
                            }
                        }
                    }
                    if (player.isCreative() && nbt.contains("UUID")) {
                        nbt.putUUID("UUID", Mth.createInsecureUUID(world.random));
                    }
                } else {
                    Supplementaries.LOGGER.error("Failed to load entity from itemstack");
                }
            }
            if (success) {
                if (!world.isClientSide) {
                    this.playReleaseSound(world, v);
                    if (!player.isCreative()) {
                        ItemStack returnItem = new ItemStack(this);
                        if (stack.hasCustomHoverName()) {
                            returnItem.setHoverName(stack.getHoverName());
                        }
                        Utils.swapItemNBT(player, context.getHand(), stack, returnItem);
                    }
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }
        return super.useOn(context);
    }

    public boolean blocksPlacement() {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if (tag != null) {
            CompoundTag com = tag.getCompound("MobHolder");
            if (com.isEmpty()) {
                com = tag.getCompound("BucketHolder");
            }
            if (com.contains("Name")) {
                tooltip.add(Component.translatable(com.getString("Name")).withStyle(ChatFormatting.GRAY));
            }
        }
        if (MiscUtils.showsHints(worldIn, flagIn)) {
            this.addPlacementTooltip(tooltip);
        }
    }

    public void addPlacementTooltip(List<Component> tooltip) {
        tooltip.add(Component.translatable("message.supplementaries.cage").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
    }

    private void angerNearbyEntities(Entity entity, Player player) {
        if (entity instanceof NeutralMob && entity instanceof Mob) {
            getEntitiesInRange((Mob) entity).stream().filter(mob -> mob != entity).map(NeutralMob.class::cast).forEach(mob -> {
                mob.forgetCurrentTargetAndRefreshUniversalAnger();
                mob.setPersistentAngerTarget(player.m_20148_());
                mob.setLastHurtByMob(player);
            });
        }
        Level level = entity.level();
        if (entity instanceof Piglin) {
            entity.hurt(level.damageSources().playerAttack(player), 0.0F);
        }
        if (entity instanceof Villager villager && level instanceof ServerLevel serverLevel) {
            Optional<NearestVisibleLivingEntities> optional = villager.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
            optional.ifPresent(entities -> entities.findAll(ReputationEventHandler.class::isInstance).forEach(e -> serverLevel.onReputationEvent(ReputationEventType.VILLAGER_HURT, player, (ReputationEventHandler) e)));
        }
    }

    private static List<?> getEntitiesInRange(Mob e) {
        double d0 = e.m_21133_(Attributes.FOLLOW_RANGE);
        AABB aabb = AABB.unitCubeFromLowerCorner(e.m_20182_()).inflate(d0, 10.0, d0);
        return e.m_9236_().m_6443_(e.getClass(), aabb, EntitySelector.NO_SPECTATORS);
    }

    public InteractionResult doInteract(ItemStack stack, Player player, Entity entity, InteractionHand hand) {
        if (hand == null) {
            return InteractionResult.PASS;
        } else if (this.canCatch(entity, player)) {
            ItemStack bucket = ItemStack.EMPTY;
            if (this.isAquarium) {
                bucket = BucketHelper.getBucketFromEntity(entity);
            }
            ForgeHelper.reviveEntity(entity);
            if (player.m_9236_().isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.playCatchSound(player);
                this.angerNearbyEntities(entity, player);
                if ((Boolean) CommonConfigs.Functional.CAGE_PERSISTENT_MOBS.get() && entity instanceof Mob mob) {
                    mob.setPersistenceRequired();
                }
                if (entity instanceof Mob mob) {
                    mob.dropLeash(true, !player.getAbilities().instabuild);
                }
                Utils.swapItemNBT(player, hand, stack, this.saveEntityInItem(entity, stack, bucket));
                entity.remove(Entity.RemovalReason.DISCARDED);
                return InteractionResult.CONSUME;
            }
        } else {
            if (player.m_9236_().isClientSide && entity instanceof LivingEntity) {
                player.displayClientMessage(Component.translatable("message.supplementaries.cage.fail"), true);
            }
            this.playFailSound(player);
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.m_43723_();
        return player != null && !player.m_6144_() && this.blocksPlacement() ? InteractionResult.PASS : super.place(context);
    }
}