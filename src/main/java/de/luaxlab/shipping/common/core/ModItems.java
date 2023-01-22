package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.entity.vessel.tug.SteamTugEntity;
import de.luaxlab.shipping.common.item.SpringItem;
import de.luaxlab.shipping.common.item.TugRouteItem;
import de.luaxlab.shipping.common.item.VesselItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModItems {

	/*
	 *  Empty Icons
	 */

	public static final ResourceLocation EMPTY_ATLAS_LOC = InventoryMenu.BLOCK_ATLAS;
	public static final ResourceLocation LOCO_ROUTE_ICON = new ResourceLocation(ModCommon.MODID, "item/empty_loco_route");
	public static final ResourceLocation TUG_ROUTE_ICON = new ResourceLocation(ModCommon.MODID, "item/empty_tug_route");
	public static final ResourceLocation EMPTY_ENERGY = new ResourceLocation(ModCommon.MODID, "item/empty_energy");


	private static final Map<ResourceLocation, Item> SCHEDULED_ITEMS = new HashMap<>();

    public static final List<Item> ALL_ITEMS = new ArrayList<>();
    public static final Item.Properties DEFAULT_ITEM_SETTINGS = new FabricItemSettings().group(CreativeModeTab.TAB_TRANSPORTATION);

    /* Items */

    public static final Item TUG_ROUTE = defferedRegister(identifier("tug_route"),new TugRouteItem(DEFAULT_ITEM_SETTINGS));
    public static final Item SPRING = defferedRegister(identifier("spring"), new SpringItem(DEFAULT_ITEM_SETTINGS));
    /* Items: Spawn Eggs */
    public static final Item STEAM_TUG = defferedRegister(identifier("tug"), new VesselItem(DEFAULT_ITEM_SETTINGS, SteamTugEntity::new));
    //public static final Item CHEST_BARGE = defferedRegister(identifier("barge"), new SimpleEntityAddItem<>(DEFAULT_ITEM_SETTINGS, ChestBargeEntity::new));
    //public static final Item FISHING_BARGE = defferedRegister(identifier("fishing_barge"), new SimpleEntityAddItem<>(DEFAULT_ITEM_SETTINGS, FishingBargeEntity::new));
    /* Code */

    static <T extends Item> T defferedRegister(ResourceLocation identifier, T item)
    {
        ALL_ITEMS.add(item);
        SCHEDULED_ITEMS.put(identifier, item);
        return item;
    }


    /**
     * Called by {@link ModCommon} to handle late-registering
     */
    /*default*/ static void register()
    {
        /* easy-registry */
            SCHEDULED_ITEMS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
            SCHEDULED_ITEMS.clear();
        /* space for compelx registry */
    }

	@Deprecated
    private static ResourceLocation identifier(String path) { return ModCommon.identifier(path); }
}
