package net.minecraft.client;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.platform.InputConstants;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.Mth;
import net.minecraft.util.SmoothDouble;
import org.lwjgl.glfw.GLFWDropCallback;

public class MouseHandler {

    private final Minecraft minecraft;

    private boolean isLeftPressed;

    private boolean isMiddlePressed;

    private boolean isRightPressed;

    private double xpos;

    private double ypos;

    private int fakeRightMouse;

    private int activeButton = -1;

    private boolean ignoreFirstMove = true;

    private int clickDepth;

    private double mousePressedTime;

    private final SmoothDouble smoothTurnX = new SmoothDouble();

    private final SmoothDouble smoothTurnY = new SmoothDouble();

    private double accumulatedDX;

    private double accumulatedDY;

    private double accumulatedScroll;

    private double lastMouseEventTime = Double.MIN_VALUE;

    private boolean mouseGrabbed;

    public MouseHandler(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    private void onPress(long long0, int int1, int int2, int int3) {
        if (long0 == this.minecraft.getWindow().getWindow()) {
            if (this.minecraft.screen != null) {
                this.minecraft.setLastInputType(InputType.MOUSE);
            }
            boolean $$4 = int2 == 1;
            if (Minecraft.ON_OSX && int1 == 0) {
                if ($$4) {
                    if ((int3 & 2) == 2) {
                        int1 = 1;
                        this.fakeRightMouse++;
                    }
                } else if (this.fakeRightMouse > 0) {
                    int1 = 1;
                    this.fakeRightMouse--;
                }
            }
            int $$5 = int1;
            if ($$4) {
                if (this.minecraft.options.touchscreen().get() && this.clickDepth++ > 0) {
                    return;
                }
                this.activeButton = $$5;
                this.mousePressedTime = Blaze3D.getTime();
            } else if (this.activeButton != -1) {
                if (this.minecraft.options.touchscreen().get() && --this.clickDepth > 0) {
                    return;
                }
                this.activeButton = -1;
            }
            boolean[] $$6 = new boolean[] { false };
            if (this.minecraft.getOverlay() == null) {
                if (this.minecraft.screen == null) {
                    if (!this.mouseGrabbed && $$4) {
                        this.grabMouse();
                    }
                } else {
                    double $$7 = this.xpos * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
                    double $$8 = this.ypos * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();
                    Screen $$9 = this.minecraft.screen;
                    if ($$4) {
                        $$9.afterMouseAction();
                        Screen.wrapScreenError(() -> $$6[0] = $$9.m_6375_($$7, $$8, $$5), "mouseClicked event handler", $$9.getClass().getCanonicalName());
                    } else {
                        Screen.wrapScreenError(() -> $$6[0] = $$9.m_6348_($$7, $$8, $$5), "mouseReleased event handler", $$9.getClass().getCanonicalName());
                    }
                }
            }
            if (!$$6[0] && this.minecraft.screen == null && this.minecraft.getOverlay() == null) {
                if ($$5 == 0) {
                    this.isLeftPressed = $$4;
                } else if ($$5 == 2) {
                    this.isMiddlePressed = $$4;
                } else if ($$5 == 1) {
                    this.isRightPressed = $$4;
                }
                KeyMapping.set(InputConstants.Type.MOUSE.getOrCreate($$5), $$4);
                if ($$4) {
                    if (this.minecraft.player.m_5833_() && $$5 == 2) {
                        this.minecraft.gui.getSpectatorGui().onMouseMiddleClick();
                    } else {
                        KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate($$5));
                    }
                }
            }
        }
    }

    private void onScroll(long long0, double double1, double double2) {
        if (long0 == Minecraft.getInstance().getWindow().getWindow()) {
            double $$3 = (this.minecraft.options.discreteMouseScroll().get() ? Math.signum(double2) : double2) * this.minecraft.options.mouseWheelSensitivity().get();
            if (this.minecraft.getOverlay() == null) {
                if (this.minecraft.screen != null) {
                    double $$4 = this.xpos * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
                    double $$5 = this.ypos * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();
                    this.minecraft.screen.m_6050_($$4, $$5, $$3);
                    this.minecraft.screen.afterMouseAction();
                } else if (this.minecraft.player != null) {
                    if (this.accumulatedScroll != 0.0 && Math.signum($$3) != Math.signum(this.accumulatedScroll)) {
                        this.accumulatedScroll = 0.0;
                    }
                    this.accumulatedScroll += $$3;
                    int $$6 = (int) this.accumulatedScroll;
                    if ($$6 == 0) {
                        return;
                    }
                    this.accumulatedScroll -= (double) $$6;
                    if (this.minecraft.player.m_5833_()) {
                        if (this.minecraft.gui.getSpectatorGui().isMenuActive()) {
                            this.minecraft.gui.getSpectatorGui().onMouseScrolled(-$$6);
                        } else {
                            float $$7 = Mth.clamp(this.minecraft.player.m_150110_().getFlyingSpeed() + (float) $$6 * 0.005F, 0.0F, 0.2F);
                            this.minecraft.player.m_150110_().setFlyingSpeed($$7);
                        }
                    } else {
                        this.minecraft.player.m_150109_().swapPaint((double) $$6);
                    }
                }
            }
        }
    }

    private void onDrop(long long0, List<Path> listPath1) {
        if (this.minecraft.screen != null) {
            this.minecraft.screen.onFilesDrop(listPath1);
        }
    }

