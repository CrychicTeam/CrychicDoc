package harmonised.pmmo.core.perks;

import com.google.common.collect.LinkedListMultimap;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.config.PerksConfig;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.Functions;
import harmonised.pmmo.util.RegistryUtil;
import harmonised.pmmo.util.TagBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import net.minecraft.commands.CommandFunction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.village.ReputationEventType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE)
public class FeaturePerks {

    private static final CompoundTag NONE = new CompoundTag();

    private static final Map<String, Attribute> attributeCache = new HashMap();

    public static final Perk ATTRIBUTE = Perk.begin().addDefaults(TagBuilder.start().withDouble("max_boost", 0.0).withDouble("per_level", 0.0).withDouble("base", 0.0).withBool("multiplicative", false).build()).setStart((player, nbt) -> {
        double perLevel = nbt.getDouble("per_level");
        double maxBoost = nbt.getDouble("max_boost");
        AttributeInstance instance = player.m_21051_(getAttribute(nbt));
        double boost = Math.min(perLevel * (double) nbt.getInt("level"), maxBoost) + nbt.getDouble("base");
        AttributeModifier.Operation operation = nbt.getBoolean("multiplicative") ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION;
        UUID attributeID = Functions.getReliableUUID(nbt.getString("attribute") + "/" + nbt.getString("skill"));
        AttributeModifier modifier = new AttributeModifier(attributeID, "PMMO-modifier based on user skill", boost, operation);
        instance.removeModifier(attributeID);
        instance.addPermanentModifier(modifier);
        return NONE;
    }).setDescription(LangProvider.PERK_ATTRIBUTE_DESC.asComponent()).setStatus((player, settings) -> {
        double perLevel = settings.getDouble("per_level");
        String skillname = settings.getString("skill");
        int skillLevel = settings.getInt("level");
        return List.of(LangProvider.PERK_ATTRIBUTE_STATUS_1.asComponent(Component.translatable(getAttribute(settings).getDescriptionId())), LangProvider.PERK_ATTRIBUTE_STATUS_2.asComponent(perLevel, Component.translatable("pmmo." + skillname)), LangProvider.PERK_ATTRIBUTE_STATUS_3.asComponent(perLevel * (double) skillLevel));
    }).build();

    private static final LinkedListMultimap<Player, FeaturePerks.AttributeRecord> respawnAttributes = LinkedListMultimap.create();

    public static final Perk TEMP_ATTRIBUTE = Perk.begin().addDefaults(ATTRIBUTE.propertyDefaults()).setStart((player, nbt) -> {
        double perLevel = nbt.getDouble("per_level");
        double maxBoost = nbt.getDouble("max_boost");
        AttributeInstance instance = player.m_21051_(getAttribute(nbt));
        double boost = Math.min(perLevel * (double) nbt.getInt("level"), maxBoost) + nbt.getDouble("base");
        AttributeModifier.Operation operation = nbt.getBoolean("multiplicative") ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION;
        UUID attributeID = Functions.getReliableUUID("temp/" + nbt.getString("attribute") + "/" + nbt.getString("skill"));
        AttributeModifier modifier = new AttributeModifier(attributeID, "temporary PMMO-modifier based on user skill", boost, operation);
        if (instance.hasModifier(modifier)) {
            instance.removeModifier(attributeID);
        }
        instance.addTransientModifier(modifier);
        return NONE;
    }).setStop((player, nbt) -> {
        UUID attributeID = Functions.getReliableUUID("temp/" + nbt.getString("attribute") + "/" + nbt.getString("skill"));
        player.m_21051_(getAttribute(nbt)).removeModifier(attributeID);
        return NONE;
    }).setDescription(ATTRIBUTE.description()).setStatus(ATTRIBUTE.status()).build();

