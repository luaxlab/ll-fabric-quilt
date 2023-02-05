package de.luaxlab.shipping.common.network;

import de.luaxlab.shipping.common.core.ModCommon;
import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.resources.ResourceLocation;

public final class VehiclePacketHandler {
    public static final ResourceLocation LOCATION = ModCommon.identifier("locomotive_channel");
    public static final SimpleChannel INSTANCE = new SimpleChannel(
            LOCATION
    );

    private static int id = 0;
    public static void register() {
        // Class<MSG> messageType, int index
        INSTANCE.registerC2SPacket(SetEnginePacket.class, id++);

        INSTANCE.initServerListener();
    }
}
