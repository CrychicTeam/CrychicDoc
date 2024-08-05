package com.mna.blocks.sorcery;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.blocks.tileentities.wizard_lab.InscriptionTableTile;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

public class InscriptionTableBlock extends WizardLabBlock implements EntityBlock, ICutoutBlock {

    public static final IntegerProperty CONTAINED_RESOURCES = IntegerProperty.create("contained_resources", 0, 7);

    public InscriptionTableBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0F).randomTicks().noOcclusion());
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(f_54117_, Direction.NORTH)).m_61124_(CONTAINED_RESOURCES, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONTAINED_RESOURCES);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        Direction d1 = (Direction) stateIn.m_61143_(f_54117_);
        float h1 = 0.875F;
        float h2 = 0.75F;
        float v1 = 1.255F;
        if (d1 == Direction.NORTH) {
            h2 = 1.0F - h2;
            h1 = 1.0F - h1;
        } else if (d1 == Direction.EAST) {
            h2 = 0.125F;
            h1 = 0.75F;
        } else if (d1 == Direction.WEST) {
            h2 = 0.875F;
            h1 = 0.25F;
        }
        for (int i = 0; i < 2; i++) {
            worldIn.addParticle(new MAParticleType(ParticleInit.FLAME.get()).setScale(0.01F).setMaxAge((int) (20.0 + Math.random() * 20.0)).setColor(30, 172, 255), (double) ((float) pos.m_123341_() + h1), (double) ((float) pos.m_123342_() + v1), (double) ((float) pos.m_123343_() + h2), 0.0, 0.005, 0.0);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InscriptionTableTile(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.INSCRIPTION_TABLE.get() && !level.isClientSide() ? (lvl, pos, state1, be) -> InscriptionTableTile.ServerTick(lvl, pos, state1, (InscriptionTableTile) be) : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult p_225533_6_) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        if (te instanceof InscriptionTableTile) {
            if (player.isCreative()) {
                ((InscriptionTableTile) te).setCreative(true);
            } else {
                ((InscriptionTableTile) te).setCreative(false);
            }
        }
        return super.use(state, worldIn, pos, player, handIn, p_225533_6_);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity te = worldIn.getBlockEntity(pos);
        return te instanceof InscriptionTableTile && ((InscriptionTableTile) te).isBuilding() ? 15 : 0;
    }

    @Override
    protected void spawnDestroyParticles(Level world, Player player, BlockPos pos, BlockState state) {
        for (int i = 0; i < 20; i++) {
            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DARK_OAK_WOOD.defaultBlockState()), (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + Math.random(), (double) pos.m_123343_() + Math.random(), 0.0, 0.05 * Math.random(), 0.0);
        }
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity workbench = level.getBlockEntity(pos);
        return workbench != null && workbench instanceof InscriptionTableTile ? (InscriptionTableTile) workbench : null;
    }

    @Override
    protected Consumer<FriendlyByteBuf> getContainerBufferWriter(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity workbench = level.getBlockEntity(pos);
        return workbench != null && workbench instanceof InscriptionTableTile ? (InscriptionTableTile) workbench : null;
    }
}