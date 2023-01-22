package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.entity.generic.HeadVehicle;
import de.luaxlab.shipping.common.network.SetEnginePacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.quiltmc.qsl.networking.api.PacketSender;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

public class ModNetworking {
	public static final int MAX_ALLOWED_INTERACTION_DISTANCE = 6;
	public static final ResourceLocation ENGINE_CHANNEL = ModCommon.identifier("engine_channel");

	static void receiveEnginePacket(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf, PacketSender responseSender) {
		server.execute(() -> {
			var operation = new SetEnginePacket((buf));
			//We have to schedule on the server thread.
			var engine = player.level.getEntity(operation.locoId);
			if(engine instanceof HeadVehicle head && engine.distanceTo(player) < MAX_ALLOWED_INTERACTION_DISTANCE)
			{
				head.setEngineOn(operation.state);
			}
		});
	}

	public static void register()
	{
		ServerPlayNetworking.registerGlobalReceiver(ENGINE_CHANNEL, ModNetworking::receiveEnginePacket);
	}
}
