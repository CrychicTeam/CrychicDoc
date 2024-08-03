package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class EntityDataAccessor implements DataAccessor {

    private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType(Component.translatable("commands.data.entity.invalid"));

    public static final Function<String, DataCommands.DataProvider> PROVIDER = p_139517_ -> new DataCommands.DataProvider() {

        @Override
        public DataAccessor access(CommandContext<CommandSourceStack> p_139530_) throws CommandSyntaxException {
            return new EntityDataAccessor(EntityArgument.getEntity(p_139530_, p_139517_));
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> p_139527_, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> p_139528_) {
            return p_139527_.then(Commands.literal("entity").then((ArgumentBuilder) p_139528_.apply(Commands.argument(p_139517_, EntityArgument.entity()))));
        }
    };

    private final Entity entity;

    public EntityDataAccessor(Entity entity0) {
        this.entity = entity0;
    }

    @Override
    public void setData(CompoundTag compoundTag0) throws CommandSyntaxException {
        if (this.entity instanceof Player) {
            throw ERROR_NO_PLAYERS.create();
        } else {
            UUID $$1 = this.entity.getUUID();
            this.entity.load(compoundTag0);
            this.entity.setUUID($$1);
        }
    }

    @Override
    public CompoundTag getData() {
        return NbtPredicate.getEntityTagToCompare(this.entity);
    }

    @Override
    public Component getModifiedSuccess() {
        return Component.translatable("commands.data.entity.modified", this.entity.getDisplayName());
    }

    @Override
    public Component getPrintSuccess(Tag tag0) {
        return Component.translatable("commands.data.entity.query", this.entity.getDisplayName(), NbtUtils.toPrettyComponent(tag0));
    }

    @Override
    public Component getPrintSuccess(NbtPathArgument.NbtPath nbtPathArgumentNbtPath0, double double1, int int2) {
        return Component.translatable("commands.data.entity.get", nbtPathArgumentNbtPath0, this.entity.getDisplayName(), String.format(Locale.ROOT, "%.2f", double1), int2);
    }
}