package io.github.lightman314.lightmanscurrency.common.menus.validation.types;

import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidatorType;
import io.github.lightman314.lightmanscurrency.common.util.TagUtil;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BlockEntityValidator extends MenuValidator {

    public static final MenuValidatorType TYPE = new BlockEntityValidator.Type();

    private static final MenuValidator NULL = new BlockEntityValidator((BlockEntity) null);

    private BlockEntity be;

    private BlockPos bePos;

    private void validateBE(@Nonnull Player player) {
        if (this.bePos != null) {
            this.be = player.m_9236_().getBlockEntity(this.bePos);
            this.bePos = null;
        }
    }

    protected BlockEntityValidator(BlockEntity be) {
        super(TYPE);
        this.be = be;
        this.bePos = null;
    }

    protected BlockEntityValidator(BlockPos pos) {
        super(TYPE);
        this.be = null;
        this.bePos = pos;
    }

    public static MenuValidator of(@Nullable BlockEntity be) {
        return (MenuValidator) (be == null ? NULL : new BlockEntityValidator(be));
    }

    @Override
    protected void encodeAdditional(@Nonnull FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.be != null);
        if (this.be != null) {
            buffer.writeBlockPos(this.be.getBlockPos());
        }
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        if (this.be != null) {
            tag.put("Position", TagUtil.saveBlockPos(this.be.getBlockPos()));
        }
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        this.validateBE(player);
        return this.be != null && Container.stillValidBlockEntity(this.be, player);
    }

    private static class Type extends MenuValidatorType {

        protected Type() {
            super(new ResourceLocation("lightmanscurrency", "block_entity"));
        }

        @Nonnull
        @Override
        public MenuValidator decode(@Nonnull FriendlyByteBuf buffer) {
            return (MenuValidator) (buffer.readBoolean() ? new BlockEntityValidator(buffer.readBlockPos()) : BlockEntityValidator.NULL);
        }

        @Nonnull
        @Override
        public MenuValidator load(@Nonnull CompoundTag tag) {
            return (MenuValidator) (tag.contains("Position") ? new BlockEntityValidator(TagUtil.loadBlockPos(tag.getCompound("Position"))) : BlockEntityValidator.NULL);
        }
    }
}