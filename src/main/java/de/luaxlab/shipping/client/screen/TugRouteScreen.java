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
import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.core.ModCommon;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.item.container.TugRouteClientHandler;
import de.luaxlab.shipping.common.item.container.TugRouteContainer;
import de.luaxlab.shipping.common.util.TugRouteNode;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class TugRouteScreen extends FixedAbstractContainerScreen<TugRouteContainer> {
    private static final Logger LOGGER = LogManager.getLogger(TugRouteScreen.class);
    public static final ResourceLocation GUI = new ResourceLocation(ModCommon.MODID, "textures/container/tug_route.png");

	private final TugRouteClientHandler route;

    public TugRouteScreen(TugRouteContainer menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 256;
        this.imageHeight = 233;

		ItemStack stack = this.menu.getItemStack();
        this.route = new TugRouteClientHandler(this, this.minecraft, TugRouteItem.getRoute(stack), menu.isOffHand());
    }

    private int getRight() {
        return this.leftPos + imageWidth;
    }

    private int getBot() {
        return this.topPos + imageHeight;
    }

    // https://github.com/ChAoSUnItY/EkiLib/blob/9b63591608cefafce32113a68bc8fd4b71972ece/src/main/java/com/chaos/eki_lib/gui/screen/StationSelectionScreen.java
    // https://github.com/ChAoSUnItY/EkiLib/blob/9b63591608cefafce32113a68bc8fd4b71972ece/src/main/java/com/chaos/eki_lib/utils/handlers/StationHandler.java#L21
    // https://github.com/ChAoSUnItY/EkiLib/blob/9b63591608cefafce32113a68bc8fd4b71972ece/src/main/java/com/chaos/eki_lib/utils/network/PacketInitStationHandler.java
    // https://github.com/ChAoSUnItY/EkiLib/blob/9b63591608cefafce32113a68bc8fd4b71972ece/src/main/java/com/chaos/eki_lib/utils/handlers/PacketHandler.java

    private Button.OnTooltip getTooltip(Component tooltip) {
        return (button, stack, x, y) -> renderTooltip(stack, tooltip, x, y);
    }

    @Override
    protected void init() {
        super.init();

        LOGGER.info("Initializing TugRouteScreen");

        this.addRenderableWidget(this.route.initializeWidget(TugRouteScreen.this.width, TugRouteScreen.this.height,
                topPos + 40, topPos + TugRouteScreen.this.imageHeight - 45, 20));

        this.addRenderableWidget(new Button(getRight() - 92, getBot() - 24, 20, 20,
                Component.literal("..ꕯ").withStyle(ChatFormatting.BOLD),
                button -> {
                    Optional<Pair<Integer, TugRouteNode>> selectedOpt = route.getSelected();
                    if (selectedOpt.isPresent()) {
                        Pair<Integer, TugRouteNode> selected = selectedOpt.get();
                        //this.minecraft.pushGuiLayer(new StringInputScreen(selected.getSecond(), selected.getFirst(), this.route::renameSelected));
						this.minecraft.setScreen(new StringInputScreen(selected.getSecond(), selected.getFirst(), this.route::renameSelected, this));
                    }
                },
                getTooltip(Component.translatable("screen.littlelogistics.tug_route.rename_button"))));

        this.addRenderableWidget(new Button(getRight() - 70, getBot() - 24, 20, 20,
                Component.literal("▲"),
                button -> route.moveSelectedUp(),
                getTooltip(Component.translatable("screen.littlelogistics.tug_route.up_button"))));

        this.addRenderableWidget(new Button(getRight() - 47, getBot() - 24, 20, 20,
                Component.literal("▼"),
                button -> route.moveSelectedDown(),
                getTooltip(Component.translatable("screen.littlelogistics.tug_route.down_button"))));

        this.addRenderableWidget(new Button(getRight() - 24, getBot() - 24, 20, 20,
                Component.literal("✘"),
                button -> route.deleteSelected(),
                getTooltip(Component.translatable("screen.littlelogistics.tug_route.delete_button"))));
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI);
        int left = this.getGuiLeft();
        int top = this.getGuiTop();
        int right = this.getRight();
        int bot = this.getBot();

        // topleft
        this.blit(matrixStack, left, top, 0, 0, 4, 4);
        // topright
        this.blit(matrixStack, getRight() - 4, top, 8, 0, 4, 4);
        // botleft
        this.blit(matrixStack, left, getBot() - 4, 0, 8, 4, 4);
        // botright
        this.blit(matrixStack, getRight() - 4, getBot() - 4, 8, 8, 4, 4);

        int zoom = 1000;
        // top
        blit(matrixStack, left + 4, top, this.getBlitOffset(),
                4 * zoom, 0,
                getXSize() - 8, 4,
                256 * zoom, 256);

        // bottom
        blit(matrixStack, left + 4, bot - 4, this.getBlitOffset(),
                4 * zoom, 8,
                getXSize() - 8, 4,
                256 * zoom, 256);

        // left
        blit(matrixStack, left, top + 4, this.getBlitOffset(),
                0, 4 * zoom,
                4, getYSize() - 8,
                256, 256 * zoom);

        // right
        blit(matrixStack, right - 4, top + 4, this.getBlitOffset(),
                8, 4 * zoom,
                4, getYSize() - 8,
                256, 256 * zoom);

        // middle
        blit(matrixStack, left + 4, top + 4, this.getBlitOffset(), 4 * zoom, 4 * zoom, getXSize() - 8, getYSize() - 8, 256 * zoom, 256 * zoom);
    }

    // remove inventory tag
    protected void renderLabels(@NotNull PoseStack stack, int p_230451_2_, int p_230451_3_) {
        this.font.draw(stack, this.title, (float)this.titleLabelX, (float)this.titleLabelY, 4210752);
    }

    public Font getFont() {
        return font;
    }

}
