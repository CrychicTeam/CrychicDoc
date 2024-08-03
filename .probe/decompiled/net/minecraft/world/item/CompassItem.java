package net.minecraft.world.item;

import com.mojang.logging.LogUtils;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class CompassItem extends Item implements Vanishable {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String TAG_LODESTONE_POS = "LodestonePos";

    public static final String TAG_LODESTONE_DIMENSION = "LodestoneDimension";

    public static final String TAG_LODESTONE_TRACKED = "LodestoneTracked";

    public CompassItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    public static boolean isLodestoneCompass(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null && ($$1.contains("LodestoneDimension") || $$1.contains("LodestonePos"));
    }

    private static Optional<ResourceKey<Level>> getLodestoneDimension(CompoundTag compoundTag0) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, compoundTag0.get("LodestoneDimension")).result();
    }

    @Nullable
    public static GlobalPos getLodestonePosition(CompoundTag compoundTag0) {
        boolean $$1 = compoundTag0.contains("LodestonePos");
        boolean $$2 = compoundTag0.contains("LodestoneDimension");
        if ($$1 && $$2) {
            Optional<ResourceKey<Level>> $$3 = getLodestoneDimension(compoundTag0);
            if ($$3.isPresent()) {
                BlockPos $$4 = NbtUtils.readBlockPos(compoundTag0.getCompound("LodestonePos"));
                return GlobalPos.of((ResourceKey<Level>) $$3.get(), $$4);
            }
        }
        return null;
    }

    @Nullable
    public static GlobalPos getSpawnPosition(Level level0) {
        return level0.dimensionType().natural() ? GlobalPos.of(level0.dimension(), level0.getSharedSpawnPos()) : null;
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return isLodestoneCompass(itemStack0) || super.isFoil(itemStack0);
    }

    @Override
    public void inventoryTick(ItemStack itemStack0, Level level1, Entity entity2, int int3, boolean boolean4) {
        if (!level1.isClientSide) {
            if (isLodestoneCompass(itemStack0)) {
                CompoundTag $$5 = itemStack0.getOrCreateTag();
                if ($$5.contains("LodestoneTracked") && !$$5.getBoolean("LodestoneTracked")) {
                    return;
                }
                Optional<ResourceKey<Level>> $$6 = getLodestoneDimension($$5);
                if ($$6.isPresent() && $$6.get() == level1.dimension() && $$5.contains("LodestonePos")) {
                    BlockPos $$7 = NbtUtils.readBlockPos($$5.getCompound("LodestonePos"));
                    if (!level1.isInWorldBounds($$7) || !((ServerLevel) level1).getPoiManager().existsAtPosition(PoiTypes.LODESTONE, $$7)) {
                        $$5.remove("LodestonePos");
                    }
                }
            }
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        BlockPos $$1 = useOnContext0.getClickedPos();
        Level $$2 = useOnContext0.getLevel();
        if (!$$2.getBlockState($$1).m_60713_(Blocks.LODESTONE)) {
            return super.useOn(useOnContext0);
        } else {
            $$2.playSound(null, $$1, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            Player $$3 = useOnContext0.getPlayer();
            ItemStack $$4 = useOnContext0.getItemInHand();
            boolean $$5 = !$$3.getAbilities().instabuild && $$4.getCount() == 1;
            if ($$5) {
                this.addLodestoneTags($$2.dimension(), $$1, $$4.getOrCreateTag());
            } else {
                ItemStack $$6 = new ItemStack(Items.COMPASS, 1);
                CompoundTag $$7 = $$4.hasTag() ? $$4.getTag().copy() : new CompoundTag();
                $$6.setTag($$7);
                if (!$$3.getAbilities().instabuild) {
                    $$4.shrink(1);
                }
                this.addLodestoneTags($$2.dimension(), $$1, $$7);
                if (!$$3.getInventory().add($$6)) {
                    $$3.drop($$6, false);
                }
            }
            return InteractionResult.sidedSuccess($$2.isClientSide);
        }
    }

    private void addLodestoneTags(ResourceKey<Level> resourceKeyLevel0, BlockPos blockPos1, CompoundTag compoundTag2) {
        compoundTag2.put("LodestonePos", NbtUtils.writeBlockPos(blockPos1));
        Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, resourceKeyLevel0).resultOrPartial(LOGGER::error).ifPresent(p_40731_ -> compoundTag2.put("LodestoneDimension", p_40731_));
        compoundTag2.putBoolean("LodestoneTracked", true);
    }

    @Override
    public String getDescriptionId(ItemStack itemStack0) {
        return isLodestoneCompass(itemStack0) ? "item.minecraft.lodestone_compass" : super.getDescriptionId(itemStack0);
    }
}