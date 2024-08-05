package fuzs.puzzleslib.api.client.screen.v2;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public final class TooltipRenderHelper {

    private TooltipRenderHelper() {
    }

    public static List<Component> getTooltipLines(ItemStack itemStack) {
        return getTooltipLines(itemStack, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_);
    }

    public static List<Component> getTooltipLines(ItemStack itemStack, TooltipFlag tooltipFlag) {
        Objects.requireNonNull(itemStack, "item stack is null");
        Objects.requireNonNull(tooltipFlag, "tooltip flag is null");
        return itemStack.getTooltipLines(Minecraft.getInstance().player, tooltipFlag);
    }

    public static List<ClientTooltipComponent> getTooltip(ItemStack itemStack) {
        return getTooltip(itemStack, Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.f_256730_ : TooltipFlag.Default.f_256752_);
    }

    public static List<ClientTooltipComponent> getTooltip(ItemStack itemStack, TooltipFlag tooltipFlag) {
        Objects.requireNonNull(itemStack, "item stack is null");
        Objects.requireNonNull(tooltipFlag, "tooltip flag is null");
        List<Component> components = getTooltipLines(itemStack, tooltipFlag);
        List<TooltipComponent> imageComponents = (List<TooltipComponent>) itemStack.getTooltipImage().map(List::of).orElse(List.of());
        return createClientComponents(components, imageComponents);
    }

    public static void renderTooltip(GuiGraphics guiGraphics, int posX, int posY, ItemStack itemStack) {
        Objects.requireNonNull(itemStack, "item stack is null");
        renderTooltipInternal(guiGraphics, posX, posY, getTooltip(itemStack));
    }

    public static void renderTooltip(GuiGraphics guiGraphics, int posX, int posY, Component component, TooltipComponent imageComponent) {
        Objects.requireNonNull(component, "component is null");
        Objects.requireNonNull(imageComponent, "image component is null");
        renderTooltip(guiGraphics, posX, posY, List.of(component), imageComponent);
    }

    public static void renderTooltip(GuiGraphics guiGraphics, int posX, int posY, List<Component> components, TooltipComponent imageComponent) {
        Objects.requireNonNull(imageComponent, "image component is null");
        renderTooltip(guiGraphics, posX, posY, components, List.of(imageComponent));
    }

    public static void renderTooltip(GuiGraphics guiGraphics, int posX, int posY, List<Component> components) {
        renderTooltip(guiGraphics, posX, posY, components, List.of());
    }

    public static void renderTooltip(GuiGraphics guiGraphics, int posX, int posY, List<Component> components, List<TooltipComponent> imageComponents) {
        renderTooltipInternal(guiGraphics, posX, posY, createClientComponents(components, imageComponents));
    }

    public static List<ClientTooltipComponent> createClientComponents(List<Component> components, List<TooltipComponent> imageComponents) {
        return createClientComponents(components, imageComponents, 1);
    }

    public static List<ClientTooltipComponent> createClientComponents(List<Component> components, List<TooltipComponent> imageComponents, int insertAt) {
        List<ClientTooltipComponent> clientComponents = (List<ClientTooltipComponent>) components.stream().map(Component::m_7532_).map(ClientTooltipComponent::m_169948_).collect(Collectors.toList());
        List<ClientTooltipComponent> clientImageComponents = imageComponents.stream().map(ClientAbstractions.INSTANCE::createImageComponent).toList();
        if (insertAt == -1) {
            clientComponents.addAll(clientImageComponents);
        } else {
            clientComponents.addAll(insertAt, clientImageComponents);
        }
        return ImmutableList.copyOf(clientComponents);
    }

    public static void renderTooltipInternal(GuiGraphics guiGraphics, int posX, int posY, List<ClientTooltipComponent> components) {
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            for (ClientTooltipComponent component : components) {
                int k = component.getWidth(font);
                if (k > i) {
                    i = k;
                }
                j += component.getHeight();
            }
            int l = posX + 12;
            int m = posY - 12;
            guiGraphics.pose().pushPose();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::m_172811_);
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            TooltipRenderUtil.renderTooltipBackground(guiGraphics, l, m, i, j, 400);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            BufferUploader.drawWithShader(bufferBuilder.end());
            MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
            guiGraphics.pose().translate(0.0F, 0.0F, 400.0F);
            int p = m;
            for (int q = 0; q < components.size(); q++) {
                ClientTooltipComponent clientTooltipComponent2 = (ClientTooltipComponent) components.get(q);
                clientTooltipComponent2.renderText(font, l, p, guiGraphics.pose().last().pose(), bufferSource);
                p += clientTooltipComponent2.getHeight() + (q == 0 ? 2 : 0);
            }
            bufferSource.endBatch();
            p = m;
            ItemRenderer itemRenderer = minecraft.getItemRenderer();
            for (int var21 = 0; var21 < components.size(); var21++) {
                ClientTooltipComponent clientTooltipComponent2 = (ClientTooltipComponent) components.get(var21);
                clientTooltipComponent2.renderImage(font, l, p, guiGraphics);
                p += clientTooltipComponent2.getHeight() + (var21 == 0 ? 2 : 0);
            }
            guiGraphics.pose().popPose();
        }
    }
}