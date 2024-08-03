package se.mickelus.tetra.items.modular.impl.holo.gui.scan;

import com.mojang.blaze3d.platform.Window;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.holo.ModularHolosphereItem;

@ParametersAreNonnullByDefault
public class ScannerOverlayGui extends GuiRoot implements IGuiOverlay {

    public static final TagKey<Block> tag = BlockTags.create(new ResourceLocation("tetra", "scannable"));

    private static final int snoozeLength = 6000;

    public static ScannerOverlayGui instance;

    private final ScannerBarGui scanner;

    BlockPos upHighlight;

    BlockPos midHighlight;

    BlockPos downHighlight;

    float widthRatio = 1.0F;

    ScannerSound sound;

    boolean available;

    int horizontalSpread = 44;

    int verticalSpread = 3;

    float cooldown = 1.2F;

    int range = 32;

    private int ticks;

    private int snooze = -1;

    public ScannerOverlayGui() {
        super(Minecraft.getInstance());
        this.scanner = new ScannerBarGui(2, 16, this.horizontalSpread);
        this.scanner.setAttachment(GuiAttachment.topCenter);
        this.scanner.setOpacity(0.0F);
        this.scanner.setVisible(false);
        this.addChild(this.scanner);
        this.sound = new ScannerSound(this.mc);
        if (ConfigHandler.development.get()) {
            MinecraftForge.EVENT_BUS.register(new ScannerDebugRenderer(this));
        }
        instance = this;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void toggleSnooze() {
        if (this.isSnoozed()) {
            this.snooze = -1;
            this.mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GRINDSTONE_USE, 2.0F, 0.3F));
        } else {
            this.snooze = this.ticks + 6000;
            this.mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GRINDSTONE_USE, 1.6F, 0.3F));
        }
    }

    public boolean isSnoozed() {
        return this.ticks < this.snooze;
    }

    public String getStatus() {
        if (this.isSnoozed()) {
            int seconds = Math.round((float) (this.snooze - this.ticks) / 20.0F);
            return seconds > 60 ? I18n.get("tetra.holo.scan.snoozed", String.format("%02d", seconds / 60), String.format("%02d", seconds % 60)) : I18n.get("tetra.holo.scan.snoozed", String.format("%02d", seconds / 60), String.format("%02d", seconds % 60));
        } else {
            return I18n.get("tetra.holo.scan.active");
        }
    }

    private void updateStats() {
        ItemStack itemStack = ModularHolosphereItem.findHolosphere(this.mc.player);
        if (!itemStack.isEmpty()) {
            ModularHolosphereItem item = (ModularHolosphereItem) itemStack.getItem();
            this.horizontalSpread = 2 * item.getEffectLevel(itemStack, ItemEffect.sweeperHorizontalSpread);
            this.verticalSpread = item.getEffectLevel(itemStack, ItemEffect.sweeperVerticalSpread);
            this.range = item.getEffectLevel(itemStack, ItemEffect.sweeperRange);
            this.cooldown = Math.max((float) item.getCooldownBase(itemStack), 1.0F);
            this.scanner.setHorizontalSpread(this.horizontalSpread);
            this.available = this.range > 0;
        } else {
            this.available = false;
        }
    }

    private void updateGuiVisibility() {
        int sweeperRange = (Integer) Stream.of(this.mc.player.m_21205_(), this.mc.player.m_21206_()).filter(stack -> stack.getItem() instanceof ModularHolosphereItem).map(stack -> ((IModularItem) stack.getItem()).getEffectLevel(stack, ItemEffect.sweeperRange)).findFirst().orElse(0);
        if (!this.scanner.isVisible() && sweeperRange > 0) {
            this.updateStats();
        }
        if (sweeperRange > 0) {
            this.scanner.show();
        } else {
            this.scanner.hide();
        }
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        this.mc.getSoundManager().stop(this.sound);
        this.sound = new ScannerSound(this.mc);
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        this.mc.getSoundManager().stop(this.sound);
        this.sound = new ScannerSound(this.mc);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Level world = this.mc.level;
        Player player = this.mc.player;
        if (world != null && player != null && TickEvent.Phase.START == event.phase) {
            this.updateGuiVisibility();
            this.ticks++;
            if (this.ticks % 200 == 0) {
                this.updateStats();
            }
            if (this.available && this.ticks % 20 == 0) {
                if (this.isSnoozed()) {
                    this.scanner.setStatus(this.getStatus());
                } else {
                    this.scanner.setStatus(null);
                }
            }
            if (this.available && this.ticks % 2 == 0 && !this.isSnoozed()) {
                int offset = this.ticks / 2 % (int) ((float) (this.horizontalSpread * 2) * this.cooldown);
                if (offset < this.horizontalSpread * 2) {
                    int yawOffset = (int) ((double) (-this.horizontalSpread + offset) * ScannerBarGui.getDegreesPerUnit());
                    if (offset % 2 == 0) {
                        if (this.verticalSpread > 0) {
                            this.upHighlight = (BlockPos) IntStream.range(0, this.verticalSpread).map(i -> i * -5 - 25).mapToObj(pitch -> this.getPositions(player, world, pitch, yawOffset)).filter(result -> result.getType() != HitResult.Type.MISS).map(BlockHitResult::m_82425_).findAny().orElse(null);
                            this.scanner.highlightUp(offset / 2, this.upHighlight != null);
                            if (this.upHighlight != null) {
                                this.sound.activate();
                            }
                            this.downHighlight = (BlockPos) IntStream.range(0, this.verticalSpread).map(i -> i * 5 + 25).mapToObj(pitch -> this.getPositions(player, world, pitch, yawOffset)).filter(result -> result.getType() != HitResult.Type.MISS).map(BlockHitResult::m_82425_).findAny().orElse(null);
                            this.scanner.highlightDown(offset / 2, this.downHighlight != null);
                            if (this.downHighlight != null) {
                                this.sound.activate();
                            }
                        }
                    } else if (offset / 2 < this.horizontalSpread - 1) {
                        this.midHighlight = (BlockPos) IntStream.range(-1, 2).map(i -> i * 10).mapToObj(pitch -> this.getPositions(player, world, pitch, yawOffset)).filter(result -> result.getType() != HitResult.Type.MISS).map(BlockHitResult::m_82425_).findAny().orElse(null);
                        this.scanner.highlightMid(offset / 2, this.midHighlight != null);
                        if (this.midHighlight != null) {
                            this.sound.activate();
                        }
                    }
                }
            }
        }
    }

    private BlockHitResult getPositions(Player player, Level world, int pitchOffset, int yawOffset) {
        Vec3 eyePosition = player.m_20299_(0.0F);
        Vec3 lookVector = this.getVectorForRotation(player.m_5686_(1.0F) + (float) pitchOffset, player.m_5675_(1.0F) + (float) yawOffset);
        Vec3 endVector = eyePosition.add(lookVector.x * (double) this.range, lookVector.y * (double) this.range, lookVector.z * (double) this.range);
        return this.isBlockInLine(world, new ClipBlockStateContext(eyePosition, endVector, blockState -> blockState.m_204336_(tag)));
    }

    private Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = pitch * (float) (Math.PI / 180.0);
        float f1 = -yaw * (float) (Math.PI / 180.0);
        float f2 = Mth.cos(f1);
        float f3 = Mth.sin(f1);
        float f4 = Mth.cos(f);
        float f5 = Mth.sin(f);
        return new Vec3((double) (f3 * f4), (double) (-f5), (double) (f2 * f4));
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (this.isVisible()) {
            Window window = this.mc.getWindow();
            this.width = window.getGuiScaledWidth();
            this.height = window.getGuiScaledHeight();
            int mouseX = (int) (this.mc.mouseHandler.xpos() * (double) this.width / (double) window.getScreenWidth());
            int mouseY = (int) (this.mc.mouseHandler.ypos() * (double) this.height / (double) window.getScreenHeight());
            this.drawChildren(guiGraphics, 0, 0, this.width, this.height, mouseX, mouseY, 1.0F);
            this.widthRatio = (float) this.scanner.getWidth() * 1.0F / (float) this.width;
        }
    }

    BlockHitResult isBlockInLine(Level level, ClipBlockStateContext context) {
        return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context, (innerContext, pos) -> {
            BlockState blockstate = level.getBlockState(pos);
            Vec3 vec3 = innerContext.getFrom().subtract(innerContext.getTo());
            return innerContext.isTargetBlock().test(blockstate) ? new BlockHitResult(innerContext.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), pos, false) : null;
        }, innerContext -> {
            Vec3 vec3 = innerContext.getFrom().subtract(innerContext.getTo());
            return BlockHitResult.miss(innerContext.getTo(), Direction.getNearest(vec3.x, vec3.y, vec3.z), BlockPos.containing(innerContext.getTo()));
        });
    }
}