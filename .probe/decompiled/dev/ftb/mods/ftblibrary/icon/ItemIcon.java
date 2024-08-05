package dev.ftb.mods.ftblibrary.icon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class ItemIcon extends Icon implements IResourceIcon {

    private final ItemStack stack;

    public static Icon getItemIcon(ItemStack stack) {
        if (stack.isEmpty()) {
            return empty();
        } else {
            return (Icon) (stack.getItem() instanceof CustomIconItem ? ((CustomIconItem) stack.getItem()).getCustomIcon(stack) : new ItemIcon(stack));
        }
    }

    public static Icon getItemIcon(Item item) {
        return (Icon) (item == Items.AIR ? empty() : getItemIcon(item.getDefaultInstance()));
    }

    public static Icon getItemIcon(String lazyStackString) {
        return (Icon) (lazyStackString.isEmpty() ? empty() : new LazyIcon(() -> {
            String[] s = lazyStackString.split(" ", 4);
            ItemStack stack = BuiltInRegistries.ITEM.get(new ResourceLocation(s[0])).getDefaultInstance();
            if (s.length >= 2 && !s[1].equals("1")) {
                stack.setCount(Integer.parseInt(s[1]));
            }
            if (s.length >= 3 && !s[2].equals("0")) {
                stack.setDamageValue(Integer.parseInt(s[2]));
            }
            if (s.length >= 4 && !s[3].equals("null")) {
                try {
                    stack.setTag(TagParser.parseTag(s[3]));
                } catch (CommandSyntaxException var4) {
                    var4.printStackTrace();
                }
            }
            if (stack.isEmpty()) {
                stack = new ItemStack(Items.BARRIER);
                stack.setHoverName(Component.literal(lazyStackString));
            }
            return getItemIcon(stack);
        }) {

            @Override
            public String toString() {
                return "item:" + lazyStackString;
            }
        });
    }

    private ItemIcon(ItemStack is) {
        this.stack = is;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(GuiGraphics graphics, int x, int y, int w, int h) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((double) x + (double) w / 2.0, (double) y + (double) h / 2.0, 100.0);
        if (w != 16 || h != 16) {
            int s = Math.min(w, h);
            poseStack.scale((float) s / 16.0F, (float) s / 16.0F, (float) s / 16.0F);
        }
        GuiHelper.drawItem(graphics, this.getStack(), 0, true, null);
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawStatic(GuiGraphics graphics, int x, int y, int w, int h) {
        PoseStack poseStack = graphics.pose();
        poseStack.pushPose();
        poseStack.translate((double) x + (double) w / 2.0, (double) y + (double) h / 2.0, 100.0);
        if (w != 16 || h != 16) {
            int s = Math.min(w, h);
            poseStack.scale((float) s / 16.0F, (float) s / 16.0F, (float) s / 16.0F);
        }
        GuiHelper.drawItem(graphics, this.getStack(), 0, false, null);
        poseStack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    public static void drawItem3D(GuiGraphics graphics, ItemStack stack) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.FIXED, 240, OverlayTexture.NO_OVERLAY, graphics.pose(), Minecraft.getInstance().renderBuffers().bufferSource(), Minecraft.getInstance().level, 0);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw3D(GuiGraphics graphics) {
        drawItem3D(graphics, this.getStack());
    }

    public String toString() {
        ItemStack is = this.getStack();
        StringBuilder builder = new StringBuilder("item:");
        builder.append(RegistrarManager.getId(is.getItem(), Registries.ITEM));
        int count = is.getCount();
        int damage = is.getDamageValue();
        CompoundTag nbt = is.getTag();
        if (count > 1 || damage > 0 || nbt != null) {
            builder.append(' ');
            builder.append(count);
        }
        if (damage > 0 || nbt != null) {
            builder.append(' ');
            builder.append(damage);
        }
        if (nbt != null) {
            builder.append(' ');
            builder.append(nbt);
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        ItemStack stack = this.getStack();
        int h = stack.getItem().hashCode();
        h = h * 31 + stack.getCount();
        return h * 31 + Objects.hashCode(stack.getTag());
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof ItemIcon && ItemStack.matches(this.getStack(), ((ItemIcon) o).getStack());
    }

    @Nullable
    @Override
    public Object getIngredient() {
        return this.getStack();
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return BuiltInRegistries.ITEM.getKey(this.stack.getItem());
    }
}