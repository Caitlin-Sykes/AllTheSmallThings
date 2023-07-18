package com.csykes.allthesmallthings.screen.Debarker;


import com.csykes.allthesmallthings.AllTheSmallThings;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

public class DebarkerButtonIcon extends Button {

     private static final ResourceLocation DEBARKER_BUTTON_TEXTURE = new ResourceLocation(AllTheSmallThings.MOD_ID, "textures/gui/icons/debarker_icon.png");

    public DebarkerButtonIcon(int x, int y, int width, int height, IPressable onPress) {
        super(x, y, width, height, StringTextComponent.EMPTY, onPress);
    }

   @Override
   /**
    * A function to render the button to the GUI and its location
    * @param matrices - gui stuff
    * @param mouseX - mousex pos
    * @param mouseY - mousy pos
    * @param delta - floaty
    */
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        Minecraft.getInstance().getTextureManager().bindTexture(DEBARKER_BUTTON_TEXTURE);
        blit(matrices, this.x, this.y, 0, 0, 18, 18, 18, 18);
    }
}
