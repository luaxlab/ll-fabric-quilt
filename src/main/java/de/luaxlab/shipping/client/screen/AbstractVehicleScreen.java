package de.luaxlab.shipping.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import de.luaxlab.shipping.common.entity.container.AbstractItemHandlerContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class AbstractVehicleScreen<T extends AbstractItemHandlerContainer> extends FixedAbstractContainerScreen<T> {
    public AbstractVehicleScreen(T menu, Inventory inventory, Component p_i51105_3_) {
        super(menu, inventory, p_i51105_3_);
    }

    protected static boolean inBounds(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
        return (mouseX >= x1) && (mouseX < x2) && (mouseY >= y1) && (mouseY < y2);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }
}
