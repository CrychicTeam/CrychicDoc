package net.mehvahdjukaar.supplementaries.forge;

import net.mehvahdjukaar.moonlight.api.util.FakePlayerManager;
import net.mehvahdjukaar.supplementaries.common.capabilities.CapabilityHandler;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.mixins.forge.MobBucketItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public class SuppPlatformStuffImpl {

    public static EntityType<?> getFishType(MobBucketItem bucketItem) {
        return ((MobBucketItemAccessor) bucketItem).invokeGetFishType();
    }

    @Nullable
    public static <T> T getForgeCap(Object object, Class<T> capClass) {
        Capability<T> t = CapabilityHandler.getToken(capClass);
        return t != null && object instanceof ICapabilityProvider cp ? CapabilityHandler.get(cp, t) : null;
    }

    @Nullable
    public static BlockState getUnoxidised(Level level, BlockPos pos, BlockState state) {
        Player fp = FakePlayerManager.getDefault(level);
        fp.m_21008_(InteractionHand.MAIN_HAND, Items.IRON_AXE.getDefaultInstance());
        Block b = state.m_60734_();
        UseOnContext context = new UseOnContext(fp, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        BlockState modified = b.getToolModifiedState(state, context, ToolActions.AXE_WAX_OFF, false);
        if (modified == null) {
            modified = state;
        }
        while (true) {
            BlockState newMod = b.getToolModifiedState(modified, context, ToolActions.AXE_SCRAPE, false);
            if (newMod == null || newMod == modified) {
                return modified == state ? null : modified;
            }
            modified = newMod;
        }
    }

    public static boolean isEndermanMask(EnderMan enderMan, Player player, ItemStack itemstack) {
        try {
            return itemstack.isEnderMask(player, enderMan);
        } catch (Exception var4) {
            return false;
        }
    }

    public static int getItemLifeSpawn(ItemEntity itemEntity) {
        return itemEntity.lifespan;
    }

    public static void onItemPickup(Player player, ItemEntity itemEntity, ItemStack copy) {
        ForgeEventFactory.firePlayerItemPickupEvent(player, itemEntity, copy);
    }

    public static CreativeModeTab.Builder searchBar(CreativeModeTab.Builder c) {
        return c.withSearchBar();
    }

    public static float getDownfall(Biome biome) {
        return biome.getModifiedClimateSettings().downfall();
    }

    public static void disableAMWarn() {
        ((ForgeConfigSpec.BooleanValue) ClientConfigs.General.NO_AMENDMENTS_WARN).set(Boolean.valueOf(true));
    }

    public static void disableOFWarn(boolean on) {
        ((ForgeConfigSpec.BooleanValue) ClientConfigs.General.NO_OPTIFINE_WARN).set(Boolean.valueOf(on));
    }
}