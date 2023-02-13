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
package de.luaxlab.shipping.common.entity.accessor;

import net.minecraft.world.inventory.ContainerData;

import java.util.function.IntSupplier;

public class SteamHeadVehicleDataAccessor extends HeadVehicleDataAccessor{
    public SteamHeadVehicleDataAccessor(ContainerData data) {
        super(data);
    }

    public int getBurnProgress() {
        return this.data.get(15);
    }

    public static class Builder extends HeadVehicleDataAccessor.Builder{

        public Builder() {
            this.arr = new SupplierIntArray(20);
        }

        public Builder withBurnProgress(IntSupplier burnProgress) {
            this.arr.setSupplier(15, burnProgress);
            return this;
        }

        public SteamHeadVehicleDataAccessor build() {
            return new SteamHeadVehicleDataAccessor(this.arr);
        }
    }
}
