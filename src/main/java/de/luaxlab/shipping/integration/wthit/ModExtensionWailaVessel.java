package de.luaxlab.shipping.integration.wthit;

import de.luaxlab.shipping.common.entity.vessel.VesselEntity;
import mcp.mobius.waila.api.*;
import mcp.mobius.waila.api.component.ItemComponent;
import org.jetbrains.annotations.Nullable;

public class ModExtensionWailaVessel implements IWailaPlugin, IEntityComponentProvider {
@Override
	public void register(IRegistrar registrar) {
		registrar.addIcon(this, VesselEntity.class);
	}

	@Override
	public @Nullable ITooltipComponent getIcon(IEntityAccessor accessor, IPluginConfig config) {
		return new ItemComponent(((VesselEntity)accessor.getEntity()).getDropItem());
	}

}
