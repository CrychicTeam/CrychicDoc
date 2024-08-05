package net.mehvahdjukaar.moonlight.api.set.leaves;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class LeavesType extends BlockType {

    public static final Codec<LeavesType> CODEC = ResourceLocation.CODEC.flatXmap(r -> {
        LeavesType w = LeavesTypeRegistry.getValue(r);
        return w == null ? DataResult.error(() -> "No such leaves type: " + r) : DataResult.success(w);
    }, t -> DataResult.success(t.id));

    public final Block leaves;

    private final Supplier<WoodType> woodType;

    protected LeavesType(ResourceLocation id, Block leaves) {
        this(id, leaves, Suppliers.memoize(() -> (WoodType) Objects.requireNonNullElse(WoodTypeRegistry.getValue(id), WoodTypeRegistry.OAK_TYPE)));
    }

    protected LeavesType(ResourceLocation id, Block leaves, Supplier<WoodType> woodType) {
        super(id);
        this.leaves = leaves;
        this.woodType = woodType;
    }

    public WoodType getWoodType() {
        return (WoodType) this.woodType.get();
    }

    @Override
    public ItemLike mainChild() {
        return this.leaves;
    }

    @Override
    public String getTranslationKey() {
        return "leaves_type." + this.getNamespace() + "." + this.getTypeName();
    }

    @Override
    public void initializeChildrenBlocks() {
        this.addChild("leaves", this.leaves);
        this.woodType.get();
    }

    @Override
    public void initializeChildrenItems() {
    }

    public static class Finder implements BlockType.SetFinder<LeavesType> {

        private final Supplier<Block> leavesFinder;

        private final Supplier<WoodType> woodFinder;

        private final ResourceLocation id;

        public Finder(ResourceLocation id, Supplier<Block> leaves, @Nullable Supplier<WoodType> wood) {
            this.id = id;
            this.leavesFinder = leaves;
            this.woodFinder = wood;
        }

        public static LeavesType.Finder simple(String modId, String leavesTypeName, String leavesName) {
            return new LeavesType.Finder(new ResourceLocation(modId, leavesTypeName), () -> BuiltInRegistries.BLOCK.get(new ResourceLocation(modId, leavesName)), null);
        }

        public static LeavesType.Finder simple(String modId, String leavesTypeName, String leavesName, String woodTypeName) {
            return new LeavesType.Finder(new ResourceLocation(modId, leavesTypeName), () -> BuiltInRegistries.BLOCK.get(new ResourceLocation(modId, leavesName)), () -> WoodTypeRegistry.INSTANCE.get(new ResourceLocation(woodTypeName)));
        }

        @Override
        public Optional<LeavesType> get() {
            if (PlatHelper.isModLoaded(this.id.getNamespace())) {
                try {
                    Block leaves = (Block) this.leavesFinder.get();
                    Block d = BuiltInRegistries.BLOCK.get(BuiltInRegistries.BLOCK.getDefaultKey());
                    if (leaves != d && leaves != null) {
                        if (this.woodFinder == null) {
                            return Optional.of(new LeavesType(this.id, leaves));
                        }
                        return Optional.of(new LeavesType(this.id, leaves, this.woodFinder));
                    }
                } catch (Exception var3) {
                }
                Moonlight.LOGGER.warn("Failed to find custom wood type {}", this.id);
            }
            return Optional.empty();
        }
    }
}