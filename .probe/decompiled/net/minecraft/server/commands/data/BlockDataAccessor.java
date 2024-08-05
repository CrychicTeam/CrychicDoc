package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Locale;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockDataAccessor implements DataAccessor {

    static final SimpleCommandExceptionType ERROR_NOT_A_BLOCK_ENTITY = new SimpleCommandExceptionType(Component.translatable("commands.data.block.invalid"));

    public static final Function<String, DataCommands.DataProvider> PROVIDER = p_139305_ -> new DataCommands.DataProvider() {

        @Override
        public DataAccessor access(CommandContext<CommandSourceStack> p_139319_) throws CommandSyntaxException {
            BlockPos $$1 = BlockPosArgument.getLoadedBlockPos(p_139319_, p_139305_ + "Pos");
            BlockEntity $$2 = ((CommandSourceStack) p_139319_.getSource()).getLevel().m_7702_($$1);
            if ($$2 == null) {
                throw BlockDataAccessor.ERROR_NOT_A_BLOCK_ENTITY.create();
            } else {
                return new BlockDataAccessor($$2, $$1);
            }
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> p_139316_, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> p_139317_) {
            return p_139316_.then(Commands.literal("block").then((ArgumentBuilder) p_139317_.apply(Commands.argument(p_139305_ + "Pos", BlockPosArgument.blockPos()))));
        }
    };

    private final BlockEntity entity;

    private final BlockPos pos;

    public BlockDataAccessor(BlockEntity blockEntity0, BlockPos blockPos1) {
        this.entity = blockEntity0;
        this.pos = blockPos1;
    }

    @Override
    public void setData(CompoundTag compoundTag0) {
        BlockState $$1 = this.entity.getLevel().getBlockState(this.pos);
        this.entity.load(compoundTag0);
        this.entity.setChanged();
        this.entity.getLevel().sendBlockUpdated(this.pos, $$1, $$1, 3);
    }

    @Override
    public CompoundTag getData() {
        return this.entity.saveWithFullMetadata();
    }

    @Override
    public Component getModifiedSuccess() {
        return Component.translatable("commands.data.block.modified", this.pos.m_123341_(), this.pos.m_123342_(), this.pos.m_123343_());
    }

    @Override
    public Component getPrintSuccess(Tag tag0) {
        return Component.translatable("commands.data.block.query", this.pos.m_123341_(), this.pos.m_123342_(), this.pos.m_123343_(), NbtUtils.toPrettyComponent(tag0));
    }

    @Override
    public Component getPrintSuccess(NbtPathArgument.NbtPath nbtPathArgumentNbtPath0, double double1, int int2) {
        return Component.translatable("commands.data.block.get", nbtPathArgumentNbtPath0, this.pos.m_123341_(), this.pos.m_123342_(), this.pos.m_123343_(), String.format(Locale.ROOT, "%.2f", double1), int2);
    }
}