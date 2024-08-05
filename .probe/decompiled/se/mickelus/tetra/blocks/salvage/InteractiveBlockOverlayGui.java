package se.mickelus.tetra.blocks.salvage;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import se.mickelus.mutil.gui.hud.GuiRootHud;
import se.mickelus.tetra.properties.PropertyHelper;

@ParametersAreNonnullByDefault
public class InteractiveBlockOverlayGui extends GuiRootHud {

    public void update(Level world, BlockPos pos, BlockState blockState, Direction face, Player player, boolean transition) {
        if (blockState.m_60734_() instanceof IInteractiveBlock) {
            IInteractiveBlock block = (IInteractiveBlock) blockState.m_60734_();
            BlockInteraction[] interactions = block.getPotentialInteractions(world, pos, blockState, face, PropertyHelper.getPlayerTools(player));
            if (transition) {
                Collection<InteractiveOutlineGui> previousOutlines = (Collection<InteractiveOutlineGui>) this.elements.stream().filter(element -> element instanceof InteractiveOutlineGui).map(element -> (InteractiveOutlineGui) element).collect(Collectors.toCollection(LinkedList::new));
                previousOutlines.stream().filter(outline -> Arrays.stream(interactions).noneMatch(interaction -> interaction.equals(outline.getBlockInteraction()))).forEach(outline -> outline.transitionOut(outline::remove));
                Arrays.stream(interactions).filter(interaction -> previousOutlines.stream().map(InteractiveOutlineGui::getBlockInteraction).noneMatch(interaction::equals)).map(interaction -> new InteractiveOutlineGui(interaction, player)).forEach(this::addChild);
            } else {
                this.clearChildren();
                Arrays.stream(interactions).map(interaction -> new InteractiveOutlineGui(interaction, player)).forEach(this::addChild);
            }
        } else {
            this.clearChildren();
        }
    }
}