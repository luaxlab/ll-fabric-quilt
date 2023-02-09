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

public class TugRouteScreenDataAccessor extends DataAccessor {
    public TugRouteScreenDataAccessor(ContainerData data) {
        super(data);
    }

    public boolean isOffHand() {
        return this.data.get(1) == 1;
    }

    public static class Builder {
        SupplierIntArray arr;

        public Builder(int uuid) {
            this.arr = new SupplierIntArray(2);
            this.arr.set(0, uuid);
        }

        public Builder withOffHand(boolean isOffHand) {
            this.arr.setSupplier(1, () -> isOffHand ? 1 : 0);
            return this;
        }

        public TugRouteScreenDataAccessor build() {
            return new TugRouteScreenDataAccessor(this.arr);
        }
    }
}
