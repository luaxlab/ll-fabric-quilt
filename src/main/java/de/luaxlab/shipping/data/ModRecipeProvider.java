/*
 Little Logistics: Quilt Edition Data Generation
 Copyright © 2023 LuaX, Abbie

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
package de.luaxlab.shipping.data;

import de.luaxlab.shipping.common.core.ModBlocks;
import de.luaxlab.shipping.common.core.ModItems;
import de.luaxlab.shipping.common.energy.IntegratedEnergyExtension;
import me.alphamode.forgetags.Tags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {

    public ModRecipeProvider(FabricDataGenerator gen) {
        super(gen);
    }

    @Override
    protected void generateRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(ModBlocks.TUG_DOCK.get(), 2)
                .define('#', ModItems.SPRING.get())
                .define('_', Tags.Items.STONE)
                .define('$', Items.IRON_INGOT)
                .pattern("___")
                .pattern("#_#")
                .pattern("$$$")
                .unlockedBy("has_item", has(ModItems.SPRING.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.BARGE_DOCK.get(), 2)
                .define('#', ModItems.SPRING.get())
                .define('_', Tags.Items.STONE)
                .define('$', Items.IRON_INGOT)
                .pattern("___")
                .pattern("_#_")
                .pattern("$$$")
                .unlockedBy("has_item", has(ModItems.SPRING.get()))
                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModBlocks.VESSEL_DETECTOR.get(), 2)
                .define('#', ModItems.SPRING.get())
                .define('_', Tags.Items.STONE)
                .define('$', Items.REDSTONE_TORCH)
                .pattern("_#_")
                .pattern("_$_")
                .pattern("___")
                .unlockedBy("has_item", has(ModItems.SPRING.get()))
                .save(consumer);
*/


        ShapedRecipeBuilder.shaped(ModBlocks.GUIDE_RAIL_CORNER.get(), 3)
                .define('#', ModItems.SPRING.get())
                .define('_', Tags.Items.STONE)
                .define('$', Items.POWERED_RAIL)
                .pattern("#__")
                .pattern("$__")
                .pattern("#__")
                .unlockedBy("has_item", has(Items.POWERED_RAIL))
                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModBlocks.SWITCH_RAIL.get(), 4)
                .define('#', Items.RAIL)
                .pattern("# ")
                .pattern("##")
                .pattern("# ")
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.TEE_JUNCTION_RAIL.get(), 4)
                .define('#', Items.RAIL)
                .pattern("###")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.JUNCTION_RAIL.get(), 5)
                .define('#', Items.RAIL)
                .pattern(" # ")
                .pattern("###")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModBlocks.AUTOMATIC_SWITCH_RAIL.get(), 1)
                .requires(ModBlocks.SWITCH_RAIL.get())
                .requires(ModItems.RECEIVER_COMPONENT.get())
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(ModBlocks.AUTOMATIC_TEE_JUNCTION_RAIL.get(), 1)
                .requires(ModBlocks.TEE_JUNCTION_RAIL.get())
                .requires(ModItems.RECEIVER_COMPONENT.get())
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.LOCOMOTIVE_DOCK_RAIL.get(), 2)
                .define('#', Items.RAIL)
                .define('$', ModItems.SPRING.get())
                .pattern(" $ ")
                .pattern(" # ")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModBlocks.CAR_DOCK_RAIL.get(), 3)
                .define('#', Items.RAIL)
                .define('$', ModItems.SPRING.get())
                .pattern(" # ")
                .pattern("$#$")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.RAIL))
                .save(consumer);
*/

        ShapedRecipeBuilder.shaped(ModBlocks.GUIDE_RAIL_TUG.get(), 8)
                .define('#', ModItems.SPRING.get())
                .define('_', Tags.Items.STONE)
                .define('$', Items.POWERED_RAIL)
                .pattern("#$#")
                .pattern("___")
                .pattern("___")
                .unlockedBy("has_item", has(Items.POWERED_RAIL))
                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModBlocks.FLUID_HOPPER.get(), 1)
                .define('_', Items.GLASS)
                .define('$', Items.HOPPER)
                .pattern("_$_")
                .pattern(" _ ")
                .unlockedBy("has_item", has(Items.HOPPER))
                .save(consumer);
*/
        ShapedRecipeBuilder.shaped(IntegratedEnergyExtension.VESSEL_CHARGER_BLOCK.get(), 1)
                .define('_', Items.REDSTONE_BLOCK)
                .define('$', Items.IRON_INGOT)
                .define('.', Items.GOLD_INGOT)
                .pattern(" . ")
                .pattern(" $ ")
                .pattern("_$_")
                .unlockedBy("has_item", has(Items.REDSTONE))
                .save(consumer);


        ShapedRecipeBuilder.shaped(ModItems.SPRING.get(), 6)
                .define('_', Tags.Items.STRING)
                .define('$', Items.IRON_NUGGET)
                .pattern("_$_")
                .pattern("$_$")
                .unlockedBy("has_item", has(Items.STRING))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.TUG_ROUTE.get())
                .define('_', ModItems.TRANSMITTER_COMPONENT.get())
                .define('#', Items.REDSTONE)
                .define('$', Items.IRON_NUGGET)
                .pattern(" # ")
                .pattern("$_$")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.REDSTONE))
                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModItems.LOCO_ROUTE.get())
                .define('_', ModItems.TRANSMITTER_COMPONENT.get())
                .define('#', Items.IRON_NUGGET)
                .define('$', Items.REDSTONE)
                .pattern(" # ")
                .pattern("$_$")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.REDSTONE))
                .save(consumer);
