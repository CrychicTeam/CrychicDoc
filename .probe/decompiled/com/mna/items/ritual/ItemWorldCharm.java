package com.mna.items.ritual;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.items.TieredItem;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class ItemWorldCharm extends TieredItem {

    private static final String KEY_WORLD_TARGET_REGISTRY_NAME = "world_key_type";

    private static final String KEY_WORLD_TARGET_LOCATION = "world_key_value";

    public ItemWorldCharm() {
        super(new Item.Properties().stacksTo(1));
    }

    public boolean SetWorldTarget(Player entity, ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        LazyOptional<IPlayerMagic> magic = entity.getCapability(PlayerMagicProvider.MAGIC);
        if (magic.isPresent()) {
            nbt.putString("world_key_type", entity.m_9236_().dimension().registry().toString());
            nbt.putString("world_key_value", entity.m_9236_().dimension().location().toString());
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public ResourceKey<Level> GetWorldTarget(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("world_key_type") && nbt.contains("world_key_value")) {
            ResourceLocation location = new ResourceLocation(nbt.getString("world_key_value"));
            return ResourceKey.create(Registries.DIMENSION, location);
        } else {
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
            if (!nbt.contains("world_key_type") && !nbt.contains("world_key_value")) {
                this.SetWorldTarget(playerIn, stack);
                playerIn.m_213846_(Component.translatable("item.mna.world_charm.attuned_success"));
            } else {
                playerIn.m_213846_(Component.translatable("item.mna.world_charm.attuned_failure"));
            }
        }
        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("world_key_value")) {
            ResourceLocation worldLocation = new ResourceLocation(nbt.getString("world_key_value"));
            tooltip.add(Component.translatable("item.mna.world_charm.attuned", worldLocation.toString()));
        } else {
            tooltip.add(Component.translatable("item.mna.world_charm.not_attuned"));
        }
    }
}