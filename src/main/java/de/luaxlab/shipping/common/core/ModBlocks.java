package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.block.dock.BargeDockBlock;
import de.luaxlab.shipping.common.block.dock.TugDockBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import de.luaxlab.shipping.common.block.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlocks {

    private static final Map<ResourceLocation, Block> SCHEDULED_BLOCKS = new HashMap<>();
    private static final Map<ResourceLocation, Item> SCHEDULED_ITEMS = new HashMap<>();

    public static final List<Block> ALL_BLOCKS = new ArrayList<>();
    public static final BlockBehaviour.Properties DEFAULT_BLOCK_SETTINGS = FabricBlockSettings.of(Material.METAL);

    /* Blocks */

    public static final Block GUIDE_RAIL_CORNER = defferedRegister(identifier("guide_rail_corner"), new CornerGuideRailBlock(DEFAULT_BLOCK_SETTINGS));
    public static final Block GUIDE_RAIL_TUG = defferedRegister(identifier("guide_rail_tug"), new TugGuideRailBlock(DEFAULT_BLOCK_SETTINGS));

	public static final Block TUG_DOCK = defferedRegister(identifier("tug_dock"), new TugDockBlock(DEFAULT_BLOCK_SETTINGS));
	public static final Block BARGE_DOCK = defferedRegister(identifier("barge_dock"), new BargeDockBlock(DEFAULT_BLOCK_SETTINGS));

    /* Code */

    static <T extends Block> T defferedRegister(ResourceLocation identifier, T block, boolean item)
    {
        ALL_BLOCKS.add(block);
        SCHEDULED_BLOCKS.put(identifier, block);
        if(item)
            SCHEDULED_ITEMS.put(identifier, new BlockItem(block, ModItems.DEFAULT_ITEM_SETTINGS));
        return block;
    }

    static <T extends Block> T defferedRegister(ResourceLocation identifier, T block)
    {
        return defferedRegister(identifier, block, true);
    }

    /**
     * Called by {@link ModCommon} to handle late-registering
     */
    /*default*/ static void register()
    {
        /* easy-registry */
            SCHEDULED_BLOCKS.forEach((identifier, block) -> Registry.register(Registry.BLOCK, identifier, block));
            SCHEDULED_BLOCKS.clear();
            SCHEDULED_ITEMS.forEach((identifier, item) -> Registry.register(Registry.ITEM, identifier, item));
            SCHEDULED_ITEMS.clear();
        /* space for compelx registry */
    }

	@Deprecated
    private static ResourceLocation identifier(String path) { return ModCommon.identifier(path); }
}
