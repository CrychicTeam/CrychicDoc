package dev.xkmc.l2backpack.content.remote.drawer;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.common.ContentTransfer;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import dev.xkmc.l2backpack.init.registrate.BackpackBlocks;
import dev.xkmc.l2backpack.init.registrate.BackpackItems;
import dev.xkmc.l2modularblock.mult.OnClickBlockMethod;
import dev.xkmc.l2modularblock.mult.SetPlacedByBlockMethod;
import dev.xkmc.l2modularblock.one.BlockEntityBlockMethod;
import dev.xkmc.l2modularblock.one.GetBlockItemBlockMethod;
import dev.xkmc.l2modularblock.one.SpecialDropBlockMethod;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class EnderDrawerBlock implements OnClickBlockMethod, GetBlockItemBlockMethod, SpecialDropBlockMethod, SetPlacedByBlockMethod {

    public static final EnderDrawerBlock INSTANCE = new EnderDrawerBlock();

    public static final BlockEntityBlockMethod<EnderDrawerBlockEntity> BLOK_ENTITY = new EnderDrawerAnalogBlockEntity<EnderDrawerBlockEntity>(BackpackBlocks.TE_ENDER_DRAWER, EnderDrawerBlockEntity.class);

    public InteractionResult onClick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        ItemStack stack = player.m_21120_(hand);
        if (blockentity instanceof EnderDrawerBlockEntity chest) {
            if (!stack.isEmpty() && !stack.hasTag() && stack.getItem() == chest.item) {
                if (!level.isClientSide()) {
                    stack = new EnderDrawerItemHandler(chest.getAccess(), false).insertItem(0, stack, false);
                    player.m_21008_(hand, stack);
                } else {
                    ContentTransfer.playDrawerSound(player);
                }
                return InteractionResult.SUCCESS;
            } else if (stack.isEmpty()) {
                if (!level.isClientSide()) {
                    DrawerAccess access = chest.getAccess();
                    stack = new EnderDrawerItemHandler(access, false).extractItem(0, access.item().getMaxStackSize(), false);
                    player.m_21008_(hand, stack);
                } else {
                    ContentTransfer.playDrawerSound(player);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        UUID id = stack.getOrCreateTag().getUUID("owner_id");
        String name = stack.getOrCreateTag().getString("owner_name");
        if (blockentity instanceof EnderDrawerBlockEntity chest) {
            chest.owner_id = id;
            chest.owner_name = name;
            chest.item = BaseDrawerItem.getItem(stack);
            chest.config = PickupConfig.getConfig(stack);
            chest.addToListener();
        }
    }

    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof EnderDrawerBlockEntity chest ? this.buildStack(chest) : BackpackItems.ENDER_DRAWER.asStack();
    }

    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockentity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        return blockentity instanceof EnderDrawerBlockEntity chest ? List.of(this.buildStack(chest)) : List.of(BackpackItems.ENDER_DRAWER.asStack());
    }

    private ItemStack buildStack(EnderDrawerBlockEntity chest) {
        ItemStack stack = BackpackItems.ENDER_DRAWER.asStack();
        if (chest.owner_id != null) {
            stack.getOrCreateTag().putUUID("owner_id", chest.owner_id);
            stack.getOrCreateTag().putString("owner_name", chest.owner_name);
            ResourceLocation rl = ForgeRegistries.ITEMS.getKey(chest.item);
            assert rl != null;
            stack.getOrCreateTag().putString("drawerItem", rl.toString());
            PickupConfig.setConfig(stack, chest.config);
        }
        return stack;
    }
}