package snownee.jade.addon.create;

import com.simibubi.create.content.contraptions.IDisplayAssemblyExceptions;
import com.simibubi.create.content.contraptions.piston.MechanicalPistonBlock;
import com.simibubi.create.content.contraptions.piston.PistonExtensionPoleBlock;
import com.simibubi.create.content.equipment.goggles.GoggleOverlayRenderer;
import com.simibubi.create.content.equipment.goggles.GogglesItem;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.equipment.goggles.IHaveHoveringInformation;
import com.simibubi.create.content.fluids.drain.ItemDrainBlockEntity;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.fluids.tank.FluidTankBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.content.trains.entity.TrainRelocator;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.IElementHelper;

public class GogglesProvider implements IBlockComponentProvider {

    private static final ResourceLocation CREATE_OVERLAY_ID = new ResourceLocation("create", "goggle_info");

    private static final Set<String> REMOVE_KEYS = Set.of("create.tooltip.chute.contains", "create.tooltip.deployer.contains");

    private final Block PISTON_EXTENSION_POLE = block("piston_extension_pole");

    private static Block block(String id) {
        return BuiltInRegistries.BLOCK.get(new ResourceLocation("create", id));
    }

    public GogglesProvider() {
        MinecraftForge.EVENT_BUS.addListener(this::hideCreateOverlay);
    }

    private void hideCreateOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().equals(CREATE_OVERLAY_ID) && IWailaConfig.get().getPlugin().get(CreatePlugin.GOGGLES)) {
            event.setCanceled(true);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return CreatePlugin.GOGGLES;
    }

    @Override
    public void appendTooltip(ITooltip tooltip1, BlockAccessor accessor, IPluginConfig config) {
        if (!config.get(CreatePlugin.GOGGLES_DETAILED) || accessor.showDetails()) {
            Level world = accessor.getLevel();
            BlockPos pos = GoggleOverlayRenderer.proxiedOverlayPosition(world, accessor.getPosition());
            BlockEntity te = world.getBlockEntity(pos);
            boolean wearingGoggles = !config.get(CreatePlugin.REQUIRES_GOGGLES) || GogglesItem.isWearingGoggles(accessor.getPlayer());
            boolean hasGoggleInformation = te instanceof IHaveGoggleInformation && !(te instanceof SpoutBlockEntity) && !(te instanceof ItemDrainBlockEntity) && !(te instanceof BasinBlockEntity) && (!(te instanceof FluidTankBlockEntity tank) || tank.getControllerBE() == null || tank.getControllerBE().boiler.isActive());
            boolean hasHoveringInformation = te instanceof IHaveHoveringInformation;
            boolean goggleAddedInformation = false;
            boolean hoverAddedInformation = false;
            List<Component> tooltip = new ArrayList();
            if (hasGoggleInformation && wearingGoggles) {
                IHaveGoggleInformation gte = (IHaveGoggleInformation) te;
                goggleAddedInformation = gte.addToGoggleTooltip(tooltip, accessor.showDetails());
            }
            if (hasHoveringInformation) {
                if (!tooltip.isEmpty()) {
                    tooltip.add(Components.immutableEmpty());
                }
                IHaveHoveringInformation hte = (IHaveHoveringInformation) te;
                hoverAddedInformation = hte.addToTooltip(tooltip, accessor.showDetails());
                if (goggleAddedInformation && !hoverAddedInformation) {
                    tooltip.remove(tooltip.size() - 1);
                }
            }
            if (te instanceof IDisplayAssemblyExceptions) {
                boolean exceptionAdded = ((IDisplayAssemblyExceptions) te).addExceptionToTooltip(tooltip);
                if (exceptionAdded) {
                    hasHoveringInformation = true;
                    hoverAddedInformation = true;
                }
            }
            if (!hasHoveringInformation) {
                hasHoveringInformation = hoverAddedInformation = TrainRelocator.addToTooltip(tooltip, accessor.showDetails());
            }
            if (!hasGoggleInformation || goggleAddedInformation || !hasHoveringInformation || hoverAddedInformation) {
                tooltip.removeIf(c -> {
                    for (Component sibling : c.getSiblings()) {
                        if (sibling.getContents() instanceof TranslatableContents contents && REMOVE_KEYS.contains(contents.getKey())) {
                            return true;
                        }
                    }
                    return false;
                });
                tooltip.replaceAll(c -> {
                    if (c.getContents() instanceof LiteralContents literal && literal.text().startsWith("    ")) {
                        MutableComponent mutableComponent = Component.literal(literal.text().substring(4)).withStyle(c.getStyle());
                        c.getSiblings().forEach(mutableComponent::m_7220_);
                        return mutableComponent;
                    }
                    return c;
                });
                BlockState state = world.getBlockState(pos);
                if (wearingGoggles && state.m_60713_(this.PISTON_EXTENSION_POLE)) {
                    Direction[] directions = Iterate.directionsInAxis(((Direction) state.m_61143_(DirectionalBlock.FACING)).getAxis());
                    int poles = 1;
                    boolean pistonFound = false;
                    for (Direction dir : directions) {
                        int attachedPoles = PistonExtensionPoleBlock.PlacementHelper.get().attachedPoles(world, pos, dir);
                        poles += attachedPoles;
                        pistonFound |= world.getBlockState(pos.relative(dir, attachedPoles + 1)).m_60734_() instanceof MechanicalPistonBlock;
                    }
                    if (!pistonFound) {
                        return;
                    }
                    if (!tooltip.isEmpty()) {
                        tooltip.add(Components.immutableEmpty());
                    }
                    tooltip.add(Lang.translateDirect("gui.goggles.pole_length").append(Components.literal(" " + poles)));
                }
                tooltip.stream().map(c -> c.getString().isBlank() ? IElementHelper.get().spacer(3, 3) : IElementHelper.get().text(c)).forEach(tooltip1::add);
            }
        }
    }

    @Override
    public boolean enabledByDefault() {
        return false;
    }
}