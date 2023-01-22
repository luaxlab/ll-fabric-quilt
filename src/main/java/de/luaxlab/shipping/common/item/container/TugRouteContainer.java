package de.luaxlab.shipping.common.item.container;

import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.entity.accessor.TugRouteScreenDataAccessor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class TugRouteContainer extends AbstractContainerMenu {
    private static final Logger LOGGER = LogManager.getLogger(TugRouteContainer.class);

    private boolean isOffHand;
    private ItemStack itemStack;

    public TugRouteContainer(int windowId, Level level, TugRouteScreenDataAccessor data, Inventory playerInventory, Player player) {
        super(ModContainers.TUG_ROUTE_CONTAINER, windowId);

        this.isOffHand = data.isOffHand();
        this.itemStack = player.getItemInHand(isOffHand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        LOGGER.debug("Got item stack {} in {}hand", itemStack.toString(), isOffHand ? "off" : "main");
    }

    public boolean isOffHand() {
        return isOffHand;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public ItemStack quickMoveStack(@NotNull Player p_38941_, int p_38942_) {
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
