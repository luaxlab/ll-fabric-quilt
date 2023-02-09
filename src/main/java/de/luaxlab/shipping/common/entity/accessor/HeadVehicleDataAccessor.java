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

import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

public class HeadVehicleDataAccessor extends DataAccessor{
    public HeadVehicleDataAccessor(ContainerData data) {
        super(data);
    }

    public boolean isLit() {
        return this.data.get(1) == 1;
    }

    public boolean isOn() {
        return this.data.get(2) == 1;
    }

    public int visitedSize() {
        return this.data.get(3);
    }

    public int routeSize() {
        return this.data.get(4);
    }

    public boolean canMove() {
        return this.data.get(5) == 1;
    }

    public static class Builder {
        SupplierIntArray arr;

        public Builder withId(int id) {
            this.arr.set(0, id);
            return this;
        }

        public Builder withLit(BooleanSupplier lit) {
            this.arr.setSupplier(1, () -> lit.getAsBoolean() ? 1 : -1);
            return this;
        }


        public Builder withOn(BooleanSupplier lit) {
            this.arr.setSupplier(2, () -> lit.getAsBoolean() ? 1 : -1);
            return this;
        }

        public Builder withVisitedSize(IntSupplier s) {
            this.arr.setSupplier(3, s);
            return this;
        }

        public Builder withRouteSize(IntSupplier s) {
            this.arr.setSupplier(4, s);
            return this;
        }

        public Builder withCanMove(BooleanSupplier lit) {
            this.arr.setSupplier(5, () -> lit.getAsBoolean() ? 1 : -1);
            return this;
        }

        public HeadVehicleDataAccessor build() {
            return new HeadVehicleDataAccessor(this.arr);
        }
    }
}