    public void setup(long long0) {
        InputConstants.setupMouseCallbacks(long0, (p_91591_, p_91592_, p_91593_) -> this.minecraft.execute(() -> this.onMove(p_91591_, p_91592_, p_91593_)), (p_91566_, p_91567_, p_91568_, p_91569_) -> this.minecraft.execute(() -> this.onPress(p_91566_, p_91567_, p_91568_, p_91569_)), (p_91576_, p_91577_, p_91578_) -> this.minecraft.execute(() -> this.onScroll(p_91576_, p_91577_, p_91578_)), (p_91536_, p_91537_, p_91538_) -> {
            Path[] $$3 = new Path[p_91537_];
            for (int $$4 = 0; $$4 < p_91537_; $$4++) {
                $$3[$$4] = Paths.get(GLFWDropCallback.getName(p_91538_, $$4));
            }
            this.minecraft.execute(() -> this.onDrop(p_91536_, Arrays.asList($$3)));
        });
    }

    private void onMove(long long0, double double1, double double2) {
        if (long0 == Minecraft.getInstance().getWindow().getWindow()) {
            if (this.ignoreFirstMove) {
                this.xpos = double1;
                this.ypos = double2;
                this.ignoreFirstMove = false;
            }
            Screen $$3 = this.minecraft.screen;
            if ($$3 != null && this.minecraft.getOverlay() == null) {
                double $$4 = double1 * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
                double $$5 = double2 * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();
                Screen.wrapScreenError(() -> $$3.m_94757_($$4, $$5), "mouseMoved event handler", $$3.getClass().getCanonicalName());
                if (this.activeButton != -1 && this.mousePressedTime > 0.0) {
                    double $$6 = (double1 - this.xpos) * (double) this.minecraft.getWindow().getGuiScaledWidth() / (double) this.minecraft.getWindow().getScreenWidth();
                    double $$7 = (double2 - this.ypos) * (double) this.minecraft.getWindow().getGuiScaledHeight() / (double) this.minecraft.getWindow().getScreenHeight();
                    Screen.wrapScreenError(() -> $$3.m_7979_($$4, $$5, this.activeButton, $$6, $$7), "mouseDragged event handler", $$3.getClass().getCanonicalName());
                }
                $$3.afterMouseMove();
            }
            this.minecraft.getProfiler().push("mouse");
            if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
                this.accumulatedDX = this.accumulatedDX + (double1 - this.xpos);
                this.accumulatedDY = this.accumulatedDY + (double2 - this.ypos);
            }
            this.turnPlayer();
            this.xpos = double1;
            this.ypos = double2;
            this.minecraft.getProfiler().pop();
        }
    }

    public void turnPlayer() {
        double $$0 = Blaze3D.getTime();
        double $$1 = $$0 - this.lastMouseEventTime;
        this.lastMouseEventTime = $$0;
        if (this.isMouseGrabbed() && this.minecraft.isWindowActive()) {
            double $$2 = this.minecraft.options.sensitivity().get() * 0.6F + 0.2F;
            double $$3 = $$2 * $$2 * $$2;
            double $$4 = $$3 * 8.0;
            double $$7;
            double $$8;
            if (this.minecraft.options.smoothCamera) {
                double $$5 = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * $$4, $$1 * $$4);
                double $$6 = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * $$4, $$1 * $$4);
                $$7 = $$5;
                $$8 = $$6;
            } else if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.m_150108_()) {
                this.smoothTurnX.reset();
                this.smoothTurnY.reset();
                $$7 = this.accumulatedDX * $$3;
                $$8 = this.accumulatedDY * $$3;
            } else {
                this.smoothTurnX.reset();
                this.smoothTurnY.reset();
                $$7 = this.accumulatedDX * $$4;
                $$8 = this.accumulatedDY * $$4;
            }
            this.accumulatedDX = 0.0;
            this.accumulatedDY = 0.0;
            int $$13 = 1;
            if (this.minecraft.options.invertYMouse().get()) {
                $$13 = -1;
            }
            this.minecraft.getTutorial().onMouse($$7, $$8);
            if (this.minecraft.player != null) {
                this.minecraft.player.m_19884_($$7, $$8 * (double) $$13);
            }
        } else {
            this.accumulatedDX = 0.0;
            this.accumulatedDY = 0.0;
        }
    }

    public boolean isLeftPressed() {
        return this.isLeftPressed;
    }

    public boolean isMiddlePressed() {
        return this.isMiddlePressed;
    }

    public boolean isRightPressed() {
        return this.isRightPressed;
    }

    public double xpos() {
        return this.xpos;
    }

    public double ypos() {
        return this.ypos;
    }

    public void setIgnoreFirstMove() {
        this.ignoreFirstMove = true;
    }

    public boolean isMouseGrabbed() {
        return this.mouseGrabbed;
    }

    public void grabMouse() {
        if (this.minecraft.isWindowActive()) {
            if (!this.mouseGrabbed) {
                if (!Minecraft.ON_OSX) {
                    KeyMapping.setAll();
                }
                this.mouseGrabbed = true;
                this.xpos = (double) (this.minecraft.getWindow().getScreenWidth() / 2);
                this.ypos = (double) (this.minecraft.getWindow().getScreenHeight() / 2);
                InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212995, this.xpos, this.ypos);
                this.minecraft.setScreen(null);
                this.minecraft.missTime = 10000;
                this.ignoreFirstMove = true;
            }
        }
    }

    public void releaseMouse() {
        if (this.mouseGrabbed) {
            this.mouseGrabbed = false;
            this.xpos = (double) (this.minecraft.getWindow().getScreenWidth() / 2);
            this.ypos = (double) (this.minecraft.getWindow().getScreenHeight() / 2);
            InputConstants.grabOrReleaseMouse(this.minecraft.getWindow().getWindow(), 212993, this.xpos, this.ypos);
        }
    }

    public void cursorEntered() {
        this.ignoreFirstMove = true;
    }
}