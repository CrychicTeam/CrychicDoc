package yesman.epicfight.api.client.model.armor;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.SingleVertex;

@OnlyIn(Dist.CLIENT)
public abstract class ArmorModelTransformer {

    protected abstract AnimatedMesh transformModel(HumanoidModel<?> var1, ArmorItem var2, EquipmentSlot var3, boolean var4);

    public abstract static class PartTransformer<T> {

        void putIndexCount(Map<String, List<Integer>> indices, String partName, int value) {
            List<Integer> list = (List<Integer>) indices.computeIfAbsent(partName, key -> Lists.newArrayList());
            for (int i = 0; i < 3; i++) {
                list.add(value);
            }
        }

        public abstract void bakeCube(PoseStack var1, String var2, T var3, List<SingleVertex> var4, Map<String, List<Integer>> var5);
    }
}