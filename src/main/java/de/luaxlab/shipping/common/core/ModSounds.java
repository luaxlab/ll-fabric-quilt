package de.luaxlab.shipping.common.core;

import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

	public static final RegistryObject<SoundEvent> STEAM_TUG_WHISTLE = Registration.SOUND_EVENTS.register("steam_tug_whistle",
			() -> new SoundEvent(new ResourceLocation(ModCommon.MODID, "steam_tug_whistle")));

	public static final RegistryObject<SoundEvent> TUG_DOCKING = Registration.SOUND_EVENTS.register("tug_docking",
			() -> new SoundEvent(new ResourceLocation(ModCommon.MODID, "tug_docking")));

	public static final RegistryObject<SoundEvent> TUG_UNDOCKING = Registration.SOUND_EVENTS.register("tug_undocking",
			() -> new SoundEvent(new ResourceLocation(ModCommon.MODID, "tug_undocking")));

	public static void register () {}
}
