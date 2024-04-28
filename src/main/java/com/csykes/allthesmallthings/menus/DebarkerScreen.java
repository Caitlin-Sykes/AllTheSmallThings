package com.csykes.allthesmallthings.menus;

import com.csykes.allthesmallthings.AllTheSmallThings;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class DebarkerScreen extends AbstractContainerScreen<DebarkerMenu> {
    private static final ResourceLocation background = new ResourceLocation(AllTheSmallThings.MOD_ID, "textures/gui/debarker_screen.png");

    public DebarkerScreen(DebarkerMenu p_97741_, Inventory p_97742_, Component p_97743_) {
        super(p_97741_, p_97742_, p_97743_);
    }

    @Override
    protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        renderBackground(pPoseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        RenderSystem.setShaderTexture(0, background);
        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        pPoseStack.translate(0,0,-5);
        fillGradient(pPoseStack, x+53, y+52, Math.round(x+53+((float) menu.getData().get(2) /menu.getData().get(3))*70) ,y+54, 0xffff0000, 0xff5500ff);
        fill(pPoseStack,x+79, y+38, Math.round(x+79+((float) menu.getData().get(0) /(menu.getData().get(1))*18)),y+40, 0xffffffff);

        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        super.render(pPoseStack, mouseX, mouseY, delta);
    }
}
