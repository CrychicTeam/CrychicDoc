package com.simibubi.create.content.contraptions.mounted;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllMovementBehaviours;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.ContraptionData;
import com.simibubi.create.content.contraptions.ContraptionMovementSetting;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.contraptions.actors.psi.PortableStorageInterfaceMovement;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.commons.lang3.tuple.MutablePair;

@EventBusSubscriber
public class MinecartContraptionItem extends Item {

    private final AbstractMinecart.Type minecartType;

    private static final DispenseItemBehavior DISPENSER_BEHAVIOR = new DefaultDispenseItemBehavior() {

        private final DefaultDispenseItemBehavior behaviourDefaultDispenseItem = new DefaultDispenseItemBehavior();

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
            Level world = source.getLevel();
            double d0 = source.x() + (double) direction.getStepX() * 1.125;
            double d1 = Math.floor(source.y()) + (double) direction.getStepY();
            double d2 = source.z() + (double) direction.getStepZ() * 1.125;
            BlockPos blockpos = source.getPos().relative(direction);
            BlockState blockstate = world.getBlockState(blockpos);
            RailShape railshape = blockstate.m_60734_() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate.m_60734_()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
            double d3;
            if (blockstate.m_204336_(BlockTags.RAILS)) {
                if (railshape.isAscending()) {
                    d3 = 0.6;
                } else {
                    d3 = 0.1;
                }
            } else {
                if (!blockstate.m_60795_() || !world.getBlockState(blockpos.below()).m_204336_(BlockTags.RAILS)) {
                    return this.behaviourDefaultDispenseItem.dispense(source, stack);
                }
                BlockState blockstate1 = world.getBlockState(blockpos.below());
                RailShape railshape1 = blockstate1.m_60734_() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate1.m_60734_()).getRailDirection(blockstate1, world, blockpos.below(), null) : RailShape.NORTH_SOUTH;
                if (direction != Direction.DOWN && railshape1.isAscending()) {
                    d3 = -0.4;
                } else {
                    d3 = -0.9;
                }
            }
            AbstractMinecart abstractminecartentity = AbstractMinecart.createMinecart(world, d0, d1 + d3, d2, ((MinecartContraptionItem) stack.getItem()).minecartType);
            if (stack.hasCustomHoverName()) {
                abstractminecartentity.m_6593_(stack.getHoverName());
            }
            world.m_7967_(abstractminecartentity);
            MinecartContraptionItem.addContraptionToMinecart(world, stack, abstractminecartentity, direction);
            stack.shrink(1);
            return stack;
        }

        @Override
        protected void playSound(BlockSource source) {
            source.getLevel().m_46796_(1000, source.getPos(), 0);
        }
    };

    public static MinecartContraptionItem rideable(Item.Properties builder) {
        return new MinecartContraptionItem(AbstractMinecart.Type.RIDEABLE, builder);
    }

    public static MinecartContraptionItem furnace(Item.Properties builder) {
        return new MinecartContraptionItem(AbstractMinecart.Type.FURNACE, builder);
    }

    public static MinecartContraptionItem chest(Item.Properties builder) {
        return new MinecartContraptionItem(AbstractMinecart.Type.CHEST, builder);
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return AllConfigs.server().kinetics.minecartContraptionInContainers.get();
    }

    private MinecartContraptionItem(AbstractMinecart.Type minecartTypeIn, Item.Properties builder) {
        super(builder);
        this.minecartType = minecartTypeIn;
        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = world.getBlockState(blockpos);
        if (!blockstate.m_204336_(BlockTags.RAILS)) {
            return InteractionResult.FAIL;
        } else {
            ItemStack itemstack = context.getItemInHand();
            if (!world.isClientSide) {
                RailShape railshape = blockstate.m_60734_() instanceof BaseRailBlock ? ((BaseRailBlock) blockstate.m_60734_()).getRailDirection(blockstate, world, blockpos, null) : RailShape.NORTH_SOUTH;
                double d0 = 0.0;
                if (railshape.isAscending()) {
                    d0 = 0.5;
                }
                AbstractMinecart abstractminecartentity = AbstractMinecart.createMinecart(world, (double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.0625 + d0, (double) blockpos.m_123343_() + 0.5, this.minecartType);
                if (itemstack.hasCustomHoverName()) {
                    abstractminecartentity.m_6593_(itemstack.getHoverName());
                }
                Player player = context.getPlayer();
                world.m_7967_(abstractminecartentity);
                addContraptionToMinecart(world, itemstack, abstractminecartentity, player == null ? null : player.m_6350_());
            }
            itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        }
    }

    public static void addContraptionToMinecart(Level world, ItemStack itemstack, AbstractMinecart cart, @Nullable Direction newFacing) {
        CompoundTag tag = itemstack.getOrCreateTag();
        if (tag.contains("Contraption")) {
            CompoundTag contraptionTag = tag.getCompound("Contraption");
            Direction intialOrientation = NBTHelper.readEnum(contraptionTag, "InitialOrientation", Direction.class);
            Contraption mountedContraption = Contraption.fromNBT(world, contraptionTag, false);
            OrientedContraptionEntity contraptionEntity = newFacing == null ? OrientedContraptionEntity.create(world, mountedContraption, intialOrientation) : OrientedContraptionEntity.createAtYaw(world, mountedContraption, intialOrientation, newFacing.toYRot());
            contraptionEntity.m_20329_(cart);
            contraptionEntity.m_6034_(cart.m_20185_(), cart.m_20186_(), cart.m_20189_());
            world.m_7967_(contraptionEntity);
        }
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.create.minecart_contraption";
    }

    @SubscribeEvent
    public static void wrenchCanBeUsedToPickUpMinecartContraptions(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        if (player != null && entity != null) {
            if (AllConfigs.server().kinetics.survivalContraptionPickup.get() || player.isCreative()) {
                ItemStack wrench = player.m_21120_(event.getHand());
                if (AllItems.WRENCH.isIn(wrench)) {
                    if (entity instanceof AbstractContraptionEntity) {
                        entity = entity.getVehicle();
                    }
                    if (entity instanceof AbstractMinecart) {
                        if (entity.isAlive()) {
                            if (player instanceof DeployerFakePlayer dfp && dfp.onMinecartContraption) {
                                return;
                            }
                            AbstractMinecart cart = (AbstractMinecart) entity;
                            AbstractMinecart.Type type = cart.getMinecartType();
                            if (type == AbstractMinecart.Type.RIDEABLE || type == AbstractMinecart.Type.FURNACE || type == AbstractMinecart.Type.CHEST) {
                                List<Entity> passengers = cart.m_20197_();
                                if (!passengers.isEmpty() && passengers.get(0) instanceof OrientedContraptionEntity) {
                                    OrientedContraptionEntity oce = (OrientedContraptionEntity) passengers.get(0);
                                    Contraption contraption = oce.getContraption();
                                    if (ContraptionMovementSetting.isNoPickup(contraption.getBlocks().values())) {
                                        player.displayClientMessage(Lang.translateDirect("contraption.minecart_contraption_illegal_pickup").withStyle(ChatFormatting.RED), true);
                                    } else if (event.getLevel().isClientSide) {
                                        event.setCancellationResult(InteractionResult.SUCCESS);
                                        event.setCanceled(true);
                                    } else {
                                        contraption.stop(event.getLevel());
                                        for (MutablePair<StructureTemplate.StructureBlockInfo, MovementContext> pair : contraption.getActors()) {
                                            if (AllMovementBehaviours.getBehaviour(((StructureTemplate.StructureBlockInfo) pair.left).state()) instanceof PortableStorageInterfaceMovement psim) {
                                                psim.reset((MovementContext) pair.right);
                                            }
                                        }
                                        ItemStack generatedStack = create(type, oce).setHoverName(entity.getCustomName());
                                        if (ContraptionData.isTooLargeForPickup(generatedStack.serializeNBT())) {
                                            MutableComponent message = Lang.translateDirect("contraption.minecart_contraption_too_big").withStyle(ChatFormatting.RED);
                                            player.displayClientMessage(message, true);
                                        } else {
                                            if (contraption.getBlocks().size() > 200) {
                                                AllAdvancements.CART_PICKUP.awardTo(player);
                                            }
                                            player.getInventory().placeItemBackInInventory(generatedStack);
                                            oce.m_146870_();
                                            entity.discard();
                                            event.setCancellationResult(InteractionResult.SUCCESS);
                                            event.setCanceled(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static ItemStack create(AbstractMinecart.Type type, OrientedContraptionEntity entity) {
        ItemStack stack = ItemStack.EMPTY;
        switch(type) {
            case RIDEABLE:
                stack = AllItems.MINECART_CONTRAPTION.asStack();
                break;
            case FURNACE:
                stack = AllItems.FURNACE_MINECART_CONTRAPTION.asStack();
                break;
            case CHEST:
                stack = AllItems.CHEST_MINECART_CONTRAPTION.asStack();
        }
        if (stack.isEmpty()) {
            return stack;
        } else {
            CompoundTag tag = entity.getContraption().writeNBT(false);
            tag.remove("UUID");
            tag.remove("Pos");
            tag.remove("Motion");
            NBTHelper.writeEnum(tag, "InitialOrientation", entity.getInitialOrientation());
            stack.getOrCreateTag().put("Contraption", tag);
            return stack;
        }
    }
}