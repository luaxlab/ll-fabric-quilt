package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.component.ItemHandlerComponent;
import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.entity.vessel.barge.AbstractBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.barge.ChestBargeEntity;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
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


	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AbstractTugEntity.class, STALLING, AbstractTugEntity::createStallingComponent);
		registry.registerFor(AbstractBargeEntity.class, STALLING, AbstractBargeEntity::createStallingComponent);

		registry.registerFor(SteamTugEntity.class, ITEM_HANDLER, SteamTugEntity::createItemHandlerComponent);
		registry.registerFor(ChestBargeEntity.class, ITEM_HANDLER, ChestBargeEntity::createItemHandlerComponent);
	}
}
