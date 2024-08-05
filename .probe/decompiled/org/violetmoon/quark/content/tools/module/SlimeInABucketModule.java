package org.violetmoon.quark.content.tools.module;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.gameevent.GameEvent;
import org.violetmoon.quark.content.tools.item.SlimeInABucketItem;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.ItemNBTHelper;

@ZetaLoadModule(category = "tools")
public class SlimeInABucketModule extends ZetaModule {

    @Hint
    public static Item slime_in_a_bucket;

    @LoadEvent
    public final void register(ZRegister event) {
        slime_in_a_bucket = new SlimeInABucketItem(this);
    }

    @PlayEvent
    public void entityInteract(ZPlayerInteract.EntityInteract event) {
        if (event.getTarget() != null && event.getTarget().getType() == EntityType.SLIME && ((Slime) event.getTarget()).getSize() == 1 && event.getTarget().isAlive()) {
            Player player = event.getEntity();
            InteractionHand hand = InteractionHand.MAIN_HAND;
            ItemStack stack = player.m_21205_();
            if (stack.isEmpty() || stack.getItem() != Items.BUCKET) {
                stack = player.m_21206_();
                hand = InteractionHand.OFF_HAND;
            }
            if (!stack.isEmpty() && stack.getItem() == Items.BUCKET) {
                if (!event.getLevel().isClientSide) {
                    ItemStack outStack = new ItemStack(slime_in_a_bucket);
                    CompoundTag cmp = event.getTarget().serializeNBT();
                    ItemNBTHelper.setCompound(outStack, "slime_nbt", cmp);
                    if (stack.getCount() == 1) {
                        player.m_21008_(hand, outStack);
                    } else {
                        stack.shrink(1);
                        if (stack.getCount() == 0) {
                            player.m_21008_(hand, outStack);
                        } else if (!player.getInventory().add(outStack)) {
                            player.drop(outStack, false);
                        }
                    }
                    event.getLevel().m_220400_(player, GameEvent.ENTITY_INTERACT, event.getTarget().position());
                    event.getTarget().discard();
                }
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide));
            }
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends SlimeInABucketModule {

        @LoadEvent
        public void clientSetup(ZClientSetup event) {
            event.enqueueWork(() -> ItemProperties.register(slime_in_a_bucket, new ResourceLocation("excited"), (stack, world, e, id) -> ItemNBTHelper.getBoolean(stack, "excited", false) ? 1.0F : 0.0F));
        }
    }
}