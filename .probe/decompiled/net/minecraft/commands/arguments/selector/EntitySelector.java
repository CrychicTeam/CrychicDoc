package net.minecraft.commands.arguments.selector;

import com.google.common.collect.Lists;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EntitySelector {

    public static final int INFINITE = Integer.MAX_VALUE;

    public static final BiConsumer<Vec3, List<? extends Entity>> ORDER_ARBITRARY = (p_261404_, p_261405_) -> {
    };

    private static final EntityTypeTest<Entity, ?> ANY_TYPE = new EntityTypeTest<Entity, Entity>() {

        public Entity tryCast(Entity p_175109_) {
            return p_175109_;
        }

        @Override
        public Class<? extends Entity> getBaseClass() {
            return Entity.class;
        }
    };

    private final int maxResults;

    private final boolean includesEntities;

    private final boolean worldLimited;

    private final Predicate<Entity> predicate;

    private final MinMaxBounds.Doubles range;

    private final Function<Vec3, Vec3> position;

    @Nullable
    private final AABB aabb;

    private final BiConsumer<Vec3, List<? extends Entity>> order;

    private final boolean currentEntity;

    @Nullable
    private final String playerName;

    @Nullable
    private final UUID entityUUID;

    private final EntityTypeTest<Entity, ?> type;

    private final boolean usesSelector;

    public EntitySelector(int int0, boolean boolean1, boolean boolean2, Predicate<Entity> predicateEntity3, MinMaxBounds.Doubles minMaxBoundsDoubles4, Function<Vec3, Vec3> functionVecVec5, @Nullable AABB aABB6, BiConsumer<Vec3, List<? extends Entity>> biConsumerVecListExtendsEntity7, boolean boolean8, @Nullable String string9, @Nullable UUID uUID10, @Nullable EntityType<?> entityType11, boolean boolean12) {
        this.maxResults = int0;
        this.includesEntities = boolean1;
        this.worldLimited = boolean2;
        this.predicate = predicateEntity3;
        this.range = minMaxBoundsDoubles4;
        this.position = functionVecVec5;
        this.aabb = aABB6;
        this.order = biConsumerVecListExtendsEntity7;
        this.currentEntity = boolean8;
        this.playerName = string9;
        this.entityUUID = uUID10;
        this.type = (EntityTypeTest<Entity, ?>) (entityType11 == null ? ANY_TYPE : entityType11);
        this.usesSelector = boolean12;
    }

    public int getMaxResults() {
        return this.maxResults;
    }

    public boolean includesEntities() {
        return this.includesEntities;
    }

    public boolean isSelfSelector() {
        return this.currentEntity;
    }

    public boolean isWorldLimited() {
        return this.worldLimited;
    }

    public boolean usesSelector() {
        return this.usesSelector;
    }

    private void checkPermissions(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        if (this.usesSelector && !commandSourceStack0.hasPermission(2)) {
            throw EntityArgument.ERROR_SELECTORS_NOT_ALLOWED.create();
        }
    }

    public Entity findSingleEntity(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        this.checkPermissions(commandSourceStack0);
        List<? extends Entity> $$1 = this.findEntities(commandSourceStack0);
        if ($$1.isEmpty()) {
            throw EntityArgument.NO_ENTITIES_FOUND.create();
        } else if ($$1.size() > 1) {
            throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
        } else {
            return (Entity) $$1.get(0);
        }
    }

    public List<? extends Entity> findEntities(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        return this.findEntitiesRaw(commandSourceStack0).stream().filter(p_247981_ -> p_247981_.getType().m_245993_(commandSourceStack0.enabledFeatures())).toList();
    }

    private List<? extends Entity> findEntitiesRaw(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        this.checkPermissions(commandSourceStack0);
        if (!this.includesEntities) {
            return this.findPlayers(commandSourceStack0);
        } else if (this.playerName != null) {
            ServerPlayer $$1 = commandSourceStack0.getServer().getPlayerList().getPlayerByName(this.playerName);
            return (List<? extends Entity>) ($$1 == null ? Collections.emptyList() : Lists.newArrayList(new ServerPlayer[] { $$1 }));
        } else if (this.entityUUID != null) {
            for (ServerLevel $$2 : commandSourceStack0.getServer().getAllLevels()) {
                Entity $$3 = $$2.getEntity(this.entityUUID);
                if ($$3 != null) {
                    return Lists.newArrayList(new Entity[] { $$3 });
                }
            }
            return Collections.emptyList();
        } else {
            Vec3 $$4 = (Vec3) this.position.apply(commandSourceStack0.getPosition());
            Predicate<Entity> $$5 = this.getPredicate($$4);
            if (this.currentEntity) {
                return (List<? extends Entity>) (commandSourceStack0.getEntity() != null && $$5.test(commandSourceStack0.getEntity()) ? Lists.newArrayList(new Entity[] { commandSourceStack0.getEntity() }) : Collections.emptyList());
            } else {
                List<Entity> $$6 = Lists.newArrayList();
                if (this.isWorldLimited()) {
                    this.addEntities($$6, commandSourceStack0.getLevel(), $$4, $$5);
                } else {
                    for (ServerLevel $$7 : commandSourceStack0.getServer().getAllLevels()) {
                        this.addEntities($$6, $$7, $$4, $$5);
                    }
                }
                return this.sortAndLimit($$4, $$6);
            }
        }
    }

    private void addEntities(List<Entity> listEntity0, ServerLevel serverLevel1, Vec3 vec2, Predicate<Entity> predicateEntity3) {
        int $$4 = this.getResultLimit();
        if (listEntity0.size() < $$4) {
            if (this.aabb != null) {
                serverLevel1.m_260826_(this.type, this.aabb.move(vec2), predicateEntity3, listEntity0, $$4);
            } else {
                serverLevel1.getEntities(this.type, predicateEntity3, listEntity0, $$4);
            }
        }
    }

    private int getResultLimit() {
        return this.order == ORDER_ARBITRARY ? this.maxResults : Integer.MAX_VALUE;
    }

    public ServerPlayer findSinglePlayer(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        this.checkPermissions(commandSourceStack0);
        List<ServerPlayer> $$1 = this.findPlayers(commandSourceStack0);
        if ($$1.size() != 1) {
            throw EntityArgument.NO_PLAYERS_FOUND.create();
        } else {
            return (ServerPlayer) $$1.get(0);
        }
    }

    public List<ServerPlayer> findPlayers(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        this.checkPermissions(commandSourceStack0);
        if (this.playerName != null) {
            ServerPlayer $$1 = commandSourceStack0.getServer().getPlayerList().getPlayerByName(this.playerName);
            return (List<ServerPlayer>) ($$1 == null ? Collections.emptyList() : Lists.newArrayList(new ServerPlayer[] { $$1 }));
        } else if (this.entityUUID != null) {
            ServerPlayer $$2 = commandSourceStack0.getServer().getPlayerList().getPlayer(this.entityUUID);
            return (List<ServerPlayer>) ($$2 == null ? Collections.emptyList() : Lists.newArrayList(new ServerPlayer[] { $$2 }));
        } else {
            Vec3 $$3 = (Vec3) this.position.apply(commandSourceStack0.getPosition());
            Predicate<Entity> $$4 = this.getPredicate($$3);
            if (this.currentEntity) {
                if (commandSourceStack0.getEntity() instanceof ServerPlayer) {
                    ServerPlayer $$5 = (ServerPlayer) commandSourceStack0.getEntity();
                    if ($$4.test($$5)) {
                        return Lists.newArrayList(new ServerPlayer[] { $$5 });
                    }
                }
                return Collections.emptyList();
            } else {
                int $$6 = this.getResultLimit();
                List<ServerPlayer> $$7;
                if (this.isWorldLimited()) {
                    $$7 = commandSourceStack0.getLevel().getPlayers($$4, $$6);
                } else {
                    $$7 = Lists.newArrayList();
                    for (ServerPlayer $$9 : commandSourceStack0.getServer().getPlayerList().getPlayers()) {
                        if ($$4.test($$9)) {
                            $$7.add($$9);
                            if ($$7.size() >= $$6) {
                                return $$7;
                            }
                        }
                    }
                }
                return this.sortAndLimit($$3, $$7);
            }
        }
    }

    private Predicate<Entity> getPredicate(Vec3 vec0) {
        Predicate<Entity> $$1 = this.predicate;
        if (this.aabb != null) {
            AABB $$2 = this.aabb.move(vec0);
            $$1 = $$1.and(p_121143_ -> $$2.intersects(p_121143_.getBoundingBox()));
        }
        if (!this.range.m_55327_()) {
            $$1 = $$1.and(p_121148_ -> this.range.matchesSqr(p_121148_.distanceToSqr(vec0)));
        }
        return $$1;
    }

    private <T extends Entity> List<T> sortAndLimit(Vec3 vec0, List<T> listT1) {
        if (listT1.size() > 1) {
            this.order.accept(vec0, listT1);
        }
        return listT1.subList(0, Math.min(this.maxResults, listT1.size()));
    }

    public static Component joinNames(List<? extends Entity> listExtendsEntity0) {
        return ComponentUtils.formatList(listExtendsEntity0, Entity::m_5446_);
    }
}