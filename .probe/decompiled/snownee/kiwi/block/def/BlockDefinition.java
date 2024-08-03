package snownee.kiwi.block.def;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;

public interface BlockDefinition {

    Map<String, BlockDefinition.Factory<?>> MAP = Maps.newConcurrentMap();

    List<BlockDefinition.Factory<?>> FACTORIES = Lists.newLinkedList();

    static void registerFactory(BlockDefinition.Factory<?> factory) {
        MAP.put(factory.getId(), factory);
        if ("Block".equals(factory.getId())) {
            FACTORIES.add(factory);
        } else {
            FACTORIES.add(0, factory);
        }
    }

    static BlockDefinition fromNBT(CompoundTag tag) {
        BlockDefinition.Factory<?> factory = (BlockDefinition.Factory<?>) MAP.get(tag.getString("Type"));
        return factory == null ? null : factory.fromNBT(tag);
    }

    static BlockDefinition fromBlock(BlockState state, BlockEntity blockEntity, LevelReader level, BlockPos pos) {
        for (BlockDefinition.Factory<?> factory : FACTORIES) {
            BlockDefinition supplier = factory.fromBlock(state, blockEntity, level, pos);
            if (supplier != null) {
                return supplier;
            }
        }
        return null;
    }

    static BlockDefinition fromItem(ItemStack stack, @Nullable BlockPlaceContext context) {
        if (!stack.isEmpty()) {
            for (BlockDefinition.Factory<?> factory : FACTORIES) {
                BlockDefinition supplier = factory.fromItem(stack, context);
                if (supplier != null) {
                    return supplier;
                }
            }
        }
        return null;
    }

    BlockDefinition.Factory<?> getFactory();

    @OnlyIn(Dist.CLIENT)
    BakedModel model();

    @OnlyIn(Dist.CLIENT)
    default ModelData modelData() {
        return ModelData.EMPTY;
    }

    @OnlyIn(Dist.CLIENT)
    Material renderMaterial(@Nullable Direction var1);

    void save(CompoundTag var1);

    @OnlyIn(Dist.CLIENT)
    default boolean canRenderInLayer(RenderType layer) {
        return this.getRenderTypes().contains(layer);
    }

    @OnlyIn(Dist.CLIENT)
    ChunkRenderTypeSet getRenderTypes();

    boolean canOcclude();

    @OnlyIn(Dist.CLIENT)
    int getColor(BlockState var1, BlockAndTintGetter var2, BlockPos var3, int var4);

    Component getDescription();

    void place(Level var1, BlockPos var2);

    ItemStack createItem(HitResult var1, BlockGetter var2, @Nullable BlockPos var3, @Nullable Player var4);

    BlockState getBlockState();

    default BlockDefinition getCamoDefinition() {
        return null;
    }

    static BlockDefinition getCamo(BlockDefinition definition) {
        for (BlockDefinition camo = definition.getCamoDefinition(); camo != null && camo != definition; camo = camo.getCamoDefinition()) {
            definition = camo;
        }
        return definition;
    }

    default int getLightEmission(BlockGetter level, BlockPos pos) {
        return this.getBlockState().getLightEmission(level, pos);
    }

    SoundType getSoundType();

    public interface Factory<T extends BlockDefinition> {

        T fromNBT(CompoundTag var1);

        String getId();

        @Nullable
        T fromBlock(BlockState var1, BlockEntity var2, LevelReader var3, BlockPos var4);

        @Nullable
        T fromItem(ItemStack var1, @Nullable BlockPlaceContext var2);
    }
}