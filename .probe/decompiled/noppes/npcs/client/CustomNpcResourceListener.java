package noppes.npcs.client;

import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import noppes.npcs.client.gui.select.GuiTextureSelection;
import noppes.npcs.client.parts.MpmPartReader;
import noppes.npcs.shared.client.model.util.CustomRenderStates;
import noppes.npcs.shared.client.util.TextureCache;

public class CustomNpcResourceListener implements ResourceManagerReloadListener {

    public static int DefaultTextColor = 4210752;

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        try {
            DefaultTextColor = Integer.parseInt(I18n.get("customnpcs.defaultTextColor"), 16);
        } catch (NumberFormatException var3) {
            DefaultTextColor = 4210752;
        }
        GuiTextureSelection.clear();
        MpmPartReader.reload();
        RenderSystem.recordRenderCall(() -> {
            try {
                CustomRenderStates.posTexNormalShader = new ShaderInstance(manager, "moreplayermodels:position_tex_normal", CustomRenderStates.POS_TEX_NORMAL);
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        });
    }

    private void createTextureCache() {
        this.enlargeTexture("acacia_planks");
        this.enlargeTexture("birch_planks");
        this.enlargeTexture("crimson_planks");
        this.enlargeTexture("dark_oak_planks");
        this.enlargeTexture("jungle_planks");
        this.enlargeTexture("oak_planks");
        this.enlargeTexture("spruce_planks");
        this.enlargeTexture("warped_planks");
        this.enlargeTexture("iron_block");
        this.enlargeTexture("diamond_block");
        this.enlargeTexture("stone");
        this.enlargeTexture("gold_block");
        this.enlargeTexture("white_wool");
    }

    private void enlargeTexture(String texture) {
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        ResourceLocation location = new ResourceLocation("customnpcs:textures/cache/" + texture + ".png");
        AbstractTexture ob = manager.getTexture(location);
        if (!(ob instanceof TextureCache)) {
            AbstractTexture var5 = new TextureCache(location, new ResourceLocation("textures/block/" + texture + ".png"));
            manager.register(location, var5);
        }
    }
}