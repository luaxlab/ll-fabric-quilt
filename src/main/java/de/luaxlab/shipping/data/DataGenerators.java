package de.luaxlab.shipping.data;

import de.luaxlab.shipping.data.client.ModBlockStateProvider;
import de.luaxlab.shipping.data.client.ModItemModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

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
