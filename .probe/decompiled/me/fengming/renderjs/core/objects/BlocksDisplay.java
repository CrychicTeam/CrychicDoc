package me.fengming.renderjs.core.objects;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import me.fengming.renderjs.core.RenderObject;
import me.fengming.renderjs.core.Utils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

@RemapPrefixForJS("rjs$")
public class BlocksDisplay extends RenderObject {

    protected BlockState blockState;

    protected int worldLight = 15;

    protected int blockLight = 15;

    protected RenderType renderType = null;

    public BlocksDisplay(float[] vertices, RenderObject.ObjectType type) {
        super(vertices, type);
    }

    public void rjs$setBlockState(BlockState blockState) {
        this.blockState = blockState;
    }

    public void rjs$setWorldLight(int worldLight) {
        this.worldLight = worldLight;
    }

    public void rjs$setBlockLight(int blockLight) {
        this.blockLight = blockLight;
    }

    public void rjs$setRenderType(RenderType renderType) {
        this.renderType = renderType;
    }

    @Override
    public void loadInner(CompoundTag object) {
        if (object.contains("block")) {
            this.rjs$setBlockState(Utils.parseBlock(object.getString("block"), true));
        } else {
            ConsoleJS.CLIENT.error("Missing a necessary key: block");
        }
        if (object.contains("world_light")) {
            this.rjs$setWorldLight(object.getInt("world_light"));
        }
        if (object.contains("block_light")) {
            this.rjs$setBlockLight(object.getInt("block_light"));
        }
        if (object.contains("render_type")) {
            this.rjs$setRenderType(Utils.getRenderTypeById(object.getString("render_type")));
        }
    }

    @Override
    public void renderInner() {
        for (int i = 0; i < this.vertices.length; i += 3) {
            this.poseStack.translate(this.vertices[i], this.vertices[i + 1], this.vertices[i + 2]);
            mc.getBlockRenderer().renderSingleBlock(this.blockState, this.poseStack, mc.renderBuffers().bufferSource(), LightTexture.pack(this.worldLight, this.blockLight), OverlayTexture.NO_OVERLAY, ModelData.EMPTY, this.renderType);
        }
    }
}