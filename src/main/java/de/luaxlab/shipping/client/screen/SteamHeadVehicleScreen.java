package de.luaxlab.shipping.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.entity.container.SteamHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamHeadVehicleScreen<T extends SteamTugEntity> extends AbstractHeadVehicleScreen<T, SteamHeadVehicleContainer<T>> {
    private static final ResourceLocation GUI = ModCommon.identifier("textures/container/steam_locomotive.png");

    public SteamHeadVehicleScreen(SteamHeadVehicleContainer menu, Inventory inventory, Component p_i51105_3_) {
        super(menu, inventory, p_i51105_3_);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        this.blit(matrixStack, i, j, 0, 0, this.getXSize(), this.getYSize());
        if(menu.isLit()) {
            int k = this.menu.getBurnProgress();
            this.blit(matrixStack, i + 43, j + 23 + 12 - k, 176, 12 - k, 14, k + 1);
        }
    }


}
