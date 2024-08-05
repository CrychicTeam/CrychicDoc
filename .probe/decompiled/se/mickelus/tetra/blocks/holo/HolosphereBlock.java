package se.mickelus.tetra.blocks.holo;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.RotationHelper;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.advancements.BlockUseCriterion;
import se.mickelus.tetra.blocks.TetraWaterloggedBlock;
import se.mickelus.tetra.interactions.SecondaryInteractionHandler;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;

public class HolosphereBlock extends TetraWaterloggedBlock implements EntityBlock {

    public static final String identifier = "holosphere";

    private static final VoxelShape shape = Block.box(5.5, 0.0, 5.5, 10.5, 5.0, 10.5);

    public static RegistryObject<HolosphereBlock> instance;

    public HolosphereBlock() {
        super(BlockBehaviour.Properties.of().strength(0.0F, 20.0F).sound(SoundType.DEEPSLATE));
    }

    public static InteractionResult place(BlockPlaceContext context) {
        if (context.canPlace()) {
            Block block = instance.get();
            BlockState blockState = block.defaultBlockState();
            boolean couldPlace = context.m_43725_().setBlock(context.getClickedPos(), blockState, 11);
            if (couldPlace) {
                BlockPos pos = context.getClickedPos();
                Level level = context.m_43725_();
                Player player = context.m_43723_();
                ItemStack itemstack = context.m_43722_();
                BlockState placedBlockState = level.getBlockState(pos);
                if (placedBlockState.m_60713_(blockState.m_60734_())) {
                    level.m_141902_(pos, HolosphereBlockEntity.type.get()).ifPresent(blockEntity -> blockEntity.setItemTag(itemstack.getTag()));
                    placedBlockState.m_60734_().setPlacedBy(level, pos, placedBlockState, player, itemstack);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, itemstack);
                    }
                }
                level.m_220407_(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placedBlockState));
                SoundType soundtype = placedBlockState.getSoundType(level, pos, context.m_43723_());
                level.playSound(player, pos, soundtype.getPlaceSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                if (player == null || !player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        SecondaryInteractionHandler.registerInteraction(new ToggleScanModeInteraction("scan_toggle_on", true));
        SecondaryInteractionHandler.registerInteraction(new ToggleScanModeInteraction("scan_toggle_off", false));
    }

    @Override
    public void clientInit() {
        BlockEntityRenderers.register(HolosphereBlockEntity.type.get(), HolosphereEntityRenderer::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new HolosphereBlockEntity(blockPos0, blockState1);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack itemStack = player.m_21120_(hand);
        if (world.getBlockEntity(pos) instanceof HolosphereBlockEntity entity && entity.inScanMode() && itemStack.getItem() instanceof ItemModularHandheld item) {
            int level = item.getToolLevel(itemStack, TetraToolActions.hammer);
            if (level > 0) {
                boolean canSwing = player.getAttackStrengthScale(0.0F) > 0.8F;
                if (!world.isClientSide() && canSwing) {
                    float angle = (float) RotationHelper.getHorizontalAngle(Vec3.atBottomCenterOf(pos), player.m_20182_());
                    entity.use(level, item.getToolEfficiency(itemStack, TetraToolActions.hammer), angle);
                    Map<String, String> data = new HashMap();
                    data.put("percussion_scan", "true");
                    BlockUseCriterion.trigger((ServerPlayer) player, blockState, itemStack, data);
                }
                if (canSwing) {
                    item.tickProgression(player, itemStack, 2);
                    item.applyDamage(2, itemStack, player);
                    world.playSound(player, pos, SoundEvents.NETHERITE_BLOCK_HIT, SoundSource.PLAYERS, 0.3F, 1.0F + 0.5F * (float) Math.random());
                } else {
                    world.playSound(player, pos, SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.2F, 0.5F);
                }
                player.resetAttackStrengthTicker();
                return InteractionResult.sidedSuccess(canSwing);
            }
        }
        if (world.isClientSide()) {
            ModularHolosphereItem.showGui();
        } else {
            Map<String, String> data = new HashMap();
            data.put("holosphere_open", "true");
            BlockUseCriterion.trigger((ServerPlayer) player, blockState, itemStack, data);
        }
        return InteractionResult.sidedSuccess(world.isClientSide());
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.m_5707_(world, pos, state, player);
        if (!world.isClientSide && !player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            world.m_141902_(pos, HolosphereBlockEntity.type.get()).ifPresent(blockEntity -> {
                ItemStack itemStack = blockEntity.getItemStack();
                ItemEntity itemEntity = new ItemEntity(world, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.m_7967_(itemEntity);
            });
        }
    }
}