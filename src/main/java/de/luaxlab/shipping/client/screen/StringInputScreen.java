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
import de.luaxlab.shipping.common.util.TugRouteNode;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class StringInputScreen extends Screen {
    private static final Logger LOGGER = LogManager.getLogger(StringInputScreen.class);
    public static final ResourceLocation GUI = new ResourceLocation(ModCommon.MODID, "textures/container/tug_route_rename.png");

    private String text;
    private EditBox textFieldWidget;
    private final Consumer<String> callback;

	protected Screen parent;

    public StringInputScreen(TugRouteNode node, int index, Consumer<String> callback, Screen parent) {
        super(Component.translatable("screen.littlelogistics.tug_route.rename", node.getDisplayName(index)));

        this.callback = callback;
        this.text = node.hasCustomName() ? node.getName() : "";
		this.parent = parent;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();

        LOGGER.info("Initializing StringInputScreen");

        int w = 156, h = 65;
        int left = (this.width - w) / 2;
        int top = (this.height - h) / 2;

        // x, y, width, height
        this.textFieldWidget = new EditBox(this.font, left + 10, top + 10, 135, 20, Component.literal(text));
        this.textFieldWidget.setValue(text);
        this.textFieldWidget.setMaxLength(20);
        this.textFieldWidget.setResponder((s) -> text = s);
        this.addRenderableWidget(textFieldWidget);

        // add button
        this.addRenderableWidget(new Button(left + 105, top + 37, 40, 20, Component.translatable("screen.littlelogistics.tug_route.confirm"), (b) -> {
            LOGGER.info("Setting to {}", text);
            callback.accept(text.isEmpty() ? null : text);
            //this.minecraft.popGuiLayer();
			minecraft.setScreen(null);
        }));
    }

    @Override
    public void render(@NotNull PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public void renderBackground(PoseStack poseStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);

        int w = 156, h = 65;
        int i = (this.width - w) / 2;
        int j = (this.height - h) / 2;
        this.blit(poseStack, i, j, 0, 0, w, h);
    }
}
