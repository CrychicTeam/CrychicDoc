package net.minecraft.server.commands;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic4CommandExceptionType;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.scores.Team;

public class SpreadPlayersCommand {

    private static final int MAX_ITERATION_COUNT = 10000;

    private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_TEAMS = new Dynamic4CommandExceptionType((p_138745_, p_138746_, p_138747_, p_138748_) -> Component.translatable("commands.spreadplayers.failed.teams", p_138745_, p_138746_, p_138747_, p_138748_));

    private static final Dynamic4CommandExceptionType ERROR_FAILED_TO_SPREAD_ENTITIES = new Dynamic4CommandExceptionType((p_138723_, p_138724_, p_138725_, p_138726_) -> Component.translatable("commands.spreadplayers.failed.entities", p_138723_, p_138724_, p_138725_, p_138726_));

    private static final Dynamic2CommandExceptionType ERROR_INVALID_MAX_HEIGHT = new Dynamic2CommandExceptionType((p_201854_, p_201855_) -> Component.translatable("commands.spreadplayers.failed.invalid.height", p_201854_, p_201855_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("spreadplayers").requires(p_201852_ -> p_201852_.hasPermission(2))).then(Commands.argument("center", Vec2Argument.vec2()).then(Commands.argument("spreadDistance", FloatArgumentType.floatArg(0.0F)).then(((RequiredArgumentBuilder) Commands.argument("maxRange", FloatArgumentType.floatArg(1.0F)).then(Commands.argument("respectTeams", BoolArgumentType.bool()).then(Commands.argument("targets", EntityArgument.entities()).executes(p_288627_ -> spreadPlayers((CommandSourceStack) p_288627_.getSource(), Vec2Argument.getVec2(p_288627_, "center"), FloatArgumentType.getFloat(p_288627_, "spreadDistance"), FloatArgumentType.getFloat(p_288627_, "maxRange"), ((CommandSourceStack) p_288627_.getSource()).getLevel().m_151558_(), BoolArgumentType.getBool(p_288627_, "respectTeams"), EntityArgument.getEntities(p_288627_, "targets")))))).then(Commands.literal("under").then(Commands.argument("maxHeight", IntegerArgumentType.integer()).then(Commands.argument("respectTeams", BoolArgumentType.bool()).then(Commands.argument("targets", EntityArgument.entities()).executes(p_201850_ -> spreadPlayers((CommandSourceStack) p_201850_.getSource(), Vec2Argument.getVec2(p_201850_, "center"), FloatArgumentType.getFloat(p_201850_, "spreadDistance"), FloatArgumentType.getFloat(p_201850_, "maxRange"), IntegerArgumentType.getInteger(p_201850_, "maxHeight"), BoolArgumentType.getBool(p_201850_, "respectTeams"), EntityArgument.getEntities(p_201850_, "targets")))))))))));
    }

    private static int spreadPlayers(CommandSourceStack commandSourceStack0, Vec2 vec1, float float2, float float3, int int4, boolean boolean5, Collection<? extends Entity> collectionExtendsEntity6) throws CommandSyntaxException {
        ServerLevel $$7 = commandSourceStack0.getLevel();
        int $$8 = $$7.m_141937_();
        if (int4 < $$8) {
            throw ERROR_INVALID_MAX_HEIGHT.create(int4, $$8);
        } else {
            RandomSource $$9 = RandomSource.create();
            double $$10 = (double) (vec1.x - float3);
            double $$11 = (double) (vec1.y - float3);
            double $$12 = (double) (vec1.x + float3);
            double $$13 = (double) (vec1.y + float3);
            SpreadPlayersCommand.Position[] $$14 = createInitialPositions($$9, boolean5 ? getNumberOfTeams(collectionExtendsEntity6) : collectionExtendsEntity6.size(), $$10, $$11, $$12, $$13);
            spreadPositions(vec1, (double) float2, $$7, $$9, $$10, $$11, $$12, $$13, int4, $$14, boolean5);
            double $$15 = setPlayerPositions(collectionExtendsEntity6, $$7, $$14, int4, boolean5);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.spreadplayers.success." + (boolean5 ? "teams" : "entities"), $$14.length, vec1.x, vec1.y, String.format(Locale.ROOT, "%.2f", $$15)), true);
            return $$14.length;
        }
    }

    private static int getNumberOfTeams(Collection<? extends Entity> collectionExtendsEntity0) {
        Set<Team> $$1 = Sets.newHashSet();
        for (Entity $$2 : collectionExtendsEntity0) {
            if ($$2 instanceof Player) {
                $$1.add($$2.getTeam());
            } else {
                $$1.add(null);
            }
        }
        return $$1.size();
    }

    private static void spreadPositions(Vec2 vec0, double double1, ServerLevel serverLevel2, RandomSource randomSource3, double double4, double double5, double double6, double double7, int int8, SpreadPlayersCommand.Position[] spreadPlayersCommandPosition9, boolean boolean10) throws CommandSyntaxException {
        boolean $$11 = true;
        double $$12 = Float.MAX_VALUE;
        int $$13;
        for ($$13 = 0; $$13 < 10000 && $$11; $$13++) {
            $$11 = false;
            $$12 = Float.MAX_VALUE;
            for (int $$14 = 0; $$14 < spreadPlayersCommandPosition9.length; $$14++) {
                SpreadPlayersCommand.Position $$15 = spreadPlayersCommandPosition9[$$14];
                int $$16 = 0;
                SpreadPlayersCommand.Position $$17 = new SpreadPlayersCommand.Position();
                for (int $$18 = 0; $$18 < spreadPlayersCommandPosition9.length; $$18++) {
                    if ($$14 != $$18) {
                        SpreadPlayersCommand.Position $$19 = spreadPlayersCommandPosition9[$$18];
                        double $$20 = $$15.dist($$19);
                        $$12 = Math.min($$20, $$12);
                        if ($$20 < double1) {
                            $$16++;
                            $$17.x = $$17.x + ($$19.x - $$15.x);
                            $$17.z = $$17.z + ($$19.z - $$15.z);
                        }
                    }
                }
                if ($$16 > 0) {
                    $$17.x /= (double) $$16;
                    $$17.z /= (double) $$16;
                    double $$21 = $$17.getLength();
                    if ($$21 > 0.0) {
                        $$17.normalize();
                        $$15.moveAway($$17);
                    } else {
                        $$15.randomize(randomSource3, double4, double5, double6, double7);
                    }
                    $$11 = true;
                }
                if ($$15.clamp(double4, double5, double6, double7)) {
                    $$11 = true;
                }
            }
            if (!$$11) {
                for (SpreadPlayersCommand.Position $$22 : spreadPlayersCommandPosition9) {
                    if (!$$22.isSafe(serverLevel2, int8)) {
                        $$22.randomize(randomSource3, double4, double5, double6, double7);
                        $$11 = true;
                    }
                }
            }
        }
        if ($$12 == Float.MAX_VALUE) {
            $$12 = 0.0;
        }
        if ($$13 >= 10000) {
            if (boolean10) {
                throw ERROR_FAILED_TO_SPREAD_TEAMS.create(spreadPlayersCommandPosition9.length, vec0.x, vec0.y, String.format(Locale.ROOT, "%.2f", $$12));
            } else {
                throw ERROR_FAILED_TO_SPREAD_ENTITIES.create(spreadPlayersCommandPosition9.length, vec0.x, vec0.y, String.format(Locale.ROOT, "%.2f", $$12));
            }
        }
    }

    private static double setPlayerPositions(Collection<? extends Entity> collectionExtendsEntity0, ServerLevel serverLevel1, SpreadPlayersCommand.Position[] spreadPlayersCommandPosition2, int int3, boolean boolean4) {
        double $$5 = 0.0;
        int $$6 = 0;
        Map<Team, SpreadPlayersCommand.Position> $$7 = Maps.newHashMap();
        for (Entity $$8 : collectionExtendsEntity0) {
            SpreadPlayersCommand.Position $$10;
            if (boolean4) {
                Team $$9 = $$8 instanceof Player ? $$8.getTeam() : null;
                if (!$$7.containsKey($$9)) {
                    $$7.put($$9, spreadPlayersCommandPosition2[$$6++]);
                }
                $$10 = (SpreadPlayersCommand.Position) $$7.get($$9);
            } else {
                $$10 = spreadPlayersCommandPosition2[$$6++];
            }
            $$8.teleportTo(serverLevel1, (double) Mth.floor($$10.x) + 0.5, (double) $$10.getSpawnY(serverLevel1, int3), (double) Mth.floor($$10.z) + 0.5, Set.of(), $$8.getYRot(), $$8.getXRot());
            double $$12 = Double.MAX_VALUE;
            for (SpreadPlayersCommand.Position $$13 : spreadPlayersCommandPosition2) {
                if ($$10 != $$13) {
                    double $$14 = $$10.dist($$13);
                    $$12 = Math.min($$14, $$12);
                }
            }
            $$5 += $$12;
        }
        return collectionExtendsEntity0.size() < 2 ? 0.0 : $$5 / (double) collectionExtendsEntity0.size();
    }

    private static SpreadPlayersCommand.Position[] createInitialPositions(RandomSource randomSource0, int int1, double double2, double double3, double double4, double double5) {
        SpreadPlayersCommand.Position[] $$6 = new SpreadPlayersCommand.Position[int1];
        for (int $$7 = 0; $$7 < $$6.length; $$7++) {
            SpreadPlayersCommand.Position $$8 = new SpreadPlayersCommand.Position();
            $$8.randomize(randomSource0, double2, double3, double4, double5);
            $$6[$$7] = $$8;
        }
        return $$6;
    }

    static class Position {

        double x;

        double z;

        double dist(SpreadPlayersCommand.Position spreadPlayersCommandPosition0) {
            double $$1 = this.x - spreadPlayersCommandPosition0.x;
            double $$2 = this.z - spreadPlayersCommandPosition0.z;
            return Math.sqrt($$1 * $$1 + $$2 * $$2);
        }

        void normalize() {
            double $$0 = this.getLength();
            this.x /= $$0;
            this.z /= $$0;
        }

        double getLength() {
            return Math.sqrt(this.x * this.x + this.z * this.z);
        }

        public void moveAway(SpreadPlayersCommand.Position spreadPlayersCommandPosition0) {
            this.x = this.x - spreadPlayersCommandPosition0.x;
            this.z = this.z - spreadPlayersCommandPosition0.z;
        }

        public boolean clamp(double double0, double double1, double double2, double double3) {
            boolean $$4 = false;
            if (this.x < double0) {
                this.x = double0;
                $$4 = true;
            } else if (this.x > double2) {
                this.x = double2;
                $$4 = true;
            }
            if (this.z < double1) {
                this.z = double1;
                $$4 = true;
            } else if (this.z > double3) {
                this.z = double3;
                $$4 = true;
            }
            return $$4;
        }

        public int getSpawnY(BlockGetter blockGetter0, int int1) {
            BlockPos.MutableBlockPos $$2 = new BlockPos.MutableBlockPos(this.x, (double) (int1 + 1), this.z);
            boolean $$3 = blockGetter0.getBlockState($$2).m_60795_();
            $$2.move(Direction.DOWN);
            boolean $$4 = blockGetter0.getBlockState($$2).m_60795_();
            while ($$2.m_123342_() > blockGetter0.m_141937_()) {
                $$2.move(Direction.DOWN);
                boolean $$5 = blockGetter0.getBlockState($$2).m_60795_();
                if (!$$5 && $$4 && $$3) {
                    return $$2.m_123342_() + 1;
                }
                $$3 = $$4;
                $$4 = $$5;
            }
            return int1 + 1;
        }

        public boolean isSafe(BlockGetter blockGetter0, int int1) {
            BlockPos $$2 = BlockPos.containing(this.x, (double) (this.getSpawnY(blockGetter0, int1) - 1), this.z);
            BlockState $$3 = blockGetter0.getBlockState($$2);
            return $$2.m_123342_() < int1 && !$$3.m_278721_() && !$$3.m_204336_(BlockTags.FIRE);
        }

        public void randomize(RandomSource randomSource0, double double1, double double2, double double3, double double4) {
            this.x = Mth.nextDouble(randomSource0, double1, double3);
            this.z = Mth.nextDouble(randomSource0, double2, double4);
        }
    }
}