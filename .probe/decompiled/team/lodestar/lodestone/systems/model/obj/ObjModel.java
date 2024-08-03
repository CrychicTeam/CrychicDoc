package team.lodestar.lodestone.systems.model.obj;

import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import team.lodestar.lodestone.LodestoneLib;

public class ObjModel {

    public ArrayList<Face> faces = new ArrayList();

    public ResourceLocation modelLocation;

    protected ObjParser objParser;

    public ObjModel(ResourceLocation modelLocation) {
        this.modelLocation = modelLocation;
        this.objParser = new ObjParser();
    }

    public void loadModel() {
        LodestoneLib.LOGGER.info("Loading model: " + this.modelLocation);
        String modID = this.modelLocation.getNamespace();
        String fileName = this.modelLocation.getPath();
        ResourceLocation resourceLocation = new ResourceLocation(modID, "obj/" + fileName + ".obj");
        Optional<Resource> resourceO = Minecraft.getInstance().getResourceManager().m_213713_(resourceLocation);
        if (resourceO.isEmpty()) {
            throw new RuntimeException("Resource not found: " + resourceLocation);
        } else {
            Resource resource = (Resource) resourceO.get();
            try {
                this.objParser.parseObjFile(resource);
                this.faces = this.objParser.getFaces();
            } catch (IOException var7) {
                LodestoneLib.LOGGER.error("Error parsing OBJ file: " + resourceLocation, var7);
                var7.printStackTrace();
            }
        }
    }

    public void renderModel(PoseStack poseStack, RenderType renderType, int packedLight) {
        this.faces.forEach(face -> face.renderFace(poseStack, renderType, packedLight));
    }
}