package se.mickelus.tetra.mixin;

import com.google.common.collect.Streams;
import java.util.Set;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.aspect.TetraEnchantmentHelper;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
@Mixin({ ItemStack.class })
public class ItemStackMixin {

    @Inject(at = { @At("RETURN") }, method = { "enchant(Lnet/minecraft/world/item/enchantment/Enchantment;I)V" })
    private void addEnchantment(Enchantment enchantment, int level, CallbackInfo callback) {
        if (this.getItem() instanceof IModularItem item) {
            ItemStack itemStack = this.getInstance();
            TetraEnchantmentHelper.mapEnchantments(itemStack);
            item.assemble(itemStack, null, 0.0F);
        }
    }

    @Inject(at = { @At("RETURN") }, method = { "is(Lnet/minecraft/tags/TagKey;)Z" }, cancellable = true)
    private void isWithModularTags(TagKey<Item> pTag, CallbackInfoReturnable<Boolean> callback) {
        if (!(Boolean) callback.getReturnValue() && this.getItem() instanceof IModularItem item) {
            Set<TagKey<Item>> tags = item.getPropertiesCached(this.getInstance()).tags;
            if (tags != null) {
                callback.setReturnValue(tags.contains(pTag));
            }
        }
    }

    @Inject(at = { @At("RETURN") }, method = { "getTags()Ljava/util/stream/Stream;" }, cancellable = true)
    private void getTagsWithModularTags(CallbackInfoReturnable<Stream<TagKey<Item>>> callback) {
        if (this.getItem() instanceof IModularItem item) {
            Set<TagKey<Item>> tags = item.getPropertiesCached(this.getInstance()).tags;
            if (tags != null) {
                callback.setReturnValue(Streams.concat(new Stream[] { (Stream) callback.getReturnValue(), tags.stream() }));
            }
        }
    }

    @Shadow
    public Item getItem() {
        throw new IllegalStateException("Mixin failed to shadow getItem()");
    }

    private ItemStack getInstance() {
        return (ItemStack) this;
    }
}