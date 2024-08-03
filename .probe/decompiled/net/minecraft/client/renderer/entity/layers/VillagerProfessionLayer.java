package net.minecraft.client.renderer.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.IOException;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerHeadModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.resources.metadata.animation.VillagerMetaDataSection;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerDataHolder;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;

public class VillagerProfessionLayer<T extends LivingEntity & VillagerDataHolder, M extends EntityModel<T> & VillagerHeadModel> extends RenderLayer<T, M> {

    private static final Int2ObjectMap<ResourceLocation> LEVEL_LOCATIONS = Util.make(new Int2ObjectOpenHashMap(), p_117657_ -> {
        p_117657_.put(1, new ResourceLocation("stone"));
        p_117657_.put(2, new ResourceLocation("iron"));
        p_117657_.put(3, new ResourceLocation("gold"));
        p_117657_.put(4, new ResourceLocation("emerald"));
        p_117657_.put(5, new ResourceLocation("diamond"));
    });

    private final Object2ObjectMap<VillagerType, VillagerMetaDataSection.Hat> typeHatCache = new Object2ObjectOpenHashMap();

    private final Object2ObjectMap<VillagerProfession, VillagerMetaDataSection.Hat> professionHatCache = new Object2ObjectOpenHashMap();

    private final ResourceManager resourceManager;

    private final String path;

    public VillagerProfessionLayer(RenderLayerParent<T, M> renderLayerParentTM0, ResourceManager resourceManager1, String string2) {
        super(renderLayerParentTM0);
        this.resourceManager = resourceManager1;
        this.path = string2;
    }

    public void render(PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2, T t3, float float4, float float5, float float6, float float7, float float8, float float9) {
        if (!t3.m_20145_()) {
            VillagerData $$10 = t3.getVillagerData();
            VillagerType $$11 = $$10.getType();
            VillagerProfession $$12 = $$10.getProfession();
            VillagerMetaDataSection.Hat $$13 = this.getHatData(this.typeHatCache, "type", BuiltInRegistries.VILLAGER_TYPE, $$11);
            VillagerMetaDataSection.Hat $$14 = this.getHatData(this.professionHatCache, "profession", BuiltInRegistries.VILLAGER_PROFESSION, $$12);
            M $$15 = (M) this.m_117386_();
            $$15.hatVisible($$14 == VillagerMetaDataSection.Hat.NONE || $$14 == VillagerMetaDataSection.Hat.PARTIAL && $$13 != VillagerMetaDataSection.Hat.FULL);
            ResourceLocation $$16 = this.getResourceLocation("type", BuiltInRegistries.VILLAGER_TYPE.getKey($$11));
            m_117376_($$15, $$16, poseStack0, multiBufferSource1, int2, t3, 1.0F, 1.0F, 1.0F);
            $$15.hatVisible(true);
            if ($$12 != VillagerProfession.NONE && !t3.isBaby()) {
                ResourceLocation $$17 = this.getResourceLocation("profession", BuiltInRegistries.VILLAGER_PROFESSION.getKey($$12));
                m_117376_($$15, $$17, poseStack0, multiBufferSource1, int2, t3, 1.0F, 1.0F, 1.0F);
                if ($$12 != VillagerProfession.NITWIT) {
                    ResourceLocation $$18 = this.getResourceLocation("profession_level", (ResourceLocation) LEVEL_LOCATIONS.get(Mth.clamp($$10.getLevel(), 1, LEVEL_LOCATIONS.size())));
                    m_117376_($$15, $$18, poseStack0, multiBufferSource1, int2, t3, 1.0F, 1.0F, 1.0F);
                }
            }
        }
    }

    private ResourceLocation getResourceLocation(String string0, ResourceLocation resourceLocation1) {
        return resourceLocation1.withPath((UnaryOperator<String>) (p_247944_ -> "textures/entity/" + this.path + "/" + string0 + "/" + p_247944_ + ".png"));
    }

    public <K> VillagerMetaDataSection.Hat getHatData(Object2ObjectMap<K, VillagerMetaDataSection.Hat> objectObjectMapKVillagerMetaDataSectionHat0, String string1, DefaultedRegistry<K> defaultedRegistryK2, K k3) {
        return (VillagerMetaDataSection.Hat) objectObjectMapKVillagerMetaDataSectionHat0.computeIfAbsent(k3, p_258159_ -> (VillagerMetaDataSection.Hat) this.resourceManager.m_213713_(this.getResourceLocation(string1, defaultedRegistryK2.getKey(k3))).flatMap(p_234875_ -> {
            try {
                return p_234875_.metadata().getSection(VillagerMetaDataSection.SERIALIZER).map(VillagerMetaDataSection::m_119070_);
            } catch (IOException var2x) {
                return Optional.empty();
            }
        }).orElse(VillagerMetaDataSection.Hat.NONE));
    }
}