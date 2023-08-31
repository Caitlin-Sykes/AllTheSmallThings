package com.csykes.allthesmallthings.debarker;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import java.awt.*;

/**
 * User: brandon3055
 * Date: 06/01/2015
 *
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
public class ContainerScreenBasic extends ContainerScreen<ContainerBasic> {

  public ContainerScreenBasic(ContainerBasic containerBasic, PlayerInventory playerInventory, ITextComponent title) {
    super(containerBasic, playerInventory, title);

		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 176;
		ySize = 168;
	}

  @Override
  public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
    this.renderBackground(matrixStack);
    super.render(matrixStack, mouseX, mouseY, partialTicks);
    this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
  }

  /**
   * Draw the foreground layer for the GuiContainer (everything in front of the items)
   * Taken directly from ContainerScreen
   */
  @Override
  protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		final float LABEL_XPOS = 5;
		final float FONT_Y_SPACING = 12;
		final float CHEST_LABEL_YPOS = ContainerBasic.TILE_INVENTORY_YPOS - FONT_Y_SPACING;
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

  // This is the resource location for the background image for the GUI
  private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("allthesmallthings", "textures/gui/debarker_screen.png");
}



