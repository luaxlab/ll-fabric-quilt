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

import com.mojang.blaze3d.vertex.PoseStack;
import de.luaxlab.shipping.common.entity.container.AbstractHeadVehicleContainer;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractHeadVehicleScreen<U extends Entity & HeadVehicle, T extends AbstractHeadVehicleContainer<?, U>> extends AbstractVehicleScreen<T>{
    private Button on;
    private Button off;
    public AbstractHeadVehicleScreen(T menu, Inventory inventory, Component p_i51105_3_) {
        super(menu, inventory, p_i51105_3_);
    }

    private Button.OnTooltip getTooltip(Component tooltip) {
        return (button, stack, x, y) -> renderTooltip(stack, tooltip, x, y);
    }

    @Override
    protected void init() {
        super.init();
        on = new Button(this.getGuiLeft() + 130, this.getGuiTop() + 25, 20, 20,
                Component.literal("\u23F5"),
                pButton -> menu.setEngine(true),
                getTooltip(Component.translatable("screen.littlelogistics.locomotive.on")));

        off = new Button(this.getGuiLeft() + 96, this.getGuiTop() + 25, 20, 20,
                Component.literal("\u23F8"),
                pButton -> menu.setEngine(false),
                getTooltip(Component.translatable("screen.littlelogistics.locomotive.off")));


        this.addRenderableWidget(off);
        this.addRenderableWidget(on);

    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.font.draw(matrixStack, Component.translatable("screen.littlelogistics.locomotive.route"), this.getGuiLeft() + 120, this.getGuiTop() + 55, 4210752);
        this.font.draw(matrixStack, menu.getRouteText(), this.getGuiLeft() + 120, this.getGuiTop() + 65, 4210752);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        off.active = menu.isOn();
        on.active = !menu.isOn();
    }
}
