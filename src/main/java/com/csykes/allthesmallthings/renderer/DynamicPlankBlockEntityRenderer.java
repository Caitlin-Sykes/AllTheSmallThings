package com.csykes.allthesmallthings.renderer;

import com.csykes.allthesmallthings.blockEntities.DynamicPlankBlockEntity;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import java.awt.*;


public class DynamicPlankBlockEntityRenderer implements BlockEntityRenderer<DynamicPlankBlockEntity> {

    private DynamicTexture dynamicTexture;
    private ResourceLocation dynamicTextureLocation;
    
    public DynamicPlankBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super();
        this.dynamicTexture = new DynamicTexture(16, 16, false);
        this.dynamicTextureLocation = new ResourceLocation("allthesmallthings", "block/dynamic_plank.png");
        System.out.println("CALLED HIHIHIHIHI!!!!! -----------------------------");
    }

    @Override
    public void render(DynamicPlankBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay) {
        System.out.println("COOOOOOOOOOKIE");

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.scale(1.0f, 1.0f, 1.0f);

        // Bind the dynamic texture
        Minecraft.getInstance().getTextureManager().bindForSetup(dynamicTextureLocation);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(dynamicTextureLocation));
        poseStack.translate(-0.5, -0.5, -0.5);

        BlockPos pos = blockEntity.getBlockPos();

        // Draw a simple quad with the dynamic texture
        vertexConsumer.vertex(poseStack.last().pose(), 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLight).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLight).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 1, 1, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLight).normal(0, 1, 0).endVertex();
        vertexConsumer.vertex(poseStack.last().pose(), 0, 1, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(combinedLight).normal(0, 1, 0).endVertex();

        poseStack.popPose();
    }

    private void updateDynamicTexture() {
        NativeImage nativeImage = new NativeImage(NativeImage.Format.RGBA, 16, 16, true);

        // Fill the texture with translucent purple color
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                nativeImage.setPixelRGBA(x, y, new Color(128, 0, 128, 128).getRGB());
            }
        }

        this.dynamicTexture = new DynamicTexture(nativeImage);
        this.dynamicTexture.upload();
        this.dynamicTextureLocation = Minecraft.getInstance().getTextureManager().register("dynamic_plank", this.dynamicTexture);
        System.out.println("Dynamic texture updated and registered");
    }
}
        