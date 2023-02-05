package de.luaxlab.shipping.common.network;

import de.luaxlab.shipping.common.core.ModCommon;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class TugRoutePacketHandler {
    protected static final Logger LOGGER = LogManager.getLogger(TugRoutePacketHandler.class);
    public static final ResourceLocation LOCATION = ModCommon.identifier("tug_route_channel");
    public static final SimpleChannel INSTANCE = new SimpleChannel(
            LOCATION
    );

    private static int id = 0;
    public static void register() {
        // Class<MSG> messageType, int index
        INSTANCE.registerC2SPacket(SetRouteTagPacket.class, id++);

        INSTANCE.initServerListener();
    }
}
