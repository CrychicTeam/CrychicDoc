package yesman.epicfight.world.capabilities.item;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.data.reloader.ItemCapabilityReloadListener;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.WeaponCapabilityPresetRegistryEvent;
import yesman.epicfight.data.conditions.Condition;
import yesman.epicfight.data.conditions.EpicFightConditions;
import yesman.epicfight.data.conditions.entity.LivingEntityCondition;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.network.server.SPDatapackSync;
import yesman.epicfight.particle.HitParticleType;

public class WeaponTypeReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private static final Map<ResourceLocation, Function<Item, CapabilityItem.Builder>> PRESETS = Maps.newHashMap();

    private static final Map<ResourceLocation, CompoundTag> TAGMAP = Maps.newHashMap();

    public static void registerDefaultWeaponTypes() {
        Map<ResourceLocation, Function<Item, CapabilityItem.Builder>> typeEntry = Maps.newHashMap();
        typeEntry.put(new ResourceLocation("epicfight", "axe"), WeaponCapabilityPresets.AXE);
        typeEntry.put(new ResourceLocation("epicfight", "fist"), WeaponCapabilityPresets.FIST);
        typeEntry.put(new ResourceLocation("epicfight", "hoe"), WeaponCapabilityPresets.HOE);
        typeEntry.put(new ResourceLocation("epicfight", "pickaxe"), WeaponCapabilityPresets.PICKAXE);
        typeEntry.put(new ResourceLocation("epicfight", "shovel"), WeaponCapabilityPresets.SHOVEL);
        typeEntry.put(new ResourceLocation("epicfight", "sword"), WeaponCapabilityPresets.SWORD);
        typeEntry.put(new ResourceLocation("epicfight", "spear"), WeaponCapabilityPresets.SPEAR);
        typeEntry.put(new ResourceLocation("epicfight", "greatsword"), WeaponCapabilityPresets.GREATSWORD);
        typeEntry.put(new ResourceLocation("epicfight", "uchigatana"), WeaponCapabilityPresets.UCHIGATANA);
        typeEntry.put(new ResourceLocation("epicfight", "tachi"), WeaponCapabilityPresets.TACHI);
        typeEntry.put(new ResourceLocation("epicfight", "longsword"), WeaponCapabilityPresets.LONGSWORD);
        typeEntry.put(new ResourceLocation("epicfight", "dagger"), WeaponCapabilityPresets.DAGGER);
        typeEntry.put(new ResourceLocation("epicfight", "bow"), WeaponCapabilityPresets.BOW);
        typeEntry.put(new ResourceLocation("epicfight", "crossbow"), WeaponCapabilityPresets.CROSSBOW);
        typeEntry.put(new ResourceLocation("epicfight", "trident"), WeaponCapabilityPresets.TRIDENT);
        typeEntry.put(new ResourceLocation("epicfight", "shield"), WeaponCapabilityPresets.SHIELD);
        WeaponCapabilityPresetRegistryEvent weaponCapabilityPresetRegistryEvent = new WeaponCapabilityPresetRegistryEvent(typeEntry);
        ModLoader.get().postEvent(weaponCapabilityPresetRegistryEvent);
        weaponCapabilityPresetRegistryEvent.getTypeEntry().forEach(PRESETS::put);
    }

    public WeaponTypeReloadListener() {
        super(GSON, "capabilities/weapons/types");
    }

    protected void apply(Map<ResourceLocation, JsonElement> packEntry, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        registerDefaultWeaponTypes();
        for (Entry<ResourceLocation, JsonElement> entry : packEntry.entrySet()) {
            CompoundTag nbt = null;
            try {
                nbt = TagParser.parseTag(((JsonElement) entry.getValue()).toString());
            } catch (CommandSyntaxException var9) {
                var9.printStackTrace();
            }
            try {
                WeaponCapability.Builder builder = deserializeWeaponCapabilityBuilder(nbt);
                PRESETS.put((ResourceLocation) entry.getKey(), (Function) itemstack -> builder);
                TAGMAP.put((ResourceLocation) entry.getKey(), nbt);
            } catch (Exception var8) {
                EpicFightMod.LOGGER.warn("Error while deserializing weapon type datapack: " + entry.getKey());
                var8.printStackTrace();
            }
        }
    }

    public static Function<Item, CapabilityItem.Builder> get(String typeName) {
        ResourceLocation rl = new ResourceLocation(typeName);
        if (!PRESETS.containsKey(rl)) {
            throw new IllegalArgumentException("Can't find weapon type: " + rl);
        } else {
            return (Function<Item, CapabilityItem.Builder>) PRESETS.get(rl);
        }
    }

    private static WeaponCapability.Builder deserializeWeaponCapabilityBuilder(CompoundTag tag) {
        WeaponCapability.Builder builder = WeaponCapability.builder();
        builder.category(WeaponCategory.ENUM_MANAGER.get(tag.getString("category")));
        builder.collider(ItemCapabilityReloadListener.deserializeCollider(tag.getCompound("collider")));
        if (tag.contains("hit_particle")) {
            ParticleType<?> particleType = ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(tag.getString("hit_particle")));
            if (particleType == null) {
                EpicFightMod.LOGGER.warn("Can't find particle type " + tag.getString("hit_particle"));
            } else if (!(particleType instanceof HitParticleType)) {
                EpicFightMod.LOGGER.warn(tag.getString("hit_particle") + " is not a hit particle type");
            } else {
                builder.hitParticle((HitParticleType) particleType);
            }
        }
        if (tag.contains("swing_sound")) {
            SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(tag.getString("swing_sound")));
            if (sound == null) {
                EpicFightMod.LOGGER.warn("Can't find swing sound " + tag.getString("swing_sound"));
            } else {
                builder.swingSound(sound);
            }
        }
        if (tag.contains("hit_sound")) {
            SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(tag.getString("hit_sound")));
            if (sound == null) {
                EpicFightMod.LOGGER.warn("Can't find hit sound " + tag.getString("hit_sound"));
            } else {
                builder.hitSound(sound);
            }
        }
        CompoundTag combosTag = tag.getCompound("combos");
        for (String key : combosTag.getAllKeys()) {
            Style style = Style.ENUM_MANAGER.get(key);
            ListTag comboAnimations = combosTag.getList(key, 8);
            StaticAnimation[] animArray = new StaticAnimation[comboAnimations.size()];
            for (int i = 0; i < comboAnimations.size(); i++) {
                animArray[i] = EpicFightMod.getInstance().animationManager.findAnimationByPath(comboAnimations.getString(i));
            }
            builder.newStyleCombo(style, animArray);
        }
        CompoundTag innateSkillsTag = tag.getCompound("innate_skills");
        for (String key : innateSkillsTag.getAllKeys()) {
            Style style = Style.ENUM_MANAGER.get(key);
            builder.innateSkill(style, itemstack -> SkillManager.getSkill(innateSkillsTag.getString(key)));
        }
        CompoundTag livingmotionModifierTag = tag.getCompound("livingmotion_modifier");
        for (String sStyle : livingmotionModifierTag.getAllKeys()) {
            Style style = Style.ENUM_MANAGER.get(sStyle);
            CompoundTag styleAnimationTag = livingmotionModifierTag.getCompound(sStyle);
            for (String sLivingmotion : styleAnimationTag.getAllKeys()) {
                LivingMotion livingmotion = LivingMotion.ENUM_MANAGER.get(sLivingmotion);
                StaticAnimation animation = EpicFightMod.getInstance().animationManager.findAnimationByPath(styleAnimationTag.getString(sLivingmotion));
                builder.livingMotionModifier(style, livingmotion, animation);
            }
        }
        CompoundTag stylesTag = tag.getCompound("styles");
        StyleEntry styleEntry = new StyleEntry();
        stylesTag.getList("cases", 10);
        for (Tag caseTag : stylesTag.getList("cases", 10)) {
            CompoundTag caseCompTag = (CompoundTag) caseTag;
            Function<CompoundTag, LivingEntityCondition> conditionProvider = EpicFightConditions.getConditionOrThrow(new ResourceLocation(caseCompTag.getString("condition")));
            LivingEntityCondition condition = Condition.ConditionBuilder.<LivingEntityCondition>builder(conditionProvider).setTag(caseCompTag.getCompound("predicate")).build();
            Style style = Style.ENUM_MANAGER.get(caseCompTag.getString("style"));
            styleEntry.putNewEntry(condition, style);
        }
        styleEntry.elseStyle = Style.ENUM_MANAGER.get(stylesTag.getString("default"));
        builder.styleProvider(styleEntry::getStyle);
        CompoundTag offhandValidatorTag = tag.getCompound("offhand_item_compatible_predicate");
        Function<CompoundTag, LivingEntityCondition> conditionProvider = EpicFightConditions.getConditionOrThrow(new ResourceLocation(offhandValidatorTag.getString("condition")));
        builder.weaponCombinationPredicator(((LivingEntityCondition) Condition.ConditionBuilder.builder(conditionProvider).setTag(offhandValidatorTag.getCompound("predicate")).build())::predicate);
        return builder;
    }

    public static int getTagCount() {
        return TAGMAP.size();
    }

    public static Stream<CompoundTag> getWeaponTypeDataStream() {
        return TAGMAP.entrySet().stream().map(entry -> {
            ((CompoundTag) entry.getValue()).putString("registry_name", ((ResourceLocation) entry.getKey()).toString());
            return (CompoundTag) entry.getValue();
        });
    }

    public static void clear() {
        PRESETS.clear();
    }

    @OnlyIn(Dist.CLIENT)
    public static void processServerPacket(SPDatapackSync packet) {
        if (packet.getType() == SPDatapackSync.Type.WEAPON_TYPE) {
            PRESETS.clear();
            registerDefaultWeaponTypes();
            for (CompoundTag tag : packet.getTags()) {
                PRESETS.put(new ResourceLocation(tag.getString("registry_name")), (Function) itemstack -> deserializeWeaponCapabilityBuilder(tag));
            }
            ItemCapabilityReloadListener.weaponTypeProcessedCheck();
        }
    }
}