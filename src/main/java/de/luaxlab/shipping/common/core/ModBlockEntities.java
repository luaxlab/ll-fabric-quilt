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

import de.luaxlab.shipping.common.block.dock.BargeDockTileEntity;
import de.luaxlab.shipping.common.block.dock.TugDockTileEntity;
import de.luaxlab.shipping.common.energy.VesselChargerTileEntity;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
	public static final RegistryObject<BlockEntityType<TugDockTileEntity>> TUG_DOCK = register(
			"tug_dock",
			TugDockTileEntity::new,
			ModBlocks.TUG_DOCK
	);

	public static final RegistryObject<BlockEntityType<BargeDockTileEntity>> BARGE_DOCK = register(
			"barge_dock",
			BargeDockTileEntity::new,
			ModBlocks.BARGE_DOCK
	);

//	public static final RegistryObject<BlockEntityType<LocomotiveDockTileEntity>> LOCOMOTIVE_DOCK = register(
//			"locomotive_dock",
//			LocomotiveDockTileEntity::new,
//			ModBlocks.LOCOMOTIVE_DOCK_RAIL
//	);
//
//	public static final RegistryObject<BlockEntityType<TrainCarDockTileEntity>> CAR_DOCK = register(
//			"car_dock",
//			TrainCarDockTileEntity::new,
//			ModBlocks.CAR_DOCK_RAIL
//	);
//
//	public static final RegistryObject<BlockEntityType<VesselDetectorTileEntity>> VESSEL_DETECTOR = register(
//			"vessel_detector",
//			VesselDetectorTileEntity::new,
//			ModBlocks.VESSEL_DETECTOR
//	);
//
//	public static final RegistryObject<BlockEntityType<FluidHopperTileEntity>> FLUID_HOPPER = register(
//			"fluid_hopper",
//			FluidHopperTileEntity::new,
//			ModBlocks.FLUID_HOPPER
//	);
//
	public static final RegistryObject<BlockEntityType<VesselChargerTileEntity>> VESSEL_CHARGER = register(
			"vessel_charger",
			VesselChargerTileEntity::new,
			ModBlocks.VESSEL_CHARGER
	);
//
//	public static final RegistryObject<BlockEntityType<RapidHopperTileEntity>> RAPID_HOPPER = register(
//			"rapid_hopper",
//			RapidHopperTileEntity::new,
//			ModBlocks.RAPID_HOPPER
//	);

	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(
			String name,
			BlockEntityType.BlockEntitySupplier<T> factory,
			RegistryObject<? extends Block> block) {
		return Registration.TILE_ENTITIES.register(name, () ->
				BlockEntityType.Builder.of(factory, block.get()).build(null));
	}

	public static void register () {

	}
}
