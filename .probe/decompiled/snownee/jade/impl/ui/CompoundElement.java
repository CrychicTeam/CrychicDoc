package snownee.jade.impl.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.DisplayHelper;

public class CompoundElement extends Element {

    private final ItemStack stack;

    private final float scale;

    private final String text;

    public static final CompoundElement EMPTY = new CompoundElement(ItemStack.EMPTY, 1.0F, null);

    private CompoundElement(ItemStack stack, float scale, @Nullable String text) {
        this.stack = stack;
        this.scale = scale == 0.0F ? 1.0F : scale;
        this.text = text;
    }

    public static CompoundElement of(ItemStack stack) {
        return of(stack, 1.0F);
    }

    public static CompoundElement of(ItemStack stack, float scale) {
        return of(stack, scale, null);
    }

    public static CompoundElement of(ItemStack stack, float scale, @Nullable String text) {
        return scale == 1.0F && stack.isEmpty() ? EMPTY : new CompoundElement(stack, scale, text);
    }

    @Override
    public Vec2 getSize() {
        int size = Mth.floor(18.0F * this.scale);
        return new Vec2((float) size, (float) size);
    }

    @Override
    public void render(GuiGraphics guiGraphics, float x, float y, float maxX, float maxY) {
        if (!this.stack.isEmpty()) {
            DisplayHelper.INSTANCE.drawItem(guiGraphics, x + 1.0F, y + 1.0F, this.stack, this.scale, this.text);
        }
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.stack.isEmpty() ? null : "%s %s".formatted(this.stack.getCount(), this.stack.getHoverName().getString());
    }
}