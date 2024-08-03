package snownee.jade.api.ui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import snownee.jade.Internals;
import snownee.jade.api.ITooltip;
import snownee.jade.api.fluid.JadeFluidObject;

public interface IElementHelper {

    static IElementHelper get() {
        return Internals.getElementHelper();
    }

    IElement text(Component var1);

    IElement spacer(int var1, int var2);

    IElement item(ItemStack var1);

    IElement item(ItemStack var1, float var2);

    IElement item(ItemStack var1, float var2, @Nullable String var3);

    IElement smallItem(ItemStack var1);

    IElement fluid(JadeFluidObject var1);

    IElement progress(float var1, @Nullable Component var2, IProgressStyle var3, IBoxStyle var4, boolean var5);

    IBoxElement box(ITooltip var1, IBoxStyle var2);

    ITooltip tooltip();

    IProgressStyle progressStyle();
}