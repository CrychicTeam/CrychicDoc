package net.mehvahdjukaar.moonlight.api.set.wood;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.events.AfterLanguageLoadEvent;
import net.mehvahdjukaar.moonlight.api.set.BlockTypeRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WoodTypeRegistry extends BlockTypeRegistry<WoodType> {

    public static final WoodType OAK_TYPE = new WoodType(new ResourceLocation("oak"), Blocks.OAK_PLANKS, Blocks.OAK_LOG);

    public static final WoodTypeRegistry INSTANCE = new WoodTypeRegistry();

    private final Map<net.minecraft.world.level.block.state.properties.WoodType, WoodType> fromVanilla = new IdentityHashMap();

    public static Set<String> IGNORED_MODS = new HashSet(Set.of("chipped", "securitycraft", "absentbydesign", "immersive_weathering"));

    private static final List<net.minecraft.world.level.block.state.properties.WoodType> VANILLA_ORDER = List.of(net.minecraft.world.level.block.state.properties.WoodType.OAK, net.minecraft.world.level.block.state.properties.WoodType.SPRUCE, net.minecraft.world.level.block.state.properties.WoodType.BIRCH, net.minecraft.world.level.block.state.properties.WoodType.JUNGLE, net.minecraft.world.level.block.state.properties.WoodType.ACACIA, net.minecraft.world.level.block.state.properties.WoodType.DARK_OAK, net.minecraft.world.level.block.state.properties.WoodType.MANGROVE, net.minecraft.world.level.block.state.properties.WoodType.CHERRY, net.minecraft.world.level.block.state.properties.WoodType.BAMBOO, net.minecraft.world.level.block.state.properties.WoodType.CRIMSON, net.minecraft.world.level.block.state.properties.WoodType.WARPED);

    public static Collection<WoodType> getTypes() {
        return INSTANCE.getValues();
    }

    @Nullable
    public static WoodType getValue(ResourceLocation name) {
        return INSTANCE.get(name);
    }

    public static WoodType fromNBT(String name) {
        return INSTANCE.getFromNBT(name);
    }

    public static WoodType fromVanilla(net.minecraft.world.level.block.state.properties.WoodType vanillaType) {
        return INSTANCE.getFromVanilla(vanillaType);
    }

    public WoodTypeRegistry() {
        super(WoodType.class, "wood_type");
        this.addFinder(() -> {
            WoodType b = new WoodType(new ResourceLocation("bamboo"), Blocks.BAMBOO_PLANKS, Blocks.BAMBOO_BLOCK);
            b.addChild("stripped_log", Blocks.STRIPPED_BAMBOO_BLOCK);
            return Optional.of(b);
        });
    }

    public WoodType getDefaultType() {
        return OAK_TYPE;
    }

    @Override
    public Optional<WoodType> detectTypeFromBlock(Block baseBlock, ResourceLocation baseRes) {
        String name = null;
        String path = baseRes.getPath();
        if (!baseRes.getNamespace().equals("tfc") && !baseRes.getNamespace().equals("afc")) {
            if (path.endsWith("_planks")) {
                name = path.substring(0, path.length() - "_planks".length());
            } else if (path.startsWith("planks_")) {
                name = path.substring("planks_".length());
            } else if (path.endsWith("_plank")) {
                name = path.substring(0, path.length() - "_plank".length());
            } else if (path.startsWith("plank_")) {
                name = path.substring("plank_".length());
            }
            String namespace = baseRes.getNamespace();
            if (name != null && !IGNORED_MODS.contains(namespace)) {
                BlockState state = baseBlock.defaultBlockState();
                if (state.m_61147_().size() <= 2 && !(baseBlock instanceof SlabBlock)) {
                    name = name.replace("/", "_");
                    ResourceLocation id = new ResourceLocation(baseRes.getNamespace(), name);
                    Block logBlock = findLog(id);
                    if (logBlock != null) {
                        return Optional.of(new WoodType(id, baseBlock, logBlock));
                    }
                }
            }
            return Optional.empty();
        } else {
            if (path.contains("wood/planks/")) {
                Optional<Block> log = BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(baseRes.getNamespace(), path.replace("planks", "log")));
                if (log.isPresent()) {
                    ResourceLocation id = new ResourceLocation(baseRes.getNamespace(), path.replace("wood/planks/", ""));
                    return Optional.of(new WoodType(id, baseBlock, (Block) log.get()));
                }
            }
            return Optional.empty();
        }
    }

    @Nullable
    private static Block findLog(ResourceLocation id) {
        List<String> keywords = List.of("log", "stem", "stalk", "hyphae");
        List<ResourceLocation> resources = new ArrayList();
        for (String keyword : keywords) {
            resources.add(new ResourceLocation(id.getNamespace(), id.getPath() + "_" + keyword));
            resources.add(new ResourceLocation(id.getNamespace(), keyword + "_" + id.getPath()));
            resources.add(new ResourceLocation(id.getPath() + "_" + keyword));
            resources.add(new ResourceLocation(keyword + "_" + id.getPath()));
        }
        ResourceLocation[] test = (ResourceLocation[]) resources.toArray(new ResourceLocation[0]);
        Block temp = null;
        for (ResourceLocation r : test) {
            if (BuiltInRegistries.BLOCK.m_7804_(r)) {
                temp = BuiltInRegistries.BLOCK.get(r);
                break;
            }
        }
        return temp;
    }

    @Override
    public void addTypeTranslations(AfterLanguageLoadEvent language) {
        this.getValues().forEach(w -> {
            if (language.isDefault()) {
                language.addEntry(w.getTranslationKey(), w.getReadableName());
            }
        });
    }

    @Nullable
    public WoodType getFromVanilla(net.minecraft.world.level.block.state.properties.WoodType woodType) {
        return (WoodType) this.fromVanilla.get(woodType);
    }

    @Override
    protected void finalizeAndFreeze() {
        for (WoodType w : this.builder) {
            net.minecraft.world.level.block.state.properties.WoodType vanilla = w.toVanilla();
            if (vanilla != null) {
                this.fromVanilla.put(vanilla, w);
            }
        }
        List<WoodType> temp = new ArrayList(this.builder);
        this.builder.clear();
        for (net.minecraft.world.level.block.state.properties.WoodType v : VANILLA_ORDER) {
            for (WoodType t : temp) {
                if (t.toVanilla() == v) {
                    this.builder.add(t);
                    temp.remove(t);
                    break;
                }
            }
        }
        this.builder.addAll(temp);
        super.finalizeAndFreeze();
    }
}