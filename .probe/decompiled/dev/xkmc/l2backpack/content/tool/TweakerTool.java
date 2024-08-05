package dev.xkmc.l2backpack.content.tool;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2backpack.compat.CuriosCompat;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.remote.worldchest.WorldChestBlockEntity;
import dev.xkmc.l2backpack.events.BackpackSlotClickListener;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public abstract class TweakerTool extends Item implements IBagTool {

    public TweakerTool(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        ItemStack bag = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!BackpackSlotClickListener.canOpen(bag)) {
            Optional<Pair<ItemStack, PlayerSlot<?>>> opt = CuriosCompat.getSlot(player, BackpackSlotClickListener::canOpen);
            if (opt.isPresent()) {
                bag = (ItemStack) ((Pair) opt.get()).getFirst();
            }
        }
        if (BackpackSlotClickListener.canOpen(bag)) {
            if (!level.isClientSide()) {
                this.click(bag);
                if (player instanceof ServerPlayer sp) {
                    MutableComponent msg = bag.getHoverName().copy().append(": ").append(this.message(PickupConfig.getPickupMode(bag)));
                    sp.sendSystemMessage(msg, true);
                }
            }
            return InteractionResultHolder.success(stack);
        } else {
            return InteractionResultHolder.pass(stack);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (ctx.getLevel().getBlockEntity(ctx.getClickedPos()) instanceof WorldChestBlockEntity be) {
            if (!ctx.getLevel().isClientSide()) {
                be.setPickupMode(this.click(be.config));
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(ctx);
        }
    }

    public abstract PickupConfig click(PickupConfig var1);

    public abstract Component message(PickupConfig var1);
}