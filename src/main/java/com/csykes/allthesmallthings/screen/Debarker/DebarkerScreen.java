package com.csykes.allthesmallthings.screen.Debarker;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.container.DebarkerContainer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.widget.button.Button;

public class DebarkerScreen extends ContainerScreen<DebarkerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/debarker_screen.png");

    public DebarkerScreen(DebarkerContainer screen, PlayerInventory pi, ITextComponent title) {
        super(screen, pi, title);
    }

     @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float ticks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, ticks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float ticks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        this.blit(ms, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
    }
}
