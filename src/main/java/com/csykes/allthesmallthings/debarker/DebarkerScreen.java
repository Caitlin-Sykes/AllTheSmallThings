package com.csykes.allthesmallthings.debarker;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.INBTSerializable;

import java.awt.*;

/**
 * GuiInventoryBasic is a simple gui that does nothing but draw a background image and a line of text on the screen.
 * everything else is handled by the vanilla container code.
 *
 * The Screen is drawn in several layers, most importantly:
 *
 * Background - renderBackground() - eg a grey fill
 * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
 * Foreground layer - typically text labels
 *
 */
public class DebarkerScreen extends ContainerScreen<DebarkerContainer> {

   private static final ResourceLocation redChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/red_chargebar.png");

    private static final ResourceLocation orangeChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/orange_chargebar.png");

    private static final ResourceLocation greenChargeBar = new ResourceLocation(AllTheSmallThings.MOD_ID,
            "textures/gui/charge_bar/green_chargebar.png");

    private int ENERGY_STORED;
    private static final int MAX_WIDTH_CHARGEBAR = 70;

  public DebarkerScreen(DebarkerContainer containerBasic, PlayerInventory playerInventory, ITextComponent title) {
    super(containerBasic, playerInventory, title);

		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 176;
		ySize = 168;
    
	}

  /**
   * Renders the gui
   */
  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    this.chargeBarFill(matrixStack);
  }

  /**
   * Draw the foreground layer for the GuiContainer (everything in front of the items)
   * Taken directly from ContainerScreen
   */
  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		final float LABEL_XPOS = 5;
		final float FONT_Y_SPACING = 12;
		final float CHEST_LABEL_YPOS = DebarkerContainer.TILE_INVENTORY_YPOS - FONT_Y_SPACING;
    this.font.drawString(matrixStack, "Debarker",
            LABEL_XPOS, CHEST_LABEL_YPOS, Color.darkGray.getRGB());
  }

  /**
   * Draws the background layer of this container (behind the items).
   * Taken directly from ChestScreen / BeaconScreen
   * @deprecated
   */
  @Override
  protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
    RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);   // this.minecraft.getTextureManager()
    this.blit(matrixStack, this.guiLeft, this.guiTop, 0, 0, xSize, ySize);
  }

  private void chargeBarFill(MatrixStack ms) {

    int fillPercentage = (int) (ENERGY_STORED / DebarkerTileEntity.MAX_ENERGY_STORED)
        * MAX_WIDTH_CHARGEBAR;

    System.out.println("FILL PERCENTAGE: " + ENERGY_STORED);
    // // Binds chargebar texture
    // this.minecraft.getTextureManager().bindTexture(chargeBar);

    // If under 1/4 energy left, show red
    if (fillPercentage < (DebarkerTileEntity.MAX_ENERGY_STORED / 4)) {
      this.minecraft.getTextureManager().bindTexture(redChargeBar);

    }

    // If over 1/4 and under 3/4 energy left, show orange
    else if (fillPercentage > (DebarkerTileEntity.MAX_ENERGY_STORED / 4)
        && fillPercentage < (DebarkerTileEntity.MAX_ENERGY_STORED / 0.75)) {
      this.minecraft.getTextureManager().bindTexture(orangeChargeBar);

    }

    // Else show green
    else {
      this.minecraft.getTextureManager().bindTexture(greenChargeBar);

    }
    // Render the charge bar texture using blit
    this.blit(ms, guiLeft + 53, guiTop + 52, 0, 0, fillPercentage, 2);

  }

  // This is the resource location for the background image for the GUI
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("allthesmallthings", "textures/gui/debarker_screen.png");
}



