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

import de.luaxlab.shipping.common.component.EnergyComponent;
import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.entity.vessel.barge.AbstractBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.barge.ChestBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.barge.FishingBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.EnergyTugEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;

/**
 * Registers the cardinal components. Is registered in the mod.quilt.json
 */
public class ModComponents implements EntityComponentInitializer {


	public static final ComponentKey<StallingComponent> STALLING =
			ComponentRegistry.getOrCreate(ModCommon.identifier("stalling"), StallingComponent.class);

	public static final ComponentKey<ItemHandlerComponent> ITEM_HANDLER =
			ComponentRegistry.getOrCreate(ModCommon.identifier("item_handler"), ItemHandlerComponent.class);

	public static final ComponentKey<EnergyComponent> ENERGY_HANDLER =
			ComponentRegistry.getOrCreate(ModCommon.identifier("energy_handler"), EnergyComponent.class);


	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AbstractTugEntity.class, STALLING, AbstractTugEntity::createStallingComponent);
		registry.registerFor(AbstractBargeEntity.class, STALLING, AbstractBargeEntity::createStallingComponent);

		registry.registerFor(SteamTugEntity.class, ITEM_HANDLER, SteamTugEntity::createItemHandlerComponent);
		registry.registerFor(EnergyTugEntity.class, ITEM_HANDLER, EnergyTugEntity::createItemHandlerComponent);
		registry.registerFor(ChestBargeEntity.class, ITEM_HANDLER, ChestBargeEntity::createItemHandlerComponent);
		registry.registerFor(FishingBargeEntity.class, ITEM_HANDLER, FishingBargeEntity::createItemHandlerComponent);

		registry.registerFor(EnergyTugEntity.class, ENERGY_HANDLER, EnergyTugEntity::createEnergyComponent);
	}
}
