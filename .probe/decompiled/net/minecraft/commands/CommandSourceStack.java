package net.minecraft.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.ResultConsumer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.TaskChainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class CommandSourceStack implements SharedSuggestionProvider {

    public static final SimpleCommandExceptionType ERROR_NOT_PLAYER = new SimpleCommandExceptionType(Component.translatable("permissions.requires.player"));

    public static final SimpleCommandExceptionType ERROR_NOT_ENTITY = new SimpleCommandExceptionType(Component.translatable("permissions.requires.entity"));

    private final CommandSource source;

    private final Vec3 worldPosition;

    private final ServerLevel level;

    private final int permissionLevel;

    private final String textName;

    private final Component displayName;

    private final MinecraftServer server;

    private final boolean silent;

    @Nullable
    private final Entity entity;

    @Nullable
    private final ResultConsumer<CommandSourceStack> consumer;

    private final EntityAnchorArgument.Anchor anchor;

    private final Vec2 rotation;

    private final CommandSigningContext signingContext;

    private final TaskChainer chatMessageChainer;

    private final IntConsumer returnValueConsumer;

    public CommandSourceStack(CommandSource commandSource0, Vec3 vec1, Vec2 vec2, ServerLevel serverLevel3, int int4, String string5, Component component6, MinecraftServer minecraftServer7, @Nullable Entity entity8) {
        this(commandSource0, vec1, vec2, serverLevel3, int4, string5, component6, minecraftServer7, entity8, false, (p_81361_, p_81362_, p_81363_) -> {
        }, EntityAnchorArgument.Anchor.FEET, CommandSigningContext.ANONYMOUS, TaskChainer.immediate(minecraftServer7), p_280930_ -> {
        });
    }

    protected CommandSourceStack(CommandSource commandSource0, Vec3 vec1, Vec2 vec2, ServerLevel serverLevel3, int int4, String string5, Component component6, MinecraftServer minecraftServer7, @Nullable Entity entity8, boolean boolean9, @Nullable ResultConsumer<CommandSourceStack> resultConsumerCommandSourceStack10, EntityAnchorArgument.Anchor entityAnchorArgumentAnchor11, CommandSigningContext commandSigningContext12, TaskChainer taskChainer13, IntConsumer intConsumer14) {
        this.source = commandSource0;
        this.worldPosition = vec1;
        this.level = serverLevel3;
        this.silent = boolean9;
        this.entity = entity8;
        this.permissionLevel = int4;
        this.textName = string5;
        this.displayName = component6;
        this.server = minecraftServer7;
        this.consumer = resultConsumerCommandSourceStack10;
        this.anchor = entityAnchorArgumentAnchor11;
        this.rotation = vec2;
        this.signingContext = commandSigningContext12;
        this.chatMessageChainer = taskChainer13;
        this.returnValueConsumer = intConsumer14;
    }

    public CommandSourceStack withSource(CommandSource commandSource0) {
        return this.source == commandSource0 ? this : new CommandSourceStack(commandSource0, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withEntity(Entity entity0) {
        return this.entity == entity0 ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, entity0.getName().getString(), entity0.getDisplayName(), this.server, entity0, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withPosition(Vec3 vec0) {
        return this.worldPosition.equals(vec0) ? this : new CommandSourceStack(this.source, vec0, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withRotation(Vec2 vec0) {
        return this.rotation.equals(vec0) ? this : new CommandSourceStack(this.source, this.worldPosition, vec0, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> resultConsumerCommandSourceStack0) {
        return Objects.equals(this.consumer, resultConsumerCommandSourceStack0) ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, resultConsumerCommandSourceStack0, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withCallback(ResultConsumer<CommandSourceStack> resultConsumerCommandSourceStack0, BinaryOperator<ResultConsumer<CommandSourceStack>> binaryOperatorResultConsumerCommandSourceStack1) {
        ResultConsumer<CommandSourceStack> $$2 = (ResultConsumer<CommandSourceStack>) binaryOperatorResultConsumerCommandSourceStack1.apply(this.consumer, resultConsumerCommandSourceStack0);
        return this.withCallback($$2);
    }

    public CommandSourceStack withSuppressedOutput() {
        return !this.silent && !this.source.alwaysAccepts() ? new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, true, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer) : this;
    }

    public CommandSourceStack withPermission(int int0) {
        return int0 == this.permissionLevel ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, int0, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withMaximumPermission(int int0) {
        return int0 <= this.permissionLevel ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, int0, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withAnchor(EntityAnchorArgument.Anchor entityAnchorArgumentAnchor0) {
        return entityAnchorArgumentAnchor0 == this.anchor ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, entityAnchorArgumentAnchor0, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withLevel(ServerLevel serverLevel0) {
        if (serverLevel0 == this.level) {
            return this;
        } else {
            double $$1 = DimensionType.getTeleportationScale(this.level.m_6042_(), serverLevel0.m_6042_());
            Vec3 $$2 = new Vec3(this.worldPosition.x * $$1, this.worldPosition.y, this.worldPosition.z * $$1);
            return new CommandSourceStack(this.source, $$2, this.rotation, serverLevel0, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, this.returnValueConsumer);
        }
    }

    public CommandSourceStack facing(Entity entity0, EntityAnchorArgument.Anchor entityAnchorArgumentAnchor1) {
        return this.facing(entityAnchorArgumentAnchor1.apply(entity0));
    }

    public CommandSourceStack facing(Vec3 vec0) {
        Vec3 $$1 = this.anchor.apply(this);
        double $$2 = vec0.x - $$1.x;
        double $$3 = vec0.y - $$1.y;
        double $$4 = vec0.z - $$1.z;
        double $$5 = Math.sqrt($$2 * $$2 + $$4 * $$4);
        float $$6 = Mth.wrapDegrees((float) (-(Mth.atan2($$3, $$5) * 180.0F / (float) Math.PI)));
        float $$7 = Mth.wrapDegrees((float) (Mth.atan2($$4, $$2) * 180.0F / (float) Math.PI) - 90.0F);
        return this.withRotation(new Vec2($$6, $$7));
    }

    public CommandSourceStack withSigningContext(CommandSigningContext commandSigningContext0) {
        return commandSigningContext0 == this.signingContext ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, commandSigningContext0, this.chatMessageChainer, this.returnValueConsumer);
    }

    public CommandSourceStack withChatMessageChainer(TaskChainer taskChainer0) {
        return taskChainer0 == this.chatMessageChainer ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, taskChainer0, this.returnValueConsumer);
    }

    public CommandSourceStack withReturnValueConsumer(IntConsumer intConsumer0) {
        return intConsumer0 == this.returnValueConsumer ? this : new CommandSourceStack(this.source, this.worldPosition, this.rotation, this.level, this.permissionLevel, this.textName, this.displayName, this.server, this.entity, this.silent, this.consumer, this.anchor, this.signingContext, this.chatMessageChainer, intConsumer0);
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public String getTextName() {
        return this.textName;
    }

    @Override
    public boolean hasPermission(int int0) {
        return this.permissionLevel >= int0;
    }

    public Vec3 getPosition() {
        return this.worldPosition;
    }

    public ServerLevel getLevel() {
        return this.level;
    }

    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    public Entity getEntityOrException() throws CommandSyntaxException {
        if (this.entity == null) {
            throw ERROR_NOT_ENTITY.create();
        } else {
            return this.entity;
        }
    }

    public ServerPlayer getPlayerOrException() throws CommandSyntaxException {
        Entity var2 = this.entity;
        if (var2 instanceof ServerPlayer) {
            return (ServerPlayer) var2;
        } else {
            throw ERROR_NOT_PLAYER.create();
        }
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.entity instanceof ServerPlayer $$0 ? $$0 : null;
    }

    public boolean isPlayer() {
        return this.entity instanceof ServerPlayer;
    }

    public Vec2 getRotation() {
        return this.rotation;
    }

    public MinecraftServer getServer() {
        return this.server;
    }

    public EntityAnchorArgument.Anchor getAnchor() {
        return this.anchor;
    }

    public CommandSigningContext getSigningContext() {
        return this.signingContext;
    }

    public TaskChainer getChatMessageChainer() {
        return this.chatMessageChainer;
    }

    public IntConsumer getReturnValueConsumer() {
        return this.returnValueConsumer;
    }

    public boolean shouldFilterMessageTo(ServerPlayer serverPlayer0) {
        ServerPlayer $$1 = this.getPlayer();
        return serverPlayer0 == $$1 ? false : $$1 != null && $$1.isTextFilteringEnabled() || serverPlayer0.isTextFilteringEnabled();
    }

    public void sendChatMessage(OutgoingChatMessage outgoingChatMessage0, boolean boolean1, ChatType.Bound chatTypeBound2) {
        if (!this.silent) {
            ServerPlayer $$3 = this.getPlayer();
            if ($$3 != null) {
                $$3.sendChatMessage(outgoingChatMessage0, boolean1, chatTypeBound2);
            } else {
                this.source.sendSystemMessage(chatTypeBound2.decorate(outgoingChatMessage0.content()));
            }
        }
    }

    public void sendSystemMessage(Component component0) {
        if (!this.silent) {
            ServerPlayer $$1 = this.getPlayer();
            if ($$1 != null) {
                $$1.sendSystemMessage(component0);
            } else {
                this.source.sendSystemMessage(component0);
            }
        }
    }

    public void sendSuccess(Supplier<Component> supplierComponent0, boolean boolean1) {
        boolean $$2 = this.source.acceptsSuccess() && !this.silent;
        boolean $$3 = boolean1 && this.source.shouldInformAdmins() && !this.silent;
        if ($$2 || $$3) {
            Component $$4 = (Component) supplierComponent0.get();
            if ($$2) {
                this.source.sendSystemMessage($$4);
            }
            if ($$3) {
                this.broadcastToAdmins($$4);
            }
        }
    }

    private void broadcastToAdmins(Component component0) {
        Component $$1 = Component.translatable("chat.type.admin", this.getDisplayName(), component0).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
        if (this.server.getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
            for (ServerPlayer $$2 : this.server.getPlayerList().getPlayers()) {
                if ($$2 != this.source && this.server.getPlayerList().isOp($$2.m_36316_())) {
                    $$2.sendSystemMessage($$1);
                }
            }
        }
        if (this.source != this.server && this.server.getGameRules().getBoolean(GameRules.RULE_LOGADMINCOMMANDS)) {
            this.server.sendSystemMessage($$1);
        }
    }

    public void sendFailure(Component component0) {
        if (this.source.acceptsFailure() && !this.silent) {
            this.source.sendSystemMessage(Component.empty().append(component0).withStyle(ChatFormatting.RED));
        }
    }

    public void onCommandComplete(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, boolean boolean1, int int2) {
        if (this.consumer != null) {
            this.consumer.onCommandComplete(commandContextCommandSourceStack0, boolean1, int2);
        }
    }

    @Override
    public Collection<String> getOnlinePlayerNames() {
        return Lists.newArrayList(this.server.getPlayerNames());
    }

    @Override
    public Collection<String> getAllTeams() {
        return this.server.getScoreboard().m_83488_();
    }

    @Override
    public Stream<ResourceLocation> getAvailableSounds() {
        return BuiltInRegistries.SOUND_EVENT.stream().map(SoundEvent::m_11660_);
    }

    @Override
    public Stream<ResourceLocation> getRecipeNames() {
        return this.server.getRecipeManager().getRecipeIds();
    }

    @Override
    public CompletableFuture<Suggestions> customSuggestion(CommandContext<?> commandContext0) {
        return Suggestions.empty();
    }

    @Override
    public CompletableFuture<Suggestions> suggestRegistryElements(ResourceKey<? extends Registry<?>> resourceKeyExtendsRegistry0, SharedSuggestionProvider.ElementSuggestionType sharedSuggestionProviderElementSuggestionType1, SuggestionsBuilder suggestionsBuilder2, CommandContext<?> commandContext3) {
        return (CompletableFuture<Suggestions>) this.registryAccess().registry(resourceKeyExtendsRegistry0).map(p_212328_ -> {
            this.m_212335_(p_212328_, sharedSuggestionProviderElementSuggestionType1, suggestionsBuilder2);
            return suggestionsBuilder2.buildFuture();
        }).orElseGet(Suggestions::empty);
    }

    @Override
    public Set<ResourceKey<Level>> levels() {
        return this.server.levelKeys();
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.server.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.level.enabledFeatures();
    }
}