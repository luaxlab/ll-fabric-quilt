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

public class SupplierIntArray implements ContainerData {
    //private int count;
    private final IntSupplier[] suppliers;

    public SupplierIntArray(int count) {
        this.suppliers = new IntSupplier[count];
    }

    @Override
    public int get(int i) {
        return suppliers[i] == null ? 0 : suppliers[i].getAsInt();
    }

    @Override
    public void set(int i, int j) {
        this.suppliers[i] = () -> j;
    }

    public void setSupplier(int i, IntSupplier supplier) {
        this.suppliers[i] = supplier;
    }

    @Override
    public int getCount() {
        return suppliers.length;
    }
}
