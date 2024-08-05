package io.github.lightman314.lightmanscurrency.common.menus.validation.types;

import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidator;
import io.github.lightman314.lightmanscurrency.common.menus.validation.MenuValidatorType;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;

public final class SimpleValidator extends MenuValidator {

    public static final MenuValidatorType TYPE = new SimpleValidator.Type();

    public static final MenuValidator NULL = new SimpleValidator(p -> true);

    private final NonNullFunction<Player, Boolean> test;

    private SimpleValidator(@Nonnull NonNullSupplier<Boolean> test) {
        super(TYPE);
        this.test = p -> test.get();
    }

    private SimpleValidator(@Nonnull NonNullFunction<Player, Boolean> test) {
        super(TYPE);
        this.test = test;
    }

    public static MenuValidator of(@Nonnull NonNullSupplier<Boolean> test) {
        return new SimpleValidator(test);
    }

    public static MenuValidator of(@Nonnull NonNullFunction<Player, Boolean> test) {
        return new SimpleValidator(test);
    }

    @Override
    protected void encodeAdditional(@Nonnull FriendlyByteBuf buffer) {
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        try {
            return this.test.apply(player);
        } catch (Throwable var3) {
            return false;
        }
    }

    private static class Type extends MenuValidatorType {

        protected Type() {
            super(new ResourceLocation("lightmanscurrency", "null"));
        }

        @Nonnull
        @Override
        public MenuValidator decode(@Nonnull FriendlyByteBuf buffer) {
            return SimpleValidator.NULL;
        }

        @Nonnull
        @Override
        public MenuValidator load(@Nonnull CompoundTag tag) {
            return SimpleValidator.NULL;
        }
    }
}