/*
 Little Logistics: Quilt Edition Data Generation
 Copyright © 2023 LuaX, Abbie

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
package de.luaxlab.shipping.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addTags() {
/*
        tag(BlockTags.RAILS).add(ModBlocks.SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.LOCOMOTIVE_DOCK_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.CAR_DOCK_RAIL.get());
*/
    }
}
