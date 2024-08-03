package net.mehvahdjukaar.moonlight.core;

import com.mojang.blaze3d.platform.Lighting;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidColors;
import net.mehvahdjukaar.moonlight.api.map.client.MapDecorationClientManager;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynClientResourcesGenerator;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicResourcePack;
import net.mehvahdjukaar.moonlight.api.resources.pack.DynamicTexturePack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Vector3f;

@Internal
public class MoonlightClient {

    private static final ThreadLocal<Boolean> MAP_MIPMAP = ThreadLocal.withInitial(() -> false);

    private static MoonlightClient.MergedDynamicTexturePack mergedDynamicPack;

    private static ClientConfigs.ShadeFix fixShade = ClientConfigs.ShadeFix.FALSE;

    private static Vector3f oldL0 = null;

    private static Vector3f oldL1 = null;

    private static Vector3f oldL0n = null;

    private static Vector3f oldL1n = null;

    private static final Vector3f NEW_L_0 = new Vector3f(0.2F, 0.7777778F, -0.6F).normalize();

    private static final Vector3f NEW_L_1 = new Vector3f(-0.2F, 0.7777778F, 0.6F).normalize();

    public static void initClient() {
        ClientHelper.addClientReloadListener(SoftFluidColors::new, Moonlight.res("soft_fluids"));
        ClientHelper.addClientReloadListener(MapDecorationClientManager::new, Moonlight.res("map_markers"));
        ClientConfigs.init();
        MoonlightClient.Gen gen = new MoonlightClient.Gen();
        gen.register();
    }

    public static DynamicTexturePack maybeMergePack(DynamicTexturePack pack) {
        if (!(Boolean) ClientConfigs.MERGE_PACKS.get()) {
            return pack;
        } else {
            if (mergedDynamicPack == null) {
                mergedDynamicPack = new MoonlightClient.MergedDynamicTexturePack() {
                };
            }
            for (String n : pack.m_5698_(pack.getPackType())) {
                mergedDynamicPack.addNamespaces(new String[] { n });
            }
            mergedDynamicPack.mods++;
            return mergedDynamicPack;
        }
    }

    public static void afterTextureReload() {
        DynamicResourcePack.clearAfterReload(PackType.CLIENT_RESOURCES);
    }

    public static void setMipMap(boolean b) {
        if ((Integer) ClientConfigs.MAPS_MIPMAP.get() == 0) {
            b = false;
        }
        MAP_MIPMAP.set(b);
    }

    public static boolean isMapMipMap() {
        return (Boolean) MAP_MIPMAP.get();
    }

    public static void beforeRenderGui() {
        if (fixShade == ClientConfigs.ShadeFix.NO_GUI) {
            revertFixedShade();
        }
    }

    public static void afterRenderGui() {
        if (fixShade == ClientConfigs.ShadeFix.NO_GUI) {
            applyFixedShade();
        }
    }

    private static void revertFixedShade() {
        if (oldL0 != null) {
            Lighting.DIFFUSE_LIGHT_0 = oldL0;
            Lighting.DIFFUSE_LIGHT_1 = oldL1;
            Lighting.NETHER_DIFFUSE_LIGHT_0 = oldL0n;
            Lighting.NETHER_DIFFUSE_LIGHT_1 = oldL1n;
            oldL0 = null;
            oldL1 = null;
            oldL0n = null;
            oldL1n = null;
        }
    }

    private static void applyFixedShade() {
        if (oldL0 == null) {
            oldL0 = Lighting.DIFFUSE_LIGHT_0;
            oldL1 = Lighting.DIFFUSE_LIGHT_1;
            oldL0n = Lighting.NETHER_DIFFUSE_LIGHT_0;
            oldL1n = Lighting.NETHER_DIFFUSE_LIGHT_1;
        }
        Lighting.DIFFUSE_LIGHT_0 = NEW_L_0;
        Lighting.DIFFUSE_LIGHT_1 = NEW_L_1;
        Lighting.NETHER_DIFFUSE_LIGHT_0 = NEW_L_0;
        Lighting.NETHER_DIFFUSE_LIGHT_1 = NEW_L_1;
    }

    private static class Gen extends DynClientResourcesGenerator {

        public Gen() {
            super(new DynamicTexturePack(Moonlight.res("generated_pack")));
            this.dynamicPack.addNamespaces(new String[] { "minecraft" });
        }

        @Override
        public Logger getLogger() {
            return Moonlight.LOGGER;
        }

        @Override
        public boolean dependsOnLoadedPacks() {
            return true;
        }

        @Override
        public void regenerateDynamicAssets(ResourceManager manager) {
            MoonlightClient.fixShade = (ClientConfigs.ShadeFix) ClientConfigs.FIX_SHADE.get();
            MoonlightClient.revertFixedShade();
            if (MoonlightClient.fixShade != ClientConfigs.ShadeFix.FALSE) {
                MoonlightClient.applyFixedShade();
                this.dynamicPack.addBytes(new ResourceLocation("shaders/include/light.glsl"), "#version 150\n\n#define MINECRAFT_LIGHT_POWER   (0.6)\n#define MINECRAFT_LIGHT_POWER_FIXED   (0.5)\n#define MINECRAFT_AMBIENT_LIGHT (0.4)\n#define MINECRAFT_AMBIENT_LIGHT_FIXED (0.5)\n\nvec4 minecraft_mix_light(vec3 lightDir0, vec3 lightDir1, vec3 normal, vec4 color) {\n    lightDir0 = normalize(lightDir0);\n    lightDir1 = normalize(lightDir1);\n    float light0 = max(0.0, dot(lightDir0, normal));\n    float light1 = max(0.0, dot(lightDir1, normal));\n\n    float dotP = dot(lightDir0, lightDir1);\n    bool isFixed = dotP > 0.20 && dotP < 0.205;\n    float lightPow = isFixed ? MINECRAFT_LIGHT_POWER_FIXED : MINECRAFT_LIGHT_POWER;\n    float ambientLight = isFixed ? MINECRAFT_AMBIENT_LIGHT_FIXED : MINECRAFT_AMBIENT_LIGHT;\n\n    float lightAccum = min(1.0, (light0 + light1) * lightPow + ambientLight);\n    return vec4(color.rgb * lightAccum, color.a);\n}\n\nvec4 minecraft_sample_lightmap(sampler2D lightMap, ivec2 uv) {\n    return texture(lightMap, clamp(uv / 256.0, vec2(0.5 / 16.0), vec2(15.5 / 16.0)));\n}".getBytes(), ResType.GENERIC);
            }
        }
    }

    private static class MergedDynamicTexturePack extends DynamicTexturePack {

        int mods = 0;

        public MergedDynamicTexturePack() {
            super(Moonlight.res("mods_dynamic_assets"));
        }

        @Override
        public Component makeDescription() {
            return Component.literal("Dynamic resources for " + this.mods + (this.mods == 1 ? " mod" : " mods"));
        }
    }
}