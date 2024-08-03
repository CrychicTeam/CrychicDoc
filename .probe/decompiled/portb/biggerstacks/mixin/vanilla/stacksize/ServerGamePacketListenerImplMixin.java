package portb.biggerstacks.mixin.vanilla.stacksize;

import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import portb.biggerstacks.util.StackSizeHelper;

@Mixin({ ServerGamePacketListenerImpl.class })
public class ServerGamePacketListenerImplMixin {

    @ModifyConstant(method = { "handleSetCreativeModeSlot" }, constant = { @Constant(intValue = 64) })
    private int increaseStackLimit(int value) {
        return StackSizeHelper.getNewStackSize();
    }
}