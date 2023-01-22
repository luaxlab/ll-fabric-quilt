package de.luaxlab.shipping.common.core;

import de.luaxlab.shipping.common.block.dock.BargeDockTileEntity;
import de.luaxlab.shipping.common.block.dock.TugDockTileEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {

	public static BlockEntityType<TugDockTileEntity> TUG_DOCK;
	public static BlockEntityType<BargeDockTileEntity> BARGE_DOCK;

    /**
     * Called by {@link ModCommon} to handle late-registering
     */
    static void register()
    {
		TUG_DOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier("tug_dock"), FabricBlockEntityTypeBuilder.create(TugDockTileEntity::new, ModBlocks.TUG_DOCK).build(null));
		BARGE_DOCK = Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier("barge_dock"), FabricBlockEntityTypeBuilder.create(BargeDockTileEntity::new, ModBlocks.BARGE_DOCK).build(null));
	}

	@Deprecated
    private static ResourceLocation identifier(String path) { return ModCommon.identifier(path); }
}
