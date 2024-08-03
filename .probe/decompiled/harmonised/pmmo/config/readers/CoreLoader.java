package harmonised.pmmo.config.readers;

import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.config.codecs.DataSource;
import harmonised.pmmo.config.codecs.EnhancementsData;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.config.codecs.ObjectData;
import harmonised.pmmo.config.codecs.PlayerData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import java.util.List;
import java.util.Map;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE)
public class CoreLoader {

    private static final Logger DATA_LOGGER = LogManager.getLogger();

    public static final ExecutableListener RELOADER = new ExecutableListener(() -> Core.get(LogicalSide.SERVER).getLoader().resetData());

    public final MergeableCodecDataManager<ObjectData, Item> ITEM_LOADER = new MergeableCodecDataManager<>("pmmo/items", DATA_LOGGER, ObjectData.CODEC, this::mergeLoaderData, this::printData, ObjectData::new, Registries.ITEM);

    public final MergeableCodecDataManager<ObjectData, Block> BLOCK_LOADER = new MergeableCodecDataManager<>("pmmo/blocks", DATA_LOGGER, ObjectData.CODEC, this::mergeLoaderData, this::printData, ObjectData::new, Registries.BLOCK);

    public final MergeableCodecDataManager<ObjectData, EntityType<?>> ENTITY_LOADER = new MergeableCodecDataManager<>("pmmo/entities", DATA_LOGGER, ObjectData.CODEC, this::mergeLoaderData, this::printData, ObjectData::new, Registries.ENTITY_TYPE);

    public final MergeableCodecDataManager<LocationData, Biome> BIOME_LOADER = new MergeableCodecDataManager<>("pmmo/biomes", DATA_LOGGER, LocationData.CODEC, this::mergeLoaderData, this::printData, LocationData::new, Registries.BIOME);

    public final MergeableCodecDataManager<LocationData, Level> DIMENSION_LOADER = new MergeableCodecDataManager<>("pmmo/dimensions", DATA_LOGGER, LocationData.CODEC, this::mergeLoaderData, this::printData, LocationData::new, Registries.DIMENSION);

    public final MergeableCodecDataManager<PlayerData, Player> PLAYER_LOADER = new MergeableCodecDataManager<>("pmmo/players", DATA_LOGGER, PlayerData.CODEC, this::mergeLoaderData, this::printData, PlayerData::new, null);

    public final MergeableCodecDataManager<EnhancementsData, Enchantment> ENCHANTMENT_LOADER = new MergeableCodecDataManager<>("pmmo/enchantments", DATA_LOGGER, EnhancementsData.CODEC, this::mergeLoaderData, this::printData, EnhancementsData::new, Registries.ENCHANTMENT);

    public final MergeableCodecDataManager<EnhancementsData, MobEffect> EFFECT_LOADER = new MergeableCodecDataManager<>("pmmo/effects", DATA_LOGGER, EnhancementsData.CODEC, this::mergeLoaderData, this::printData, EnhancementsData::new, Registries.MOB_EFFECT);

    @SubscribeEvent
    public static void onTagLoad(TagsUpdatedEvent event) {
        Core core = Core.get(event.getUpdateCause() == TagsUpdatedEvent.UpdateCause.CLIENT_PACKET_RECEIVED ? LogicalSide.CLIENT : LogicalSide.SERVER);
        core.getLoader().ITEM_LOADER.postProcess(event.getRegistryAccess());
        core.getLoader().BLOCK_LOADER.postProcess(event.getRegistryAccess());
        core.getLoader().ENTITY_LOADER.postProcess(event.getRegistryAccess());
        core.getLoader().BIOME_LOADER.postProcess(event.getRegistryAccess());
    }

    public <T extends DataSource<T>> void applyData(ObjectType type, Map<ResourceLocation, T> data) {
        switch(type) {
            case ITEM:
                this.ITEM_LOADER.data.putAll(data);
                break;
            case BLOCK:
                this.BLOCK_LOADER.data.putAll(data);
                break;
            case ENTITY:
                this.ENTITY_LOADER.data.putAll(data);
                break;
            case DIMENSION:
                this.DIMENSION_LOADER.data.putAll(data);
                break;
            case BIOME:
                this.BIOME_LOADER.data.putAll(data);
                break;
            case PLAYER:
                this.PLAYER_LOADER.data.putAll(data);
                break;
            case ENCHANTMENT:
                this.ENCHANTMENT_LOADER.data.putAll(data);
                break;
            case EFFECT:
                this.EFFECT_LOADER.data.putAll(data);
        }
        this.printData(data);
    }

    public MergeableCodecDataManager<?, ?> getLoader(ObjectType type) {
        return switch(type) {
            case ITEM ->
                this.ITEM_LOADER;
            case BLOCK ->
                this.BLOCK_LOADER;
            case ENTITY ->
                this.ENTITY_LOADER;
            case DIMENSION ->
                this.DIMENSION_LOADER;
            case BIOME ->
                this.BIOME_LOADER;
            case PLAYER ->
                this.PLAYER_LOADER;
            case ENCHANTMENT ->
                this.ENCHANTMENT_LOADER;
            case EFFECT ->
                this.EFFECT_LOADER;
            default ->
                null;
        };
    }

    public MergeableCodecDataManager<?, ?> getLoader(ModifierDataType type) {
        return switch(type) {
            case WORN, HELD ->
                this.ITEM_LOADER;
            case DIMENSION ->
                this.DIMENSION_LOADER;
            case BIOME ->
                this.BIOME_LOADER;
            default ->
                null;
        };
    }

    public void resetData() {
        this.ITEM_LOADER.clearData();
        this.BLOCK_LOADER.clearData();
        this.ENTITY_LOADER.clearData();
        this.BIOME_LOADER.clearData();
        this.DIMENSION_LOADER.clearData();
        this.PLAYER_LOADER.clearData();
        this.ENCHANTMENT_LOADER.clearData();
        this.EFFECT_LOADER.clearData();
    }

    private <T extends DataSource<T>> T mergeLoaderData(List<T> raws) {
        T out = (T) raws.stream().reduce((existing, element) -> existing.combine(element)).get();
        return out.isUnconfigured() ? null : out;
    }

    private void printData(Map<ResourceLocation, ? extends Record> data) {
        data.forEach((id, value) -> {
            if (id != null && value != null) {
                MsLoggy.INFO.log(MsLoggy.LOG_CODE.DATA, "Object: {} with Data: {}", id.toString(), value.toString());
            }
        });
    }
}