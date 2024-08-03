package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiFunction;
import java.util.function.ToIntFunction;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.TriPredicate;

public class CapabilityChemical {

    public abstract static class ChemicalBuilder<I extends CapabilityProvider<I>, H extends IChemicalHandler<C, S>, C extends Chemical<C>, S extends ChemicalStack<C>> extends CapabilityBuilderForge<I, H> {

        protected ToIntFunction<I> getTanks;

        protected BiFunction<I, Integer, S> getChemicalInTank;

        protected CapabilityChemical.SetChemicalInTank<I, C, S> setChemicalInTank;

        protected BiFunction<I, Integer, Long> getTankCapacity;

        protected TriPredicate<I, Integer, S> isValid;

        protected CapabilityChemical.InsertChemical<I, C, S> insertChemical;

        protected CapabilityChemical.ExtractChemical<I, C, S> extractChemical;

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> getTanks(ToIntFunction<I> getTanks) {
            this.getTanks = getTanks;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> getChemicalInTank(BiFunction<I, Integer, S> getChemicalInTank) {
            this.getChemicalInTank = getChemicalInTank;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> setChemicalInTank(CapabilityChemical.SetChemicalInTank<I, C, S> setChemicalInTank) {
            this.setChemicalInTank = setChemicalInTank;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> getTankCapacity(BiFunction<I, Integer, Long> getTankCapacity) {
            this.getTankCapacity = getTankCapacity;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> isValid(TriPredicate<I, Integer, S> isValid) {
            this.isValid = isValid;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> insertChemical(CapabilityChemical.InsertChemical<I, C, S> insertChemical) {
            this.insertChemical = insertChemical;
            return this;
        }

        public CapabilityChemical.ChemicalBuilder<I, H, C, S> extractChemical(CapabilityChemical.ExtractChemical<I, C, S> extractChemical) {
            this.extractChemical = extractChemical;
            return this;
        }
    }

    @FunctionalInterface
    public interface ExtractChemical<I, C extends Chemical<C>, S extends ChemicalStack<C>> {

        S apply(I var1, int var2, long var3, boolean var5);
    }

    @FunctionalInterface
    public interface InsertChemical<I, C extends Chemical<C>, S extends ChemicalStack<C>> {

        S apply(I var1, int var2, S var3, boolean var4);
    }

    @FunctionalInterface
    public interface SetChemicalInTank<I, C extends Chemical<C>, S extends ChemicalStack<C>> {

        void accept(I var1, int var2, S var3);
    }
}