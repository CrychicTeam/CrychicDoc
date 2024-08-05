package com.mna.items.artifice;

import com.mna.ManaAndArtifice;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemThaumaturgicCompass extends CompassItem {

    public static final String trackingKey = "tracking_key";

    public static final String trackingType = "tracking_type";

    public static final String trackingId = "tracking_id";

    public static final String trackingMode = "tracking_mode";

    private static Optional<ResourceKey<Level>> getLodestoneDimension(CompoundTag pCompoundTag) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pCompoundTag.get("LodestoneDimension")).result();
    }

    public ItemThaumaturgicCompass() {
        super(new Item.Properties());
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return this.m_5524_();
    }

    @Nullable
    public BlockPos getTrackedPosition(ItemStack stack, ResourceKey<Level> dimension) {
        if (!m_40736_(stack)) {
            return null;
        } else {
            Optional<ResourceKey<Level>> world = getLodestoneDimension(stack.getTag());
            return world.isPresent() && ((ResourceKey) world.get()).equals(dimension) ? NbtUtils.readBlockPos(stack.getTag().getCompound("LodestonePos")) : null;
        }
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.UNCOMMON;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = stack.getTag();
        if (tag != null) {
            if (tag.contains("tracking_key")) {
                tooltip.add(Component.translatable("item.mna.thaumaturgic_compass.locating", Component.translatable(tag.getString("tracking_key"))));
            }
            if (getTrackMode(stack)) {
                tooltip.add(Component.translatable("item.mna.thaumaturgic_compass.mode_new"));
            } else {
                tooltip.add(Component.translatable("item.mna.thaumaturgic_compass.mode_existing"));
            }
            if (getTrackType(stack) == ItemThaumaturgicCompass.TrackType.Structure) {
                tooltip.add(Component.translatable("item.mna.thaumaturgic_compass.mode_hint_structure"));
            } else {
                tooltip.add(Component.translatable("item.mna.thaumaturgic_compass.mode_hint_biome"));
            }
        }
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public static String getLocalizedStructureName(@Nonnull String resource) {
        if (resource.equals("")) {
            return "";
        } else {
            int split = resource.indexOf(":");
            if (split == -1) {
                return resource;
            } else {
                String source = resource.substring(0, split);
                String name = resource.substring(split + 1);
                return I18n.get(String.format("structure.%s.%s", source, name));
            }
        }
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (!world.isClientSide) {
            if (player.m_6047_()) {
                switch(getTrackType(stack)) {
                    case Biome:
                        search_structure((ServerPlayer) player, stack, getTrackMode(stack));
                        break;
                    case Structure:
                        search_structure((ServerPlayer) player, stack, getTrackMode(stack));
                }
            } else if (getTrackType(stack) == ItemThaumaturgicCompass.TrackType.Structure) {
                boolean newmode = !getTrackMode(stack);
                stack.getTag().putBoolean("tracking_mode", newmode);
                player.m_213846_(Component.translatable("item.mna.thaumaturgic_compass.mode_changed"));
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, world.isClientSide);
    }

    public static void setTrackedPosition(ItemStack stack, ResourceKey<Level> dimension, BlockPos position, ResourceLocation trackedID, ItemThaumaturgicCompass.TrackType trackType) {
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.putBoolean("tracking_mode", false);
        stackTag.putInt("tracking_type", trackType.ordinal());
        stackTag.putString("tracking_key", trackedID.toString());
        stackTag.put("LodestonePos", NbtUtils.writeBlockPos(position));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, dimension).resultOrPartial(ManaAndArtifice.LOGGER::error).ifPresent(inbt -> stackTag.put("LodestoneDimension", inbt));
        stackTag.putBoolean("LodestoneTracked", true);
    }

    @Nullable
    public static ResourceLocation getTrackedID(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("tracking_key") ? new ResourceLocation(stack.getTag().getString("tracking_key")) : null;
    }

    public static ItemThaumaturgicCompass.TrackType getTrackType(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("tracking_type") ? ItemThaumaturgicCompass.TrackType.values()[stack.getTag().getInt("tracking_type") % ItemThaumaturgicCompass.TrackType.values().length] : ItemThaumaturgicCompass.TrackType.Unset;
    }

    public static boolean getTrackMode(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("tracking_mode") ? stack.getTag().getBoolean("tracking_mode") : false;
    }

    public static void search_structure(ServerPlayer player, ItemStack stack, boolean skipGeneratedChunks) {
        if (getTrackType(stack) == ItemThaumaturgicCompass.TrackType.Structure) {
            ResourceLocation structureRegistryName = getTrackedID(stack);
            ServerLevel world = (ServerLevel) player.m_9236_();
            player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.searching"));
            Registry<Structure> registry = player.m_9236_().registryAccess().registryOrThrow(Registries.STRUCTURE);
            Structure pStructure = registry.get(structureRegistryName);
            try {
                HolderSet<Structure> holderset = HolderSet.direct(Holder.direct(pStructure));
                BlockPos blockpos = BlockPos.containing(player.m_20182_());
                Pair<BlockPos, Holder<Structure>> pair = world.getChunkSource().getGenerator().findNearestMapStructure(world, holderset, blockpos, 100, false);
                if (pair == null) {
                    player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_fail"));
                } else {
                    player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_success"));
                    BlockPos pos = (BlockPos) pair.getFirst();
                    setTrackedPosition(stack, world.m_46472_(), pos, structureRegistryName, ItemThaumaturgicCompass.TrackType.Structure);
                }
            } catch (Throwable var11) {
                player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_fail"));
            }
        }
    }

    public static void search_biome(ServerPlayer player, ItemStack stack) {
        if (getTrackType(stack) == ItemThaumaturgicCompass.TrackType.Structure) {
            ResourceLocation biomeLoc = getTrackedID(stack);
            ServerLevel world = (ServerLevel) player.m_9236_();
            player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.searching"));
            Registry<Biome> registry = player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME);
            Biome biome = registry.get(biomeLoc);
            if (biome == null) {
                player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_fail"));
            } else {
                Pair<BlockPos, Holder<Biome>> result = world.findClosestBiome3d(b -> b.isBound() && ((Biome) b.value()).equals(biome), player.m_20183_(), 6400, 8, 64);
                if (result == null) {
                    player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_fail"));
                } else {
                    BlockPos pos = (BlockPos) result.getFirst();
                    player.sendSystemMessage(Component.translatable("item.mna.thaumaturgic_compass.search_success"));
                    setTrackedPosition(stack, world.m_46472_(), pos, biomeLoc, ItemThaumaturgicCompass.TrackType.Structure);
                }
            }
        }
    }

    public static enum TrackType {

        Unset, Structure, Biome
    }
}