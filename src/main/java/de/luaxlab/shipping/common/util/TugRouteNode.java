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
package de.luaxlab.shipping.common.util;

import lombok.Getter;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

import javax.annotation.Nullable;
import java.util.Objects;

public class TugRouteNode {
    private static final String NAME_TAG = "name";
    private static final String X_TAG = "x";
    private static final String Z_TAG = "z";
    private static final String COORDS_TAG = "coordinates";

    private String name;
	@Getter
    private final double x, z;

    public TugRouteNode(String name, double x, double z) {
        this.name = name;
        this.x = x;
        this.z = z;
    }

    public TugRouteNode(double x, double y) {
        this(null, x, y);
    }

    public String getDisplayName(int index) {
        if (!this.hasCustomName()) {
            return I18n.get("item.littlelogistics.tug_route.node", index);
        } else {
            return I18n.get("item.littlelogistics.tug_route.node_named", index, getName());
        }
    }

    public String getDisplayCoords() {
        return this.x + ", " + this.z;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getName() {
        return this.name;
    }

    public boolean hasCustomName() {
        return this.name != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TugRouteNode that = (TugRouteNode) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.z, z) == 0 && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, x, z);
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();
        if (this.hasCustomName()) {
			tag.putString(NAME_TAG, this.getName());
        }

        CompoundTag coords = new CompoundTag();
        coords.putDouble(X_TAG, x);
        coords.putDouble(Z_TAG, z);

        tag.put(COORDS_TAG, coords);
        return tag;
    }

    public static TugRouteNode fromNBT(CompoundTag tag) {
        String name = null;
        if (tag.contains(NAME_TAG)) {
            name = tag.getString(NAME_TAG);
        }

        CompoundTag coords = tag.getCompound(COORDS_TAG);
        double x = coords.getDouble(X_TAG);
        double z = coords.getDouble(Z_TAG);

        return new TugRouteNode(name, x, z);
    }

    public static TugRouteNode fromVector2f(Vec2 node) {
        double x = node.x, z = node.y;
        return new TugRouteNode(null, x, z);
    }
}
