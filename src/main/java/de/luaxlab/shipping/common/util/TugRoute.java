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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TugRoute extends ArrayList<TugRouteNode> {

    private static final String NAME_TAG = "name";
    private static final String NODES_TAG = "nodes";
    private static final String HASH_TAG = "hash"; // # :)

    private final String name;

    public TugRoute(String name, List<TugRouteNode> nodes) {
        super(nodes);
        this.name = name;
    }

    public TugRoute(List<TugRouteNode> nodes) {
        this(null, nodes);
    }

    public TugRoute() {
        this(null, new ArrayList<>());
    }

    public boolean hasCustomName() {
        return this.name != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TugRoute that = (TugRoute) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag list = new ListTag();
        for (TugRouteNode node : this) {
            list.add(node.toNBT());
        }

        tag.put(NODES_TAG, list);
        if (hasCustomName()) {
            tag.putString(NAME_TAG, this.name);
        }
        return tag;
    }

    public static TugRoute fromNBT(CompoundTag tag) {
        String name = null;
        if (tag.contains(NAME_TAG)) {
            name = tag.getString(NAME_TAG);
        }

        // 10 == magic number of Compound Tag
        ListTag nodesNBT = tag.getList(NODES_TAG, Tag.TAG_COMPOUND);

        ArrayList<TugRouteNode> nodes = new ArrayList<>();
        for (int i = 0; i < nodesNBT.size(); i++) {
            nodes.add(TugRouteNode.fromNBT(nodesNBT.getCompound(i)));
        }

        return new TugRoute(name, nodes);
    }
}
