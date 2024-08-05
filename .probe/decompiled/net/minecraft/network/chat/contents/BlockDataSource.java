package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public record BlockDataSource(String f_237309_, @Nullable Coordinates f_237310_) implements DataSource {

    private final String posPattern;

    @Nullable
    private final Coordinates compiledPos;

    public BlockDataSource(String p_237312_) {
        this(p_237312_, compilePos(p_237312_));
    }

    public BlockDataSource(String f_237309_, @Nullable Coordinates f_237310_) {
        this.posPattern = f_237309_;
        this.compiledPos = f_237310_;
    }

    @Nullable
    private static Coordinates compilePos(String p_237318_) {
        try {
            return BlockPosArgument.blockPos().parse(new StringReader(p_237318_));
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack p_237323_) {
        if (this.compiledPos != null) {
            ServerLevel $$1 = p_237323_.getLevel();
            BlockPos $$2 = this.compiledPos.getBlockPos(p_237323_);
            if ($$1.m_46749_($$2)) {
                BlockEntity $$3 = $$1.m_7702_($$2);
                if ($$3 != null) {
                    return Stream.of($$3.saveWithFullMetadata());
                }
            }
        }
        return Stream.empty();
    }

    public String toString() {
        return "block=" + this.posPattern;
    }

    public boolean equals(Object p_237321_) {
        if (this == p_237321_) {
            return true;
        } else {
            if (p_237321_ instanceof BlockDataSource $$1 && this.posPattern.equals($$1.posPattern)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return this.posPattern.hashCode();
    }
}