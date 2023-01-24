package de.luaxlab.shipping.common.core;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class Registration  {
	public static final LazyRegistrar<Block> BLOCKS = create(Registry.BLOCK);
	public static final LazyRegistrar<MenuType<?>> CONTAINERS = create(Registry.MENU);
	public static final LazyRegistrar<EntityType<?>> ENTITIES = create(Registry.ENTITY_TYPE);
	public static final LazyRegistrar<Item> ITEMS = create(Registry.ITEM);
	public static final LazyRegistrar<RecipeSerializer<?>> RECIPE_SERIALIZERS = create(Registry.RECIPE_SERIALIZER);
	public static final LazyRegistrar<BlockEntityType<?>> TILE_ENTITIES = create(Registry.BLOCK_ENTITY_TYPE);
	public static final LazyRegistrar<SoundEvent> SOUND_EVENTS = create(Registry.SOUND_EVENT);


	private static<T> LazyRegistrar<T> create(Registry<T> registry) {
		return LazyRegistrar.create(registry, ModCommon.MODID);
	}

	public static void register () {
		ModEntities.register();
		ModItems.register();
		ModBlocks.register();
		ModBlockEntities.register();
//		ModRecipeSerializers.register();
		ModContainers.register();
//		TugRoutePacketHandler.register();
//		VehicleTrackerPacketHandler.register();
//		VehiclePacketHandler.register();
		ModSounds.register();

		BLOCKS.register();
		ITEMS.register();
		CONTAINERS.register();
		RECIPE_SERIALIZERS.register();
		TILE_ENTITIES.register();
		ENTITIES.register();
		SOUND_EVENTS.register();
	}
}
