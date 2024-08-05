package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class AllIcons implements ScreenElement {

    public static final ResourceLocation ICON_ATLAS = Create.asResource("textures/gui/icons.png");

    public static final int ICON_ATLAS_SIZE = 256;

    private static int x = 0;

    private static int y = -1;

    private int iconX;

    private int iconY;

    public static final AllIcons I_ADD = newRow();

    public static final AllIcons I_TRASH = next();

    public static final AllIcons I_3x3 = next();

    public static final AllIcons I_TARGET = next();

    public static final AllIcons I_PRIORITY_VERY_LOW = next();

    public static final AllIcons I_PRIORITY_LOW = next();

    public static final AllIcons I_PRIORITY_HIGH = next();

    public static final AllIcons I_PRIORITY_VERY_HIGH = next();

    public static final AllIcons I_BLACKLIST = next();

    public static final AllIcons I_WHITELIST = next();

    public static final AllIcons I_WHITELIST_OR = next();

    public static final AllIcons I_WHITELIST_AND = next();

    public static final AllIcons I_WHITELIST_NOT = next();

    public static final AllIcons I_RESPECT_NBT = next();

    public static final AllIcons I_IGNORE_NBT = next();

    public static final AllIcons I_CONFIRM = newRow();

    public static final AllIcons I_NONE = next();

    public static final AllIcons I_OPEN_FOLDER = next();

    public static final AllIcons I_REFRESH = next();

    public static final AllIcons I_ACTIVE = next();

    public static final AllIcons I_PASSIVE = next();

    public static final AllIcons I_ROTATE_PLACE = next();

    public static final AllIcons I_ROTATE_PLACE_RETURNED = next();

    public static final AllIcons I_ROTATE_NEVER_PLACE = next();

    public static final AllIcons I_MOVE_PLACE = next();

    public static final AllIcons I_MOVE_PLACE_RETURNED = next();

    public static final AllIcons I_MOVE_NEVER_PLACE = next();

    public static final AllIcons I_CART_ROTATE = next();

    public static final AllIcons I_CART_ROTATE_PAUSED = next();

    public static final AllIcons I_CART_ROTATE_LOCKED = next();

    public static final AllIcons I_DONT_REPLACE = newRow();

    public static final AllIcons I_REPLACE_SOLID = next();

    public static final AllIcons I_REPLACE_ANY = next();

    public static final AllIcons I_REPLACE_EMPTY = next();

    public static final AllIcons I_CENTERED = next();

    public static final AllIcons I_ATTACHED = next();

    public static final AllIcons I_INSERTED = next();

    public static final AllIcons I_FILL = next();

    public static final AllIcons I_PLACE = next();

    public static final AllIcons I_REPLACE = next();

    public static final AllIcons I_CLEAR = next();

    public static final AllIcons I_OVERLAY = next();

    public static final AllIcons I_FLATTEN = next();

    public static final AllIcons I_LMB = next();

    public static final AllIcons I_SCROLL = next();

    public static final AllIcons I_RMB = next();

    public static final AllIcons I_TOOL_DEPLOY = newRow();

    public static final AllIcons I_SKIP_MISSING = next();

    public static final AllIcons I_SKIP_BLOCK_ENTITIES = next();

    public static final AllIcons I_DICE = next();

    public static final AllIcons I_TUNNEL_SPLIT = next();

    public static final AllIcons I_TUNNEL_FORCED_SPLIT = next();

    public static final AllIcons I_TUNNEL_ROUND_ROBIN = next();

    public static final AllIcons I_TUNNEL_FORCED_ROUND_ROBIN = next();

    public static final AllIcons I_TUNNEL_PREFER_NEAREST = next();

    public static final AllIcons I_TUNNEL_RANDOMIZE = next();

    public static final AllIcons I_TUNNEL_SYNCHRONIZE = next();

    public static final AllIcons I_TOOLBOX = next();

    public static final AllIcons I_VIEW_SCHEDULE = next();

    public static final AllIcons I_TOOL_MOVE_XZ = newRow();

    public static final AllIcons I_TOOL_MOVE_Y = next();

    public static final AllIcons I_TOOL_ROTATE = next();

    public static final AllIcons I_TOOL_MIRROR = next();

    public static final AllIcons I_ARM_ROUND_ROBIN = next();

    public static final AllIcons I_ARM_FORCED_ROUND_ROBIN = next();

    public static final AllIcons I_ARM_PREFER_FIRST = next();

    public static final AllIcons I_ADD_INVERTED_ATTRIBUTE = next();

    public static final AllIcons I_FLIP = next();

    public static final AllIcons I_ROLLER_PAVE = next();

    public static final AllIcons I_ROLLER_FILL = next();

    public static final AllIcons I_ROLLER_WIDE_FILL = next();

    public static final AllIcons I_PLAY = newRow();

    public static final AllIcons I_PAUSE = next();

    public static final AllIcons I_STOP = next();

    public static final AllIcons I_PLACEMENT_SETTINGS = next();

    public static final AllIcons I_ROTATE_CCW = next();

    public static final AllIcons I_HOUR_HAND_FIRST = next();

    public static final AllIcons I_MINUTE_HAND_FIRST = next();

    public static final AllIcons I_HOUR_HAND_FIRST_24 = next();

    public static final AllIcons I_PATTERN_SOLID = newRow();

    public static final AllIcons I_PATTERN_CHECKERED = next();

    public static final AllIcons I_PATTERN_CHECKERED_INVERSED = next();

    public static final AllIcons I_PATTERN_CHANCE_25 = next();

    public static final AllIcons I_PATTERN_CHANCE_50 = newRow();

    public static final AllIcons I_PATTERN_CHANCE_75 = next();

    public static final AllIcons I_FOLLOW_DIAGONAL = next();

    public static final AllIcons I_FOLLOW_MATERIAL = next();

    public static final AllIcons I_CLEAR_CHECKED = next();

    public static final AllIcons I_SCHEMATIC = newRow();

    public static final AllIcons I_SEQ_REPEAT = next();

    public static final AllIcons VALUE_BOX_HOVER_6PX = next();

    public static final AllIcons VALUE_BOX_HOVER_4PX = next();

    public static final AllIcons VALUE_BOX_HOVER_8PX = next();

    public static final AllIcons I_MTD_LEFT = newRow();

    public static final AllIcons I_MTD_CLOSE = next();

    public static final AllIcons I_MTD_RIGHT = next();

    public static final AllIcons I_MTD_SCAN = next();

    public static final AllIcons I_MTD_REPLAY = next();

    public static final AllIcons I_MTD_USER_MODE = next();

    public static final AllIcons I_MTD_SLOW_MODE = next();

    public static final AllIcons I_CONFIG_UNLOCKED = newRow();

    public static final AllIcons I_CONFIG_LOCKED = next();

    public static final AllIcons I_CONFIG_DISCARD = next();

    public static final AllIcons I_CONFIG_SAVE = next();

    public static final AllIcons I_CONFIG_RESET = next();

    public static final AllIcons I_CONFIG_BACK = next();

    public static final AllIcons I_CONFIG_PREV = next();

    public static final AllIcons I_CONFIG_NEXT = next();

    public static final AllIcons I_DISABLE = next();

    public static final AllIcons I_CONFIG_OPEN = next();

    public static final AllIcons I_FX_SURFACE_OFF = newRow();

    public static final AllIcons I_FX_SURFACE_ON = next();

    public static final AllIcons I_FX_FIELD_OFF = next();

    public static final AllIcons I_FX_FIELD_ON = next();

    public static final AllIcons I_FX_BLEND = next();

    public static final AllIcons I_FX_BLEND_OFF = next();

    public AllIcons(int x, int y) {
        this.iconX = x * 16;
        this.iconY = y * 16;
    }

    private static AllIcons next() {
        return new AllIcons(++x, y);
    }

    private static AllIcons newRow() {
        x = 0;
        return new AllIcons(0, ++y);
    }

    @OnlyIn(Dist.CLIENT)
    public void bind() {
        RenderSystem.setShaderTexture(0, ICON_ATLAS);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        graphics.blit(ICON_ATLAS, x, y, 0, (float) this.iconX, (float) this.iconY, 16, 16, 256, 256);
    }

    @OnlyIn(Dist.CLIENT)
    public void render(PoseStack ms, MultiBufferSource buffer, int color) {
        VertexConsumer builder = buffer.getBuffer(RenderType.text(ICON_ATLAS));
        Matrix4f matrix = ms.last().pose();
        Color rgb = new Color(color);
        int light = 15728880;
        Vec3 vec1 = new Vec3(0.0, 0.0, 0.0);
        Vec3 vec2 = new Vec3(0.0, 1.0, 0.0);
        Vec3 vec3 = new Vec3(1.0, 1.0, 0.0);
        Vec3 vec4 = new Vec3(1.0, 0.0, 0.0);
        float u1 = (float) this.iconX * 1.0F / 256.0F;
        float u2 = (float) (this.iconX + 16) * 1.0F / 256.0F;
        float v1 = (float) this.iconY * 1.0F / 256.0F;
        float v2 = (float) (this.iconY + 16) * 1.0F / 256.0F;
        this.vertex(builder, matrix, vec1, rgb, u1, v1, light);
        this.vertex(builder, matrix, vec2, rgb, u1, v2, light);
        this.vertex(builder, matrix, vec3, rgb, u2, v2, light);
        this.vertex(builder, matrix, vec4, rgb, u2, v1, light);
    }

    @OnlyIn(Dist.CLIENT)
    private void vertex(VertexConsumer builder, Matrix4f matrix, Vec3 vec, Color rgb, float u, float v, int light) {
        builder.vertex(matrix, (float) vec.x, (float) vec.y, (float) vec.z).color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), 255).uv(u, v).uv2(light).endVertex();
    }

    @OnlyIn(Dist.CLIENT)
    public DelegatedStencilElement asStencil() {
        return new DelegatedStencilElement().<DelegatedStencilElement>withStencilRenderer((ms, w, h, alpha) -> this.render(ms, 0, 0)).withBounds(16, 16);
    }
}