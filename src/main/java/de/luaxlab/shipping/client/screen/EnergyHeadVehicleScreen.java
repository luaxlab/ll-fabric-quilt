/*
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, Murad Akhundov, LuaX, Abbie

 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 3 of the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public License
 along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package de.luaxlab.shipping.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.entity.container.EnergyHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public class EnergyHeadVehicleScreen<T extends Entity & HeadVehicle> extends AbstractHeadVehicleScreen<T, EnergyHeadVehicleContainer<T>> {
    private static final ResourceLocation GUI = ModCommon.identifier("textures/container/energy_locomotive.png");

    public EnergyHeadVehicleScreen(EnergyHeadVehicleContainer menu, Inventory inventory, Component p_i51105_3_) {
        super(menu, inventory, p_i51105_3_);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        if (inBounds(mouseX - leftPos, mouseY - topPos, 56, 17, 68, 67)) {
            this.renderTooltip(matrixStack,
                    Component.translatable("screen.littlelogistics.energy_tug.energy",
                            getMenu().getEnergy(),
                            getMenu().getCapacity()),
                    mouseX, mouseY);
        }
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

        double r = this.menu.getEnergyCapacityRatio();
        int k = (int) (r * 50);
        this.blit(matrixStack, i + 56, j + 17 + 50 - k, 176, 50 - k, 12, k);
    }
}
