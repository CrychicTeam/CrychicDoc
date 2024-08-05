package com.mna.gui.block;

import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.particle.ParticleEmissionContainer;
import com.mna.gui.widgets.MASliderNotifiable;
import com.mna.particles.emitter.EmitterData;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiParticleEmission extends GuiJEIDisable<ParticleEmissionContainer> {

    private Button enableButton;

    public GuiParticleEmission(ParticleEmissionContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title);
    }

    @Override
    protected void init() {
        super.m_7856_();
        if (((ParticleEmissionContainer) this.f_97732_).isForPlayer() && this.f_96541_.options.getCameraType() == CameraType.FIRST_PERSON) {
            this.f_96541_.options.setCameraType(CameraType.THIRD_PERSON_BACK);
        }
        int yPos = 10;
        int spacing = 12;
        int buttonWidth = 120;
        Minecraft mc = Minecraft.getInstance();
        this.enableButton = Button.builder(Component.literal("Enabled: " + (((ParticleEmissionContainer) this.f_97732_).getData().enabled ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().enabled = !((ParticleEmissionContainer) this.f_97732_).getData().enabled;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Enabled: " + (((ParticleEmissionContainer) this.f_97732_).getData().enabled ? "Yes" : "No")));
            this.setControlsEnabled(((ParticleEmissionContainer) this.f_97732_).getData().enabled);
        }).pos(mc.screen.width / 2 - buttonWidth / 2, 5).size(buttonWidth, 20).build();
        this.m_142416_(this.enableButton);
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Amount: "), Component.literal(""), 1.0, 20.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().amount, 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().amount = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Rate: "), Component.literal(""), 1.0, 40.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().rate, 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().rate = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Red: "), Component.literal(""), -1.0, 255.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[0], 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[0] = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Green: "), Component.literal(""), -1.0, 255.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[1], 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[1] = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Blue: "), Component.literal(""), -1.0, 255.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[2], 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[2] = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Alpha: "), Component.literal(""), 30.0, 255.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[3], 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().rgbStart[3] = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Vel X: "), Component.literal(""), -1.0, 1.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().velocity[0], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().velocity[0] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Vel Y: "), Component.literal(""), -1.0, 1.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().velocity[1], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().velocity[1] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Vel Z: "), Component.literal(""), -1.0, 1.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().velocity[2], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().velocity[2] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Offset X: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().offset[0], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().offset[0] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Offset Y: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().offset[1], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().offset[1] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Offset Z: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().offset[2], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().offset[2] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("X Spread: "), Component.literal(""), 0.0, 2.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().spread[0], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().spread[0] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Y Spread: "), Component.literal(""), 0.0, 2.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().spread[1], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().spread[1] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Z Spread: "), Component.literal(""), 0.0, 2.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().spread[2], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().spread[2] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Target X: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().target[0], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().target[0] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Target Y: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().target[1], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().target[1] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Target Z: "), Component.literal(""), -8.0, 8.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().target[2], 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().target[2] = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing * 2;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Scale: "), Component.literal(""), 0.25, 10.0, (double) (((ParticleEmissionContainer) this.f_97732_).getData().scale * 25.0F), 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().scale = (float) s.getValue() / 25.0F;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Gravity: "), Component.literal(""), 0.0, 1.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().gravity, 0.0, 4, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().gravity = (float) s.getValue();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        this.m_142416_(new MASliderNotifiable(10, yPos, 120, 10, Component.literal("Max Age: "), Component.literal(""), 0.0, 60.0, (double) ((ParticleEmissionContainer) this.f_97732_).getData().age, 1.0, 0, true, s -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().age = s.getValueInt();
            ((ParticleEmissionContainer) this.f_97732_).update(false);
        }));
        yPos += spacing;
        int var26 = 10;
        int var35 = 22;
        this.m_142416_(Button.builder(Component.literal("Type: " + ((ParticleEmissionContainer) this.f_97732_).getData().type.name()), b -> {
            int ordinal = (((ParticleEmissionContainer) this.f_97732_).getData().type.ordinal() + 1) % EmitterData.ParticleTypes.values().length;
            ((ParticleEmissionContainer) this.f_97732_).getData().type = EmitterData.ParticleTypes.values()[ordinal];
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Type: " + ((ParticleEmissionContainer) this.f_97732_).getData().type.name()));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Random Color: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomColor ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().randomColor = !((ParticleEmissionContainer) this.f_97732_).getData().randomColor;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Random Color: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomColor ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Random Scale: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomScale ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().randomScale = !((ParticleEmissionContainer) this.f_97732_).getData().randomScale;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Random Scale: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomScale ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Random Target: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomTarget ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().randomTarget = !((ParticleEmissionContainer) this.f_97732_).getData().randomTarget;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Random Target: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomTarget ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Random Speed: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomSpeed ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().randomSpeed = !((ParticleEmissionContainer) this.f_97732_).getData().randomSpeed;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Random Speed: " + (((ParticleEmissionContainer) this.f_97732_).getData().randomSpeed ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Mirror: " + (((ParticleEmissionContainer) this.f_97732_).getData().mirror ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().mirror = !((ParticleEmissionContainer) this.f_97732_).getData().mirror;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Mirror: " + (((ParticleEmissionContainer) this.f_97732_).getData().mirror ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Collision: " + (((ParticleEmissionContainer) this.f_97732_).getData().collision ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().collision = !((ParticleEmissionContainer) this.f_97732_).getData().collision;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Collision: " + (((ParticleEmissionContainer) this.f_97732_).getData().collision ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.m_142416_(Button.builder(Component.literal("Show in First Person: " + (((ParticleEmissionContainer) this.f_97732_).getData().showInFirstPerson ? "Yes" : "No")), b -> {
            ((ParticleEmissionContainer) this.f_97732_).getData().showInFirstPerson = !((ParticleEmissionContainer) this.f_97732_).getData().showInFirstPerson;
            ((ParticleEmissionContainer) this.f_97732_).update(false);
            b.m_93666_(Component.literal("Show in First Person: " + (((ParticleEmissionContainer) this.f_97732_).getData().showInFirstPerson ? "Yes" : "No")));
        }).pos(mc.screen.width - 10 - buttonWidth, var26).size(buttonWidth, 20).build());
        var26 += var35;
        this.setControlsEnabled(((ParticleEmissionContainer) this.f_97732_).getData().enabled);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        return this.m_7222_() != null && this.m_7282_() && pButton == 0 ? this.m_7222_().mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY) : false;
    }

    public void setControlsEnabled(boolean enabled) {
        this.m_6702_().forEach(c -> {
            if (c instanceof Button) {
                ((Button) c).f_93623_ = enabled;
                ((Button) c).f_93624_ = enabled;
            }
        });
        this.enableButton.f_93623_ = true;
        this.enableButton.f_93624_ = true;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float partialTick, int mouseX, int mouseY) {
        MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, pGuiGraphics));
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int int0, int int1) {
    }

    @Override
    public void onClose() {
        super.m_7379_();
        ((ParticleEmissionContainer) this.f_97732_).update(true);
    }
}