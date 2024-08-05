package org.violetmoon.quark.content.tools.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class AbacusItem extends ZetaItem {

    public static final String TAG_POS_X = "boundPosX";

    public static final String TAG_POS_Y = "boundPosY";

    public static final String TAG_POS_Z = "boundPosZ";

    public static int MAX_COUNT = 48;

    private static final int DEFAULT_Y = -999;

    public AbacusItem(ZetaModule module) {
        super("abacus", module, new Item.Properties().stacksTo(1));
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.SPYGLASS, true);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        BlockPos curr = getBlockPos(stack);
        if (curr != null) {
            setBlockPos(stack, null);
        } else {
            setBlockPos(stack, context.getClickedPos());
        }
        return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    public static void setBlockPos(ItemStack stack, BlockPos pos) {
        if (pos == null) {
            ItemNBTHelper.setInt(stack, "boundPosY", -999);
        } else {
            ItemNBTHelper.setInt(stack, "boundPosX", pos.m_123341_());
            ItemNBTHelper.setInt(stack, "boundPosY", pos.m_123342_());
            ItemNBTHelper.setInt(stack, "boundPosZ", pos.m_123343_());
        }
    }

    public static BlockPos getBlockPos(ItemStack stack) {
        int y = ItemNBTHelper.getInt(stack, "boundPosY", -999);
        if (y == -999) {
            return null;
        } else {
            int x = ItemNBTHelper.getInt(stack, "boundPosX", 0);
            int z = ItemNBTHelper.getInt(stack, "boundPosZ", 0);
            return new BlockPos(x, y, z);
        }
    }

    public static int getCount(ItemStack stack, BlockPos target, Level world) {
        BlockPos pos = getBlockPos(stack);
        return pos != null && !world.m_46859_(target) ? Mth.clamp(target.m_123333_(pos), 0, MAX_COUNT) : -1;
    }

    public static class Client {

        public static final ClampedItemPropertyFunction ITEM_PROPERTY_FUNCTION = (stack, level, entityIn, id) -> {
            int count = getCount(stack, entityIn);
            return count == -1 ? 1.0F : 0.01F * (float) count + 0.005F;
        };

        public static int getCount(ItemStack stack, LivingEntity entityIn) {
            int count = -1;
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (entityIn == player && player != null) {
                HitResult result = mc.hitResult;
                if (result instanceof BlockHitResult) {
                    BlockPos target = ((BlockHitResult) result).getBlockPos();
                    count = AbacusItem.getCount(stack, target, player.m_9236_());
                }
            }
            return count;
        }
    }
}