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

import de.luaxlab.shipping.data.client.ModBlockStateProvider;
import de.luaxlab.shipping.data.client.ModItemModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;

public final class DataGenerators implements DataGeneratorEntrypoint {
	@Override
    public void onInitializeDataGenerator(FabricDataGenerator gen){
		Path existingData = Path.of(System.getProperty("de.luaxlab.shipping.existingData"));
        ExistingFileHelper existingFileHelper = ExistingFileHelper.withResources(existingData);

        gen.addProvider(true, new ModBlockStateProvider(gen, existingFileHelper));
        gen.addProvider(true, new ModItemModelProvider(gen, existingFileHelper));

        ModBlockTagsProvider modBlockTagsProvider = new ModBlockTagsProvider(gen);
        gen.addProvider(true, modBlockTagsProvider);
        gen.addProvider(true, new ModItemTagsProvider(gen, modBlockTagsProvider));
        gen.addProvider(true, new ModLootTableProvider(gen));
        gen.addProvider(true, new ModRecipeProvider(gen));
    }

}
