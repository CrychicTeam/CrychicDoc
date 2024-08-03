package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.slot_machine;

import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.rendering.Sprite;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;

public abstract class SlotMachineRenderBlock {

    public static final Sprite EMPTY_SPRITE = Sprite.LockedSprite(IconAndButtonUtil.ICON_TEXTURE, 16, 32, 16, 16);

    public final int weight;

    public abstract void render(@Nonnull EasyGuiGraphics var1, int var2, int var3);

    protected SlotMachineRenderBlock(int weight) {
        this.weight = weight;
    }

    public static SlotMachineRenderBlock empty() {
        return SlotMachineRenderBlock.Empty.INSTANCE;
    }

    public static SlotMachineRenderBlock forItem(int weight, ItemStack item) {
        return new SlotMachineRenderBlock.ItemBlock(weight, item);
    }

    private static class Empty extends SlotMachineRenderBlock {

        protected static final SlotMachineRenderBlock INSTANCE = new SlotMachineRenderBlock.Empty();

        private Empty() {
            super(0);
        }

        @Override
        public void render(@Nonnull EasyGuiGraphics gui, int x, int y) {
            gui.blitSprite(EMPTY_SPRITE, x, y);
        }
    }

    private static class ItemBlock extends SlotMachineRenderBlock {

        private final ItemStack item;

        protected ItemBlock(int weight, ItemStack item) {
            super(weight);
            this.item = item.copy();
        }

        @Override
        public void render(@Nonnull EasyGuiGraphics gui, int x, int y) {
            gui.renderItem(this.item, x, y);
        }
    }
}