    public static BiFunction<Player, CompoundTag, CompoundTag> EFFECT_SETTER = (player, nbt) -> {
        MobEffect effect;
        if ((effect = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(nbt.getString("effect")))) != null) {
            int skillLevel = nbt.getInt("level");
            int configDuration = nbt.getInt("duration");
            double perLevel = nbt.getDouble("per_level");
            int base = nbt.getInt("base");
            int calculatedDuration = (int) ((double) skillLevel * (double) configDuration * perLevel) + base;
            calculatedDuration = Math.min(nbt.getInt("max_boost"), calculatedDuration);
            int duration = player.m_21023_(effect) && player.m_21124_(effect).getDuration() > calculatedDuration ? player.m_21124_(effect).getDuration() : calculatedDuration;
            int amplifier = nbt.getInt("modifier");
            boolean ambient = nbt.getBoolean("ambient");
            boolean visible = nbt.getBoolean("visible");
            boolean showIcon = nbt.getBoolean("show_icon");
            player.m_7292_(new MobEffectInstance(effect, duration, amplifier, ambient, visible, showIcon));
        }
        return NONE;
    };

    public static final Perk EFFECT = Perk.begin().addDefaults(TagBuilder.start().withString("effect", "modid:effect").withInt("duration", 100).withDouble("base", 0.0).withInt("per_level", 1).withInt("min_level", 1).withInt("max_boost", Integer.MAX_VALUE).withInt("modifier", 0).withBool("ambient", false).withBool("visible", true).withBool("show_icon", true).build()).setStart(EFFECT_SETTER).setTick((player, nbt, ticks) -> (CompoundTag) EFFECT_SETTER.apply(player, nbt)).setDescription(LangProvider.PERK_EFFECT_DESC.asComponent()).setStatus((player, nbt) -> List.of(LangProvider.PERK_EFFECT_STATUS_1.asComponent(Component.translatable(ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(nbt.getString("effect"))).getDescriptionId())), LangProvider.PERK_EFFECT_STATUS_2.asComponent(nbt.getInt("modifier"), (double) nbt.getInt("duration") * nbt.getDouble("per_level") * (double) nbt.getInt("level") / 20.0))).build();

    private static BiFunction<Player, CompoundTag, List<MutableComponent>> JUMP_LINES = (player, nbt) -> List.of(LangProvider.PERK_JUMP_BOOST_STATUS_1.asComponent(nbt.getInt("per_level") * nbt.getInt("level")));

    private static CompoundTag JUMP_DEFAULTS = TagBuilder.start().withDouble("base", 0.0).withDouble("per_level", 5.0E-4).withDouble("max_boost", 0.25).build();

    public static final Perk JUMP_CLIENT = Perk.begin().addDefaults(JUMP_DEFAULTS).setStart((player, nbt) -> {
        double jumpBoost = Math.min(nbt.getDouble("max_boost"), -0.011 + (double) nbt.getInt("level") * nbt.getDouble("per_level")) + nbt.getDouble("base");
        player.m_20256_(player.m_20184_().add(0.0, jumpBoost, 0.0));
        player.f_19864_ = true;
        return NONE;
    }).setDescription(LangProvider.PERK_JUMP_BOOST_DESC.asComponent()).setStatus(JUMP_LINES).build();

    public static final Perk JUMP_SERVER = Perk.begin().addDefaults(JUMP_DEFAULTS).setStart((player, nbt) -> {
        double jumpBoost = Math.min(nbt.getDouble("max_boost"), -0.011 + (double) nbt.getInt("level") * nbt.getDouble("per_level")) + nbt.getDouble("base");
        return TagBuilder.start().withDouble("jump_boost_output", player.m_20184_().y + jumpBoost).build();
    }).setDescription(LangProvider.PERK_JUMP_BOOST_DESC.asComponent()).setStatus(JUMP_LINES).build();

    public static final Perk BREATH = Perk.begin().addConditions((player, nbt) -> player.m_20146_() < 2).addDefaults(TagBuilder.start().withLong("cooldown", 600L).withDouble("base", 0.0).withDouble("per_level", 1.0).withInt("max_boost", Integer.MAX_VALUE).build()).setStart((player, nbt) -> {
        int perLevel = Math.max(1, (int) ((double) nbt.getInt("level") * nbt.getDouble("per_level"))) + nbt.getInt("base");
        perLevel = Math.min(nbt.getInt("max_boost"), perLevel);
        player.m_20301_(player.m_20146_() + perLevel);
        player.m_213846_(LangProvider.PERK_BREATH_REFRESH.asComponent());
        return NONE;
    }).setDescription(LangProvider.PERK_BREATH_DESC.asComponent()).setStatus((player, nbt) -> List.of(LangProvider.PERK_BREATH_STATUS_1.asComponent((int) ((double) nbt.getInt("level") * nbt.getDouble("per_level"))), LangProvider.PERK_BREATH_STATUS_2.asComponent(nbt.getInt("cooldown") / 20))).build();

    public static final Perk DAMAGE_REDUCE = Perk.begin().addConditions((player, nbt) -> {
        String perkApplicableDamageType = nbt.getString("for_damage");
        Registry<DamageType> damageRegistry = (Registry<DamageType>) player.m_9236_().registryAccess().registry(Registries.DAMAGE_TYPE).get();
        ResourceKey<DamageType> resourceKey = ResourceKey.create(damageRegistry.key(), new ResourceLocation(nbt.getString("damage_type")));
        if (perkApplicableDamageType.startsWith("#") && damageRegistry.getTag(TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(perkApplicableDamageType.substring(1)))).stream().anyMatch(typeTag -> typeTag.contains((Holder) damageRegistry.getHolder(resourceKey).get()))) {
            return true;
        } else {
            return perkApplicableDamageType.endsWith(":*") && perkApplicableDamageType.substring(0, perkApplicableDamageType.indexOf(58)).equals(nbt.getString("damage_type").substring(0, nbt.getString("damage_type").indexOf(58))) ? true : perkApplicableDamageType.equals(nbt.getString("damage_type"));
        }
    }).addDefaults(TagBuilder.start().withDouble("per_level", 0.025).withDouble("base", 0.0).withFloat("damageIn", 0.0F).withString("damage_type", "missing").withInt("max_boost", Integer.MAX_VALUE).withString("for_damage", "omitted").build()).setStart((player, nbt) -> {
        float saved = (float) ((int) (nbt.getDouble("per_level") * (double) nbt.getInt("level")) + nbt.getInt("base"));
        saved = Math.min((float) nbt.getInt("max_boost"), saved);
        float baseDamage = nbt.contains("damage") ? nbt.getFloat("damage") : nbt.getFloat("damageIn");
        return TagBuilder.start().withFloat("damage", Math.max(baseDamage - saved, 0.0F)).build();
    }).setDescription(LangProvider.PERK_FALL_SAVE_DESC.asComponent()).setStatus((player, nbt) -> List.of(LangProvider.PERK_FALL_SAVE_STATUS_1.asComponent((double) nbt.getInt("level") * nbt.getDouble("per_level")), LangProvider.PERK_BREATH_STATUS_2.asComponent(nbt.getInt("cooldown") / 20))).build();

    public static final String APPLICABLE_TO = "applies_to";

    public static final Perk DAMAGE_BOOST = Perk.begin().addConditions((player, nbt) -> {
        if (nbt.getList("applies_to", 8).isEmpty()) {
            return true;
        } else {
            for (String key : nbt.getList("applies_to", 8).stream().map(Tag::m_7916_).toList()) {
                if (key.startsWith("#") && ForgeRegistries.ITEMS.tags().getTag(TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(key.substring(1)))).stream().anyMatch(item -> player.m_21205_().getItem().equals(item))) {
                    return true;
                }
                if (key.endsWith(":*") && ForgeRegistries.ITEMS.getValues().stream().anyMatch(item -> player.m_21205_().getItem().equals(item))) {
                    return true;
                }
                if (key.equals(RegistryUtil.getId(player.m_21205_()).toString())) {
                    return true;
                }
            }
            return false;
        }
    }).addDefaults(TagBuilder.start().withFloat("damageIn", 0.0F).withFloat("damage", 0.0F).withList("applies_to").withDouble("per_level", 0.05).withDouble("base", 1.0).withInt("max_boost", Integer.MAX_VALUE).withBool("multiplicative", true).build()).setStart((player, nbt) -> {
        float damageModification = (float) (nbt.getDouble("base") + nbt.getDouble("per_level") * (double) nbt.getInt("level"));
        damageModification = Math.min((float) nbt.getInt("max_boost"), damageModification);
        float damage = nbt.getBoolean("multiplicative") ? nbt.getFloat("damage") * damageModification : nbt.getFloat("damage") + damageModification;
        return TagBuilder.start().withFloat("damage", damage).build();
    }).setDescription(LangProvider.PERK_DAMAGE_BOOST_DESC.asComponent()).setStatus((player, nbt) -> {
        List<MutableComponent> lines = new ArrayList();
        MutableComponent line1 = LangProvider.PERK_DAMAGE_BOOST_STATUS_1.asComponent();
        for (Tag entry : nbt.getList("applies_to", 8)) {
            Component description = Component.literal(entry.getAsString());
            if (ResourceLocation.isValidResourceLocation(entry.getAsString())) {
                Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(entry.getAsString()));
                if (item != null && !item.equals(Items.AIR)) {
                    description = item.getDescription();
                }
            }
            line1.append(description);
            line1.append(Component.literal(", "));
        }
        lines.add(line1);
        lines.add(LangProvider.PERK_DAMAGE_BOOST_STATUS_2.asComponent(nbt.getBoolean("multiplicative") ? "x" : "+", (double) nbt.getInt("level") * nbt.getDouble("per_level")));
        return lines;
    }).build();

    private static final String COMMAND = "command";

    private static final String FUNCTION = "function";

    public static final Perk RUN_COMMAND = Perk.begin().setStart((p, nbt) -> {
        if (!(p instanceof ServerPlayer player)) {
            return NONE;
        } else {
            if (nbt.contains("function")) {
                player.m_20194_().getFunctions().execute((CommandFunction) player.m_20194_().getFunctions().get(new ResourceLocation(nbt.getString("function"))).get(), player.m_20203_().withSuppressedOutput().withMaximumPermission(2));
            } else if (nbt.contains("command")) {
                player.m_20194_().getCommands().performPrefixedCommand(player.m_20203_().withSuppressedOutput().withMaximumPermission(2), nbt.getString("command"));
            }
            return NONE;
        }
    }).setDescription(LangProvider.PERK_COMMAND_DESC.asComponent()).setStatus((player, nbt) -> List.of(LangProvider.PERK_COMMAND_STATUS_1.asComponent(nbt.contains("function") ? "Function" : "Command", nbt.contains("function") ? nbt.getString("function") : nbt.getString("command")))).build();

    public static final Perk VILLAGER_TRADING = Perk.begin().addConditions((player, tag) -> tag.getString("target").equals("minecraft:villager")).addDefaults(TagBuilder.start().withString("target", "missing").withDouble("base", 0.0).withInt("entity_id", -1).withDouble("per_level", 0.05).withLong("cooldown", 1000L).build()).setStart((player, nbt) -> {
        int villagerID = nbt.getInt("entity_id");
        Villager villager = (Villager) player.m_9236_().getEntity(villagerID);
        villager.onReputationEventFrom(ReputationEventType.ZOMBIE_VILLAGER_CURED, player);
        player.m_213846_(LangProvider.PERK_VILLAGE_FEEDBACK.asComponent());
        return NONE;
    }).setDescription(LangProvider.PERK_VILLAGER_DESC.asComponent()).setStatus((player, nbt) -> List.of(LangProvider.PERK_VILLAGE_STATUS_1.asComponent((double) nbt.getInt("level") * nbt.getDouble("per_level")))).build();

    private static Attribute getAttribute(CompoundTag nbt) {
        return (Attribute) attributeCache.computeIfAbsent(nbt.getString("attribute"), name -> ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(name)));
    }

    @SubscribeEvent
    public static void saveAttributesOnDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            for (CompoundTag nbt : ((List) PerksConfig.PERK_SETTINGS.get().get(EventType.SKILL_UP)).stream().filter(tag -> tag.getString("perk").equals("pmmo:attribute")).toList()) {
                Attribute attribute = getAttribute(nbt);
                player.m_21204_().getInstance(attribute).getModifiers().stream().filter(mod -> mod.getId().equals(Functions.getReliableUUID(nbt.getString("attribute") + "/" + nbt.getString("skill")))).forEach(mod -> respawnAttributes.put(player, new FeaturePerks.AttributeRecord(attribute, mod)));
            }
        }
    }

    @SubscribeEvent
    public static void restoreAttributesOnSpawn(PlayerEvent.PlayerRespawnEvent event) {
        if (respawnAttributes.containsKey(event.getEntity())) {
            respawnAttributes.get(event.getEntity()).stream().filter(mod -> !event.getEntity().m_21051_(mod.attribute()).hasModifier(mod.modifier())).forEach(mod -> event.getEntity().m_21204_().getInstance(mod.attribute()).addPermanentModifier(mod.modifier()));
            respawnAttributes.get(event.getEntity()).clear();
        }
    }

    private static record AttributeRecord(Attribute attribute, AttributeModifier modifier) {
    }
}