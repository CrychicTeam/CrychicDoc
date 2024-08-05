package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.NBTProcessors;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.Nullable;

public abstract class ZapperItem extends Item implements CustomArmPoseItem {

    public ZapperItem(Item.Properties properties) {
        super(properties.stacksTo(1));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.hasTag() && stack.getTag().contains("BlockUsed")) {
            MutableComponent usedBlock = NbtUtils.readBlockState(worldIn.m_246945_(Registries.BLOCK), stack.getTag().getCompound("BlockUsed")).m_60734_().getName();
            tooltip.add(Lang.translateDirect("terrainzapper.usingBlock", usedBlock.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        boolean differentBlock = false;
        if (oldStack.hasTag() && newStack.hasTag() && oldStack.getTag().contains("BlockUsed") && newStack.getTag().contains("BlockUsed")) {
            differentBlock = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), oldStack.getTag().getCompound("BlockUsed")) != NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), newStack.getTag().getCompound("BlockUsed"));
        }
        return slotChanged || !this.isZapper(newStack) || differentBlock;
    }

    public boolean isZapper(ItemStack newStack) {
        return newStack.getItem() instanceof ZapperItem;
    }

    @Nonnull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() != null && context.getPlayer().m_6144_()) {
            if (context.getLevel().isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.openHandgunGUI(context.getItemInHand(), context.getHand()));
                context.getPlayer().getCooldowns().addCooldown(context.getItemInHand().getItem(), 10);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(context);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack item = player.m_21120_(hand);
        CompoundTag nbt = item.getOrCreateTag();
        boolean mainHand = hand == InteractionHand.MAIN_HAND;
        if (player.m_6144_()) {
            if (world.isClientSide) {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.openHandgunGUI(item, hand));
                player.getCooldowns().addCooldown(item.getItem(), 10);
            }
            return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
        } else if (ShootableGadgetItemMethods.shouldSwap(player, item, hand, this::isZapper)) {
            return new InteractionResultHolder<>(InteractionResult.FAIL, item);
        } else {
            Component msg = this.validateUsage(item);
            if (msg != null) {
                AllSoundEvents.DENY.play(world, player, player.m_20183_());
                player.displayClientMessage(msg.plainCopy().withStyle(ChatFormatting.RED), true);
                return new InteractionResultHolder<>(InteractionResult.FAIL, item);
            } else {
                BlockState stateToUse = Blocks.AIR.defaultBlockState();
                if (nbt.contains("BlockUsed")) {
                    stateToUse = NbtUtils.readBlockState(world.m_246945_(Registries.BLOCK), nbt.getCompound("BlockUsed"));
                }
                stateToUse = BlockHelper.setZeroAge(stateToUse);
                CompoundTag data = null;
                if (AllTags.AllBlockTags.SAFE_NBT.matches(stateToUse) && nbt.contains("BlockData", 10)) {
                    data = nbt.getCompound("BlockData");
                }
                Vec3 start = player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0);
                Vec3 range = player.m_20154_().scale((double) this.getZappingRange(item));
                BlockHitResult raytrace = world.m_45547_(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
                BlockPos pos = raytrace.getBlockPos();
                BlockState stateReplaced = world.getBlockState(pos);
                if (pos != null && stateReplaced.m_60734_() != Blocks.AIR) {
                    Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, mainHand, new Vec3(0.35F, -0.1F, 1.0));
                    if (world.isClientSide) {
                        CreateClient.ZAPPER_RENDER_HANDLER.dontAnimateItem(hand);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
                    } else {
                        if (this.activate(world, player, item, stateToUse, raytrace, data)) {
                            ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, this.getCooldownDelay(item));
                            ShootableGadgetItemMethods.sendPackets(player, b -> new ZapperBeamPacket(barrelPos, raytrace.m_82450_(), hand, b));
                        }
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
                    }
                } else {
                    ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, this.getCooldownDelay(item));
                    return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
                }
            }
        }
    }

    public Component validateUsage(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        return !this.canActivateWithoutSelectedBlock(item) && !tag.contains("BlockUsed") ? Lang.translateDirect("terrainzapper.leftClickToSet") : null;
    }

    protected abstract boolean activate(Level var1, Player var2, ItemStack var3, BlockState var4, BlockHitResult var5, CompoundTag var6);

    @OnlyIn(Dist.CLIENT)
    protected abstract void openHandgunGUI(ItemStack var1, InteractionHand var2);

    protected abstract int getCooldownDelay(ItemStack var1);

    protected abstract int getZappingRange(ItemStack var1);

    protected boolean canActivateWithoutSelectedBlock(ItemStack stack) {
        return false;
    }

    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level worldIn, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Nullable
    @Override
    public HumanoidModel.ArmPose getArmPose(ItemStack stack, AbstractClientPlayer player, InteractionHand hand) {
        return !player.f_20911_ ? HumanoidModel.ArmPose.CROSSBOW_HOLD : null;
    }

    public static void configureSettings(ItemStack stack, PlacementPatterns pattern) {
        CompoundTag nbt = stack.getOrCreateTag();
        NBTHelper.writeEnum(nbt, "Pattern", pattern);
    }

    public static void setBlockEntityData(Level world, BlockPos pos, BlockState state, CompoundTag data, Player player) {
        if (data != null && AllTags.AllBlockTags.SAFE_NBT.matches(state)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                data = NBTProcessors.process(blockEntity, data, !player.isCreative());
                if (data == null) {
                    return;
                }
                data.putInt("x", pos.m_123341_());
                data.putInt("y", pos.m_123342_());
                data.putInt("z", pos.m_123343_());
                blockEntity.load(data);
            }
        }
    }
}