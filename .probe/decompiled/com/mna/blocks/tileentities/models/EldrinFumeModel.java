package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.EldrinFumeTile;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EldrinFumeModel extends WizardLabModel<EldrinFumeTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_simple_rooted_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/none.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation coal = RLoc.create("block/wizard_lab/special/eldrin_fume_coal");

    public EldrinFumeModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "ROOT", coal));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "ROOT", ItemStack.EMPTY, pose -> {
            pose.translate(0.5, 1.525, 0.435);
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
    }

    public ResourceLocation getAnimationResource(EldrinFumeTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(EldrinFumeTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(EldrinFumeTile arg0) {
        return texFile;
    }
}