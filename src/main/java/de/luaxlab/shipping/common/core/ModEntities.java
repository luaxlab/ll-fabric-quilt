/**
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, LuaX, Murad Akhundov

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

import de.luaxlab.shipping.common.entity.vessel.barge.ChestBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

@SuppressWarnings("deprecation")
public class ModEntities {
	public static final RegistryObject<EntityType<ChestBargeEntity>> CHEST_BARGE =
			Registration.ENTITIES.register("barge",
					() -> EntityType.Builder.<ChestBargeEntity>of(ChestBargeEntity::new,
									MobCategory.MISC).sized(0.6f, 0.9f)
							.clientTrackingRange(8)
							.build(new ResourceLocation(ModCommon.MODID, "barge").toString()));
//
//	public static final RegistryObject<EntityType<ChunkLoaderBargeEntity>> CHUNK_LOADER_BARGE =
//			Registration.ENTITIES.register("chunk_loader_barge",
//					() -> EntityType.Builder.<ChunkLoaderBargeEntity>of(ChunkLoaderBargeEntity::new,
//									MobCategory.MISC).sized(0.6f, 0.9f)
//							.clientTrackingRange(8)
//							.build(new ResourceLocation(ModCommon.MODID, "chunk_loader_barge").toString()));
//
//	public static final RegistryObject<EntityType<FishingBargeEntity>> FISHING_BARGE =
//			Registration.ENTITIES.register("fishing_barge",
//					() -> EntityType.Builder.<FishingBargeEntity>of(FishingBargeEntity::new,
//									MobCategory.MISC).sized(0.6f, 0.9f)
//							.clientTrackingRange(8)
//							.build(new ResourceLocation(ModCommon.MODID, "fishing_barge").toString()));
//
//	public static final RegistryObject<EntityType<FluidTankBargeEntity>> FLUID_TANK_BARGE =
//			Registration.ENTITIES.register("fluid_barge",
//					() -> EntityType.Builder.<FluidTankBargeEntity>of(FluidTankBargeEntity::new,
//									MobCategory.MISC).sized(0.6f, 0.9f)
//							.clientTrackingRange(8)
//							.build(new ResourceLocation(ModCommon.MODID, "fluid_barge").toString()));
//
//	public static final RegistryObject<EntityType<SeaterBargeEntity>> SEATER_BARGE =
//			Registration.ENTITIES.register("seater_barge",
//					() -> EntityType.Builder.<SeaterBargeEntity>of(SeaterBargeEntity::new,
//									MobCategory.MISC).sized(0.6f, 0.9f)
//							.clientTrackingRange(8)
//							.build(new ResourceLocation(ModCommon.MODID, "seater_barge").toString()));

	public static final RegistryObject<EntityType<SteamTugEntity>> STEAM_TUG =
			Registration.ENTITIES.register("tug",
					() -> EntityType.Builder.<SteamTugEntity>of(SteamTugEntity::new,
									MobCategory.MISC).sized(0.7f, 0.9f)
							.clientTrackingRange(8)
							.build(new ResourceLocation(ModCommon.MODID, "tug").toString()));

	public static final RegistryObject<EntityType<EnergyTugEntity>> ENERGY_TUG =
			Registration.ENTITIES.register("energy_tug",
					() -> EntityType.Builder.<EnergyTugEntity>of(EnergyTugEntity::new,
									MobCategory.MISC).sized(0.7f, 0.9f)
							.clientTrackingRange(8)
							.build(new ResourceLocation(ModCommon.MODID, "energy_tug").toString()));
//
//	public static final RegistryObject<EntityType<ChestCarEntity>> CHEST_CAR =
//			Registration.ENTITIES.register("chest_car",
//					() -> EntityType.Builder.<ChestCarEntity>of(ChestCarEntity::new,
//									MobCategory.MISC).sized(0.7f, 0.9f)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.build(new ResourceLocation(ModCommon.MODID, "chest_car").toString()));
//
//	public static final RegistryObject<EntityType<SeaterCarEntity>> SEATER_CAR =
//			Registration.ENTITIES.register("seater_car",
//					() -> EntityType.Builder.<SeaterCarEntity>of(SeaterCarEntity::new,
//									MobCategory.MISC).sized(0.7f, 0.9f)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.build(new ResourceLocation(ModCommon.MODID, "seater_car").toString()));
//
//	public static final RegistryObject<EntityType<FluidTankCarEntity>> FLUID_CAR =
//			Registration.ENTITIES.register("fluid_car",
//					() -> EntityType.Builder.<FluidTankCarEntity>of(FluidTankCarEntity::new,
//									MobCategory.MISC).sized(0.7f, 0.9f)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.build(new ResourceLocation(ModCommon.MODID, "fluid_car").toString()));
//
//	public static final RegistryObject<EntityType<ChunkLoaderCarEntity>> CHUNK_LOADER_CAR =
//			Registration.ENTITIES.register("chunk_loader_car",
//					() -> EntityType.Builder.<ChunkLoaderCarEntity>of(ChunkLoaderCarEntity::new,
//									MobCategory.MISC).sized(0.7f, 0.9f)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.build(new ResourceLocation(ModCommon.MODID, "chunk_loader_car").toString()));
//
//
//	public static final RegistryObject<EntityType<AbstractLocomotiveEntity>> STEAM_LOCOMOTIVE =
//			Registration.ENTITIES.register("steam_locomotive",
//					() -> EntityType.Builder.<AbstractLocomotiveEntity>of(SteamLocomotiveEntity::new,
//									MobCategory.MISC).sized(0.7f, 0.9f)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.build(new ResourceLocation(ModCommon.MODID, "steam_locomotive").toString()));
//
//	public static final RegistryObject<EntityType<AbstractLocomotiveEntity>> ENERGY_LOCOMOTIVE =
//			Registration.ENTITIES.register("energy_locomotive",
//					() -> EntityType.Builder.<AbstractLocomotiveEntity>of(EnergyLocomotiveEntity::new,
//									MobCategory.MISC)
//							.clientTrackingRange(8)
//							.setShouldReceiveVelocityUpdates(true)
//							.sized(0.7f, 0.9f)
//							.build(new ResourceLocation(ModCommon.MODID, "energy_locomotive").toString()));


	public static void register () {
		FabricDefaultAttributeRegistry.register(STEAM_TUG.get(), SteamTugEntity.setCustomAttributes());
        FabricDefaultAttributeRegistry.register(CHEST_BARGE.get(), ChestBargeEntity.setCustomAttributes());
		FabricDefaultAttributeRegistry.register(ENERGY_TUG.get(), EnergyTugEntity.setCustomAttributes());
        //FabricDefaultAttributeRegistry.register(FISHING_BARGE, FishingBargeEntity.setCustomAttributes());
	}
}
