package com.simibubi.create.content.kinetics.deployer;

import com.google.common.collect.Multimap;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.mounted.CartAssemblerBlockItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.trains.track.ITrackBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeBaseRailBlock;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import org.apache.commons.lang3.tuple.Pair;

public class DeployerHandler {

    static boolean shouldActivate(ItemStack held, Level world, BlockPos targetPos, @Nullable Direction facing) {
        if (held.getItem() instanceof BlockItem && world.getBlockState(targetPos).m_60734_() == ((BlockItem) held.getItem()).getBlock()) {
            return false;
        } else {
            if (held.getItem() instanceof BucketItem) {
                BucketItem bucketItem = (BucketItem) held.getItem();
                Fluid fluid = bucketItem.getFluid();
                if (fluid != Fluids.EMPTY && world.getFluidState(targetPos).getType() == fluid) {
                    return false;
                }
            }
            return held.isEmpty() || facing != Direction.DOWN || BlockEntityBehaviour.get(world, targetPos, TransportedItemStackHandlerBehaviour.TYPE) == null;
        }
    }

    static void activate(DeployerFakePlayer player, Vec3 vec, BlockPos clickedPos, Vec3 extensionVector, DeployerBlockEntity.Mode mode) {
        Multimap<Attribute, AttributeModifier> attributeModifiers = player.m_21205_().getAttributeModifiers(EquipmentSlot.MAINHAND);
        player.m_21204_().addTransientAttributeModifiers(attributeModifiers);
        activateInner(player, vec, clickedPos, extensionVector, mode);
        player.m_21204_().addTransientAttributeModifiers(attributeModifiers);
    }

