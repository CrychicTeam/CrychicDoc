package dev.xkmc.l2complements.content.item.misc;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2library.util.nbt.NBTObj;
import dev.xkmc.l2library.util.tools.TeleportTool;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class WarpStone extends Item {

    public final boolean fragile;

    public static Optional<Pair<ResourceKey<Level>, Vec3>> getPos(ItemStack stack) {
        return Optional.ofNullable(stack.getTag()).filter(e -> e.contains("pos")).map(e -> e.getCompound("pos")).map(e -> Pair.of(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(e.getString("dim"))), new Vec3(e.getDouble("x"), e.getDouble("y"), e.getDouble("z"))));
    }

    public static void setPos(ItemStack stack, Level level, double x, double y, double z) {
        CompoundTag tag = new NBTObj(stack.getOrCreateTag()).getSub("pos").tag;
        tag.putString("dim", level.dimension().location().toString());
        tag.putDouble("x", x);
        tag.putDouble("y", y);
        tag.putDouble("z", z);
    }

    public WarpStone(Item.Properties props, boolean fragile) {
        super(props);
        this.fragile = fragile;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        return this.use(level, player, stack, e -> e.m_21190_(hand));
    }

    public void use(ServerPlayer player, ItemStack stack) {
        this.use(player.m_9236_(), player, stack, e -> {
        });
    }

    private InteractionResultHolder<ItemStack> use(Level level, Player player, ItemStack stack, Consumer<Player> breaker) {
        Optional<Pair<ResourceKey<Level>, Vec3>> ppos = getPos(stack);
        if (ppos.isPresent()) {
            if (!level.isClientSide()) {
                Pair<ResourceKey<Level>, Vec3> pair = (Pair<ResourceKey<Level>, Vec3>) ppos.get();
                ResourceKey<Level> dim = (ResourceKey<Level>) pair.getFirst();
                Vec3 pos = (Vec3) pair.getSecond();
                ServerLevel lv = ((ServerLevel) level).getServer().getLevel(dim);
                if (lv != null) {
                    TeleportTool.performTeleport(player, lv, pos.x, pos.y, pos.z, player.m_146908_(), player.m_146909_());
                }
                if (!this.fragile) {
                    stack.hurtAndBreak(1, player, breaker);
                }
            }
            if (this.fragile) {
                stack.shrink(1);
                return InteractionResultHolder.consume(stack);
            } else {
                return InteractionResultHolder.success(stack);
            }
        } else {
            if (!level.isClientSide()) {
                setPos(stack, level, player.m_20185_(), player.m_20186_() + 0.001, player.m_20189_());
            }
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        Optional<Pair<ResourceKey<Level>, Vec3>> pos = getPos(stack);
        int dur = this.fragile ? 1 : stack.getMaxDamage() - this.getDamage(stack);
        if (pos.isPresent()) {
            Pair<ResourceKey<Level>, Vec3> cpos = (Pair<ResourceKey<Level>, Vec3>) pos.get();
            MutableComponent clevel = Component.translatable(((ResourceKey) cpos.getFirst()).location().toString());
            Vec3 bpos = (Vec3) cpos.getSecond();
            list.add(LangData.IDS.WARP_POS.get(clevel, Math.round(bpos.x), Math.round(bpos.y), Math.round(bpos.x)).withStyle(ChatFormatting.GRAY));
            list.add(LangData.IDS.WARP_TELEPORT.get(dur).withStyle(ChatFormatting.GRAY));
            list.add(LangData.IDS.WARP_GRIND.get().withStyle(ChatFormatting.GRAY));
        } else {
            list.add(LangData.IDS.WARP_RECORD.get(dur).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return getPos(pStack).isPresent();
    }
}