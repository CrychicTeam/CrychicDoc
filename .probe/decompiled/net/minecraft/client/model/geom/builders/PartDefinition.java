package net.minecraft.client.model.geom.builders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;

public class PartDefinition {

    private final List<CubeDefinition> cubes;

    private final PartPose partPose;

    private final Map<String, PartDefinition> children = Maps.newHashMap();

    PartDefinition(List<CubeDefinition> listCubeDefinition0, PartPose partPose1) {
        this.cubes = listCubeDefinition0;
        this.partPose = partPose1;
    }

    public PartDefinition addOrReplaceChild(String string0, CubeListBuilder cubeListBuilder1, PartPose partPose2) {
        PartDefinition $$3 = new PartDefinition(cubeListBuilder1.getCubes(), partPose2);
        PartDefinition $$4 = (PartDefinition) this.children.put(string0, $$3);
        if ($$4 != null) {
            $$3.children.putAll($$4.children);
        }
        return $$3;
    }

    public ModelPart bake(int int0, int int1) {
        Object2ObjectArrayMap<String, ModelPart> $$2 = (Object2ObjectArrayMap<String, ModelPart>) this.children.entrySet().stream().collect(Collectors.toMap(Entry::getKey, p_171593_ -> ((PartDefinition) p_171593_.getValue()).bake(int0, int1), (p_171595_, p_171596_) -> p_171595_, Object2ObjectArrayMap::new));
        List<ModelPart.Cube> $$3 = (List<ModelPart.Cube>) this.cubes.stream().map(p_171589_ -> p_171589_.bake(int0, int1)).collect(ImmutableList.toImmutableList());
        ModelPart $$4 = new ModelPart($$3, $$2);
        $$4.setInitialPose(this.partPose);
        $$4.loadPose(this.partPose);
        return $$4;
    }

    public PartDefinition getChild(String string0) {
        return (PartDefinition) this.children.get(string0);
    }
}