    private static void activateInner(DeployerFakePlayer player, Vec3 vec, BlockPos clickedPos, Vec3 extensionVector, DeployerBlockEntity.Mode mode) {
        Vec3 rayOrigin = vec.add(extensionVector.scale(1.515625));
        Vec3 rayTarget = vec.add(extensionVector.scale(2.484375));
        player.m_6034_(rayOrigin.x, rayOrigin.y, rayOrigin.z);
        BlockPos pos = BlockPos.containing(vec);
        ItemStack stack = player.m_21205_();
        Item item = stack.getItem();
        Level world = player.m_9236_();
        List<Entity> entities = (List<Entity>) world.m_45976_(Entity.class, new AABB(clickedPos)).stream().filter(e -> !(e instanceof AbstractContraptionEntity)).collect(Collectors.toList());
        InteractionHand hand = InteractionHand.MAIN_HAND;
        if (!entities.isEmpty()) {
            Entity entity = (Entity) entities.get(world.random.nextInt(entities.size()));
            List<ItemEntity> capturedDrops = new ArrayList();
            boolean success = false;
            entity.captureDrops(capturedDrops);
            if (mode == DeployerBlockEntity.Mode.USE) {
                InteractionResult cancelResult = ForgeHooks.onInteractEntity(player, entity, hand);
                if (cancelResult == InteractionResult.FAIL) {
                    entity.captureDrops(null);
                    return;
                }
                if (cancelResult == null) {
                    if (entity.interact(player, hand).consumesAction()) {
                        if (entity instanceof AbstractVillager villager && villager.getTradingPlayer() instanceof DeployerFakePlayer) {
                            villager.setTradingPlayer(null);
                        }
                        success = true;
                    } else if (entity instanceof LivingEntity && stack.interactLivingEntity(player, (LivingEntity) entity, hand).consumesAction()) {
                        success = true;
                    }
                }
                if (!success && entity instanceof Player playerEntity) {
                    if (stack.isEdible()) {
                        FoodProperties foodProperties = item.getFoodProperties(stack, player);
                        if (playerEntity.canEat(foodProperties.canAlwaysEat())) {
                            playerEntity.eat(world, stack);
                            player.spawnedItemEffects = stack.copy();
                            success = true;
                        }
                    }
                    if (AllTags.AllItemTags.DEPLOYABLE_DRINK.matches(stack)) {
                        player.spawnedItemEffects = stack.copy();
                        player.m_21008_(hand, stack.finishUsingItem(world, playerEntity));
                        success = true;
                    }
                }
            }
            if (mode == DeployerBlockEntity.Mode.PUNCH) {
                player.m_36334_();
                player.m_5706_(entity);
                success = true;
            }
            entity.captureDrops(null);
            capturedDrops.forEach(e -> player.m_150109_().placeItemBackInInventory(e.getItem()));
            if (success) {
                return;
            }
        }
        ClipContext rayTraceContext = new ClipContext(rayOrigin, rayTarget, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player);
        BlockHitResult result = world.m_45547_(rayTraceContext);
        if (result.getBlockPos() != clickedPos) {
            result = new BlockHitResult(result.m_82450_(), result.getDirection(), clickedPos, result.isInside());
        }
        BlockState clickedState = world.getBlockState(clickedPos);
        Direction face = result.getDirection();
        if (face == null) {
            face = Direction.getNearest(extensionVector.x, extensionVector.y, extensionVector.z).getOpposite();
        }
        if (mode == DeployerBlockEntity.Mode.PUNCH) {
            if (world.mayInteract(player, clickedPos)) {
                if (clickedState.m_60808_(world, clickedPos).isEmpty()) {
                    player.blockBreakingProgress = null;
                } else {
                    PlayerInteractEvent.LeftClickBlock event = ForgeHooks.onLeftClickBlock(player, clickedPos, face);
                    if (!event.isCanceled()) {
                        if (!BlockHelper.extinguishFire(world, player, clickedPos, face)) {
                            if (event.getUseBlock() != Result.DENY) {
                                clickedState.m_60686_(world, clickedPos, player);
                            }
                            if (!stack.isEmpty()) {
                                float progress = clickedState.m_60625_(player, world, clickedPos) * 16.0F;
                                float before = 0.0F;
                                Pair<BlockPos, Float> blockBreakingProgress = player.blockBreakingProgress;
                                if (blockBreakingProgress != null) {
                                    before = (Float) blockBreakingProgress.getValue();
                                }
                                progress += before;
                                world.playSound(null, clickedPos, clickedState.m_60827_().getHitSound(), SoundSource.NEUTRAL, 0.25F, 1.0F);
                                if (progress >= 1.0F) {
                                    tryHarvestBlock(player, player.f_8941_, clickedPos);
                                    world.destroyBlockProgress(player.m_19879_(), clickedPos, -1);
                                    player.blockBreakingProgress = null;
                                } else if (progress <= 0.0F) {
                                    player.blockBreakingProgress = null;
                                } else {
                                    if ((int) (before * 10.0F) != (int) (progress * 10.0F)) {
                                        world.destroyBlockProgress(player.m_19879_(), clickedPos, (int) (progress * 10.0F));
                                    }
                                    player.blockBreakingProgress = Pair.of(clickedPos, progress);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            UseOnContext itemusecontext = new UseOnContext(player, hand, result);
            Result useBlock = Result.DEFAULT;
            Result useItem = Result.DEFAULT;
            if (!clickedState.m_60808_(world, clickedPos).isEmpty()) {
                PlayerInteractEvent.RightClickBlock event = ForgeHooks.onRightClickBlock(player, hand, clickedPos, result);
                useBlock = event.getUseBlock();
                useItem = event.getUseItem();
            }
            if (useItem != Result.DENY) {
                InteractionResult actionresult = stack.onItemUseFirst(itemusecontext);
                if (actionresult != InteractionResult.PASS) {
                    return;
                }
            }
            boolean holdingSomething = !player.m_21205_().isEmpty();
            boolean flag1 = !player.m_6144_() || !holdingSomething || stack.doesSneakBypassUse(world, clickedPos, player);
            if (useBlock == Result.DENY || !flag1 || !safeOnUse(clickedState, world, clickedPos, player, hand, result).consumesAction()) {
                if (!stack.isEmpty()) {
                    if (useItem != Result.DENY) {
                        if (item == Items.FLINT_AND_STEEL) {
                            Direction newFace = result.getDirection();
                            BlockPos newPos = result.getBlockPos();
                            if (!BaseFireBlock.canBePlacedAt(world, clickedPos, newFace)) {
                                newFace = Direction.UP;
                            }
                            if (clickedState.m_60795_()) {
                                newPos = newPos.relative(face.getOpposite());
                            }
                            result = new BlockHitResult(result.m_82450_(), newFace, newPos, result.isInside());
                            itemusecontext = new UseOnContext(player, hand, result);
                        }
                        InteractionResult onItemUse = stack.useOn(itemusecontext);
                        if (onItemUse.consumesAction()) {
                            if (stack.getItem() instanceof BlockItem bi && (bi.getBlock() instanceof IForgeBaseRailBlock || bi.getBlock() instanceof ITrackBlock)) {
                                player.placedTracks = true;
                            }
                        } else if (!(item instanceof BlockItem) || item instanceof CartAssemblerBlockItem || clickedState.m_60629_(new BlockPlaceContext(itemusecontext))) {
                            if (item != Items.ENDER_PEARL) {
                                if (!AllTags.AllItemTags.DEPLOYABLE_DRINK.matches(item)) {
                                    Level itemUseWorld = world;
                                    if (item instanceof BucketItem || item instanceof SandPaperItem) {
                                        itemUseWorld = new DeployerHandler.ItemUseWorld(world, face, pos);
                                    }
                                    InteractionResultHolder<ItemStack> onItemRightClick = item.use(itemUseWorld, player, hand);
                                    ItemStack resultStack = onItemRightClick.getObject();
                                    if (resultStack != stack || resultStack.getCount() != stack.getCount() || resultStack.getUseDuration() > 0 || resultStack.getDamageValue() != stack.getDamageValue()) {
                                        player.m_21008_(hand, onItemRightClick.getObject());
                                    }
                                    CompoundTag tag = stack.getTag();
                                    if (tag != null && stack.getItem() instanceof SandPaperItem && tag.contains("Polishing")) {
                                        player.spawnedItemEffects = ItemStack.of(tag.getCompound("Polishing"));
                                        AllSoundEvents.SANDING_SHORT.playOnServer(world, pos, 0.25F, 1.0F);
                                    }
                                    if (!player.m_21211_().isEmpty()) {
                                        player.m_21008_(hand, stack.finishUsingItem(world, player));
                                    }
                                    player.m_5810_();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean tryHarvestBlock(ServerPlayer player, ServerPlayerGameMode interactionManager, BlockPos pos) {
        ServerLevel world = player.serverLevel();
        BlockState blockstate = world.m_8055_(pos);
        GameType gameType = interactionManager.getGameModeForPlayer();
        if (ForgeHooks.onBlockBreakEvent(world, gameType, player, pos) == -1) {
            return false;
        } else {
            BlockEntity blockEntity = world.m_7702_(pos);
            if (player.m_21205_().onBlockStartBreak(pos, player)) {
                return false;
            } else if (player.m_36187_(world, pos, gameType)) {
                return false;
            } else {
                ItemStack prevHeldItem = player.m_21205_();
                ItemStack heldItem = prevHeldItem.copy();
                boolean canHarvest = blockstate.canHarvestBlock(world, pos, player);
                prevHeldItem.mineBlock(world, blockstate, pos, player);
                if (prevHeldItem.isEmpty() && !heldItem.isEmpty()) {
                    ForgeEventFactory.onPlayerDestroyItem(player, heldItem, InteractionHand.MAIN_HAND);
                }
                BlockPos posUp = pos.above();
                BlockState stateUp = world.m_8055_(posUp);
                if (blockstate.m_60734_() instanceof DoublePlantBlock && blockstate.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER && stateUp.m_60734_() == blockstate.m_60734_() && stateUp.m_61143_(DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER) {
                    world.m_7731_(pos, Blocks.AIR.defaultBlockState(), 35);
                    world.m_7731_(posUp, Blocks.AIR.defaultBlockState(), 35);
                } else if (!blockstate.onDestroyedByPlayer(world, pos, player, canHarvest, world.m_6425_(pos))) {
                    return true;
                }
                blockstate.m_60734_().destroy(world, pos, blockstate);
                if (!canHarvest) {
                    return true;
                } else {
                    Block.getDrops(blockstate, world, pos, blockEntity, player, prevHeldItem).forEach(item -> player.m_150109_().placeItemBackInInventory(item));
                    blockstate.m_222967_(world, pos, prevHeldItem, true);
                    return true;
                }
            }
        }
    }

    public static InteractionResult safeOnUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return state.m_60734_() instanceof BeehiveBlock ? safeOnBeehiveUse(state, world, pos, player, hand) : state.m_60664_(world, player, hand, ray);
    }

    protected static InteractionResult safeOnBeehiveUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand) {
        BeehiveBlock block = (BeehiveBlock) state.m_60734_();
        ItemStack prevHeldItem = player.m_21120_(hand);
        int honeyLevel = (Integer) state.m_61143_(BeehiveBlock.HONEY_LEVEL);
        boolean success = false;
        if (honeyLevel < 5) {
            return InteractionResult.PASS;
        } else {
            if (prevHeldItem.getItem() == Items.SHEARS) {
                world.playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.BEEHIVE_SHEAR, SoundSource.NEUTRAL, 1.0F, 1.0F);
                player.getInventory().placeItemBackInInventory(new ItemStack(Items.HONEYCOMB, 3));
                prevHeldItem.hurtAndBreak(1, player, s -> s.m_21190_(hand));
                success = true;
            }
            if (prevHeldItem.getItem() == Items.GLASS_BOTTLE) {
                prevHeldItem.shrink(1);
                world.playSound(player, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
                if (prevHeldItem.isEmpty()) {
                    player.m_21008_(hand, honeyBottle);
                } else {
                    player.getInventory().placeItemBackInInventory(honeyBottle);
                }
                success = true;
            }
            if (!success) {
                return InteractionResult.PASS;
            } else {
                block.resetHoneyLevel(world, state, pos);
                return InteractionResult.SUCCESS;
            }
        }
    }

    private static final class ItemUseWorld extends WrappedWorld {

        private final Direction face;

        private final BlockPos pos;

        boolean rayMode = false;

        private ItemUseWorld(Level world, Direction face, BlockPos pos) {
            super(world);
            this.face = face;
            this.pos = pos;
        }

        @Override
        public BlockHitResult clip(ClipContext context) {
            this.rayMode = true;
            BlockHitResult rayTraceBlocks = super.m_45547_(context);
            this.rayMode = false;
            return rayTraceBlocks;
        }

        @Override
        public BlockState getBlockState(BlockPos position) {
            return !this.rayMode || !this.pos.relative(this.face.getOpposite(), 3).equals(position) && !this.pos.relative(this.face.getOpposite(), 1).equals(position) ? this.world.getBlockState(position) : Blocks.BEDROCK.defaultBlockState();
        }
    }
}