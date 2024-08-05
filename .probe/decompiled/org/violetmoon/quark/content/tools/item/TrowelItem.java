package org.violetmoon.quark.content.tools.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.api.ITrowelable;
import org.violetmoon.quark.api.IUsageTickerOverride;
import org.violetmoon.quark.content.tools.module.TrowelModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.ItemNBTHelper;
import org.violetmoon.zeta.util.MiscUtil;

public class TrowelItem extends ZetaItem implements IUsageTickerOverride {

    private static final String TAG_PLACING_SEED = "placing_seed";

    private static final String TAG_LAST_STACK = "last_stack";

    public TrowelItem(ZetaModule module) {
        super("trowel", module, new Item.Properties().durability(255));
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.SHEARS, false);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        } else {
            InteractionHand hand = context.getHand();
            List<Integer> targets = new ArrayList();
            Inventory inventory = player.getInventory();
            for (int i = 0; i < Inventory.getSelectionSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (isValidTarget(stack, context)) {
                    targets.add(i);
                }
            }
            if (targets.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                ItemStack trowel = player.m_21120_(hand);
                long seed = ItemNBTHelper.getLong(trowel, "placing_seed", 0L);
                Random rand = new Random(seed);
                ItemNBTHelper.setLong(trowel, "placing_seed", rand.nextLong());
                int targetSlot = (Integer) targets.get(rand.nextInt(targets.size()));
                ItemStack toPlaceStack = inventory.getItem(targetSlot);
                player.m_21008_(hand, toPlaceStack);
                InteractionResult result = toPlaceStack.useOn(new TrowelItem.TrowelBlockItemUseContext(context, toPlaceStack));
                ItemStack newHandItem = player.m_21120_(hand);
                player.m_21008_(hand, trowel);
                inventory.setItem(targetSlot, newHandItem);
                if (result.consumesAction()) {
                    CompoundTag cmp = toPlaceStack.serializeNBT();
                    ItemNBTHelper.setCompound(trowel, "last_stack", cmp);
                    if (TrowelModule.maxDamage > 0) {
                        MiscUtil.damageStack(player, hand, context.getItemInHand(), 1);
                    }
                }
                return result;
            }
        }
    }

    private static boolean isValidTarget(ItemStack stack, UseOnContext context) {
        Item item = stack.getItem();
        if (stack.is(TrowelModule.whitelist)) {
            return true;
        } else if (stack.is(TrowelModule.blacklist)) {
            return false;
        } else {
            return item instanceof ITrowelable t ? t.canBeTroweled(stack, context) : !stack.isEmpty() && item instanceof BlockItem;
        }
    }

    public static ItemStack getLastStack(ItemStack stack) {
        CompoundTag cmp = ItemNBTHelper.getCompound(stack, "last_stack", false);
        return ItemStack.of(cmp);
    }

    @Override
    public int getMaxDamageZeta(ItemStack stack) {
        return TrowelModule.maxDamage;
    }

    @Override
    public boolean canBeDepleted() {
        return TrowelModule.maxDamage > 0;
    }

    @Override
    public ItemStack getUsageTickerItem(ItemStack stack) {
        return getLastStack(stack);
    }

    class TrowelBlockItemUseContext extends BlockPlaceContext {

        public TrowelBlockItemUseContext(UseOnContext context, ItemStack stack) {
            super(context.getLevel(), context.getPlayer(), context.getHand(), stack, new BlockHitResult(context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), context.isInside()));
        }
    }
}