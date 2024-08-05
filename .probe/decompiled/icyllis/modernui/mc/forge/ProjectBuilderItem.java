package icyllis.modernui.mc.forge;

import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

final class ProjectBuilderItem extends Item {

    ProjectBuilderItem(@Nonnull Item.Properties props) {
        super(props);
    }

    public void initializeClient(@Nonnull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ProjectBuilderRenderer();
            }
        });
    }
}