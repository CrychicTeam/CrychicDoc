package org.violetmoon.quark.addons.oddities.client.render.be;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Calendar;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.TinyPotatoBlockEntity;
import org.violetmoon.quark.addons.oddities.module.TinyPotatoModule;
import org.violetmoon.quark.addons.oddities.util.TinyPotatoInfo;
import org.violetmoon.quark.content.tools.base.RuneColor;
import org.violetmoon.quark.content.tools.module.ColorRunesModule;
import org.violetmoon.quark.mixin.mixins.client.accessor.AccessorModelManager;

public class TinyPotatoRenderer implements BlockEntityRenderer<TinyPotatoBlockEntity> {

    public static final String DEFAULT = "default";

    public static final String HALLOWEEN = "halloween";

    public static final String ANGRY = "angry";

    private static final Pattern ESCAPED = Pattern.compile("[^a-z0-9/._-]");

    private final BlockRenderDispatcher blockRenderDispatcher;

    public static boolean isTheSpookDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1 == 10 && calendar.get(5) == 31;
    }

    public TinyPotatoRenderer(BlockEntityRendererProvider.Context ctx) {
        this.blockRenderDispatcher = ctx.getBlockRenderDispatcher();
    }

    public static BakedModel getModelFromDisplayName(Component displayName, boolean angry) {
        TinyPotatoInfo info = TinyPotatoInfo.fromComponent(displayName);
        return getModel(info.name(), angry);
    }

    private static BakedModel getModel(String name, boolean angry) {
        ModelManager bmm = Minecraft.getInstance().getModelManager();
        Map<ResourceLocation, BakedModel> mm = ((AccessorModelManager) bmm).getBakedRegistry();
        BakedModel missing = bmm.getMissingModel();
        ResourceLocation location = taterLocation(name);
        BakedModel model = (BakedModel) mm.get(location);
        if (model == null) {
            if (isTheSpookDay()) {
                return (BakedModel) mm.getOrDefault(taterLocation("halloween"), missing);
            } else {
                return angry ? (BakedModel) mm.getOrDefault(taterLocation("angry"), missing) : (BakedModel) mm.getOrDefault(taterLocation("default"), missing);
            }
        } else {
            return model;
        }
    }

    private static ResourceLocation taterLocation(String name) {
        return new ResourceLocation("quark", "tiny_potato/" + normalizeName(name));
    }

    private static String normalizeName(String name) {
        return ESCAPED.matcher(name).replaceAll("_");
    }

    public void render(@NotNull TinyPotatoBlockEntity potato, float partialTicks, @NotNull PoseStack ms, @NotNull MultiBufferSource buffers, int light, int overlay) {
        ms.pushPose();
        TinyPotatoInfo info = TinyPotatoInfo.fromComponent(potato.name);
        RenderType layer = Sheets.translucentCullBlockSheet();
        BakedModel model = getModel(info.name(), potato.angry);
        ms.translate(0.5F, 0.0F, 0.5F);
        Direction potatoFacing = (Direction) potato.m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        float rotY = 0.0F;
        switch(potatoFacing) {
            case SOUTH:
            default:
                rotY = 180.0F;
            case NORTH:
                break;
            case EAST:
                rotY = 90.0F;
                break;
            case WEST:
                rotY = 270.0F;
        }
        ms.mulPose(Axis.YN.rotationDegrees(rotY));
        float jump = (float) potato.jumpTicks;
        if (jump > 0.0F) {
            jump -= partialTicks;
        }
        float up = (float) Math.abs(Math.sin((double) (jump / 10.0F) * Math.PI)) * 0.2F;
        float rotZ = (float) Math.sin((double) (jump / 10.0F) * Math.PI) * 2.0F;
        float wiggle = (float) Math.sin((double) (jump / 10.0F) * Math.PI) * 0.05F;
        ms.translate(wiggle, up, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(rotZ));
        boolean render = !info.name().equals("mami") && !info.name().equals("soaryn") && (!info.name().equals("eloraam") || jump == 0.0F);
        if (render) {
            ms.pushPose();
            ms.translate(-0.5F, 0.0F, -0.5F);
            RuneColor runeColor = info.runeColor();
            if (runeColor != null) {
                ColorRunesModule.setTargetColor(runeColor);
            }
            VertexConsumer buffer = ItemRenderer.getFoilBuffer(buffers, layer, true, info.enchanted());
            this.renderModel(ms, buffer, light, overlay, model);
            ms.popPose();
        }
        ms.translate(0.0F, 1.5F, 0.0F);
        ms.pushPose();
        ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
        this.renderItems(potato, potatoFacing, ms, buffers, light, overlay);
        ms.popPose();
        ms.mulPose(Axis.ZP.rotationDegrees(-rotZ));
        ms.mulPose(Axis.YN.rotationDegrees(-rotY));
        this.renderName(potato, info.name(), ms, buffers, light);
        ms.popPose();
    }

    private void renderName(TinyPotatoBlockEntity potato, String name, PoseStack ms, MultiBufferSource buffers, int light) {
        Minecraft mc = Minecraft.getInstance();
        HitResult pos = mc.hitResult;
        if (Minecraft.renderNames() && !name.isEmpty() && pos != null && pos.getType() == HitResult.Type.BLOCK && potato.m_58899_().equals(((BlockHitResult) pos).getBlockPos())) {
            ms.pushPose();
            ms.translate(0.0F, -0.6F, 0.0F);
            ms.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
            float f1 = 0.02666667F;
            ms.scale(-f1, -f1, f1);
            int halfWidth = mc.font.width(potato.name.getString()) / 2;
            float opacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int opacityRGB = (int) (opacity * 255.0F) << 24;
            mc.font.drawInBatch(potato.name, (float) (-halfWidth), 0.0F, 553648127, false, ms.last().pose(), buffers, Font.DisplayMode.SEE_THROUGH, opacityRGB, light);
            mc.font.drawInBatch(potato.name, (float) (-halfWidth), 0.0F, -1, false, ms.last().pose(), buffers, Font.DisplayMode.NORMAL, 0, light);
            if (name.equals("pahimar") || name.equals("soaryn")) {
                ms.translate(0.0F, 14.0F, 0.0F);
                String str = name.equals("pahimar") ? "[WIP]" : "(soon)";
                halfWidth = mc.font.width(str) / 2;
                mc.font.drawInBatch(str, (float) (-halfWidth), 0.0F, 553648127, false, ms.last().pose(), buffers, Font.DisplayMode.SEE_THROUGH, opacityRGB, light);
                mc.font.drawInBatch(str, (float) (-halfWidth), 0.0F, -1, false, ms.last().pose(), buffers, Font.DisplayMode.SEE_THROUGH, 0, light);
            }
            ms.popPose();
        }
    }

    private void renderItems(TinyPotatoBlockEntity potato, Direction facing, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        ms.pushPose();
        ms.mulPose(Axis.ZP.rotationDegrees(180.0F));
        ms.translate(0.0F, -1.0F, 0.0F);
        float s = 0.2857143F;
        ms.scale(s, s, s);
        for (int i = 0; i < potato.getContainerSize(); i++) {
            ItemStack stack = potato.m_8020_(i);
            if (!stack.isEmpty()) {
                ms.pushPose();
                Direction side = Direction.values()[i];
                if (side.getAxis() != Direction.Axis.Y) {
                    float sideAngle = side.toYRot() - facing.toYRot();
                    side = Direction.fromYRot((double) sideAngle);
                }
                boolean block = stack.getItem() instanceof BlockItem;
                boolean mySon = stack.getItem() == TinyPotatoModule.tiny_potato.asItem();
                switch(side) {
                    case SOUTH:
                        ms.translate(0.0F, -1.6F, -0.89F);
                        if (mySon) {
                            ms.translate(0.0F, -0.59F, 0.26F);
                        } else if (block) {
                            ms.translate(0.0F, 1.0F, 0.5F);
                        }
                        break;
                    case NORTH:
                        ms.translate(0.0F, -1.9F, 0.02F);
                        if (mySon) {
                            ms.translate(0.0F, -0.29F, 0.6F);
                        } else if (block) {
                            ms.translate(0.0F, 1.0F, 0.6F);
                        }
                        break;
                    case EAST:
                        if (mySon) {
                            ms.translate(-0.35F, -0.29F, -0.06F);
                        } else if (block) {
                            ms.translate(-0.4F, 0.8F, 0.0F);
                        } else {
                            ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
                        }
                        ms.translate(-0.3F, -1.9F, 0.04F);
                        break;
                    case WEST:
                        if (mySon) {
                            ms.translate(0.95F, -0.29F, 0.9F);
                            if (stack.hasCustomHoverName()) {
                                TinyPotatoInfo info = TinyPotatoInfo.fromComponent(stack.getHoverName());
                                if (info.name().equals("kingdaddydmac")) {
                                    ms.translate(0.55F, 0.0F, 0.0F);
                                }
                            }
                        } else if (block) {
                            ms.translate(1.0F, 0.8F, 1.0F);
                        } else {
                            ms.mulPose(Axis.YP.rotationDegrees(-90.0F));
                        }
                        ms.translate(-0.3F, -1.9F, -0.92F);
                        break;
                    case UP:
                        if (mySon) {
                            ms.translate(0.0F, -0.375F, 0.5F);
                        } else if (block) {
                            ms.translate(0.0F, 0.3F, 0.5F);
                        }
                        ms.translate(0.0F, -0.5F, -0.4F);
                        break;
                    case DOWN:
                        ms.translate(0.0F, -2.3F, -0.88F);
                        if (mySon) {
                            ms.translate(0.0F, 1.25F, 0.5F);
                        } else if (block) {
                            ms.translate(0.0F, 1.0F, 0.6F);
                        }
                }
                if (mySon) {
                    ms.scale(1.1F, 1.1F, 1.1F);
                } else if (block) {
                    ms.scale(0.5F, 0.5F, 0.5F);
                }
                if (block && side == Direction.NORTH) {
                    ms.mulPose(Axis.YP.rotationDegrees(180.0F));
                }
                this.renderItem(ms, buffers, light, overlay, stack);
                ms.popPose();
            }
        }
        ms.popPose();
    }

    private void renderModel(PoseStack ms, VertexConsumer buffer, int light, int overlay, BakedModel model) {
        this.blockRenderDispatcher.getModelRenderer().renderModel(ms.last(), buffer, null, model, 1.0F, 1.0F, 1.0F, light, overlay);
    }

    private void renderItem(PoseStack ms, MultiBufferSource buffers, int light, int overlay, ItemStack stack) {
        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.HEAD, light, overlay, ms, buffers, Minecraft.getInstance().level, 0);
    }
}