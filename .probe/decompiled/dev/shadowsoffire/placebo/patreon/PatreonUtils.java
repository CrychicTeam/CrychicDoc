package dev.shadowsoffire.placebo.patreon;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.shadowsoffire.placebo.patreon.wings.IWingModel;
import dev.shadowsoffire.placebo.patreon.wings.Wing;
import java.util.function.Function;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class PatreonUtils {

    private static Function<Player, ResourceLocation> wingTex(String name) {
        Supplier<ResourceLocation> supp = Suppliers.memoize(() -> new ResourceLocation("placebo", "textures/wings/" + name + ".png"));
        return player -> (ResourceLocation) supp.get();
    }

    public static enum PatreonParticleType {

        ASH(() -> ParticleTypes.ASH),
        CAMPFIRE_SMOKE(() -> ParticleTypes.CAMPFIRE_COSY_SMOKE),
        CLOUD(() -> ParticleTypes.CLOUD),
        DMG_HEART(() -> ParticleTypes.DAMAGE_INDICATOR),
        DRAGON_BREATH(() -> ParticleTypes.DRAGON_BREATH),
        ELECTRIC_SPARK(() -> ParticleTypes.ELECTRIC_SPARK),
        END_ROD(() -> ParticleTypes.END_ROD),
        FIRE(() -> ParticleTypes.FLAME),
        FIREWORK(() -> ParticleTypes.FIREWORK),
        GLOW(() -> ParticleTypes.GLOW),
        GROWTH(() -> ParticleTypes.HAPPY_VILLAGER),
        HEART(() -> ParticleTypes.HEART),
        SCULK_SOUL(() -> ParticleTypes.SCULK_SOUL),
        SLIME(() -> ParticleTypes.ITEM_SLIME),
        SNOW(() -> ParticleTypes.ITEM_SNOWBALL),
        SOUL(() -> ParticleTypes.SOUL),
        SOUL_FIRE(() -> ParticleTypes.SOUL_FIRE_FLAME),
        WITCH(() -> ParticleTypes.WITCH);

        public final java.util.function.Supplier<ParticleOptions> type;

        private PatreonParticleType(java.util.function.Supplier<ParticleOptions> type) {
            this.type = type;
        }
    }

    public static enum WingType {

        ANGEL(() -> Wing.INSTANCE, PatreonUtils.wingTex("angel"), -0.5),
        ARMORED(() -> Wing.INSTANCE, PatreonUtils.wingTex("armored"), -0.7),
        BAT(() -> Wing.INSTANCE, PatreonUtils.wingTex("bat"), -0.7),
        BLAZE(() -> Wing.INSTANCE, PatreonUtils.wingTex("blaze"), -0.73),
        BONE(() -> Wing.INSTANCE, PatreonUtils.wingTex("bone"), -1.0),
        CLOUD(() -> Wing.INSTANCE, PatreonUtils.wingTex("cloud"), -1.0),
        DEMON(() -> Wing.INSTANCE, PatreonUtils.wingTex("demon"), -0.55),
        FAIRY(() -> Wing.INSTANCE, PatreonUtils.wingTex("fairy"), -0.85),
        FLY(() -> Wing.INSTANCE, PatreonUtils.wingTex("fly"), -0.58, 6.0),
        MECHANICAL(() -> Wing.INSTANCE, PatreonUtils.wingTex("mechanical"), -0.75),
        MONARCH(() -> Wing.INSTANCE, PatreonUtils.wingTex("monarch"), -0.85),
        PIXIE(() -> Wing.INSTANCE, PatreonUtils.wingTex("pixie"), -0.65),
        SPOOKY(() -> Wing.INSTANCE, PatreonUtils.wingTex("spooky"), -0.65);

        public final java.util.function.Supplier<IWingModel> model;

        public final Function<Player, ResourceLocation> textureGetter;

        public final double yOffset;

        public final double flapSpeed;

        private WingType(java.util.function.Supplier<IWingModel> model, Function<Player, ResourceLocation> textureGetter, double yOffset) {
            this(model, textureGetter, yOffset, 1.0);
        }

        private WingType(java.util.function.Supplier<IWingModel> model, Function<Player, ResourceLocation> textureGetter, double yOffset, double flapSpeed) {
            this.model = model;
            this.textureGetter = textureGetter;
            this.yOffset = yOffset;
            this.flapSpeed = flapSpeed;
        }
    }
}