package net.mehvahdjukaar.moonlight.api.set.wood;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class WoodType extends BlockType {

    public static final Codec<WoodType> CODEC = ResourceLocation.CODEC.flatXmap(r -> {
        WoodType w = WoodTypeRegistry.getValue(r);
        return w == null ? DataResult.error(() -> "No such wood type: " + r) : DataResult.success(w);
    }, t -> DataResult.success(t.id));

    public final Block planks;

    public final Block log;

    private final Supplier<net.minecraft.world.level.block.state.properties.WoodType> vanillaType = Suppliers.memoize(this::detectVanillaWood);

    @Nullable
    private net.minecraft.world.level.block.state.properties.WoodType detectVanillaWood() {
        if (this.getChild("hanging_sign") instanceof CeilingHangingSignBlock c) {
            return c.m_56297_();
        } else if (this.getChild("sign") instanceof SignBlock f) {
            return f.type();
        } else {
            String i = this.id.getNamespace().equals("minecraft") ? this.id.getPath() : this.id.toString();
            Stream<net.minecraft.world.level.block.state.properties.WoodType> values = net.minecraft.world.level.block.state.properties.WoodType.values();
            Optional<net.minecraft.world.level.block.state.properties.WoodType> o = values.filter(v -> v.name().equals(i)).findAny();
            return (net.minecraft.world.level.block.state.properties.WoodType) o.orElse(null);
        }
    }

    public WoodType(ResourceLocation id, Block baseBlock, Block logBlock) {
        super(id);
        this.planks = baseBlock;
        this.log = logBlock;
    }

    @Nullable
    protected Block findStrippedLog(String... possibleNames) {
        for (String v : possibleNames) {
            Block b = this.getBlockOfThis(v);
            if (v != null) {
                Block stripped = (Block) AxeItem.STRIPPABLES.get(b);
                if (stripped != null && stripped != b) {
                    return stripped;
                }
            }
        }
        return this.findLogRelatedBlock("stripped", possibleNames);
    }

    @Nullable
    protected Block findLogRelatedBlock(String prefix, String... possibleNames) {
        for (String n : possibleNames) {
            Block b = this.findWithPrefix(prefix, n);
            if (b != null) {
                return b;
            }
        }
        return null;
    }

    @Nullable
    protected Block findWithPrefix(String prefix, String postfix) {
        String prefix_ = prefix.isEmpty() ? "" : prefix + "_";
        ResourceLocation id = this.getId();
        String logN = Utils.getID(this.log).getPath();
        String path = id.getPath();
        String namespace = id.getNamespace();
        if (this.id.getNamespace().equals("tfc") || this.id.getNamespace().equals("afc")) {
            Optional<Block> o = BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(namespace, "wood/" + prefix_ + postfix + "/" + path));
            if (o.isPresent()) {
                return (Block) o.get();
            }
        }
        Set<ResourceLocation> targets = new HashSet();
        Collections.addAll(targets, new ResourceLocation[] { new ResourceLocation(namespace, path + "_" + prefix_ + postfix), new ResourceLocation(namespace, prefix_ + path + "_" + postfix), new ResourceLocation(namespace, logN + "_" + prefix_ + postfix), new ResourceLocation(namespace, prefix_ + logN + "_" + postfix) });
        Block found = null;
        for (ResourceLocation r : targets) {
            if (BuiltInRegistries.BLOCK.m_7804_(r)) {
                found = BuiltInRegistries.BLOCK.get(r);
                break;
            }
        }
        return found;
    }

    @Override
    public ItemLike mainChild() {
        return this.planks;
    }

    @Nullable
    public net.minecraft.world.level.block.state.properties.WoodType toVanilla() {
        return (net.minecraft.world.level.block.state.properties.WoodType) this.vanillaType.get();
    }

    @NotNull
    public net.minecraft.world.level.block.state.properties.WoodType toVanillaOrOak() {
        net.minecraft.world.level.block.state.properties.WoodType v = this.toVanilla();
        return v != null ? v : net.minecraft.world.level.block.state.properties.WoodType.OAK;
    }

    public String getTexturePath() {
        String namespace = this.getNamespace();
        return namespace.equals("minecraft") ? this.getTypeName() : this.getNamespace() + "/" + this.getTypeName();
    }

    public boolean canBurn() {
        return this.planks.defaultBlockState().m_278200_();
    }

    public MapColor getColor() {
        return this.planks.m_284356_();
    }

    @Override
    public String getTranslationKey() {
        return "wood_type." + this.getNamespace() + "." + this.getTypeName();
    }

    @Override
    public void initializeChildrenBlocks() {
        this.addChild("planks", this.planks);
        this.addChild("log", this.log);
        this.addChild("leaves", this.findRelatedEntry("leaves", BuiltInRegistries.BLOCK));
        this.addChild("wood", this.findLogRelatedBlock("", "wood", "hyphae"));
        this.addChild("stripped_log", this.findStrippedLog("log", "stem", "stalk"));
        this.addChild("stripped_wood", this.findStrippedLog("wood", "hyphae"));
        this.addChild("slab", this.findRelatedEntry("slab", BuiltInRegistries.BLOCK));
        this.addChild("stairs", this.findRelatedEntry("stairs", BuiltInRegistries.BLOCK));
        this.addChild("fence", this.findRelatedEntry("fence", BuiltInRegistries.BLOCK));
        this.addChild("fence_gate", this.findRelatedEntry("fence_gate", BuiltInRegistries.BLOCK));
        this.addChild("door", this.findRelatedEntry("door", BuiltInRegistries.BLOCK));
        this.addChild("trapdoor", this.findRelatedEntry("trapdoor", BuiltInRegistries.BLOCK));
        this.addChild("button", this.findRelatedEntry("button", BuiltInRegistries.BLOCK));
        this.addChild("pressure_plate", this.findRelatedEntry("pressure_plate", BuiltInRegistries.BLOCK));
        this.addChild("hanging_sign", this.findRelatedEntry("hanging_sign", BuiltInRegistries.BLOCK));
        this.addChild("wall_hanging_sign", this.findRelatedEntry("wall_hanging_sign", BuiltInRegistries.BLOCK));
        this.addChild("sign", this.findRelatedEntry("sign", BuiltInRegistries.BLOCK));
    }

    @Override
    public void initializeChildrenItems() {
        this.addChild("boat", this.findRelatedEntry("boat", BuiltInRegistries.ITEM));
        this.addChild("chest_boat", this.findRelatedEntry("chest_boat", BuiltInRegistries.ITEM));
        this.addChild("sapling", this.findRelatedEntry("sapling", BuiltInRegistries.ITEM));
        this.addChild("stick", this.findRelatedEntry("twig", BuiltInRegistries.BLOCK));
    }

    public BlockBehaviour.Properties copyProperties() {
        BlockBehaviour.Properties p = BlockBehaviour.Properties.of();
        p.mapColor(this.getColor());
        if (this.canBurn()) {
            p.ignitedByLava();
        }
        p.sound(this.getSound());
        return p;
    }

    public static class Finder implements BlockType.SetFinder<WoodType> {

        private final Map<String, ResourceLocation> childNames = new HashMap();

        private final Supplier<Block> planksFinder;

        private final Supplier<Block> logFinder;

        private final ResourceLocation id;

        public Finder(ResourceLocation id, Supplier<Block> planks, Supplier<Block> log) {
            this.id = id;
            this.planksFinder = planks;
            this.logFinder = log;
        }

        public static WoodType.Finder simple(String modId, String woodTypeName, String planksName, String logName) {
            return simple(new ResourceLocation(modId, woodTypeName), new ResourceLocation(modId, planksName), new ResourceLocation(modId, logName));
        }

        public static WoodType.Finder simple(ResourceLocation woodTypeName, ResourceLocation planksName, ResourceLocation logName) {
            return new WoodType.Finder(woodTypeName, () -> BuiltInRegistries.BLOCK.get(planksName), () -> BuiltInRegistries.BLOCK.get(logName));
        }

        public void addChild(String childType, String childName) {
            this.addChild(childType, new ResourceLocation(this.id.getNamespace(), childName));
        }

        public void addChild(String childType, ResourceLocation childName) {
            this.childNames.put(childType, childName);
        }

        @Internal
        @Override
        public Optional<WoodType> get() {
            if (PlatHelper.isModLoaded(this.id.getNamespace())) {
                try {
                    Block plank = (Block) this.planksFinder.get();
                    Block log = (Block) this.logFinder.get();
                    Block d = BuiltInRegistries.BLOCK.get(BuiltInRegistries.BLOCK.getDefaultKey());
                    if (plank != d && log != d && plank != null && log != null) {
                        WoodType w = new WoodType(this.id, plank, log);
                        this.childNames.forEach((key, value) -> w.addChild(key, BuiltInRegistries.BLOCK.get(value)));
                        return Optional.of(w);
                    }
                } catch (Exception var5) {
                }
                Moonlight.LOGGER.warn("Failed to find custom wood type {}", this.id);
            }
            return Optional.empty();
        }
    }
}