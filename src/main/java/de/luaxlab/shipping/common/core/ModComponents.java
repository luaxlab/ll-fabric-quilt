package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.component.StallingComponent;
import de.luaxlab.shipping.common.entity.vessel.tug.AbstractTugEntity;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;

import java.util.function.Predicate;

/**
 * Registers the cardinal components. Is registered in the mod.quilt.json
 */
public class ModComponents implements EntityComponentInitializer {


	public static final ComponentKey<StallingComponent> STALLING =
			ComponentRegistry.getOrCreate(ModCommon.identifier("stalling"), StallingComponent.class);


	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(AbstractTugEntity.class, STALLING, AbstractTugEntity::CreateStallingComponent);
	}
}
