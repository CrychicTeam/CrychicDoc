package net.minecraft.world.item;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public class AdventureModeCheck {

    private final String tagName;

    @Nullable
    private BlockInWorld lastCheckedBlock;

    private boolean lastResult;

    private boolean checksBlockEntity;

    public AdventureModeCheck(String string0) {
        this.tagName = string0;
    }

    private static boolean areSameBlocks(BlockInWorld blockInWorld0, @Nullable BlockInWorld blockInWorld1, boolean boolean2) {
        if (blockInWorld1 == null || blockInWorld0.getState() != blockInWorld1.getState()) {
            return false;
        } else if (!boolean2) {
            return true;
        } else if (blockInWorld0.getEntity() == null && blockInWorld1.getEntity() == null) {
            return true;
        } else {
            return blockInWorld0.getEntity() != null && blockInWorld1.getEntity() != null ? Objects.equals(blockInWorld0.getEntity().saveWithId(), blockInWorld1.getEntity().saveWithId()) : false;
        }
    }

    public boolean test(ItemStack itemStack0, Registry<Block> registryBlock1, BlockInWorld blockInWorld2) {
        if (areSameBlocks(blockInWorld2, this.lastCheckedBlock, this.checksBlockEntity)) {
            return this.lastResult;
        } else {
            this.lastCheckedBlock = blockInWorld2;
            this.checksBlockEntity = false;
            CompoundTag $$3 = itemStack0.getTag();
            if ($$3 != null && $$3.contains(this.tagName, 9)) {
                ListTag $$4 = $$3.getList(this.tagName, 8);
                for (int $$5 = 0; $$5 < $$4.size(); $$5++) {
                    String $$6 = $$4.getString($$5);
                    try {
                        ???;
                        this.checksBlockEntity = this.checksBlockEntity | $$7.requiresNbt();
                        if ($$7.test(blockInWorld2)) {
                            this.lastResult = true;
                            return true;
                        }
                    } catch (CommandSyntaxException var9) {
                    }
                }
            }
            this.lastResult = false;
            return false;
        }
    }
}