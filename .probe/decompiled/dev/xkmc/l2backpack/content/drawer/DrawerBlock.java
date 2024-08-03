package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.init.registrate.BackpackBlocks;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2modularblock.impl.BlockEntityBlockMethodImpl;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.mult.SetPlacedByBlockMethod;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.l2modularblock.one.GetBlockItemBlockMethod;
import dev.xkmc.l2modularblock.one.SpecialDropBlockMethod;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class DrawerBlock implements OnClickBlockMethod, GetBlockItemBlockMethod, SpecialDropBlockMethod, SetPlacedByBlockMethod {

    public static final DrawerBlock INSTANCE = new DrawerBlock();

    public static final BlockEntityBlockMethod<DrawerBlockEntity> BLOCK_ENTITY = new BlockEntityBlockMethodImpl(BackpackBlocks.TE_DRAWER, DrawerBlockEntity.class);

    public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        ItemStack stack = player.m_21120_(hand);
        if (!(blockentity instanceof DrawerBlockEntity chest)) {
            return InteractionResult.PASS;
        } else if ((stack.isEmpty() || stack.hasTag() || stack.getItem() != chest.getItem()) && chest.getItem() != Items.AIR) {
            if (stack.isEmpty()) {
                if (!level.isClientSide()) {
                    stack = chest.handler.extractItem(0, chest.getItem().getMaxStackSize(), false);
                    player.m_21008_(hand, stack);
                } else {
                    ContentTransfer.playDrawerSound(player);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            if (!level.isClientSide()) {
                stack = chest.handler.insertItem(0, stack, false);
                player.m_21008_(hand, stack);
            } else {
                ContentTransfer.playDrawerSound(player);
            }
            return InteractionResult.SUCCESS;
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof DrawerBlockEntity chest) {
            chest.handler.count = DrawerItem.getCount(stack);
            chest.handler.item = chest.handler.count == 0 ? Items.AIR : BaseDrawerItem.getItem(stack);
            chest.handler.config = PickupConfig.getConfig(stack);
        }
    }

    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof DrawerBlockEntity chest ? this.buildStack(chest) : BackpackItems.DRAWER.asStack();
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        return blockentity instanceof DrawerBlockEntity chest ? List.of(this.buildStack(chest)) : List.of(BackpackItems.DRAWER.asStack());
    }

    private ItemStack buildStack(DrawerBlockEntity chest) {
        ItemStack stack = BackpackItems.DRAWER.asStack();
        ResourceLocation rl = ForgeRegistries.ITEMS.getKey(chest.getItem());
        assert rl != null;
        stack.getOrCreateTag().putString("drawerItem", rl.toString());
        DrawerItem.setCount(stack, chest.handler.count);
        PickupConfig.setConfig(stack, chest.handler.config);
        return stack;
    }
}