package net.minecraft.world.level;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicLike;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;

public class GameRules {

    public static final int DEFAULT_RANDOM_TICK_SPEED = 3;

    static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<GameRules.Key<?>, GameRules.Type<?>> GAME_RULE_TYPES = Maps.newTreeMap(Comparator.comparing(p_46218_ -> p_46218_.id));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOFIRETICK = register("doFireTick", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_MOBGRIEFING = register("mobGriefing", GameRules.Category.MOBS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_KEEPINVENTORY = register("keepInventory", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOMOBSPAWNING = register("doMobSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOMOBLOOT = register("doMobLoot", GameRules.Category.DROPS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOBLOCKDROPS = register("doTileDrops", GameRules.Category.DROPS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOENTITYDROPS = register("doEntityDrops", GameRules.Category.DROPS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_COMMANDBLOCKOUTPUT = register("commandBlockOutput", GameRules.Category.CHAT, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_NATURAL_REGENERATION = register("naturalRegeneration", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DAYLIGHT = register("doDaylightCycle", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_LOGADMINCOMMANDS = register("logAdminCommands", GameRules.Category.CHAT, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_SHOWDEATHMESSAGES = register("showDeathMessages", GameRules.Category.CHAT, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_RANDOMTICKING = register("randomTickSpeed", GameRules.Category.UPDATES, GameRules.IntegerValue.create(3));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_SENDCOMMANDFEEDBACK = register("sendCommandFeedback", GameRules.Category.CHAT, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_REDUCEDDEBUGINFO = register("reducedDebugInfo", GameRules.Category.MISC, GameRules.BooleanValue.create(false, (p_46212_, p_46213_) -> {
        byte $$2 = (byte) (p_46213_.get() ? 22 : 23);
        for (ServerPlayer $$3 : p_46212_.getPlayerList().getPlayers()) {
            $$3.connection.send(new ClientboundEntityEventPacket($$3, $$2));
        }
    }));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_SPECTATORSGENERATECHUNKS = register("spectatorsGenerateChunks", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_SPAWN_RADIUS = register("spawnRadius", GameRules.Category.PLAYER, GameRules.IntegerValue.create(10));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DISABLE_ELYTRA_MOVEMENT_CHECK = register("disableElytraMovementCheck", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_MAX_ENTITY_CRAMMING = register("maxEntityCramming", GameRules.Category.MOBS, GameRules.IntegerValue.create(24));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_WEATHER_CYCLE = register("doWeatherCycle", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_LIMITED_CRAFTING = register("doLimitedCrafting", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_MAX_COMMAND_CHAIN_LENGTH = register("maxCommandChainLength", GameRules.Category.MISC, GameRules.IntegerValue.create(65536));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_COMMAND_MODIFICATION_BLOCK_LIMIT = register("commandModificationBlockLimit", GameRules.Category.MISC, GameRules.IntegerValue.create(32768));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_ANNOUNCE_ADVANCEMENTS = register("announceAdvancements", GameRules.Category.CHAT, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DISABLE_RAIDS = register("disableRaids", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DOINSOMNIA = register("doInsomnia", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_IMMEDIATE_RESPAWN = register("doImmediateRespawn", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false, (p_46200_, p_46201_) -> {
        for (ServerPlayer $$2 : p_46200_.getPlayerList().getPlayers()) {
            $$2.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.IMMEDIATE_RESPAWN, p_46201_.get() ? 1.0F : 0.0F));
        }
    }));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DROWNING_DAMAGE = register("drowningDamage", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_FALL_DAMAGE = register("fallDamage", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_FIRE_DAMAGE = register("fireDamage", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_FREEZE_DAMAGE = register("freezeDamage", GameRules.Category.PLAYER, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_PATROL_SPAWNING = register("doPatrolSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_TRADER_SPAWNING = register("doTraderSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_WARDEN_SPAWNING = register("doWardenSpawning", GameRules.Category.SPAWNING, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_FORGIVE_DEAD_PLAYERS = register("forgiveDeadPlayers", GameRules.Category.MOBS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_UNIVERSAL_ANGER = register("universalAnger", GameRules.Category.MOBS, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_PLAYERS_SLEEPING_PERCENTAGE = register("playersSleepingPercentage", GameRules.Category.PLAYER, GameRules.IntegerValue.create(100));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_BLOCK_EXPLOSION_DROP_DECAY = register("blockExplosionDropDecay", GameRules.Category.DROPS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_MOB_EXPLOSION_DROP_DECAY = register("mobExplosionDropDecay", GameRules.Category.DROPS, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_TNT_EXPLOSION_DROP_DECAY = register("tntExplosionDropDecay", GameRules.Category.DROPS, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.IntegerValue> RULE_SNOW_ACCUMULATION_HEIGHT = register("snowAccumulationHeight", GameRules.Category.UPDATES, GameRules.IntegerValue.create(1));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_WATER_SOURCE_CONVERSION = register("waterSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_LAVA_SOURCE_CONVERSION = register("lavaSourceConversion", GameRules.Category.UPDATES, GameRules.BooleanValue.create(false));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_GLOBAL_SOUND_EVENTS = register("globalSoundEvents", GameRules.Category.MISC, GameRules.BooleanValue.create(true));

    public static final GameRules.Key<GameRules.BooleanValue> RULE_DO_VINES_SPREAD = register("doVinesSpread", GameRules.Category.UPDATES, GameRules.BooleanValue.create(true));

    private final Map<GameRules.Key<?>, GameRules.Value<?>> rules;

    private static <T extends GameRules.Value<T>> GameRules.Key<T> register(String string0, GameRules.Category gameRulesCategory1, GameRules.Type<T> gameRulesTypeT2) {
        GameRules.Key<T> $$3 = new GameRules.Key<>(string0, gameRulesCategory1);
        GameRules.Type<?> $$4 = (GameRules.Type<?>) GAME_RULE_TYPES.put($$3, gameRulesTypeT2);
        if ($$4 != null) {
            throw new IllegalStateException("Duplicate game rule registration for " + string0);
        } else {
            return $$3;
        }
    }

    public GameRules(DynamicLike<?> dynamicLike0) {
        this();
        this.loadFromTag(dynamicLike0);
    }

    public GameRules() {
        this.rules = (Map<GameRules.Key<?>, GameRules.Value<?>>) GAME_RULE_TYPES.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, p_46210_ -> ((GameRules.Type) p_46210_.getValue()).createRule()));
    }

    private GameRules(Map<GameRules.Key<?>, GameRules.Value<?>> mapGameRulesKeyGameRulesValue0) {
        this.rules = mapGameRulesKeyGameRulesValue0;
    }

    public <T extends GameRules.Value<T>> T getRule(GameRules.Key<T> gameRulesKeyT0) {
        return (T) this.rules.get(gameRulesKeyT0);
    }

    public CompoundTag createTag() {
        CompoundTag $$0 = new CompoundTag();
        this.rules.forEach((p_46197_, p_46198_) -> $$0.putString(p_46197_.id, p_46198_.serialize()));
        return $$0;
    }

    private void loadFromTag(DynamicLike<?> dynamicLike0) {
        this.rules.forEach((p_46187_, p_46188_) -> dynamicLike0.get(p_46187_.id).asString().result().ifPresent(p_46188_::m_7377_));
    }

    public GameRules copy() {
        return new GameRules((Map<GameRules.Key<?>, GameRules.Value<?>>) this.rules.entrySet().stream().collect(ImmutableMap.toImmutableMap(Entry::getKey, p_46194_ -> ((GameRules.Value) p_46194_.getValue()).copy())));
    }

    public static void visitGameRuleTypes(GameRules.GameRuleTypeVisitor gameRulesGameRuleTypeVisitor0) {
        GAME_RULE_TYPES.forEach((p_46205_, p_46206_) -> callVisitorCap(gameRulesGameRuleTypeVisitor0, p_46205_, p_46206_));
    }

    private static <T extends GameRules.Value<T>> void callVisitorCap(GameRules.GameRuleTypeVisitor gameRulesGameRuleTypeVisitor0, GameRules.Key<?> gameRulesKey1, GameRules.Type<?> gameRulesType2) {
        gameRulesGameRuleTypeVisitor0.visit(gameRulesKey1, gameRulesType2);
        gameRulesType2.callVisitor(gameRulesGameRuleTypeVisitor0, gameRulesKey1);
    }

    public void assignFrom(GameRules gameRules0, @Nullable MinecraftServer minecraftServer1) {
        gameRules0.rules.keySet().forEach(p_46182_ -> this.assignCap(p_46182_, gameRules0, minecraftServer1));
    }

    private <T extends GameRules.Value<T>> void assignCap(GameRules.Key<T> gameRulesKeyT0, GameRules gameRules1, @Nullable MinecraftServer minecraftServer2) {
        T $$3 = gameRules1.getRule(gameRulesKeyT0);
        this.<T>getRule(gameRulesKeyT0).setFrom($$3, minecraftServer2);
    }

    public boolean getBoolean(GameRules.Key<GameRules.BooleanValue> gameRulesKeyGameRulesBooleanValue0) {
        return this.getRule(gameRulesKeyGameRulesBooleanValue0).get();
    }

    public int getInt(GameRules.Key<GameRules.IntegerValue> gameRulesKeyGameRulesIntegerValue0) {
        return this.getRule(gameRulesKeyGameRulesIntegerValue0).get();
    }

    public static class BooleanValue extends GameRules.Value<GameRules.BooleanValue> {

        private boolean value;

        static GameRules.Type<GameRules.BooleanValue> create(boolean boolean0, BiConsumer<MinecraftServer, GameRules.BooleanValue> biConsumerMinecraftServerGameRulesBooleanValue1) {
            return new GameRules.Type<>(BoolArgumentType::bool, p_46242_ -> new GameRules.BooleanValue(p_46242_, boolean0), biConsumerMinecraftServerGameRulesBooleanValue1, GameRules.GameRuleTypeVisitor::m_6891_);
        }

        static GameRules.Type<GameRules.BooleanValue> create(boolean boolean0) {
            return create(boolean0, (p_46236_, p_46237_) -> {
            });
        }

        public BooleanValue(GameRules.Type<GameRules.BooleanValue> gameRulesTypeGameRulesBooleanValue0, boolean boolean1) {
            super(gameRulesTypeGameRulesBooleanValue0);
            this.value = boolean1;
        }

        @Override
        protected void updateFromArgument(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
            this.value = BoolArgumentType.getBool(commandContextCommandSourceStack0, string1);
        }

        public boolean get() {
            return this.value;
        }

        public void set(boolean boolean0, @Nullable MinecraftServer minecraftServer1) {
            this.value = boolean0;
            this.m_46368_(minecraftServer1);
        }

        @Override
        public String serialize() {
            return Boolean.toString(this.value);
        }

        @Override
        protected void deserialize(String string0) {
            this.value = Boolean.parseBoolean(string0);
        }

        @Override
        public int getCommandResult() {
            return this.value ? 1 : 0;
        }

        protected GameRules.BooleanValue getSelf() {
            return this;
        }

        protected GameRules.BooleanValue copy() {
            return new GameRules.BooleanValue(this.f_46360_, this.value);
        }

        public void setFrom(GameRules.BooleanValue gameRulesBooleanValue0, @Nullable MinecraftServer minecraftServer1) {
            this.value = gameRulesBooleanValue0.value;
            this.m_46368_(minecraftServer1);
        }
    }

    public static enum Category {

        PLAYER("gamerule.category.player"),
        MOBS("gamerule.category.mobs"),
        SPAWNING("gamerule.category.spawning"),
        DROPS("gamerule.category.drops"),
        UPDATES("gamerule.category.updates"),
        CHAT("gamerule.category.chat"),
        MISC("gamerule.category.misc");

        private final String descriptionId;

        private Category(String p_46273_) {
            this.descriptionId = p_46273_;
        }

        public String getDescriptionId() {
            return this.descriptionId;
        }
    }

    public interface GameRuleTypeVisitor {

        default <T extends GameRules.Value<T>> void visit(GameRules.Key<T> gameRulesKeyT0, GameRules.Type<T> gameRulesTypeT1) {
        }

        default void visitBoolean(GameRules.Key<GameRules.BooleanValue> gameRulesKeyGameRulesBooleanValue0, GameRules.Type<GameRules.BooleanValue> gameRulesTypeGameRulesBooleanValue1) {
        }

        default void visitInteger(GameRules.Key<GameRules.IntegerValue> gameRulesKeyGameRulesIntegerValue0, GameRules.Type<GameRules.IntegerValue> gameRulesTypeGameRulesIntegerValue1) {
        }
    }

    public static class IntegerValue extends GameRules.Value<GameRules.IntegerValue> {

        private int value;

        private static GameRules.Type<GameRules.IntegerValue> create(int int0, BiConsumer<MinecraftServer, GameRules.IntegerValue> biConsumerMinecraftServerGameRulesIntegerValue1) {
            return new GameRules.Type<>(IntegerArgumentType::integer, p_46293_ -> new GameRules.IntegerValue(p_46293_, int0), biConsumerMinecraftServerGameRulesIntegerValue1, GameRules.GameRuleTypeVisitor::m_6894_);
        }

        static GameRules.Type<GameRules.IntegerValue> create(int int0) {
            return create(int0, (p_46309_, p_46310_) -> {
            });
        }

        public IntegerValue(GameRules.Type<GameRules.IntegerValue> gameRulesTypeGameRulesIntegerValue0, int int1) {
            super(gameRulesTypeGameRulesIntegerValue0);
            this.value = int1;
        }

        @Override
        protected void updateFromArgument(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
            this.value = IntegerArgumentType.getInteger(commandContextCommandSourceStack0, string1);
        }

        public int get() {
            return this.value;
        }

        public void set(int int0, @Nullable MinecraftServer minecraftServer1) {
            this.value = int0;
            this.m_46368_(minecraftServer1);
        }

        @Override
        public String serialize() {
            return Integer.toString(this.value);
        }

        @Override
        protected void deserialize(String string0) {
            this.value = safeParse(string0);
        }

        public boolean tryDeserialize(String string0) {
            try {
                this.value = Integer.parseInt(string0);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }

        private static int safeParse(String string0) {
            if (!string0.isEmpty()) {
                try {
                    return Integer.parseInt(string0);
                } catch (NumberFormatException var2) {
                    GameRules.LOGGER.warn("Failed to parse integer {}", string0);
                }
            }
            return 0;
        }

        @Override
        public int getCommandResult() {
            return this.value;
        }

        protected GameRules.IntegerValue getSelf() {
            return this;
        }

        protected GameRules.IntegerValue copy() {
            return new GameRules.IntegerValue(this.f_46360_, this.value);
        }

        public void setFrom(GameRules.IntegerValue gameRulesIntegerValue0, @Nullable MinecraftServer minecraftServer1) {
            this.value = gameRulesIntegerValue0.value;
            this.m_46368_(minecraftServer1);
        }
    }

    public static final class Key<T extends GameRules.Value<T>> {

        final String id;

        private final GameRules.Category category;

        public Key(String string0, GameRules.Category gameRulesCategory1) {
            this.id = string0;
            this.category = gameRulesCategory1;
        }

        public String toString() {
            return this.id;
        }

        public boolean equals(Object object0) {
            return this == object0 ? true : object0 instanceof GameRules.Key && ((GameRules.Key) object0).id.equals(this.id);
        }

        public int hashCode() {
            return this.id.hashCode();
        }

        public String getId() {
            return this.id;
        }

        public String getDescriptionId() {
            return "gamerule." + this.id;
        }

        public GameRules.Category getCategory() {
            return this.category;
        }
    }

    public static class Type<T extends GameRules.Value<T>> {

        private final Supplier<ArgumentType<?>> argument;

        private final Function<GameRules.Type<T>, T> constructor;

        final BiConsumer<MinecraftServer, T> callback;

        private final GameRules.VisitorCaller<T> visitorCaller;

        Type(Supplier<ArgumentType<?>> supplierArgumentType0, Function<GameRules.Type<T>, T> functionGameRulesTypeTT1, BiConsumer<MinecraftServer, T> biConsumerMinecraftServerT2, GameRules.VisitorCaller<T> gameRulesVisitorCallerT3) {
            this.argument = supplierArgumentType0;
            this.constructor = functionGameRulesTypeTT1;
            this.callback = biConsumerMinecraftServerT2;
            this.visitorCaller = gameRulesVisitorCallerT3;
        }

        public RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(String string0) {
            return Commands.argument(string0, (ArgumentType<T>) this.argument.get());
        }

        public T createRule() {
            return (T) this.constructor.apply(this);
        }

        public void callVisitor(GameRules.GameRuleTypeVisitor gameRulesGameRuleTypeVisitor0, GameRules.Key<T> gameRulesKeyT1) {
            this.visitorCaller.call(gameRulesGameRuleTypeVisitor0, gameRulesKeyT1, this);
        }
    }

    public abstract static class Value<T extends GameRules.Value<T>> {

        protected final GameRules.Type<T> type;

        public Value(GameRules.Type<T> gameRulesTypeT0) {
            this.type = gameRulesTypeT0;
        }

        protected abstract void updateFromArgument(CommandContext<CommandSourceStack> var1, String var2);

        public void setFromArgument(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) {
            this.updateFromArgument(commandContextCommandSourceStack0, string1);
            this.onChanged(((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer());
        }

        protected void onChanged(@Nullable MinecraftServer minecraftServer0) {
            if (minecraftServer0 != null) {
                this.type.callback.accept(minecraftServer0, this.getSelf());
            }
        }

        protected abstract void deserialize(String var1);

        public abstract String serialize();

        public String toString() {
            return this.serialize();
        }

        public abstract int getCommandResult();

        protected abstract T getSelf();

        protected abstract T copy();

        public abstract void setFrom(T var1, @Nullable MinecraftServer var2);
    }

    interface VisitorCaller<T extends GameRules.Value<T>> {

        void call(GameRules.GameRuleTypeVisitor var1, GameRules.Key<T> var2, GameRules.Type<T> var3);
    }
}