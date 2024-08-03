package com.mna.items.ritual;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.TieredItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class ItemPlayerCharm extends TieredItem {

    private static final String KEY_PLAYER_TARGET_NAME = "player_target_name";

    private static final String KEY_PLAYER_TARGET_UUID = "player_target_uuid";

    private static final String KEY_PLAYER_TARGET_SALT = "player_target_salt";

    public ItemPlayerCharm() {
        super(new Item.Properties().stacksTo(1));
    }

    public boolean SetPlayerTarget(Player entity, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        LazyOptional<IPlayerMagic> magic = entity.getCapability(PlayerMagicProvider.MAGIC);
        if (magic.isPresent()) {
            nbt.putString("player_target_uuid", entity.m_20148_().toString());
            nbt.putInt("player_target_salt", magic.orElse(null).getTeleportSalt());
            nbt.putString("player_target_name", entity.getGameProfile().getName());
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public UUID getPlayerUUID(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null && nbt.contains("player_target_uuid") && nbt.contains("player_target_salt") ? UUID.fromString(nbt.getString("player_target_uuid")) : null;
    }

    @Nullable
    public Player GetPlayerTarget(ItemStack stack, Level world) {
        UUID uuid = this.getPlayerUUID(stack);
        if (uuid == null) {
            return null;
        } else {
            CompoundTag nbt = stack.getTag();
            Player entity = world.m_46003_(uuid);
            if (entity != null) {
                LazyOptional<IPlayerMagic> magic = entity.getCapability(PlayerMagicProvider.MAGIC);
                if (magic.isPresent() && magic.orElse(null).getTeleportSalt() == nbt.getInt("player_target_salt")) {
                    return entity;
                }
            }
            return null;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack stack = playerIn.m_21120_(handIn);
        if (!worldIn.isClientSide) {
            CompoundTag nbt = stack.getTag();
            if (nbt == null) {
                nbt = new CompoundTag();
            }
            if (!nbt.contains("player_target_uuid") && !nbt.contains("player_target_salt")) {
                this.SetPlayerTarget(playerIn, stack);
                playerIn.m_213846_(Component.translatable("item.mna.player_charm.attuned_success"));
            } else {
                playerIn.m_213846_(Component.translatable("item.mna.player_charm.attuned_failure"));
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTag();
        if (nbt == null) {
            tooltip.add(Component.translatable("item.mna.player_charm.not_attuned"));
        } else {
            String playerName = nbt.getString("player_target_name");
            if (playerName != null) {
                tooltip.add(Component.translatable("item.mna.player_charm.attuned", playerName));
            }
        }
    }
}