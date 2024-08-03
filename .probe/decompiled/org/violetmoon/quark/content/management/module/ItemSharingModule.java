package org.violetmoon.quark.content.management.module;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.ShareItemC2SMessage;
import org.violetmoon.zeta.client.event.play.ZScreen;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "management")
public class ItemSharingModule extends ZetaModule {

    @Config(description = "In ticks.")
    @Config.Min(0.0)
    private static int cooldown = 100;

    @Config
    public static boolean renderItemsInChat = true;

    private static final Object2IntMap<UUID> lastSendTimes = new Object2IntOpenHashMap();

    public static boolean canShare(UUID sender, MinecraftServer server) {
        if (!Quark.ZETA.modules.get(ItemSharingModule.class).enabled) {
            return false;
        } else {
            int now = server.getTickCount();
            int lastSend = lastSendTimes.getOrDefault(sender, -cooldown);
            if (now - lastSend >= cooldown) {
                lastSendTimes.put(sender, now);
                return true;
            } else {
                return false;
            }
        }
    }

    public static MutableComponent createStackComponent(ItemStack stack) {
        return createStackComponent(stack, (MutableComponent) stack.getDisplayName());
    }

    public static MutableComponent createStackComponent(ItemStack stack, MutableComponent component) {
        if (Quark.ZETA.modules.isEnabled(ItemSharingModule.class) && renderItemsInChat) {
            Style style = component.getStyle();
            if (stack.getCount() > 64) {
                ItemStack copyStack = stack.copy();
                copyStack.setCount(64);
                style = style.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(copyStack)));
                component.withStyle(style);
            }
            MutableComponent out = Component.literal("   ");
            out.setStyle(style);
            return out.append(component);
        } else {
            return component;
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends ItemSharingModule {

        public static float alphaValue = 1.0F;

        private static long lastClientShare = -1L;

        public static boolean requestShare() {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && Screen.hasShiftDown()) {
                if (mc.screen instanceof AbstractContainerScreen<?> gui) {
                    for (GuiEventListener c : gui.m_6702_()) {
                        if (c instanceof EditBox tf && tf.m_93696_()) {
                            return false;
                        }
                    }
                    Slot slot = gui.getSlotUnderMouse();
                    if (slot == null) {
                        return false;
                    } else {
                        ItemStack stack = slot.getItem();
                        if (stack.isEmpty()) {
                            return false;
                        } else if (mc.level.m_46467_() - lastClientShare > (long) ItemSharingModule.cooldown) {
                            lastClientShare = mc.level.m_46467_();
                            QuarkClient.ZETA_CLIENT.sendToServer(new ShareItemC2SMessage(stack));
                            return true;
                        } else {
                            return false;
                        }
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @PlayEvent
        public void onKeyInput(ZScreen.KeyPressed.Pre event) {
            KeyMapping key = this.getChatKey();
            if (key.getKey().getType() == InputConstants.Type.KEYSYM && event.getKeyCode() == key.getKey().getValue()) {
                event.setCanceled(requestShare());
            }
        }

        @PlayEvent
        public void onMouseInput(ZScreen.MouseButtonPressed.Pre event) {
            KeyMapping key = this.getChatKey();
            int btn = event.getButton();
            if (key.getKey().getType() == InputConstants.Type.MOUSE && btn != 0 && btn == key.getKey().getValue()) {
                event.setCanceled(requestShare());
            }
        }

        public static void renderItemForMessage(GuiGraphics guiGraphics, FormattedCharSequence sequence, float x, float y, int color) {
            if (Quark.ZETA.modules.isEnabled(ItemSharingModule.class) && renderItemsInChat) {
                Minecraft mc = Minecraft.getInstance();
                StringBuilder before = new StringBuilder();
                int halfSpace = mc.font.width(" ") / 2;
                sequence.accept((counter_, style, character) -> {
                    String sofar = before.toString();
                    if (sofar.endsWith("  ")) {
                        render(mc, guiGraphics, sofar.substring(0, sofar.length() - 2), character == 32 ? 0.0F : (float) (-halfSpace), x, y, style, color);
                        return false;
                    } else {
                        before.append((char) character);
                        return true;
                    }
                });
            }
        }

        private static void render(Minecraft mc, GuiGraphics guiGraphics, String before, float extraShift, float x, float y, Style style, int color) {
            float a = (float) (color >> 24 & 0xFF) / 255.0F;
            PoseStack pose = guiGraphics.pose();
            HoverEvent hoverEvent = style.getHoverEvent();
            if (hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                HoverEvent.ItemStackInfo contents = hoverEvent.getValue(HoverEvent.Action.SHOW_ITEM);
                ItemStack stack = contents != null ? contents.getItemStack() : ItemStack.EMPTY;
                if (stack.isEmpty()) {
                    stack = new ItemStack(Blocks.BARRIER);
                }
                float shift = (float) mc.font.width(before) + extraShift;
                if (a > 0.0F) {
                    alphaValue = a;
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().mulPoseMatrix(pose.last().pose());
                    guiGraphics.pose().translate(shift + x, y, 0.0F);
                    guiGraphics.pose().scale(0.5F, 0.5F, 0.5F);
                    guiGraphics.renderItem(stack, 0, 0);
                    guiGraphics.pose().popPose();
                    RenderSystem.applyModelViewMatrix();
                    alphaValue = 1.0F;
                }
            }
        }

        private KeyMapping getChatKey() {
            return Minecraft.getInstance().options.keyChat;
        }
    }
}