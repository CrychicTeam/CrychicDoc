package committee.nova.opack2reload.forge.mixin;

import committee.nova.opack2reload.forge.api.IPackSelectionModel;
import java.util.function.Consumer;
import net.minecraft.client.gui.screens.packs.PackSelectionModel;
import net.minecraft.server.packs.repository.PackRepository;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ PackSelectionModel.class })
public abstract class MixinPackSelectionModel implements IPackSelectionModel {

    @Shadow
    @Final
    private Consumer<PackRepository> output;

    @Shadow
    @Final
    private PackRepository repository;

    @Override
    public void cancel() {
        this.output.accept(this.repository);
    }
}