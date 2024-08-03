package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.CaveBookProgress;
import java.awt.Color;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class CaveInfoItem extends Item {

    private boolean hideCaveId;

    public CaveInfoItem(Item.Properties properties, boolean hideCaveId) {
        super(properties);
        this.hideCaveId = hideCaveId;
    }

    public static int getBiomeColorOf(Level level, ItemStack stack, boolean darken) {
        if (stack.getTag() != null && stack.getTag().getBoolean("Rainbow")) {
            float hue = (float) (System.currentTimeMillis() % 4000L) / 4000.0F;
            return Color.HSBtoRGB(hue, 1.0F, 0.8F);
        } else if (stack.getItem() instanceof CaveInfoItem) {
            ResourceKey<Biome> biomeResourceKey = getCaveBiome(stack);
            if (biomeResourceKey == null) {
                int selectedBiomeIndex = (int) ((float) ((long) ACBiomeRegistry.ALEXS_CAVES_BIOMES.size() * (System.currentTimeMillis() % 4000L)) / 4000.0F);
                biomeResourceKey = (ResourceKey<Biome>) ACBiomeRegistry.ALEXS_CAVES_BIOMES.get(selectedBiomeIndex);
            }
            if (darken && biomeResourceKey != null && biomeResourceKey.equals(ACBiomeRegistry.TOXIC_CAVES)) {
                return 4570660;
            } else {
                return biomeResourceKey == null ? -1 : getBiomeColor(level, biomeResourceKey);
            }
        } else {
            return -1;
        }
    }

    private static int getBiomeColor(Level level, ResourceKey<Biome> biomeResourceKey) {
        int color = ACBiomeRegistry.getBiomeTabletColor(biomeResourceKey);
        if (color == -1) {
            if (level != null) {
                Registry<Biome> registry = (Registry<Biome>) level.registryAccess().registry(Registries.BIOME).orElse(null);
                if (registry != null && registry.getHolder(biomeResourceKey).isPresent()) {
                    return ((Biome) ((Holder.Reference) registry.getHolder(biomeResourceKey).get()).value()).getFoliageColor();
                }
            }
            return 0;
        } else {
            return color;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        ResourceKey<Biome> biomeResourceKey = getCaveBiome(itemstack);
        if (itemstack.is(ACItemRegistry.CAVE_CODEX.get()) && biomeResourceKey != null) {
            String biomeStr = biomeResourceKey.location().toString();
            CaveBookProgress progress = CaveBookProgress.getCaveBookProgress(player);
            if (progress.unlockNextFor(biomeStr)) {
                player.m_6674_(hand);
                if (!level.isClientSide) {
                    CaveBookProgress.saveCaveBookProgress(progress, player);
                    CaveBookProgress.Subcategory subcategory = progress.getLastUnlockedCategory(biomeStr);
                    Component biomeTitle = Component.translatable("biome." + biomeResourceKey.location().toString().replace(":", "."));
                    if (AlexsCaves.COMMON_CONFIG.onlyOneResearchNeeded.get()) {
                        player.displayClientMessage(Component.translatable("item.alexscaves.cave_codex.add_all", biomeTitle), true);
                    } else {
                        MutableComponent unlocked = Component.translatable("item.alexscaves.cave_codex.add", biomeTitle, Component.translatable("item.alexscaves.cave_book." + subcategory.toString().toLowerCase()));
                        if (subcategory == CaveBookProgress.Subcategory.SECRETS) {
                            unlocked = unlocked.withStyle(ChatFormatting.LIGHT_PURPLE);
                        }
                        player.displayClientMessage(unlocked, true);
                    }
                }
                if (!player.isCreative()) {
                    itemstack.shrink(1);
                }
                player.m_216990_(SoundEvents.EXPERIENCE_ORB_PICKUP);
                return InteractionResultHolder.consume(itemstack);
            }
            player.displayClientMessage(Component.translatable("item.alexscaves.cave_codex.end").withStyle(ChatFormatting.RED), true);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    public static ItemStack create(Item item, ResourceKey<Biome> biomeResourceKey) {
        ItemStack map = new ItemStack(item);
        CompoundTag tag = new CompoundTag();
        tag.putString("CaveBiome", biomeResourceKey.location().toString());
        map.setTag(tag);
        return map;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ResourceKey<Biome> biomeResourceKey = getCaveBiome(stack);
        if (biomeResourceKey != null && !this.hideCaveId) {
            String biomeName = "biome." + biomeResourceKey.location().toString().replace(":", ".");
            tooltip.add(Component.translatable(biomeName).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static ResourceKey<Biome> getCaveBiome(ItemStack stack) {
        if (stack.getTag() != null) {
            String s = stack.getTag().getString("CaveBiome");
            return s == null ? null : ResourceKey.create(Registries.BIOME, new ResourceLocation(s));
        } else {
            return null;
        }
    }
}