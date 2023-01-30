package de.luaxlab.shipping.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import de.luaxlab.shipping.common.core.ModBlocks;
import de.luaxlab.shipping.common.core.Registration;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends SimpleFabricLootTableProvider {

    public ModLootTableProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, LootContextParamSets.BLOCK);
    }

	private void dropSelf(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer, Block block) {
		biConsumer.accept(Registry.BLOCK.getKey(block), BlockLoot.createSingleItemTable(block));
	}

	@Override
	public void accept(BiConsumer<ResourceLocation, LootTable.Builder> bc) {
		dropSelf(bc, ModBlocks.TUG_DOCK.get());
		dropSelf(bc, ModBlocks.BARGE_DOCK.get());
		dropSelf(bc, ModBlocks.GUIDE_RAIL_CORNER.get());
		dropSelf(bc, ModBlocks.GUIDE_RAIL_TUG.get());
//		dropSelf(bc, ModBlocks.FLUID_HOPPER.get());
//		dropSelf(bc, ModBlocks.VESSEL_CHARGER.get());
//		dropSelf(bc, ModBlocks.VESSEL_DETECTOR.get());
//		dropSelf(bc, ModBlocks.SWITCH_RAIL.get());
//		dropSelf(bc, ModBlocks.AUTOMATIC_SWITCH_RAIL.get());
//		dropSelf(bc, ModBlocks.TEE_JUNCTION_RAIL.get());
//		dropSelf(bc, ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get());
//		dropSelf(bc, ModBlocks.JUNCTION_RAIL.get());
//		dropSelf(bc, ModBlocks.RAPID_HOPPER.get());
//		dropSelf(bc, ModBlocks.CAR_DOCK_RAIL.get());
//		dropSelf(bc, ModBlocks.LOCOMOTIVE_DOCK_RAIL.get());
	}

}
