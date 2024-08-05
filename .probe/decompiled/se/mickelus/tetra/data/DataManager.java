package se.mickelus.tetra.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.math.Transformation;
import java.util.Arrays;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import se.mickelus.mutil.data.DataDistributor;
import se.mickelus.mutil.data.DataStore;
import se.mickelus.mutil.data.deserializer.BlockDeserializer;
import se.mickelus.mutil.data.deserializer.BlockPosDeserializer;
import se.mickelus.mutil.data.deserializer.ItemDeserializer;
import se.mickelus.mutil.data.deserializer.ResourceLocationDeserializer;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.aspect.ItemAspect;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.blocks.workbench.action.ConfigActionImpl;
import se.mickelus.tetra.blocks.workbench.unlocks.UnlockData;
import se.mickelus.tetra.craftingeffect.CraftingEffect;
import se.mickelus.tetra.craftingeffect.condition.CraftingEffectCondition;
import se.mickelus.tetra.craftingeffect.outcome.CraftingEffectOutcome;
import se.mickelus.tetra.data.deserializer.AttributesDeserializer;
import se.mickelus.tetra.data.deserializer.EnchantmentDeserializer;
import se.mickelus.tetra.data.deserializer.GlyphDeserializer;
import se.mickelus.tetra.data.deserializer.ItemDisplayContextDeserializer;
import se.mickelus.tetra.data.deserializer.ItemPredicateDeserializer;
import se.mickelus.tetra.data.deserializer.ItemTagKeyDeserializer;
import se.mickelus.tetra.data.deserializer.ModuleModelDeserializer;
import se.mickelus.tetra.data.deserializer.PropertyMatcherDeserializer;
import se.mickelus.tetra.data.deserializer.QuaternionDeserializer;
import se.mickelus.tetra.data.deserializer.ReplacementDeserializer;
import se.mickelus.tetra.data.deserializer.TransformationDeserializer;
import se.mickelus.tetra.data.deserializer.VectorDeserializer;
import se.mickelus.tetra.items.modular.impl.dynamic.ArchetypeDefinition;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.ReplacementDefinition;
import se.mickelus.tetra.module.data.AspectData;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.EnchantmentMapping;
import se.mickelus.tetra.module.data.GlyphData;
import se.mickelus.tetra.module.data.ImprovementData;
import se.mickelus.tetra.module.data.MaterialColors;
import se.mickelus.tetra.module.data.MaterialData;
import se.mickelus.tetra.module.data.ModuleData;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.module.data.TweakData;
import se.mickelus.tetra.module.data.VariantData;
import se.mickelus.tetra.module.improvement.DestabilizationEffect;
import se.mickelus.tetra.module.schematic.OutcomeDefinition;
import se.mickelus.tetra.module.schematic.OutcomeMaterial;
import se.mickelus.tetra.module.schematic.RepairDefinition;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirement;
import se.mickelus.tetra.module.schematic.requirement.CraftingRequirementDeserializer;
import se.mickelus.tetra.module.schematic.requirement.IntegerPredicate;
import se.mickelus.tetra.module.schematic.requirement.ModuleRequirement;

@ParametersAreNonnullByDefault
public class DataManager implements DataDistributor {

    public static final Gson gson = new GsonBuilder().registerTypeAdapter(ToolData.class, new ToolData.Deserializer()).registerTypeAdapter(AspectData.class, new AspectData.Deserializer()).registerTypeAdapter(ItemAspect.class, new ItemAspect.Deserializer()).registerTypeAdapter(EffectData.class, new EffectData.Deserializer()).registerTypeAdapter(GlyphData.class, new GlyphDeserializer()).registerTypeAdapter(ModuleModel.class, new ModuleModelDeserializer()).registerTypeAdapter(Priority.class, new Priority.Deserializer()).registerTypeAdapter(ItemPredicate.class, new ItemPredicateDeserializer()).registerTypeAdapter(PropertyMatcher.class, new PropertyMatcherDeserializer()).registerTypeAdapter(MaterialData.class, new MaterialData.Deserializer()).registerTypeAdapter(OutcomeMaterial.class, new OutcomeMaterial.Deserializer()).registerTypeAdapter(ReplacementDefinition.class, new ReplacementDeserializer()).registerTypeAdapter(BlockPos.class, new BlockPosDeserializer()).registerTypeAdapter(Block.class, new BlockDeserializer()).registerTypeAdapter(AttributesDeserializer.typeToken.getRawType(), new AttributesDeserializer()).registerTypeAdapter(ItemTagKeyDeserializer.typeToken.getRawType(), new ItemTagKeyDeserializer()).registerTypeAdapter(VariantData.class, new VariantData.Deserializer()).registerTypeAdapter(ImprovementData.class, new ImprovementData.Deserializer()).registerTypeAdapter(OutcomeDefinition.class, new OutcomeDefinition.Deserializer()).registerTypeAdapter(MaterialColors.class, new MaterialColors.Deserializer()).registerTypeAdapter(CraftingEffectCondition.class, new CraftingEffectCondition.Deserializer()).registerTypeAdapter(CraftingEffectOutcome.class, new CraftingEffectOutcome.Deserializer()).registerTypeAdapter(CraftingRequirement.class, new CraftingRequirementDeserializer()).registerTypeAdapter(ModuleRequirement.class, new ModuleRequirement.Deserializer()).registerTypeAdapter(IntegerPredicate.class, new IntegerPredicate.Deserializer()).registerTypeAdapter(Item.class, new ItemDeserializer()).registerTypeAdapter(Enchantment.class, new EnchantmentDeserializer()).registerTypeAdapter(ResourceLocation.class, new ResourceLocationDeserializer()).registerTypeAdapter(Vector3f.class, new VectorDeserializer()).registerTypeAdapter(Quaternionf.class, new QuaternionDeserializer()).registerTypeAdapter(Transformation.class, new TransformationDeserializer()).registerTypeAdapter(ItemDisplayContext.class, new ItemDisplayContextDeserializer()).create();

