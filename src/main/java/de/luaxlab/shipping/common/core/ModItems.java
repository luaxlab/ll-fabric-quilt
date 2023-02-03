package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.entity.vessel.barge.ChestBargeEntity;
import de.luaxlab.shipping.common.item.SpringItem;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.item.VesselItem;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

public class ModItems {

	/**
	 *  Empty Icons
	 */

	public static final ResourceLocation EMPTY_ATLAS_LOC = InventoryMenu.BLOCK_ATLAS;
	public static final ResourceLocation LOCO_ROUTE_ICON = new ResourceLocation(ModCommon.MODID, "item/empty_loco_route");
	public static final ResourceLocation TUG_ROUTE_ICON = new ResourceLocation(ModCommon.MODID, "item/empty_tug_route");
	public static final ResourceLocation EMPTY_ENERGY = new ResourceLocation(ModCommon.MODID, "item/empty_energy");


	/**
	 * COMMON
	 */
//	public static final RegistryObject<Item> CONDUCTORS_WRENCH = Registration.ITEMS.register("conductors_wrench",
//			() -> new WrenchItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION).stacksTo(1)));


	public static final RegistryObject<Item> SPRING = Registration.ITEMS.register("spring",
			() -> new SpringItem(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> CREATIVE_CAPACITOR = Registration.ITEMS.register("creative_capacitor",
//			() -> new CreativeCapacitor(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION)));

	/**
	 * Vessels
	 */

	public static final RegistryObject<Item> CHEST_BARGE = Registration.ITEMS.register("barge",
			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), ChestBargeEntity::new));

//    public static final RegistryObject<Item> CHUNK_LOADER_BARGE = Registration.ITEMS.register("chunk_loader_barge",
//            () -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), ChunkLoaderBargeEntity::new));

//	public static final RegistryObject<Item> FISHING_BARGE = Registration.ITEMS.register("fishing_barge",
//			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), FishingBargeEntity::new));

//	public static final RegistryObject<Item> FLUID_BARGE = Registration.ITEMS.register("fluid_barge",
//			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), FluidTankBargeEntity::new));

//	public static final RegistryObject<Item> SEATER_BARGE = Registration.ITEMS.register("seater_barge",
//			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), SeaterBargeEntity::new));

	public static final RegistryObject<Item> STEAM_TUG = Registration.ITEMS.register("tug",
			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), SteamTugEntity::new));

//	public static final RegistryObject<Item> ENERGY_TUG = Registration.ITEMS.register("energy_tug",
//			() -> new VesselItem(new Item.Properties().tab(CreativeModeTab.TAB_TRANSPORTATION), EnergyTugEntity::new));

	/**
	 * Trains
	 */

	public static final RegistryObject<Item> TUG_ROUTE = Registration.ITEMS.register("tug_route",
			() -> new TugRouteItem(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> CHEST_CAR = Registration.ITEMS.register("chest_car",
//			() -> new TrainCarItem(ChestCarEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> FLUID_CAR = Registration.ITEMS.register("fluid_car",
//			() -> new TrainCarItem(FluidTankCarEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//    public static final RegistryObject<Item> CHUNK_LOADER_CAR = Registration.ITEMS.register("chunk_loader_car",
//            () -> new TrainCarItem(ChunkLoaderCarEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> SEATER_CAR = Registration.ITEMS.register("seater_car",
//			() -> new TrainCarItem(SeaterCarEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> STEAM_LOCOMOTIVE = Registration.ITEMS.register("steam_locomotive",
//			() -> new TrainCarItem(SteamLocomotiveEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> ENERGY_LOCOMOTIVE = Registration.ITEMS.register("energy_locomotive",
//			() -> new TrainCarItem(EnergyLocomotiveEntity::new, new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

	public static final RegistryObject<Item> RECEIVER_COMPONENT = Registration.ITEMS.register("receiver_component",
			() -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

	public static final RegistryObject<Item> TRANSMITTER_COMPONENT = Registration.ITEMS.register("transmitter_component",
			() -> new Item(new Item.Properties().stacksTo(64).tab(CreativeModeTab.TAB_TRANSPORTATION)));

//	public static final RegistryObject<Item> LOCO_ROUTE = Registration.ITEMS.register("locomotive_route",
//			() -> new LocoRouteItem(new Item.Properties().stacksTo(16).tab(CreativeModeTab.TAB_TRANSPORTATION)));


	public static void register () {

	}

}
