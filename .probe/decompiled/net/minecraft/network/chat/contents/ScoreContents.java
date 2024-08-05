package net.minecraft.network.chat.contents;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;

public class ScoreContents implements ComponentContents {

    private static final String SCORER_PLACEHOLDER = "*";

    private final String name;

    @Nullable
    private final EntitySelector selector;

    private final String objective;

    @Nullable
    private static EntitySelector parseSelector(String string0) {
        try {
            return new EntitySelectorParser(new StringReader(string0)).parse();
        } catch (CommandSyntaxException var2) {
            return null;
        }
    }

    public ScoreContents(String string0, String string1) {
        this.name = string0;
        this.selector = parseSelector(string0);
        this.objective = string1;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public EntitySelector getSelector() {
        return this.selector;
    }

    public String getObjective() {
        return this.objective;
    }

    private String findTargetName(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
        if (this.selector != null) {
            List<? extends Entity> $$1 = this.selector.findEntities(commandSourceStack0);
            if (!$$1.isEmpty()) {
                if ($$1.size() != 1) {
                    throw EntityArgument.ERROR_NOT_SINGLE_ENTITY.create();
                }
                return ((Entity) $$1.get(0)).getScoreboardName();
            }
        }
        return this.name;
    }

    private String getScore(String string0, CommandSourceStack commandSourceStack1) {
        MinecraftServer $$2 = commandSourceStack1.getServer();
        if ($$2 != null) {
            Scoreboard $$3 = $$2.getScoreboard();
            Objective $$4 = $$3.getObjective(this.objective);
            if ($$3.hasPlayerScore(string0, $$4)) {
                Score $$5 = $$3.getOrCreatePlayerScore(string0, $$4);
                return Integer.toString($$5.getScore());
            }
        }
        return "";
    }

    @Override
    public MutableComponent resolve(@Nullable CommandSourceStack commandSourceStack0, @Nullable Entity entity1, int int2) throws CommandSyntaxException {
        if (commandSourceStack0 == null) {
            return Component.empty();
        } else {
            String $$3 = this.findTargetName(commandSourceStack0);
            String $$4 = entity1 != null && $$3.equals("*") ? entity1.getScoreboardName() : $$3;
            return Component.literal(this.getScore($$4, commandSourceStack0));
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof ScoreContents $$1 && this.name.equals($$1.name) && this.objective.equals($$1.objective)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        int $$0 = this.name.hashCode();
        return 31 * $$0 + this.objective.hashCode();
    }

    public String toString() {
        return "score{name='" + this.name + "', objective='" + this.objective + "'}";
    }
}