package fr.frinn.custommachinery.api.integration.jei;

import com.mojang.serialization.DataResult;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.machine.ICustomMachine;
import fr.frinn.custommachinery.impl.codec.FieldCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IDisplayInfo {

    default IDisplayInfo addTooltip(Component text) {
        return this.addTooltip(text, IDisplayInfo.TooltipPredicate.ALWAYS);
    }

    IDisplayInfo addTooltip(Component var1, IDisplayInfo.TooltipPredicate var2);

    default IDisplayInfo setTextureIcon(ResourceLocation texture) {
        return this.setTextureIcon(texture, 16, 16, 0, 0);
    }

    default IDisplayInfo setTextureIcon(ResourceLocation texture, int width, int height) {
        return this.setTextureIcon(texture, width, height, 0, 0);
    }

    IDisplayInfo setTextureIcon(ResourceLocation var1, int var2, int var3, int var4, int var5);

    IDisplayInfo setSpriteIcon(ResourceLocation var1, ResourceLocation var2);

    default IDisplayInfo setItemIcon(Item item) {
        return this.setItemIcon(item.getDefaultInstance());
    }

    IDisplayInfo setItemIcon(ItemStack var1);

    void setClickAction(IDisplayInfo.ClickAction var1);

    public interface ClickAction {

        void handleClick(ICustomMachine var1, IMachineRecipe var2, int var3);
    }

    @FunctionalInterface
    public interface TooltipPredicate {

        IDisplayInfo.TooltipPredicate ALWAYS = (player, advancedTooltips) -> true;

        IDisplayInfo.TooltipPredicate ADVANCED = (player, advancedTooltips) -> advancedTooltips;

        IDisplayInfo.TooltipPredicate CREATIVE = (player, advancedTooltips) -> player.getAbilities().instabuild;

        NamedCodec<IDisplayInfo.TooltipPredicate> CODEC = NamedCodec.STRING.comapFlatMap(s -> {
            String predicate = FieldCodec.toSnakeCase(s);
            return switch(predicate) {
                case "advanced" ->
                    DataResult.success(ADVANCED);
                case "creative" ->
                    DataResult.success(CREATIVE);
                case "always" ->
                    DataResult.success(ALWAYS);
                default ->
                    DataResult.error(() -> "Invalid tooltip predicate: " + s);
            };
        }, predicate -> predicate == ADVANCED ? "advanced" : (predicate == CREATIVE ? "creative" : "always"), "Tooltip predicate");

        boolean shouldDisplay(Player var1, boolean var2);
    }
}