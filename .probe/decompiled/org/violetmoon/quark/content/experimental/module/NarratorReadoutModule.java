package org.violetmoon.quark.content.experimental.module;

import com.mojang.text2speech.Narrator;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.play.ZInput;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "experimental", enabledByDefault = false)
public class NarratorReadoutModule extends ZetaModule {

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends NarratorReadoutModule {

        private KeyMapping keybind;

        private KeyMapping keybindFull;

        private float last;

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            this.keybind = event.init("quark.keybind.narrator_readout", null, "quark.gui.keygroup.misc");
            this.keybindFull = event.init("quark.keybind.narrator_full_readout", null, "quark.gui.keygroup.misc");
        }

        @PlayEvent
        public void onMouseInput(ZInput.MouseButton event) {
            boolean down = this.isDown(event.getButton(), 0, true, this.keybind);
            boolean full = this.isDown(event.getButton(), 0, true, this.keybindFull);
            this.acceptInput(down || full, down);
        }

        @PlayEvent
        public void onKeyInput(ZInput.Key event) {
            boolean down = this.isDown(event.getKey(), event.getScanCode(), false, this.keybind);
            boolean full = this.isDown(event.getKey(), event.getScanCode(), false, this.keybindFull);
            this.acceptInput(down || full, down);
        }

        private boolean isDown(int key, int scancode, boolean mouse, KeyMapping keybind) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen == null) {
                return keybind.isDown();
            } else {
                return mouse ? keybind.matchesMouse(key) && (keybind.getKeyModifier() == KeyModifier.NONE || keybind.getKeyModifier().isActive(KeyConflictContext.GUI)) : keybind.matches(key, scancode) && (keybind.getKeyModifier() == KeyModifier.NONE || keybind.getKeyModifier().isActive(KeyConflictContext.GUI));
            }
        }

        private void acceptInput(boolean down, boolean full) {
            Minecraft mc = Minecraft.getInstance();
            float curr = QuarkClient.ticker.total;
            if (down && curr - this.last > 10.0F) {
                Narrator narrator = Narrator.getNarrator();
                String readout = this.getReadout(mc, full);
                if (readout != null) {
                    narrator.say(readout, true);
                    this.last = curr;
                }
            }
        }

        private String getReadout(Minecraft mc, boolean full) {
            Player player = mc.player;
            if (player == null) {
                return I18n.get("quark.readout.not_ingame");
            } else {
                StringBuilder sb = new StringBuilder();
                if (mc.screen == null) {
                    HitResult ray = mc.hitResult;
                    if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
                        BlockPos pos = ((BlockHitResult) ray).getBlockPos();
                        BlockState state = mc.level.m_8055_(pos);
                        Item item = state.m_60734_().asItem();
                        if (item != null) {
                            sb.append(I18n.get("quark.readout.looking", item.getName(new ItemStack(item)).getString()));
                            if (full) {
                                sb.append(", ");
                            }
                        }
                        if (state.m_60734_() instanceof SignBlock) {
                            SignBlockEntity tile = (SignBlockEntity) mc.level.m_7702_(pos);
                            sb.append(I18n.get("quark.readout.sign_says"));
                            for (Component cmp : tile.getFrontText().getMessages(false)) {
                                String msg = cmp.getString().trim();
                                if (!msg.isEmpty()) {
                                    sb.append(cmp.getString());
                                    sb.append(" ");
                                }
                            }
                            sb.append(". ");
                            sb.append(I18n.get("quark.readout.sign_says"));
                            for (Component cmpx : tile.getBackText().getMessages(false)) {
                                String msg = cmpx.getString().trim();
                                if (!msg.isEmpty()) {
                                    sb.append(cmpx.getString());
                                    sb.append(" ");
                                }
                            }
                            sb.append(". ");
                        }
                    }
                    if (full) {
                        ItemStack stack = player.m_21205_();
                        ItemStack stack2 = player.m_21206_();
                        if (stack.isEmpty()) {
                            stack = stack2;
                            stack2 = ItemStack.EMPTY;
                        }
                        if (!stack.isEmpty()) {
                            if (!stack2.isEmpty()) {
                                sb.append(I18n.get("quark.readout.holding_with_off", stack.getCount(), stack.getHoverName().getString(), stack2.getCount(), stack2.getHoverName().getString()));
                            } else {
                                sb.append(I18n.get("quark.readout.holding", stack.getCount(), stack.getHoverName().getString()));
                            }
                            sb.append(", ");
                        }
                        sb.append(I18n.get("quark.readout.health", (int) mc.player.m_21223_()));
                        sb.append(", ");
                        sb.append(I18n.get("quark.readout.food", mc.player.m_36324_().getFoodLevel()));
                    }
                } else if (mc.screen instanceof AbstractContainerScreen<?> cnt) {
                    Slot slot = cnt.getSlotUnderMouse();
                    ItemStack stackx = slot == null ? ItemStack.EMPTY : slot.getItem();
                    if (stackx.isEmpty()) {
                        sb.append(I18n.get("quark.readout.no_item"));
                    } else {
                        for (Component t : Screen.getTooltipFromItem(mc, stackx)) {
                            Component print = t.copy();
                            List<Component> bros = print.getSiblings();
                            for (Component sib : bros) {
                                if (sib instanceof MutableComponent mut && mut.getContents() instanceof TranslatableContents ttc && ttc.getKey().contains("enchantment.level.")) {
                                    bros.set(bros.indexOf(sib), Component.translatable(ttc.getKey().substring("enchantment.level.".length())));
                                    break;
                                }
                            }
                            sb.append(print.getString());
                            if (!full) {
                                break;
                            }
                            sb.append(", ");
                        }
                    }
                } else {
                    sb.append(mc.screen.getNarrationMessage());
                }
                return sb.toString();
            }
        }
    }
}