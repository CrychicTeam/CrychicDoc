package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.LevelStem;
import org.violetmoon.quark.content.tweaks.client.item.ClockTimePropertyFunction;
import org.violetmoon.quark.content.tweaks.client.item.CompassAnglePropertyFunction;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.ItemNBTHelper;

@ZetaLoadModule(category = "tweaks")
public class CompassesWorkEverywhereModule extends ZetaModule {

    @Config
    public static boolean enableCompassNerf = true;

    @Config(flag = "clock_nerf")
    public static boolean enableClockNerf = true;

    @Config
    public static boolean enableNether = true;

    @Config
    public static boolean enableEnd = true;

    @Hint("clock_nerf")
    Item clock = Items.CLOCK;

    public static final String TAG_CLOCK_CALCULATED = "quark:clock_calculated";

    public static final String TAG_COMPASS_CALCULATED = "quark:compass_calculated";

    public static final String TAG_WAS_IN_NETHER = "quark:compass_in_nether";

    public static final String TAG_POSITION_SET = "quark:compass_position_set";

    public static final String TAG_NETHER_TARGET_X = "quark:nether_x";

    public static final String TAG_NETHER_TARGET_Z = "quark:nether_z";

    @PlayEvent
    public void addAdditionalHints(ZGatherHints event) {
        if (enableNether || enableEnd || enableCompassNerf) {
            MutableComponent comp = Component.literal("");
            String pad = "";
            if (enableNether) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.compass_nether"));
                pad = " ";
            }
            if (enableEnd) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.compass_end"));
                pad = " ";
            }
            if (enableCompassNerf) {
                comp = comp.append(pad).append(Component.translatable("quark.jei.hint.compass_nerf"));
            }
            event.accept(Items.COMPASS, comp);
        }
    }

    @PlayEvent
    public void onUpdate(ZPlayerTick.Start event) {
        Inventory inventory = event.getPlayer().getInventory();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() == Items.COMPASS) {
                tickCompass(event.getPlayer(), stack);
            } else if (stack.getItem() == Items.CLOCK) {
                tickClock(stack);
            }
        }
    }

    public static void tickClock(ItemStack stack) {
        boolean calculated = isClockCalculated(stack);
        if (!calculated) {
            ItemNBTHelper.setBoolean(stack, "quark:clock_calculated", true);
        }
    }

    public static boolean isClockCalculated(ItemStack stack) {
        return stack.hasTag() && ItemNBTHelper.getBoolean(stack, "quark:clock_calculated", false);
    }

    public static void tickCompass(Player player, ItemStack stack) {
        boolean calculated = isCompassCalculated(stack);
        boolean nether = player.m_9236_().dimension().location().equals(LevelStem.NETHER.location());
        if (calculated) {
            boolean wasInNether = ItemNBTHelper.getBoolean(stack, "quark:compass_in_nether", false);
            BlockPos pos = player.m_20183_();
            boolean isInPortal = player.m_9236_().getBlockState(pos).m_60734_() == Blocks.NETHER_PORTAL;
            if (nether && !wasInNether && isInPortal) {
                ItemNBTHelper.setInt(stack, "quark:nether_x", pos.m_123341_());
                ItemNBTHelper.setInt(stack, "quark:nether_z", pos.m_123343_());
                ItemNBTHelper.setBoolean(stack, "quark:compass_in_nether", true);
                ItemNBTHelper.setBoolean(stack, "quark:compass_position_set", true);
            } else if (!nether && wasInNether) {
                ItemNBTHelper.setBoolean(stack, "quark:compass_in_nether", false);
                ItemNBTHelper.setBoolean(stack, "quark:compass_position_set", false);
            }
        } else {
            ItemNBTHelper.setBoolean(stack, "quark:compass_calculated", true);
            ItemNBTHelper.setBoolean(stack, "quark:compass_in_nether", nether);
        }
    }

    public static boolean isCompassCalculated(ItemStack stack) {
        return stack.hasTag() && ItemNBTHelper.getBoolean(stack, "quark:compass_calculated", false);
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends CompassesWorkEverywhereModule {

        @LoadEvent
        public void clientSetup(ZClientSetup e) {
            e.enqueueWork(() -> {
                if (this.enabled) {
                    if (enableCompassNerf || enableNether || enableEnd) {
                        ItemProperties.register(Items.COMPASS, new ResourceLocation("angle"), new CompassAnglePropertyFunction());
                    }
                    if (enableClockNerf) {
                        ItemProperties.register(Items.CLOCK, new ResourceLocation("time"), new ClockTimePropertyFunction());
                    }
                }
            });
        }
    }
}