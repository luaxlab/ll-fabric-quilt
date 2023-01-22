package de.luaxlab.shipping.common.core;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModSounds {

	private static final Map<ResourceLocation, SoundEvent> SCHEDULED_SOUNDS = new HashMap<>();

    public static final List<SoundEvent> ALL_SOUNDS = new ArrayList<>();

    /* Sounds */

	public static final SoundEvent STEAM_TUG_WHISTLE = defferedRegister(ModCommon.identifier("steam_tug_whistle"), new SoundEvent(ModCommon.identifier("tug_docking")));
	public static final SoundEvent TUG_DOCKING = defferedRegister(ModCommon.identifier("tug_docking"), new SoundEvent(ModCommon.identifier("tug_docking")));
	public static final SoundEvent TUG_UNDOCKING = defferedRegister(ModCommon.identifier("tug_undocking"), new SoundEvent(ModCommon.identifier("tug_docking")));

	/* Code */

    static SoundEvent defferedRegister(ResourceLocation identifier, SoundEvent sound)
    {
        ALL_SOUNDS.add(sound);
		SCHEDULED_SOUNDS.put(identifier, sound);
        return sound;
    }


    /**
     * Called by {@link ModCommon} to handle late-registering
     */
    /*default*/ static void register()
    {
        /* easy-registry */
		SCHEDULED_SOUNDS.forEach((identifier, sound) -> Registry.register(Registry.SOUND_EVENT, identifier, sound));
		SCHEDULED_SOUNDS.clear();
        /* space for compelx registry */
    }

}
