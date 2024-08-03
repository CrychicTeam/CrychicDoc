package net.minecraft.commands.arguments;

import com.google.common.collect.Maps;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityAnchorArgument implements ArgumentType<EntityAnchorArgument.Anchor> {

    private static final Collection<String> EXAMPLES = Arrays.asList("eyes", "feet");

    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(p_90357_ -> Component.translatable("argument.anchor.invalid", p_90357_));

    public static EntityAnchorArgument.Anchor getAnchor(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
        return (EntityAnchorArgument.Anchor) commandContextCommandSourceStack0.getArgument(string1, EntityAnchorArgument.Anchor.class);
    }

    public static EntityAnchorArgument anchor() {
        return new EntityAnchorArgument();
    }

    public EntityAnchorArgument.Anchor parse(StringReader stringReader0) throws CommandSyntaxException {
        int $$1 = stringReader0.getCursor();
        String $$2 = stringReader0.readUnquotedString();
        EntityAnchorArgument.Anchor $$3 = EntityAnchorArgument.Anchor.getByName($$2);
        if ($$3 == null) {
            stringReader0.setCursor($$1);
            throw ERROR_INVALID.createWithContext(stringReader0, $$2);
        } else {
            return $$3;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggest(EntityAnchorArgument.Anchor.BY_NAME.keySet(), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static enum Anchor {

        FEET("feet", (p_90389_, p_90390_) -> p_90389_), EYES("eyes", (p_90382_, p_90383_) -> new Vec3(p_90382_.x, p_90382_.y + (double) p_90383_.getEyeHeight(), p_90382_.z));

        static final Map<String, EntityAnchorArgument.Anchor> BY_NAME = Util.make(Maps.newHashMap(), p_90387_ -> {
            for (EntityAnchorArgument.Anchor $$1 : values()) {
                p_90387_.put($$1.name, $$1);
            }
        });

        private final String name;

        private final BiFunction<Vec3, Entity, Vec3> transform;

        private Anchor(String p_90374_, BiFunction<Vec3, Entity, Vec3> p_90375_) {
            this.name = p_90374_;
            this.transform = p_90375_;
        }

        @Nullable
        public static EntityAnchorArgument.Anchor getByName(String p_90385_) {
            return (EntityAnchorArgument.Anchor) BY_NAME.get(p_90385_);
        }

        public Vec3 apply(Entity p_90378_) {
            return (Vec3) this.transform.apply(p_90378_.position(), p_90378_);
        }

        public Vec3 apply(CommandSourceStack p_90380_) {
            Entity $$1 = p_90380_.getEntity();
            return $$1 == null ? p_90380_.getPosition() : (Vec3) this.transform.apply(p_90380_.getPosition(), $$1);
        }
    }
}