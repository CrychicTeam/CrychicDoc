package noppes.npcs.mixin;

import java.util.Map;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Score;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Scoreboard.class })
public interface ScoreBoardMixin {

    @Accessor("playerScores")
    Map<String, Map<Objective, Score>> getScores();
}