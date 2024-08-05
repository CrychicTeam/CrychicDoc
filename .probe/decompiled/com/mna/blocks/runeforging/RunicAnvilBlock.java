package com.mna.blocks.runeforging;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.events.GenericProgressionEvent;
import com.mna.api.events.ProgressionEventIDs;
import com.mna.api.events.RunicAnvilItemUsedEvent;
import com.mna.api.sound.SFX;
import com.mna.api.tools.MATags;
import com.mna.blocks.WaterloggableBlock;
import com.mna.blocks.tileentities.RunicAnvilTile;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.items.ItemInit;
import com.mna.items.armor.BrokenMageArmor;
import com.mna.items.ritual.ItemPractitionersPatch;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import com.mna.items.runes.ItemRune;
import com.mna.items.runes.ItemRunescribingRecipe;
import com.mna.recipes.RecipeInit;
import com.mna.recipes.runeforging.RuneforgingRecipe;
import com.mna.recipes.runeforging.RunescribingRecipe;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;

public class RunicAnvilBlock extends WaterloggableBlock implements ICutoutBlock, EntityBlock {

    protected static final VoxelShape SHAPE_BASE_WE = Block.box(4.0, 0.0, 3.0, 12.0, 13.0, 13.0);

    protected static final VoxelShape SHAPE_HEAD_WE = Block.box(4.0, 13.0, -3.0, 12.0, 16.0, 19.0);

    protected static final VoxelShape SHAPE_BASE_NS = Block.box(3.0, 0.0, 4.0, 13.0, 13.0, 12.0);

    protected static final VoxelShape SHAPE_HEAD_NS = Block.box(-3.0, 13.0, 4.0, 19.0, 16.0, 12.0);

    protected static final VoxelShape SHAPE_WE = Shapes.or(SHAPE_BASE_WE, SHAPE_HEAD_WE);

    protected static final VoxelShape SHAPE_NS = Shapes.or(SHAPE_BASE_NS, SHAPE_HEAD_NS);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public static final ResourceLocation TAG_COALS = new ResourceLocation("coals");

