package snownee.kiwi.block.def;

import com.google.common.collect.Maps;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import snownee.kiwi.loader.Platform;

public class SimpleBlockDefinition implements BlockDefinition {

    private static final MethodHandle GET_STATE_FOR_PLACEMENT;

    public static final String TYPE = "Block";

    private static final Map<BlockState, SimpleBlockDefinition> MAP;

    public final BlockState state;

    @OnlyIn(Dist.CLIENT)
    private Material[] materials;

    @Nullable
    private static BlockState getStateForPlacement(BlockItem blockItem, BlockPlaceContext context) {
        try {
            return (BlockState) GET_STATE_FOR_PLACEMENT.invokeExact(blockItem, context);
        } catch (Throwable var3) {
            return null;
        }
    }

    public static SimpleBlockDefinition of(BlockState state) {
        if (state.m_60734_() == Blocks.GRASS_BLOCK) {
            state = (BlockState) state.m_61124_(BlockStateProperties.SNOWY, false);
        }
        return (SimpleBlockDefinition) MAP.computeIfAbsent(state, SimpleBlockDefinition::new);
    }

    private SimpleBlockDefinition(BlockState state) {
        this.state = state;
        if (Platform.isPhysicalClient()) {
            this.materials = new Material[7];
        }
    }

    @Override
    public BlockDefinition.Factory<?> getFactory() {
        return SimpleBlockDefinition.Factory.INSTANCE;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public BakedModel model() {
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(this.state);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Material renderMaterial(Direction direction) {
        int index = direction == null ? 0 : direction.ordinal() + 1;
        if (this.materials[index] == null) {
            BakedModel model = this.model();
            RandomSource random = RandomSource.create();
            random.setSeed(42L);
            ResourceLocation particleIcon = model.getParticleIcon(ModelData.EMPTY).contents().name();
            ResourceLocation sprite = particleIcon;
            if (this.state.m_60734_() == Blocks.GRASS_BLOCK) {
                direction = Direction.UP;
            }
            if (direction != null) {
                List<BakedQuad> quads = model.getQuads(this.state, direction, random, ModelData.EMPTY, null);
                if (quads.isEmpty()) {
                    quads = model.getQuads(this.state, null, random, ModelData.EMPTY, null);
                }
                for (BakedQuad quad : quads) {
                    sprite = quad.getSprite().contents().name();
                    if (sprite.equals(particleIcon)) {
                        break;
                    }
                }
            }
            this.materials[index] = new Material(InventoryMenu.BLOCK_ATLAS, sprite);
        }
        return this.materials[index];
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ChunkRenderTypeSet getRenderTypes() {
        return this.model().getRenderTypes(this.state, RandomSource.create(42L), this.modelData());
    }

    @Override
    public boolean canOcclude() {
        return this.state.m_60815_();
    }

    @Override
    public void save(CompoundTag tag) {
        tag.put("Block", NbtUtils.writeBlockState(this.state));
    }

    public String toString() {
        return this.state.toString();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getColor(BlockState blockState, BlockAndTintGetter level, BlockPos worldPosition, int index) {
        return Minecraft.getInstance().getBlockColors().getColor(this.state, level, worldPosition, index);
    }

    @Override
    public Component getDescription() {
        return this.state.m_60734_().getName();
    }

    @Override
    public void place(Level level, BlockPos pos) {
        BlockState state = this.state;
        if (state.m_61138_(BlockStateProperties.LIT)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.LIT, false);
        }
        level.setBlockAndUpdate(pos, state);
    }

    @Override
    public ItemStack createItem(HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return this.getBlockState().getCloneItemStack(target, world, pos, player);
    }

    @Override
    public BlockState getBlockState() {
        return this.state;
    }

    @Override
    public SoundType getSoundType() {
        return this.state.m_60827_();
    }

    public static void reload() {
        for (SimpleBlockDefinition supplier : MAP.values()) {
            Arrays.fill(supplier.materials, null);
        }
    }

    static {
        MethodHandle m = null;
        try {
            m = MethodHandles.lookup().unreflect(ObfuscationReflectionHelper.findMethod(BlockItem.class, "m_5965_", new Class[] { BlockPlaceContext.class }));
        } catch (Exception var2) {
            throw new RuntimeException("Report this to author", var2);
        }
        GET_STATE_FOR_PLACEMENT = m;
        MAP = Maps.newIdentityHashMap();
    }

    public static enum Factory implements BlockDefinition.Factory<SimpleBlockDefinition> {

        INSTANCE;

        public SimpleBlockDefinition fromNBT(CompoundTag tag) {
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.m_255303_(), tag.getCompound("Block"));
            return state.m_60795_() ? null : SimpleBlockDefinition.of(state);
        }

        public SimpleBlockDefinition fromBlock(BlockState state, BlockEntity blockEntity, LevelReader level, BlockPos pos) {
            return SimpleBlockDefinition.of(state);
        }

        public SimpleBlockDefinition fromItem(ItemStack stack, BlockPlaceContext context) {
            if (!(stack.getItem() instanceof BlockItem)) {
                return null;
            } else {
                BlockItem blockItem = (BlockItem) stack.getItem();
                if (context == null) {
                    return SimpleBlockDefinition.of(blockItem.getBlock().defaultBlockState());
                } else {
                    context = blockItem.updatePlacementContext(context);
                    if (context == null) {
                        return null;
                    } else {
                        BlockState state = SimpleBlockDefinition.getStateForPlacement(blockItem, context);
                        return state == null ? null : SimpleBlockDefinition.of(state);
                    }
                }
            }
        }

        @Override
        public String getId() {
            return "Block";
        }
    }
}