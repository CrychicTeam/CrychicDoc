package me.fengming.renderjs.core.objects;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import me.fengming.renderjs.core.Utils;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@RemapPrefixForJS("rjs$")
public class ItemsDisplay extends RenderObject {

    protected ItemStack item;

    protected ItemDisplayContext context = ItemDisplayContext.GUI;

    protected boolean leftHand = false;

    protected int light = 15728880;

    public ItemsDisplay(float[] vertices, RenderObject.ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setItem(ItemStack item) {
        this.item = item;
    }

    public void rjs$setItemDisplayContext(ItemDisplayContext context) {
        this.context = context;
    }

    public void rjs$setLeftHand(boolean leftHand) {
        this.leftHand = leftHand;
    }

    public void rjs$setLight(int light) {
        this.light = light;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("item")) {
            this.rjs$setItem(Utils.parseItem(object.getString("item"), object.getInt("count")));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: item");
            this.broken = true;
        }
        if (object.contains("context")) {
            this.rjs$setItemDisplayContext(ItemDisplayContext.valueOf(object.getString("context").toUpperCase()));
        }
        if (object.contains("left_hand")) {
            this.rjs$setLeftHand(object.getBoolean("left_hand"));
        }
        if (object.contains("light")) {
            this.rjs$setLight(object.getInt("light"));
        }
    }

    @Override
    public void renderInner() {
        for (int i = 0; i < this.vertices.length; i += 3) {
            this.poseStack.translate(this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]);
            mc.getItemRenderer().renderStatic(this.item, this.context, this.light, OverlayTexture.NO_OVERLAY, this.poseStack, mc.renderBuffers().bufferSource(), mc.level, i);
        }
    }
}