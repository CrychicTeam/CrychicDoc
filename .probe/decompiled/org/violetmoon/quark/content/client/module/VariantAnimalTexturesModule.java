package org.violetmoon.quark.content.client.module;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.living.ZLivingTick;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "client")
public class VariantAnimalTexturesModule extends ZetaModule {

    private static ListMultimap<VariantAnimalTexturesModule.VariantTextureType, ResourceLocation> textures;

    private static Map<VariantAnimalTexturesModule.VariantTextureType, ResourceLocation> shinyTextures;

    private static final int COW_COUNT = 4;

    private static final int PIG_COUNT = 3;

    private static final int CHICKEN_COUNT = 6;

    @Config
    public static boolean enableCow = true;

    @Config
    public static boolean enablePig = true;

    @Config
    public static boolean enableChicken = true;

    @Config
    public static boolean enableShinyRabbit = true;

    @Config
    public static boolean enableShinyLlama = true;

    @Config
    public static boolean enableShinyDolphin = true;

    @Config
    public static boolean enableShinySlime = true;

    @Config
    public static boolean enableLGBTBees = true;

    @Config
    public static boolean everyBeeIsLGBT = false;

    protected static final List<String> BEE_VARIANTS = List.of("acebee", "agenbee", "arobee", "beefluid", "beesexual", "beequeer", "enbee", "gaybee", "interbee", "lesbeean", "panbee", "polysexbee", "transbee", "helen");

    @Config(description = "The chance for an animal to have a special \"Shiny\" skin, like a shiny pokemon. This is 1 in X. Set to 0 to disable.")
    public static int shinyAnimalChance = 2048;

    @Config(description = "If a shiny animal should emit occasional sparkles.")
    public static boolean shinySparkles = true;

    public static boolean staticEnabled;

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends VariantAnimalTexturesModule {

        @LoadEvent
        public final void clientSetup(ZClientSetup event) {
            VariantAnimalTexturesModule.textures = Multimaps.newListMultimap(new EnumMap(VariantAnimalTexturesModule.VariantTextureType.class), ArrayList::new);
            VariantAnimalTexturesModule.shinyTextures = new HashMap();
            registerTextures(VariantAnimalTexturesModule.VariantTextureType.COW, 4, new ResourceLocation("textures/entity/cow/cow.png"));
            registerTextures(VariantAnimalTexturesModule.VariantTextureType.PIG, 3, new ResourceLocation("textures/entity/pig/pig.png"));
            registerTextures(VariantAnimalTexturesModule.VariantTextureType.CHICKEN, 6, new ResourceLocation("textures/entity/chicken.png"));
            registerShiny(VariantAnimalTexturesModule.VariantTextureType.RABBIT);
            registerShiny(VariantAnimalTexturesModule.VariantTextureType.LLAMA);
            registerShiny(VariantAnimalTexturesModule.VariantTextureType.DOLPHIN);
            registerShiny(VariantAnimalTexturesModule.VariantTextureType.SLIME);
        }

        @LoadEvent
        public final void configChanged(ZConfigChanged event) {
            staticEnabled = this.enabled;
        }

        @PlayEvent
        public void doShinySparkles(ZLivingTick event) {
            if (shinySparkles) {
                LivingEntity entity = event.getEntity();
                Level level = entity.m_9236_();
                if (level.isClientSide() && level.getGameTime() % 10L == 0L && isSparkly(entity)) {
                    double angle = Math.random() * 2.0 * Math.PI;
                    double dist = Math.random() * 0.5 + 0.25;
                    double dX = Math.cos(angle) * dist;
                    double dY = (double) entity.getDimensions(entity.m_20089_()).height + (Math.random() - 0.5) * 0.2;
                    double dZ = Math.sin(angle) * dist;
                    level.addParticle(ParticleTypes.HAPPY_VILLAGER, entity.m_20185_() + dX, entity.m_20186_() + dY, entity.m_20189_() + dZ, Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
                }
            }
        }

        @Nullable
        public static ResourceLocation getCowTexture(Cow entity) {
            return staticEnabled && enableCow ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.COW) : null;
        }

        @Nullable
        public static ResourceLocation getPigTexture(Pig entity) {
            return staticEnabled && enablePig ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.PIG) : null;
        }