    public static DataManager instance;

    public final DataStore<ResourceLocation[]> tierData;

    public final DataStore<TweakData[]> tweakData;

    public final MaterialStore materialData;

    public final DataStore<ImprovementData[]> improvementData;

    public final DataStore<ModuleData> moduleData;

    public final DataStore<RepairDefinition> repairData;

    public final DataStore<EnchantmentMapping[]> enchantmentData;

    public final DataStore<SynergyData[]> synergyData;

    public final DataStore<ReplacementDefinition[]> replacementData;

    public final SchematicStore schematicData;

    public final DataStore<CraftingEffect> craftingEffectData;

    public final DataStore<ConfigActionImpl[]> actionData;

    public final DataStore<DestabilizationEffect[]> destabilizationData;

    public final DataStore<UnlockData> unlockData;

    public final DataStore<ArchetypeDefinition> archetypeData;

    private final Logger logger = LogManager.getLogger();

    private final DataStore[] dataStores;

    public DataManager() {
        instance = this;
        this.tierData = new DataStore<>(gson, "tetra", "tiers", ResourceLocation[].class, this);
        this.tweakData = new DataStore<>(gson, "tetra", "tweaks", TweakData[].class, this);
        this.materialData = new MaterialStore(gson, "tetra", "materials", this);
        this.improvementData = new ImprovementStore(gson, "tetra", "improvements", this.materialData, this);
        this.moduleData = new ModuleStore(gson, "tetra", "modules", this);
        this.repairData = new DataStore<>(gson, "tetra", "repairs", RepairDefinition.class, this);
        this.enchantmentData = new DataStore<>(gson, "tetra", "enchantments", EnchantmentMapping[].class, this);
        this.synergyData = new DataStore<>(gson, "tetra", "synergies", SynergyData[].class, this);
        this.replacementData = new DataStore<>(gson, "tetra", "replacements", ReplacementDefinition[].class, this);
        this.schematicData = new SchematicStore(gson, "tetra", "schematics", this);
        this.craftingEffectData = new CraftingEffectStore(gson, "tetra", "crafting_effects", this);
        this.actionData = new DataStore<>(gson, "tetra", "actions", ConfigActionImpl[].class, this);
        this.destabilizationData = new DataStore<>(gson, "tetra", "destabilization", DestabilizationEffect[].class, this);
        this.unlockData = new DataStore<>(gson, "tetra", "unlocks", UnlockData.class, this);
        this.archetypeData = new DataStore<>(gson, "tetra", "archetypes", ArchetypeDefinition.class, this);
        this.dataStores = new DataStore[] { this.tierData, this.tweakData, this.materialData, this.improvementData, this.moduleData, this.enchantmentData, this.synergyData, this.replacementData, this.schematicData, this.craftingEffectData, this.repairData, this.actionData, this.destabilizationData, this.unlockData, this.archetypeData };
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void addReloadListener(AddReloadListenerEvent event) {
        this.logger.debug("Setting up datastore reload listeners");
        Arrays.stream(this.dataStores).forEach(event::addListener);
    }

    @SubscribeEvent
    public void tagsUpdated(TagsUpdatedEvent event) {
        this.logger.debug("Reloaded tags");
    }

    @SubscribeEvent
    public void playerConnected(PlayerEvent.PlayerLoggedInEvent event) {
        this.logger.info("Sending data to client: {}", event.getEntity().getName().getString());
        for (DataStore dataStore : this.dataStores) {
            dataStore.sendToPlayer((ServerPlayer) event.getEntity());
        }
    }

    public void onDataRecieved(String directory, Map<ResourceLocation, String> data) {
        Arrays.stream(this.dataStores).filter(dataStore -> dataStore.getDirectory().equals(directory)).forEach(dataStore -> dataStore.loadFromPacket(data));
    }

    public SynergyData[] getSynergyData(String path) {
        SynergyData[] data = (SynergyData[]) this.synergyData.getDataIn(new ResourceLocation("tetra", path)).stream().flatMap(Arrays::stream).toArray(SynergyData[]::new);
        for (SynergyData entry : data) {
            Arrays.sort(entry.moduleVariants);
            Arrays.sort(entry.modules);
        }
        return data;
    }

    @Override
    public void sendToAll(String directory, Map<ResourceLocation, JsonElement> data) {
        TetraMod.packetHandler.sendToAllPlayers(new UpdateDataPacket(directory, data));
    }

    @Override
    public void sendToPlayer(ServerPlayer player, String directory, Map<ResourceLocation, JsonElement> data) {
        TetraMod.packetHandler.sendTo(new UpdateDataPacket(directory, data), player);
    }
}