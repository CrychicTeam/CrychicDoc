package se.mickelus.tetra.blocks.forged.chthonic;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.FeatureFlag;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.blocks.PropertyMatcher;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.forged.ForgedBlockCommon;
import se.mickelus.tetra.blocks.salvage.BlockInteraction;
import se.mickelus.tetra.blocks.salvage.IInteractiveBlock;
import se.mickelus.tetra.properties.IToolProvider;

@ParametersAreNonnullByDefault
public class ChthonicExtractorBlock extends TetraBlock implements IInteractiveBlock, EntityBlock {

    public static final String identifier = "chthonic_extractor";

    public static final String usedIdentifier = "chthonic_extractor_used";

    public static final String description = "block.tetra.chthonic_extractor.description";

    public static final String extendedDescription = "block.tetra.chthonic_extractor.description_extended";

    public static final int maxDamage = 1024;

    protected static final VoxelShape shape = Shapes.or(Block.box(7.0, 0.0, 7.0, 9.0, 16.0, 9.0), Block.box(6.0, 15.0, 6.0, 10.0, 16.0, 10.0));

    static final BlockInteraction[] interactions = new BlockInteraction[] { new BlockInteraction(TetraToolActions.hammer, 4, Direction.UP, 0.0F, 4.0F, 0.0F, 4.0F, PropertyMatcher.any, (world, pos, blockState, player, hand, hitFace) -> hit(world, pos, player, hand)), new BlockInteraction(TetraToolActions.hammer, 5, Direction.UP, 0.0F, 4.0F, 0.0F, 4.0F, PropertyMatcher.any, (world, pos, blockState, player, hand, hitFace) -> hit(world, pos, player, hand)), new BlockInteraction(TetraToolActions.hammer, 6, Direction.UP, 0.0F, 4.0F, 0.0F, 4.0F, PropertyMatcher.any, (world, pos, blockState, player, hand, hitFace) -> hit(world, pos, player, hand)), new BlockInteraction(TetraToolActions.hammer, 7, Direction.UP, 0.0F, 4.0F, 0.0F, 4.0F, PropertyMatcher.any, (world, pos, blockState, player, hand, hitFace) -> hit(world, pos, player, hand)) };

    @ObjectHolder(registryName = "block", value = "tetra:chthonic_extractor")
    public static ChthonicExtractorBlock instance;

    @ObjectHolder(registryName = "item", value = "tetra:chthonic_extractor")
    public static Item item;

    @ObjectHolder(registryName = "item", value = "tetra:chthonic_extractor_used")
    public static Item usedItem;

    public ChthonicExtractorBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).sound(SoundType.NETHERITE_BLOCK).strength(2.5F, 2400.0F));
    }

    private static boolean hit(Level world, BlockPos pos, @Nullable Player playerEntity, InteractionHand hand) {
        if (FeatureFlag.isEnabled(FeatureFlag.bedrockExtraction)) {
            int amount = (Integer) Optional.ofNullable(playerEntity).map(player -> player.m_21120_(hand)).filter(itemStack -> itemStack.getItem() instanceof IToolProvider).map(itemStack -> ((IToolProvider) itemStack.getItem()).getToolEfficiency(itemStack, TetraToolActions.hammer)).map(Math::round).orElse(4);
            TileEntityOptional.from(world, pos, ChthonicExtractorTile.class).ifPresent(tile -> tile.damage(amount));
            FracturedBedrockBlock.pierce(world, pos.below(), amount);
            world.playSound(playerEntity, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.PLAYERS, 0.8F, 0.5F);
            return true;
        } else {
            return false;
        }
    }

    private static int getTier(Level world, BlockPos pos) {
        return (Integer) TileEntityOptional.from(world, pos.below(), FracturedBedrockTile.class).map(FracturedBedrockTile::getProjectedTier).orElseGet(() -> FracturedBedrockBlock.canPierce(world, pos.below()) ? 0 : -1);
    }

    public static RegistryObject<BlockItem> registerItems(DeferredRegister<Item> registry) {
        registry.register("chthonic_extractor_used", () -> new BlockItem(instance, new Item.Properties().durability(1024)));
        return registry.register("chthonic_extractor", () -> new BlockItem(instance, new Item.Properties().stacksTo(64)));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(Component.translatable("block.tetra.chthonic_extractor.description").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.literal(" "));
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Component.literal(" "));
            tooltip.add(ForgedBlockCommon.locationTooltip);
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("block.tetra.chthonic_extractor.description_extended").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        TileEntityOptional.from(world, pos, ChthonicExtractorTile.class).ifPresent(tile -> tile.setDamage(stack.getDamageValue()));
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        TileEntityOptional.from(world, pos, ChthonicExtractorTile.class).ifPresent(tile -> {
            ItemStack itemStack = this.getItemStack(tile);
            ItemEntity itemEntity = new ItemEntity(world, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemStack);
            itemEntity.setDefaultPickUpDelay();
            world.m_7967_(itemEntity);
        });
        super.m_5707_(world, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder lootParams) {
        if (lootParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof ChthonicExtractorTile tile) {
            lootParams = lootParams.withDynamicDrop(new ResourceLocation("tetra:cthtonic_drop"), consumer -> consumer.accept(this.getItemStack(tile)));
        }
        return super.m_49635_(blockState, lootParams);
    }

    private ItemStack getItemStack(ChthonicExtractorTile tile) {
        if (tile.getDamage() > 0) {
            ItemStack itemStack = new ItemStack(usedItem);
            itemStack.setDamageValue(tile.getDamage());
            return itemStack;
        } else {
            return new ItemStack(item);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public BlockInteraction[] getPotentialInteractions(Level world, BlockPos pos, BlockState blockState, Direction face, Collection<ToolAction> tools) {
        int tier = getTier(world, pos);
        return FeatureFlag.isEnabled(FeatureFlag.bedrockExtraction) && tier >= 0 && face == Direction.UP ? new BlockInteraction[] { interactions[Math.min(tier, interactions.length - 1)] } : new BlockInteraction[0];
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return BlockInteraction.attemptInteraction(world, state, pos, player, hand, hit);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new ChthonicExtractorTile(blockPos0, blockState1);
    }
}