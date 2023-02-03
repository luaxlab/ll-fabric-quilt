package de.luaxlab.shipping.common.entity.container;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.entity.accessor.SteamHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class SteamHeadVehicleContainer<T extends Entity & HeadVehicle> extends AbstractHeadVehicleContainer<SteamHeadVehicleDataAccessor, T> {
    public SteamHeadVehicleContainer(int windowId, Level world, SteamHeadVehicleDataAccessor data,
                                     Inventory playerInventory, net.minecraft.world.entity.player.Player player) {
        super(ModContainers.STEAM_TUG_CONTAINER.get(), windowId, world, data, playerInventory, player);

        if(entity != null) {
            ItemHandlerComponent component = entity.getComponent(ModComponents.ITEM_HANDLER);
            if (component != null) {
                ItemStackHandler h = component.getHandler();
                addSlot(new SlotItemHandler(h, 0, 42, 40));
            }
        }
        this.addDataSlots(data.getRawData());
    }

    public int getBurnProgress(){
        return data.getBurnProgress();
    }

}
