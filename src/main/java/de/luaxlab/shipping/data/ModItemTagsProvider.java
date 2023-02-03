package de.luaxlab.shipping.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider) {
        super(dataGenerator, blockTagsProvider);
    }

    protected void addTags() {
//        tag(ModTags.Items.WRENCHES).add(ModItems.CONDUCTORS_WRENCH.get());
    }
}
