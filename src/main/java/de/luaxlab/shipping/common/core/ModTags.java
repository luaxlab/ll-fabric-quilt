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
package de.luaxlab.shipping.common.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final class Blocks {
//        private static Tag.Named<Block> common(String path) {
//            return BlockTags.bind(new ResourceLocation("c", path).toString());
//        }
//
//        private static Tag.Named<Block> mod(String path) {
//            return BlockTags.bind(ModCommon.identifier(path).toString());
//        }
    }

    public static final class Items {
        public static final TagKey<Item> WRENCHES = common("wrenches");

        private static TagKey<Item> common(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("c", path));
        }

        private static TagKey<Item> mod(String path) {
            return TagKey.create(Registry.ITEM_REGISTRY, ModCommon.identifier(path));
        }
    }
}
