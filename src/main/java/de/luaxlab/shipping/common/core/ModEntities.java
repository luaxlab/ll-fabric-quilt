/**
 Little Logistics: Quilt Edition, a mod about transportation for Minecraft
 Copyright © 2022 EDToaster, LuaX, Murad Akhundov

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

import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    /* Tugs */
    public static final EntityType<SteamTugEntity> STEAM_TUG = Registry.register(
            Registry.ENTITY_TYPE,
            identifier("tug"),
            FabricEntityTypeBuilder.create(MobCategory.WATER_CREATURE, (EntityType.EntityFactory<SteamTugEntity>) SteamTugEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );

    /* Barges */
    /*public static final EntityType<ChestBargeEntity> CHEST_BARGE = Registry.register(
            Registry.ENTITY_TYPE,
            identifier("barge"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, (EntityType.EntityFactory<ChestBargeEntity>) ChestBargeEntity::new).dimensions(EntityDimensions.fixed(0.6f, 0.9f)).build()
    );
    public static final EntityType<FishingBargeEntity> FISHING_BARGE = Registry.register(
            Registry.ENTITY_TYPE,
            identifier("fishing_barge"),
            FabricEntityTypeBuilder.create(SpawnGroup.WATER_CREATURE, (EntityType.EntityFactory<FishingBargeEntity>) FishingBargeEntity::new).dimensions(EntityDimensions.fixed(0.6f, 0.9f)).build()
    );*/

    /* Misc */
    /*public static final EntityType<Entity> SPRING = Registry.register(
            Registry.ENTITY_TYPE,
            identifier("spring"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, SpringEntity::new).dimensions(EntityDimensions.fixed(0.05f, 0.2f)).build()
    );

    public static final EntityType<TugDummyHitboxEntity> DUMMY_TUG_HITBOX = Registry.register(
            Registry.ENTITY_TYPE,
            identifier("dummy_tug_hitbox"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<TugDummyHitboxEntity>) TugDummyHitboxEntity::new).dimensions(EntityDimensions.fixed(0.75f, 0.75f)).build()
    );*/

    /**
     * Called by {@link ModCommon} to handle late-registering
     */
    /*default*/ static void register()
    {
        /* easy-registry */
        FabricDefaultAttributeRegistry.register(STEAM_TUG, SteamTugEntity.setCustomAttributes());
        /*FabricDefaultAttributeRegistry.register(CHEST_BARGE, ChestBargeEntity.setCustomAttributes());
        FabricDefaultAttributeRegistry.register(FISHING_BARGE, FishingBargeEntity.setCustomAttributes());*/


        /* space for compelx registry */
    }

	@Deprecated
    private static ResourceLocation identifier(String path) { return ModCommon.identifier(path); }

}
