package harmonised.pmmo.core.perks;

import harmonised.pmmo.api.perks.Perk;
import harmonised.pmmo.client.utils.DP;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.TagBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class PerksImpl {

    private static Set<ToolAction> DIG_ACTIONS = Set.of(ToolActions.PICKAXE_DIG, ToolActions.AXE_DIG, ToolActions.SHOVEL_DIG, ToolActions.HOE_DIG, ToolActions.SHEARS_DIG, ToolActions.SWORD_DIG);

    private static final CompoundTag NONE = new CompoundTag();

    public static Perk BREAK_SPEED = Perk.begin().addDefaults(getDefaults()).setStart((player, nbt) -> {
        float speedBonus = getRatioForTool(player.m_21205_(), nbt);
        if (speedBonus == 0.0F) {
            return NONE;
        } else {
            float existingSpeedModification = nbt.getFloat("speed");
            float speedModification = Math.max(0.0F, (float) nbt.getInt("level") * speedBonus) + existingSpeedModification;
            speedModification = Math.min((float) nbt.getInt("max_boost"), speedModification);
            return TagBuilder.start().withFloat("speed", speedModification).build();
        }
    }).setDescription(LangProvider.PERK_BREAK_SPEED_DESC.asComponent()).setStatus((player, settings) -> {
        List<MutableComponent> lines = new ArrayList();
        int skillLevel = settings.getInt("level");
        DIG_ACTIONS.stream().filter(action -> settings.getFloat(action.name()) > 0.0F).forEach(action -> lines.add(LangProvider.PERK_BREAK_SPEED_STATUS_1.asComponent(action.name(), settings.getFloat(action.name()) * (float) skillLevel)));
        return lines;
    }).build();

    private static final UUID ATTRIBUTE_ID = UUID.fromString("b902b6aa-8393-4bdc-8f0d-b937268ef5af");

    private static final Map<Attribute, Double> ANIMAL_ATTRIBUTES = Map.of(Attributes.JUMP_STRENGTH, 0.005, Attributes.MAX_HEALTH, 1.0, Attributes.MOVEMENT_SPEED, 0.01, Attributes.ARMOR, 0.01, Attributes.ATTACK_DAMAGE, 0.01);

    public static final String ANIMAL_ID = "tamed";

    public static final Perk TAME_BOOST = Perk.begin().addDefaults(TagBuilder.start().withString("skill", "taming").withDouble("per_level", 1.0).withDouble("max_boost", Double.MAX_VALUE).build()).setStart((player, nbt) -> {
        if (player.m_9236_() instanceof ServerLevel) {
            ServerLevel world = (ServerLevel) player.m_9236_();
            UUID animalID = nbt.getUUID("tamed");
            LivingEntity animal = (LivingEntity) world.getEntities().get(animalID);
            if (animal == null) {
                return NONE;
            }
            double perLevel = nbt.getDouble("per_level");
            for (Entry<Attribute, Double> atr : ANIMAL_ATTRIBUTES.entrySet()) {
                AttributeInstance instance = animal.getAttribute((Attribute) atr.getKey());
                if (instance != null) {
                    double boost = Mth.clamp(perLevel * (Double) atr.getValue() * (double) nbt.getInt("level"), 0.0, nbt.getDouble("max_boost"));
                    AttributeModifier modifier = new AttributeModifier(ATTRIBUTE_ID, "Taming boost", boost, AttributeModifier.Operation.ADDITION);
                    instance.addPermanentModifier(modifier);
                }
            }
        }
        return NONE;
    }).setDescription(LangProvider.PERK_TAME_BOOST_DESC.asComponent()).setStatus((player, settings) -> {
        List<MutableComponent> lines = new ArrayList();
        double perLevel = settings.getDouble("per_level");
        for (Entry<Attribute, Double> atr : ANIMAL_ATTRIBUTES.entrySet()) {
            lines.add(LangProvider.PERK_TAME_BOOST_STATUS_1.asComponent(Component.translatable(((Attribute) atr.getKey()).getDescriptionId()), DP.dpCustom(perLevel * (Double) atr.getValue(), 4)));
        }
        return lines;
    }).build();

    private static float getRatioForTool(ItemStack tool, CompoundTag nbt) {
        float ratio = 0.0F;
        for (ToolAction action : DIG_ACTIONS) {
            if (tool.canPerformAction(action)) {
                ratio += nbt.getFloat(action.name());
            }
        }
        return ratio;
    }

    public static CompoundTag getDefaults() {
        TagBuilder builder = TagBuilder.start();
        builder.withInt("max_boost", Integer.MAX_VALUE);
        for (ToolAction action : DIG_ACTIONS) {
            builder.withFloat(action.name(), 0.0F);
        }
        return builder.build();
    }
}