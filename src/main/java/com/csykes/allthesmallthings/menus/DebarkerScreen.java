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
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        RenderSystem.setShaderTexture(0, background);

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

//        renderProgressArrow(pPoseStack, x, y);
//        energyInfoArea.draw(pPoseStack);
//        renderer.render(pPoseStack, x + 55, y + 15, menu.getFluidStack());

    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, mouseX, mouseY, delta);
        renderTooltip(pPoseStack, mouseX, mouseY);
    }
}
