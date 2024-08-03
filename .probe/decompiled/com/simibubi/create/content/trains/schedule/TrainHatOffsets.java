package com.simibubi.create.content.trains.schedule;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.AxolotlModel;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.FoxModel;
import net.minecraft.client.model.FrogModel;
import net.minecraft.client.model.GuardianModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.client.model.IronGolemModel;
import net.minecraft.client.model.LavaSlimeModel;
import net.minecraft.client.model.OcelotModel;
import net.minecraft.client.model.PandaModel;
import net.minecraft.client.model.ParrotModel;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.SheepModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.SnowGolemModel;
import net.minecraft.client.model.SpiderModel;
import net.minecraft.client.model.WardenModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.world.phys.Vec3;

public class TrainHatOffsets {

    public static Vec3 getOffset(EntityModel<?> model) {
        float x = 0.0F;
        float y = 0.0F;
        float z = 0.0F;
        if (model instanceof AgeableListModel) {
            if (model instanceof WolfModel) {
                x += 0.5F;
                y++;
                z += 0.25F;
            } else if (model instanceof OcelotModel) {
                y++;
                z -= 0.25F;
            } else if (model instanceof ChickenModel) {
                z -= 0.25F;
            } else if (model instanceof FoxModel) {
                x += 0.5F;
                y += 2.0F;
                z--;
            } else if (model instanceof QuadrupedModel) {
                y += 2.0F;
                if (model instanceof CowModel) {
                    z--;
                } else if (model instanceof PandaModel) {
                    z += 0.5F;
                } else if (model instanceof PigModel) {
                    z -= 2.0F;
                } else if (model instanceof SheepModel) {
                    z -= 0.75F;
                    y--;
                }
            } else if (model instanceof HoglinModel) {
                z -= 4.5F;
            } else if (model instanceof BeeModel) {
                z -= 0.75F;
                y -= 4.0F;
            } else if (model instanceof AxolotlModel) {
                z -= 5.0F;
                y += 0.5F;
            }
        }
        if (model instanceof HierarchicalModel) {
            if (model instanceof BlazeModel) {
                y += 4.0F;
            } else if (model instanceof GuardianModel) {
                y += 20.0F;
            } else if (model instanceof IronGolemModel) {
                z--;
                y -= 2.0F;
            } else if (model instanceof SnowGolemModel) {
                z -= 0.75F;
                y -= 3.0F;
            } else if (model instanceof SlimeModel || model instanceof LavaSlimeModel) {
                y += 22.0F;
            } else if (model instanceof SpiderModel) {
                z -= 3.5F;
                y += 2.0F;
            } else if (model instanceof ParrotModel) {
                z--;
            } else if (model instanceof WardenModel) {
                y += 3.5F;
                z += 0.5F;
            } else if (model instanceof FrogModel) {
                y += 16.75F;
                z -= 0.25F;
            }
        }
        return new Vec3((double) x, (double) y, (double) z);
    }
}