    public RunicAnvilBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(2.0F, 3.0F).noOcclusion().sound(SoundType.ANVIL), false);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ACTIVE);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) super.getStateForPlacement(context).m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction facing = (Direction) state.m_61143_(FACING);
        return facing != Direction.EAST && facing != Direction.WEST ? SHAPE_NS : SHAPE_WE;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RunicAnvilTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        ItemStack activeStack = player.m_21120_(handIn);
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (tileEntity == null && !(tileEntity instanceof RunicAnvilTile)) {
            return InteractionResult.SUCCESS;
        } else {
            RunicAnvilTile te = (RunicAnvilTile) tileEntity;
            if (activeStack.getItem() == ItemInit.RUNESMITH_HAMMER.get() || activeStack.getItem() == ItemInit.RUNIC_MALUS.get()) {
                return this.onHammerUse(activeStack, player, worldIn, pos, state, te);
            } else if (MATags.isItemEqual(activeStack, TAG_COALS)) {
                return this.onCharcoalUse(activeStack, player, worldIn, pos, state, te);
            } else {
                return activeStack.getItem() == ItemInit.SORCEROUS_SEWING_SET.get() ? this.onSewingKitUse(activeStack, player, worldIn, pos, state, te) : this.onGenericItemUse(activeStack, player, worldIn, pos, state, te, handIn);
            }
        }
    }

    private InteractionResult onGenericItemUse(ItemStack activeStack, Player player, Level worldIn, BlockPos pos, BlockState state, RunicAnvilTile te, InteractionHand hand) {
        if (!worldIn.isClientSide) {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression == null) {
                return InteractionResult.FAIL;
            }
            RunicAnvilItemUsedEvent event = new RunicAnvilItemUsedEvent(te.m_8020_(0), te.m_8020_(1), activeStack, player);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                te.setItem(0, event.pattern.isEmpty() ? ItemStack.EMPTY : event.pattern.copy());
                te.setItem(1, event.material.isEmpty() ? ItemStack.EMPTY : event.material.copy());
                player.m_21008_(hand, event.catalyst.isEmpty() ? ItemStack.EMPTY : event.catalyst.copy());
                return InteractionResult.SUCCESS;
            }
            ItemStack existing = te.m_8020_(0);
            if ((existing.getItem() == ItemInit.RUNESMITH_HAMMER.get() || existing.getItem() == ItemInit.RUNIC_MALUS.get()) && !activeStack.isEmpty()) {
                te.setItem(0, ItemStack.EMPTY);
                if (this.insertItems(te, player, activeStack, worldIn, pos, state, progression.getTier())) {
                    if (!player.addItem(existing)) {
                        player.drop(existing, true);
                    }
                } else {
                    te.setItem(0, existing);
                }
                return InteractionResult.SUCCESS;
            }
            if (!this.insertItems(te, player, activeStack, worldIn, pos, state, progression.getTier())) {
                this.removeItems(te, player, activeStack, worldIn, pos, state);
            }
        }
        return InteractionResult.SUCCESS;
    }

    private InteractionResult onCharcoalUse(ItemStack activeStack, Player player, Level worldIn, BlockPos pos, BlockState state, RunicAnvilTile te) {
        ItemStack rune = te.m_8020_(0);
        if (rune.isEmpty()) {
            return InteractionResult.FAIL;
        } else if (te.m_8020_(1).getItem() != Items.PAPER) {
            return InteractionResult.FAIL;
        } else {
            Optional<RuneforgingRecipe> forgeRecipe = worldIn.getRecipeManager().<CraftingContainer, RuneforgingRecipe>getAllRecipesFor(RecipeInit.RUNEFORGING_TYPE.get()).stream().filter(r -> r.getOutputResource().toString().equals(ForgeRegistries.ITEMS.getKey(rune.getItem()).toString())).findFirst();
            if (!forgeRecipe.isPresent()) {
                return InteractionResult.FAIL;
            } else {
                Optional<RunescribingRecipe> scribeRecipe = worldIn.getRecipeManager().<CraftingContainer, RunescribingRecipe>getAllRecipesFor(RecipeInit.RUNESCRIBING_TYPE.get()).stream().filter(r -> r.getOutputResource().toString().equals(((RuneforgingRecipe) forgeRecipe.get()).getPatternResource().toString())).findFirst();
                if (!scribeRecipe.isPresent()) {
                    return InteractionResult.FAIL;
                } else {
                    ItemStack patternStack = new ItemStack(ItemInit.RECIPE_SCRAP_RUNESCRIBING.get());
                    ItemRunescribingRecipe.setRecipe(patternStack, (RunescribingRecipe) scribeRecipe.get());
                    if (!worldIn.isClientSide) {
                        te.setItem(1, patternStack);
                    }
                    worldIn.playSound(player, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SFX.Gui.CHARCOAL_SCRIBBLE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.isCreative()) {
                        activeStack.shrink(1);
                    }
                    if (!worldIn.isClientSide && player instanceof ServerPlayer) {
                        CustomAdvancementTriggers.RUNIC_ANVIL_CRAFT.trigger((ServerPlayer) player, patternStack);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    private InteractionResult onHammerUse(ItemStack activeStack, Player player, Level worldIn, BlockPos pos, BlockState state, RunicAnvilTile te) {
        if (player.getCooldowns().getCooldownPercent(activeStack.getItem(), 0.0F) > 0.0F) {
            return InteractionResult.SUCCESS;
        } else {
            IPlayerProgression progression = (IPlayerProgression) player.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (progression == null) {
                return InteractionResult.FAIL;
            } else {
                if (worldIn.isClientSide) {
                    Item item = te.m_8020_(1).getItem();
                    this.spawnParticles(item, worldIn, pos);
                }
                int advanceResponse = te.advanceCrafting(player, progression.getTier());
                if (!worldIn.isClientSide) {
                    worldIn.playSound(null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, (float) Math.random() * 0.1F + 0.9F);
                    if (advanceResponse == 0 || advanceResponse == 4) {
                        player.getCooldowns().addCooldown(activeStack.getItem(), 10);
                    } else if (advanceResponse == 2) {
                        player.m_213846_(Component.translatable("block.mna.runic_anvil.low_tier"));
                        player.getCooldowns().addCooldown(activeStack.getItem(), 40);
                    } else if (te.m_8020_(0).isEmpty()) {
                        this.insertItems(te, player, activeStack, worldIn, pos, state, progression.getTier());
                    }
                }
                activeStack.hurtAndBreak(1, player, e -> {
                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    private InteractionResult onSewingKitUse(ItemStack activeStack, Player player, Level worldIn, BlockPos pos, BlockState state, RunicAnvilTile te) {
        ItemStack sewable = te.m_8020_(0);
        if (sewable.getItem() instanceof BrokenMageArmor) {
            if (te.m_8020_(1).getItem() != ItemInit.INFUSED_SILK.get()) {
                return InteractionResult.FAIL;
            } else {
                BrokenMageArmor restoredItem = (BrokenMageArmor) sewable.getItem();
                ItemStack restoredStack = restoredItem.restore(sewable);
                restoredStack.setDamageValue(0);
                te.setItem(1, ItemStack.EMPTY);
                activeStack.hurtAndBreak(1, player, e -> {
                });
                if (player instanceof ServerPlayer) {
                    CustomAdvancementTriggers.RUNIC_ANVIL_CRAFT.trigger((ServerPlayer) player, restoredStack);
                }
                worldIn.playSound(player, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
                te.setItem(0, restoredStack);
                return InteractionResult.SUCCESS;
            }
        } else if (sewable.getItem() instanceof ItemPractitionersPouch) {
            ItemStack patchStack = te.m_8020_(1);
            if (!(patchStack.getItem() instanceof ItemPractitionersPatch)) {
                return InteractionResult.FAIL;
            } else {
                PractitionersPouchPatches patch = ((ItemPractitionersPatch) patchStack.getItem()).getPatch();
                int patchLevel = ((ItemPractitionersPatch) patchStack.getItem()).getLevel();
                if (ItemInit.PRACTITIONERS_POUCH.get().getPatchLevel(sewable, patch) >= patchLevel) {
                    return InteractionResult.FAIL;
                } else if (ItemInit.PRACTITIONERS_POUCH.get().countAppliedPatchesForLimit(sewable, patchStack) >= 4) {
                    return InteractionResult.FAIL;
                } else {
                    ItemInit.PRACTITIONERS_POUCH.get().addPatch(sewable, patch, patchLevel);
                    te.setItem(1, ItemStack.EMPTY);
                    activeStack.hurtAndBreak(1, player, e -> {
                    });
                    worldIn.playSound(player, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), SoundEvents.ARMOR_EQUIP_GENERIC, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!worldIn.isClientSide) {
                        MinecraftForge.EVENT_BUS.post(new GenericProgressionEvent(player, ProgressionEventIDs.APPLY_POUCH_PATCH));
                        if (player instanceof ServerPlayer) {
                            CustomAdvancementTriggers.RUNIC_ANVIL_CRAFT.trigger((ServerPlayer) player, sewable);
                        }
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        } else {
            return InteractionResult.FAIL;
        }
    }

    private void spawnParticles(Item item, Level worldIn, BlockPos pos) {
        if (item == ItemInit.VINTEUM_INGOT_SUPERHEATED.get()) {
            for (int i = 0; i < 3; i++) {
                worldIn.addParticle(ParticleTypes.LAVA, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.1F), (double) ((float) pos.m_123343_() + 0.5F), -0.5 + Math.random(), Math.random(), -0.5 + Math.random());
            }
        } else if (item instanceof ItemRune) {
            for (int i = 0; i < 3; i++) {
                worldIn.addParticle(ParticleTypes.CRIT, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.1F), (double) ((float) pos.m_123343_() + 0.5F), -0.5 + Math.random(), Math.random(), -0.5 + Math.random());
            }
        }
    }

    private boolean insertItems(RunicAnvilTile te, Player player, ItemStack heldStack, Level world, BlockPos pos, BlockState state, int playerTier) {
        ItemStack single = heldStack.copy();
        single.setCount(1);
        if (!te.pushItemStack(single, player, playerTier)) {
            return false;
        } else {
            if (!player.isCreative() || heldStack.getItem() == ItemInit.RUNESMITH_HAMMER.get()) {
                heldStack.shrink(1);
            }
            return true;
        }
    }

    private void removeItems(RunicAnvilTile te, Player player, ItemStack heldStack, Level world, BlockPos pos, BlockState state) {
        ItemStack stack = te.popItemStack();
        if (!stack.isEmpty() && !player.addItem(stack)) {
            player.drop(stack, true);
        }
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            this.dropInventory(worldIn, pos);
        }
        super.m_6810_(state, worldIn, pos, newState, isMoving);
    }

    private void dropInventory(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            BlockEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity != null && tileEntity instanceof Container) {
                Containers.dropContents(world, pos, (Container) tileEntity);
            }
        }
    }
}