*/

        ShapedRecipeBuilder.shaped(ModItems.STEAM_TUG.get())
                .define('_', Items.PISTON)
                .define('#', Items.FURNACE)
                .define('$', Items.IRON_INGOT)
                .pattern(" $ ")
                .pattern("_#_")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.PISTON))
                .save(consumer);


        ShapedRecipeBuilder.shaped(IntegratedEnergyExtension.ENERGY_TUG_ITEM.get())
                .define('_', Items.PISTON)
                .define('#', IntegratedEnergyExtension.VESSEL_CHARGER_BLOCK.get())
                .define('$', Items.IRON_INGOT)
                .pattern(" $ ")
                .pattern("_#_")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.PISTON))
                .save(consumer);


        ShapedRecipeBuilder.shaped(ModItems.CHEST_BARGE.get())
                .define('_', Items.CHEST)
                .define('#', Items.STICK)
                .define('$', Items.IRON_INGOT)
                .pattern("#_#")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.CHEST))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.SEATER_BARGE.get())
                .define('_', ItemTags.WOODEN_STAIRS)
                .define('#', ItemTags.SIGNS)
                .define('$', Items.IRON_INGOT)
                .pattern("#_#")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.FISHING_BARGE.get())
                .define('#', Items.FISHING_ROD)
                .define('$', Items.IRON_INGOT)
                .pattern("###")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.FISHING_ROD))
                .save(consumer);

       /* ShapedRecipeBuilder.shaped(ModItems.FLUID_BARGE.get())
                .define('#', Items.GLASS)
                .define('$', Items.IRON_INGOT)
                .pattern("# #")
                .pattern(" # ")
                .pattern("$$$")
                .unlockedBy("has_item", has(Items.GLASS))
                .save(consumer);
*/

//        ShapedRecipeBuilder.shaped(ModItems.CHUNK_LOADER_BARGE.get())
//                .define('_', Items.ENDER_EYE)
//                .define('#', Items.OBSIDIAN)
//                .define('$', Items.IRON_INGOT)
//                .pattern("#_#")
//                .pattern("$$$")
//                .unlockedBy("has_item", has(Items.ENDER_EYE))
//                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModBlocks.RAPID_HOPPER.get())
                .define('_', Items.HOPPER)
                .define('#', Items.REDSTONE_BLOCK)
                .define('$', Items.GOLD_INGOT)
                .pattern("$_$")
                .pattern(" # ")
                .unlockedBy("has_item", has(Items.HOPPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.SEATER_CAR.get())
                .define('#', ItemTags.PLANKS)
                .define('$', Items.IRON_INGOT)
                .pattern("   ")
                .pattern("###")
                .pattern("$ $")
                .unlockedBy("has_item", has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.CHEST_CAR.get())
                .define('#', Items.CHEST)
                .define('$', ModItems.SEATER_CAR.get())
                .pattern("   ")
                .pattern(" # ")
                .pattern(" $ ")
                .unlockedBy("has_item", has(ModItems.SEATER_CAR.get()))
                .save(consumer);
*/
//
//        ShapedRecipeBuilder.shaped(ModItems.CHUNK_LOADER_CAR.get())
//                .define('#', Items.ENDER_EYE)
//                .define('_', Items.OBSIDIAN)
//                .define('$', ModItems.SEATER_CAR.get())
//                .pattern("   ")
//                .pattern("_#_")
//                .pattern(" $ ")
//                .unlockedBy("has_item", has(ModItems.SEATER_CAR.get()))
//                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModItems.FLUID_CAR.get())
                .define('#', Items.GLASS)
                .define('$', ModItems.SEATER_CAR.get())
                .pattern("# #")
                .pattern(" # ")
                .pattern(" $ ")
                .unlockedBy("has_item", has(ModItems.SEATER_CAR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.ENERGY_LOCOMOTIVE.get())
                .define('#', Items.IRON_INGOT)
                .define('.', ModBlocks.VESSEL_CHARGER.get())
                .define('_', Blocks.PISTON)
                .define('$', ModItems.SEATER_CAR.get())
                .pattern(" # ")
                .pattern("_._")
                .pattern("#$#")
                .unlockedBy("has_item", has(ModItems.SEATER_CAR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.STEAM_LOCOMOTIVE.get())
                .define('#', Items.IRON_INGOT)
                .define('.', Items.FURNACE)
                .define('_', Blocks.PISTON)
                .define('$', ModItems.SEATER_CAR.get())
                .pattern(" # ")
                .pattern("_._")
                .pattern("#$#")
                .unlockedBy("has_item", has(ModItems.SEATER_CAR.get()))
                .save(consumer);
*/

        ShapedRecipeBuilder.shaped(ModItems.RECEIVER_COMPONENT.get(), 8)
                .define('o', Items.ENDER_EYE)
                .define('#', Items.REDSTONE)
                .define('_', Items.STONE_SLAB)
                .pattern("o")
                .pattern("#")
                .pattern("_")
                .unlockedBy("has_item", has(Items.ENDER_EYE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(ModItems.TRANSMITTER_COMPONENT.get(), 4)
                .define('o', Items.ENDER_PEARL)
                .define('#', Items.GLOWSTONE_DUST)
                .define('_', Items.STONE_SLAB)
                .pattern("o")
                .pattern("#")
                .pattern("_")
                .unlockedBy("has_item", has(Items.ENDER_EYE))
                .save(consumer);

/*
        ShapedRecipeBuilder.shaped(ModItems.CONDUCTORS_WRENCH.get(), 1)
                .define('-', Items.IRON_INGOT)
                .define('^', ModItems.SPRING.get())
                .define('r', Items.RED_DYE)
                .pattern("  ^")
                .pattern(" -r")
                .pattern("-  ")
                .unlockedBy("has_item", has(ModItems.SPRING.get()))
                .save(consumer);
*/
    }
}
