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
