package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.item.SpringItem;
import de.luaxlab.shipping.common.item.TugRouteItem;
import net.minecraft.client.renderer.item.ItemProperties;

public class ModItemModelProperties {

    public static void register() {
        ItemProperties.register(ModItems.SPRING.get(),
                ModCommon.identifier("springstate"),
                (stack, world, entity, i) -> SpringItem.getState(stack).equals(SpringItem.State.READY) ? 0 : 1);

        ItemProperties.register(ModItems.TUG_ROUTE.get(),
                ModCommon.identifier("routestate"),
                (stack, world, entity, i) -> !TugRouteItem.getRoute(stack).isEmpty() ? 0 : 1);
    }
}
