package com.mna.blocks.artifice;

import com.mna.api.blocks.WizardLabBlock;
import com.mna.api.tools.MATags;
import com.mna.blocks.tileentities.wizard_lab.BookStandTile;
import com.mna.gui.containers.providers.NamedGuideBook;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class BookStandBlock extends WizardLabBlock implements EntityBlock {

    public static final BooleanProperty BOOK = BooleanProperty.create("has_book");

    public BookStandBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).noOcclusion().strength(3.0F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEFT, false)).m_61124_(RIGHT, false)).m_61124_(WATERLOGGED, false)).m_61124_(BOOK, false));
    }

    @Override
    protected MenuProvider getProvider(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            ItemStack handItem = player.m_21120_(hand);
            BlockEntity be = level.getBlockEntity(pos);
            if (be != null && be instanceof BookStandTile book_stand) {
                ItemStack bookStack = book_stand.m_8020_(0);
                if (!(Boolean) state.m_61143_(BOOK) || bookStack.isEmpty()) {
                    if (MATags.isItemIn(handItem.getItem(), MATags.Items.LECTERN_ALLOWED_ITEMS)) {
                        ItemStack insertStack = handItem.copy();
                        insertStack.setCount(1);
                        book_stand.m_6836_(0, insertStack);
                        handItem.shrink(1);
                        level.setBlock(pos, (BlockState) state.m_61124_(BOOK, true), 3);
                    }
                } else if (player.m_6047_()) {
                    if (!player.addItem(bookStack)) {
                        player.m_19983_(bookStack);
                    }
                    level.setBlock(pos, (BlockState) state.m_61124_(BOOK, false), 3);
                } else if (!ItemInit.GUIDE_BOOK.get().checkMagicUnlock((ServerLevel) level, player)) {
                    NetworkHooks.openScreen((ServerPlayer) player, new NamedGuideBook());
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BOOK);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BookStandTile(pPos, pState);
    }
}