        @Nullable
        public static ResourceLocation getChickenTexture(Chicken entity) {
            return staticEnabled && enableChicken ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.CHICKEN) : null;
        }

        @Nullable
        public static ResourceLocation getRabbitTexture(Rabbit entity) {
            return staticEnabled && enableShinyRabbit ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.RABBIT, () -> null) : null;
        }

        @Nullable
        public static ResourceLocation getLlamaTexture(Llama entity) {
            return staticEnabled && enableShinyLlama ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.LLAMA, () -> null) : null;
        }

        @Nullable
        public static ResourceLocation getDolphinTexture(Dolphin entity) {
            return staticEnabled && enableShinyDolphin ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.DOLPHIN, () -> null) : null;
        }

        @Nullable
        public static ResourceLocation getSlimeTexture(Slime entity) {
            return staticEnabled && enableShinySlime ? getTextureOrShiny(entity, VariantAnimalTexturesModule.VariantTextureType.SLIME, () -> null) : null;
        }

        @Nullable
        public static ResourceLocation getBeeTexture(Bee entity) {
            if (staticEnabled && enableLGBTBees) {
                UUID id = entity.m_20148_();
                long most = id.getMostSignificantBits();
                double lgbtChance = 0.056;
                boolean lgbt = VariantAnimalTexturesModule.everyBeeIsLGBT || new Random(most).nextDouble() < 0.056;
                if (entity.m_8077_() || lgbt) {
                    String custName = "";
                    if (entity.m_8077_()) {
                        Component name = entity.m_7770_();
                        if (name != null) {
                            custName = name.getString();
                        }
                    }
                    String name = custName.toLowerCase(Locale.ROOT);
                    if (!BEE_VARIANTS.contains(name)) {
                        if (custName.matches("wire(se|bee)gal")) {
                            name = "enbee";
                        } else if (lgbt) {
                            name = (String) BEE_VARIANTS.get(Math.abs((int) (most % (long) (BEE_VARIANTS.size() - 1))));
                        }
                    }
                    if (BEE_VARIANTS.contains(name)) {
                        String type = "normal";
                        boolean angery = entity.hasStung();
                        boolean nectar = entity.hasNectar();
                        if (angery) {
                            type = nectar ? "angry_nectar" : "angry";
                        } else if (nectar) {
                            type = "nectar";
                        }
                        String path = String.format("textures/model/entity/variants/bees/%s/%s.png", name, type);
                        return new ResourceLocation("quark", path);
                    }
                }
                return null;
            } else {
                return null;
            }
        }

        public static boolean isShiny(UUID id) {
            long most = id.getMostSignificantBits();
            return shinyAnimalChance > 0 && most % (long) shinyAnimalChance == 0L;
        }

        public static boolean isSparkly(Entity e) {
            EntityType<?> type = e.getType();
            return type == EntityType.COW && enableCow || type == EntityType.PIG && enablePig || type == EntityType.CHICKEN && enableChicken || type == EntityType.RABBIT && enableShinyRabbit || type == EntityType.LLAMA && enableShinyLlama || type == EntityType.DOLPHIN && enableShinyDolphin ? isShiny(e.getUUID()) : false;
        }

        public static ResourceLocation getTextureOrShiny(Entity e, VariantAnimalTexturesModule.VariantTextureType type) {
            return getTextureOrShiny(e, type, () -> getRandomTexture(e, type));
        }

        public static ResourceLocation getTextureOrShiny(Entity e, VariantAnimalTexturesModule.VariantTextureType type, Supplier<ResourceLocation> nonShiny) {
            return isShiny(e.getUUID()) ? (ResourceLocation) VariantAnimalTexturesModule.shinyTextures.get(type) : (ResourceLocation) nonShiny.get();
        }

        private static ResourceLocation getRandomTexture(Entity e, VariantAnimalTexturesModule.VariantTextureType type) {
            List<ResourceLocation> styles = VariantAnimalTexturesModule.textures.get(type);
            UUID id = e.getUUID();
            long most = id.getMostSignificantBits();
            int choice = Math.abs((int) (most % (long) styles.size()));
            return (ResourceLocation) styles.get(choice);
        }

        private static void registerTextures(VariantAnimalTexturesModule.VariantTextureType type, int count, ResourceLocation vanilla) {
            String name = type.name().toLowerCase(Locale.ROOT);
            for (int i = 1; i < count + 1; i++) {
                VariantAnimalTexturesModule.textures.put(type, new ResourceLocation("quark", String.format("textures/model/entity/variants/%s%d.png", name, i)));
            }
            if (vanilla != null) {
                VariantAnimalTexturesModule.textures.put(type, vanilla);
            }
            registerShiny(type);
        }

        private static void registerShiny(VariantAnimalTexturesModule.VariantTextureType type) {
            VariantAnimalTexturesModule.shinyTextures.put(type, new ResourceLocation("quark", String.format("textures/model/entity/variants/%s_shiny.png", type.name().toLowerCase(Locale.ROOT))));
        }
    }

    public static enum VariantTextureType {

        COW,
        PIG,
        CHICKEN,
        LLAMA,
        RABBIT,
        DOLPHIN,
        SLIME
    }
}