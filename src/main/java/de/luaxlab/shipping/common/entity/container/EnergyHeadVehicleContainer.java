package de.luaxlab.shipping.common.entity.container;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.core.ModComponents;
import de.luaxlab.shipping.common.core.ModContainers;
import de.luaxlab.shipping.common.entity.accessor.EnergyHeadVehicleDataAccessor;
import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import io.github.fabricators_of_create.porting_lib.transfer.item.ItemStackHandler;
import io.github.fabricators_of_create.porting_lib.transfer.item.SlotItemHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EnergyHeadVehicleContainer<T extends Entity & HeadVehicle> extends AbstractHeadVehicleContainer<EnergyHeadVehicleDataAccessor, T> {
    public EnergyHeadVehicleContainer(int windowId, Level world, EnergyHeadVehicleDataAccessor data,
                                      Inventory playerInventory, Player player) {
        super(ModContainers.ENERGY_TUG_CONTAINER.get(), windowId, world, data, playerInventory, player);

		if(entity != null) {
			//If the component does not exist, its safe to assume something is very wrong.
			ItemHandlerComponent component = entity.getComponent(ModComponents.ITEM_HANDLER);
			ItemStackHandler h = component.getHandler();
			addSlot(new SlotItemHandler(h, 0, 32, 35));
		}
		//this.addDataSlots(data.getRawData()); //Not for energy tug
    }

    public long getEnergy() {
        return this.data.getEnergy();
    }

    public long getCapacity() {
        return this.data.getCapacity();
    }

    public double getEnergyCapacityRatio() {
        if (getCapacity() == 0) {
            return 1.0;
        }

        return (double) getEnergy() / getCapacity();
    }
}
