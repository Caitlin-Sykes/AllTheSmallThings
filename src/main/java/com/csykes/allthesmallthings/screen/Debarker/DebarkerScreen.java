package com.csykes.allthesmallthings.screen.Debarker;

import javax.annotation.Nullable;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.csykes.allthesmallthings.container.DebarkerContainer;
import com.csykes.allthesmallthings.tileentity.DebarkerTile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DebarkerScreen extends ContainerScreen<DebarkerContainer> {

    private DebarkerTile tile;

    private final ResourceLocation gui = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/debarker_screen.png");

    private static final ResourceLocation redChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/red_chargebar.png");

    private static final ResourceLocation orangeChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/orange_chargebar.png");

    private static final ResourceLocation greenChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/green_chargebar.png");

    public DebarkerScreen(DebarkerContainer screen, PlayerInventory pi, ITextComponent title) {
        super(screen, pi, title);
    }

    @Override
    protected void init() {
        super.init();
        tile = new DebarkerTile();
    }

    @Override
    public void render(MatrixStack ms, int mouseX, int mouseY, float ticks) {
        this.renderBackground(ms);
        super.render(ms, mouseX, mouseY, ticks);
        this.renderHoveredTooltip(ms, mouseX, mouseY);
        this.chargeBarFill(ms);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float ticks, int x, int y) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
        this.minecraft.getTextureManager().bindTexture(gui);
        this.blit(ms, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        chargeBarFill(ms);

    }

    private void chargeBarFill(MatrixStack ms) {

        int fillPercentage = (int) (tile.ENERGY_STORED / DebarkerTile.MAX_ENERGY_STORED)
                * DebarkerTile.MAX_WIDTH_CHARGEBAR;
        // // Binds chargebar texture
        // this.minecraft.getTextureManager().bindTexture(chargeBar);

        // If under 1/4 energy left, show red
        if (fillPercentage < (DebarkerTile.MAX_ENERGY_STORED / 4)) {
            this.minecraft.getTextureManager().bindTexture(redChargeBar);

        }

        // If over 1/4 and under 3/4 energy left, show orange
        else if (fillPercentage > (DebarkerTile.MAX_ENERGY_STORED / 4)
                && fillPercentage < (DebarkerTile.MAX_ENERGY_STORED / 0.75)) {
            this.minecraft.getTextureManager().bindTexture(orangeChargeBar);

        }

        // Else show green
        else {
            this.minecraft.getTextureManager().bindTexture(greenChargeBar);

        }
        // Render the charge bar texture using blit
        this.blit(ms, guiLeft + 53, guiTop + 52, 0, 0, fillPercentage, 2);

    }

    // TODO: make charge bar actually drains
}
