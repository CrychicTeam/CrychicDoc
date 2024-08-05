package dev.ftb.mods.ftblibrary.config.ui;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.FTBLibraryClientConfig;
import dev.ftb.mods.ftblibrary.config.FluidConfig;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.util.ModUtils;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class SelectFluidScreen extends ResourceSelectorScreen<FluidStack> {

    public static final SearchModeIndex<ResourceSearchMode<FluidStack>> KNOWN_MODES = new SearchModeIndex<>();

    public SelectFluidScreen(FluidConfig config, ConfigCallback callback) {
        super(config, callback);
    }

    @Override
    protected int defaultQuantity() {
        return (int) FluidStackHooks.bucketAmount();
    }

    @Override
    protected SearchModeIndex<ResourceSearchMode<FluidStack>> getSearchModeIndex() {
        return KNOWN_MODES;
    }

    @Override
    protected ResourceSelectorScreen<FluidStack>.ResourceButton makeResourceButton(Panel panel, SelectableResource<FluidStack> resource) {
        return new SelectFluidScreen.FluidStackButton(panel, (SelectableResource<FluidStack>) Objects.requireNonNullElse(resource, SelectableResource.fluid(FluidStack.empty())));
    }

    static {
        KNOWN_MODES.appendMode(ResourceSearchMode.ALL_FLUIDS);
    }

    private class FluidStackButton extends ResourceSelectorScreen<FluidStack>.ResourceButton {

        private FluidStackButton(Panel panel, SelectableResource<FluidStack> resource) {
            super(panel, resource);
        }

        @Override
        public boolean shouldAdd(String search) {
            search = search.toLowerCase();
            if (search.isEmpty()) {
                return true;
            } else if (search.startsWith("@")) {
                return RegistrarManager.getId(((FluidStack) this.getStack()).getFluid(), Registries.FLUID).getNamespace().contains(search.substring(1));
            } else {
                return search.startsWith("#") && ResourceLocation.isValidResourceLocation(search.substring(1)) ? ((FluidStack) this.getStack()).getFluid().builtInRegistryHolder().is(TagKey.create(Registries.FLUID, new ResourceLocation(search.substring(1)))) : ((FluidStack) this.getStack()).getName().getString().toLowerCase().contains(search);
            }
        }

        @Override
        public void addMouseOverText(TooltipList list) {
            if (!((FluidStack) this.getStack()).isEmpty()) {
                list.add(((FluidStack) this.getStack()).getName());
                if (FTBLibraryClientConfig.FLUID_MODNAME.get()) {
                    ModUtils.getModName(((FluidStack) this.getStack()).getFluid()).ifPresent(name -> list.add(Component.literal(name).withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC)));
                }
            }
        }
    }
}