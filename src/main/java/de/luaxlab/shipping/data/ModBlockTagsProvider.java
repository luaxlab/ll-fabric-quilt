package de.luaxlab.shipping.data;

import de.luaxlab.shipping.common.core.ModCommon;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import de.luaxlab.shipping.common.core.ModBlocks;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void addTags() {
/*
        tag(BlockTags.RAILS).add(ModBlocks.SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_SWITCH_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.JUNCTION_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.LOCOMOTIVE_DOCK_RAIL.get());
        tag(BlockTags.RAILS).add(ModBlocks.CAR_DOCK_RAIL.get());
*/
    }
}
