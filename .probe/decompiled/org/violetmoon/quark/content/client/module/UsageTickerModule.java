package org.violetmoon.quark.content.client.module;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.MinecraftForge;
import org.violetmoon.quark.api.IUsageTickerOverride;
import org.violetmoon.quark.api.event.UsageTickerEvent;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZPhase;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "client")
public class UsageTickerModule extends ZetaModule {

    @Config(description = "Switch the armor display to the off hand side and the hand display to the main hand side")
    public static boolean invert = false;

    @Config
    public static int shiftLeft = 0;

    @Config
    public static int shiftRight = 0;

    @Config
    public static boolean enableMainHand = true;

    @Config
    public static boolean enableOffHand = true;

    @Config
    public static boolean enableArmor = true;

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends UsageTickerModule {

        public static List<UsageTickerModule.Client.TickerElement> elements = new ArrayList();

        @LoadEvent
        public final void configChanged(ZConfigChanged event) {
            elements = new ArrayList();
            if (enableMainHand) {
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.MAINHAND));
            }
            if (enableOffHand) {
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.OFFHAND));
            }
            if (enableArmor) {
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.HEAD));
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.CHEST));
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.LEGS));
                elements.add(new UsageTickerModule.Client.TickerElement(EquipmentSlot.FEET));
            }
        }

        @PlayEvent
        public void clientTick(ZClientTick event) {
            if (event.getPhase() == ZPhase.START) {
                Minecraft mc = Minecraft.getInstance();
                if (mc.player != null && mc.level != null) {
                    for (UsageTickerModule.Client.TickerElement ticker : elements) {
                        if (ticker != null) {
                            ticker.tick(mc.player);
                        }
                    }
                }
            }
        }

        @PlayEvent
        public void renderHUD(ZRenderGuiOverlay.Hotbar.Post event) {
            Window window = event.getWindow();
            Player player = Minecraft.getInstance().player;
            float partial = event.getPartialTick();
            GuiGraphics guiGraphics = event.getGuiGraphics();
            for (UsageTickerModule.Client.TickerElement ticker : elements) {
                if (ticker != null) {
                    ticker.render(guiGraphics, window, player, invert, partial);
                }
            }
        }

        public static class TickerElement {

            private static final int MAX_TIME = 60;

            private static final int ANIM_TIME = 5;

            public int liveTicks;

            public final EquipmentSlot slot;

            public ItemStack currStack = ItemStack.EMPTY;

            public ItemStack currRealStack = ItemStack.EMPTY;

            public int currCount;

            public TickerElement(EquipmentSlot slot) {
                this.slot = slot;
            }

            public void tick(Player player) {
                ItemStack realStack = this.getStack(player);
                int count = this.getStackCount(player, realStack, realStack, false);
                ItemStack displayedStack = this.getLogicalStack(realStack, count, player, false);
                if (displayedStack.isEmpty()) {
                    this.liveTicks = 0;
                } else if (this.shouldChange(realStack, this.currRealStack, count, this.currCount) || this.shouldChange(displayedStack, this.currStack, count, this.currCount)) {
                    boolean done = this.liveTicks == 0;
                    boolean animatingIn = this.liveTicks > 55;
                    boolean animatingOut = this.liveTicks < 5 && !done;
                    if (animatingOut) {
                        this.liveTicks = 60 - this.liveTicks;
                    } else if (!animatingIn) {
                        if (!done) {
                            this.liveTicks = 55;
                        } else {
                            this.liveTicks = 60;
                        }
                    }
                } else if (this.liveTicks > 0) {
                    this.liveTicks--;
                }
                this.currCount = count;
                this.currStack = displayedStack;
                this.currRealStack = realStack;
            }

            public void render(GuiGraphics guiGraphics, Window window, Player player, boolean invert, float partialTicks) {
                if (this.liveTicks > 0) {
                    float animProgress;
                    if (this.liveTicks < 5) {
                        animProgress = Math.max(0.0F, (float) this.liveTicks - partialTicks) / 5.0F;
                    } else {
                        animProgress = Math.min(5.0F, (float) (60 - this.liveTicks) + partialTicks) / 5.0F;
                    }
                    float anim = -animProgress * (animProgress - 2.0F) * 20.0F;
                    float x = (float) window.getGuiScaledWidth() / 2.0F;
                    float y = (float) window.getGuiScaledHeight() - anim;
                    int barWidth = 190;
                    boolean armor = this.slot.getType() == EquipmentSlot.Type.ARMOR;
                    HumanoidArm primary = player.getMainArm();
                    HumanoidArm ourSide = armor != invert ? primary : primary.getOpposite();
                    int slots = armor ? 4 : 2;
                    int index = slots - this.slot.getIndex() - 1;
                    float mul = ourSide == HumanoidArm.LEFT ? -1.0F : 1.0F;
                    if (ourSide != primary && !player.m_21120_(InteractionHand.OFF_HAND).isEmpty()) {
                        barWidth += 58;
                    }
                    x += (float) barWidth / 2.0F * mul + (float) (index * 20);
                    if (ourSide == HumanoidArm.LEFT) {
                        x -= (float) (slots * 20);
                        x += (float) UsageTickerModule.shiftLeft;
                    } else {
                        x += (float) UsageTickerModule.shiftRight;
                    }
                    ItemStack stack = this.getRenderedStack(player);
                    guiGraphics.renderItem(stack, (int) x, (int) y);
                    guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, (int) x, (int) y);
                }
            }

            public boolean shouldChange(ItemStack currStack, ItemStack prevStack, int currentTotal, int pastTotal) {
                return currStack.getItem() != prevStack.getItem() || currStack.isDamageableItem() && currStack.getDamageValue() != prevStack.getDamageValue() || currentTotal != pastTotal;
            }

            public ItemStack getStack(Player player) {
                return player.getItemBySlot(this.slot);
            }

            public ItemStack getLogicalStack(ItemStack stack, int count, Player player, boolean renderPass) {
                boolean verifySize = true;
                ItemStack returnStack = stack;
                boolean logicLock = false;
                if (stack.getItem() instanceof IUsageTickerOverride over) {
                    stack = over.getUsageTickerItem(stack);
                    returnStack = stack;
                    verifySize = over.shouldUsageTickerCheckMatchSize(this.currStack);
                } else if (isProjectileWeapon(stack)) {
                    returnStack = player.getProjectile(stack);
                    logicLock = true;
                }
                if (!logicLock) {
                    if (!stack.isStackable() && this.slot.getType() == EquipmentSlot.Type.HAND) {
                        returnStack = ItemStack.EMPTY;
                    } else if (verifySize && stack.isStackable() && count == stack.getCount()) {
                        returnStack = ItemStack.EMPTY;
                    }
                }
                UsageTickerEvent.GetStack event = new UsageTickerEvent.GetStack(this.slot, returnStack, stack, count, renderPass, player);
                MinecraftForge.EVENT_BUS.post(event);
                return event.isCanceled() ? ItemStack.EMPTY : event.getResultStack();
            }

            public int getStackCount(Player player, ItemStack displayStack, ItemStack original, boolean renderPass) {
                int val = 1;
                if (displayStack.isStackable()) {
                    Predicate<ItemStack> predicate = stackAtx -> ItemStack.isSameItemSameTags(stackAtx, displayStack);
                    int total = 0;
                    Inventory inventory = player.getInventory();
                    for (int i = 0; i < inventory.getContainerSize(); i++) {
                        ItemStack stackAt = inventory.getItem(i);
                        if (predicate.test(stackAt)) {
                            total += stackAt.getCount();
                        } else if (stackAt.getItem() instanceof IUsageTickerOverride over) {
                            total += over.getUsageTickerCountForItem(stackAt, predicate);
                        }
                    }
                    val = Math.max(total, displayStack.getCount());
                }
                UsageTickerEvent.GetCount event = new UsageTickerEvent.GetCount(this.slot, displayStack, original, val, renderPass, player);
                MinecraftForge.EVENT_BUS.post(event);
                return event.isCanceled() ? 0 : event.getResultCount();
            }

            private static boolean isProjectileWeapon(ItemStack stack) {
                return stack.getItem() instanceof ProjectileWeaponItem && Quark.ZETA.itemExtensions.get(stack).getEnchantmentLevelZeta(stack, Enchantments.INFINITY_ARROWS) == 0;
            }

            public ItemStack getRenderedStack(Player player) {
                ItemStack stack = this.getStack(player);
                int count = this.getStackCount(player, stack, stack, true);
                ItemStack logicalStack = this.getLogicalStack(stack, count, player, true).copy();
                if (logicalStack != stack) {
                    count = this.getStackCount(player, logicalStack, stack, true);
                }
                logicalStack.setCount(count);
                return logicalStack.isEmpty() ? ItemStack.EMPTY : logicalStack;
            }
        }
    }
}