package org.embeddedt.embeddium.gui.frame.tab;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gui.widgets.FlatButtonWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;

public class TabHeaderWidget extends FlatButtonWidget {

    private static final ResourceLocation FALLBACK_LOCATION = new ResourceLocation("textures/misc/unknown_pack.png");

    private static final Set<String> erroredLogos = new HashSet();

    private final ResourceLocation logoTexture;

    public static MutableComponent getLabel(String modId) {
        byte var2 = -1;
        switch(modId.hashCode()) {
            case 3540050:
                if (modId.equals("sspb")) {
                    var2 = 0;
                }
            default:
                return (switch(var2) {
                    case 0 ->
                        Component.literal("SSPB");
                    default ->
                        Tab.idComponent(modId);
                }).withStyle(s -> s.withUnderlined(true));
        }
    }

    public TabHeaderWidget(Dim2i dim, String modId) {
        super(dim, getLabel(modId), () -> {
        });
        Optional<String> logoFile = erroredLogos.contains(modId) ? Optional.empty() : ModList.get().getModContainerById(modId).flatMap(c -> c.getModInfo().getLogoFile());
        ResourceLocation texture = null;
        if (logoFile.isPresent()) {
            PathPackResources resourcePack = (PathPackResources) ResourcePackLoader.getPackFor(modId).orElse((PathPackResources) ResourcePackLoader.getPackFor("forge").orElseThrow(() -> new RuntimeException("Can't find forge, WHAT!")));
            try {
                IoSupplier<InputStream> logoResource = resourcePack.getRootResource((String) logoFile.get());
                if (logoResource != null) {
                    NativeImage logo = NativeImage.read(logoResource.get());
                    if (logo.getWidth() != logo.getHeight()) {
                        logo.close();
                        throw new IOException("Logo " + (String) logoFile.get() + " for " + modId + " is not square");
                    }
                    texture = new ResourceLocation("embeddium", "logo/" + modId);
                    Minecraft.getInstance().textureManager.register(texture, new DynamicTexture(logo));
                }
            } catch (IOException var8) {
                erroredLogos.add(modId);
                SodiumClientMod.logger().error("Exception reading logo for " + modId, var8);
            }
        }
        this.logoTexture = texture;
    }

    @Override
    protected int getLeftAlignedTextOffset() {
        return super.getLeftAlignedTextOffset() + 9;
    }

    @Override
    protected boolean isHovered(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        super.render(drawContext, mouseX, mouseY, delta);
        ResourceLocation icon = (ResourceLocation) Objects.requireNonNullElse(this.logoTexture, FALLBACK_LOCATION);
        int fontHeight = 9;
        int imgY = this.dim.getCenterY() - fontHeight / 2;
        drawContext.blit(icon, this.dim.x() + 5, imgY, 0.0F, 0.0F, fontHeight, fontHeight, fontHeight, fontHeight);
    }
}