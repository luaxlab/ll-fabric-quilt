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
package de.luaxlab.shipping.common.energy;

import de.luaxlab.shipping.common.component.EnergyComponent;
import de.luaxlab.shipping.common.core.*;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.item.VesselItem;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.reborn.energy.api.EnergyStorage;

/**
 * This class is mandantory to not load energy stuff when the energy API is not present
 */
public class IntegratedEnergyExtension {

	public static final Logger LOGGER = LoggerFactory.getLogger("Little Logistics: Energy");

	public static final RegistryObject<Block> VESSEL_CHARGER_BLOCK = ModBlocks.register(
			"vessel_charger",
			() -> new VesselChargerBlock(Block.Properties.of(Material.METAL)
					.destroyTime(0.5f)
			),
			CreativeModeTab.TAB_TRANSPORTATION);

	public static final RegistryObject<BlockEntityType<VesselChargerTileEntity>> VESSEL_CHARGER_ENTITY = ModBlockEntities.register(
			"vessel_charger",
			VesselChargerTileEntity::new,
			VESSEL_CHARGER_BLOCK
	);

	public static final RegistryObject<Item> CREATIVE_CAPACITOR = Registration.ITEMS.register("creative_capacitor",
			() -> new Item(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));


	public static final RegistryObject<EntityType<EnergyTugEntity>> ENERGY_TUG =
			Registration.ENTITIES.register("energy_tug",
					() -> EntityType.Builder.<EnergyTugEntity>of(EnergyTugEntity::new,
									MobCategory.MISC).sized(0.7f, 0.9f)
							.clientTrackingRange(8)
							.build(new ResourceLocation(ModCommon.MODID, "energy_tug").toString()));

	public static final RegistryObject<Item> ENERGY_TUG_ITEM = Registration.ITEMS.register("energy_tug",
			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), EnergyTugEntity::new));


	public static final ComponentKey<EnergyComponent> ENERGY_HANDLER =
			ComponentRegistry.getOrCreate(ModCommon.identifier("energy_handler"), EnergyComponent.class);



	@SuppressWarnings("deprecation")
	public static void register()
	{
		//Register the creative capacitor
		EnergyStorage.ITEM.registerForItems((itemStack, context) -> EnergyUtils.CREATIVE_SUPPLY, CREATIVE_CAPACITOR.get());
		//Register entities
		FabricDefaultAttributeRegistry.register(ENERGY_TUG.get(), EnergyTugEntity.setCustomAttributes());

		LOGGER.info("We're energized!");
	}

	public static void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(EnergyTugEntity.class, ENERGY_HANDLER, EnergyTugEntity::createEnergyComponent);
		registry.registerFor(EnergyTugEntity.class, ModComponents.ITEM_HANDLER, EnergyTugEntity::createItemHandlerComponent);
	}

}
