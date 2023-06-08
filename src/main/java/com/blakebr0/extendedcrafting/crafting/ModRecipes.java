package com.blakebr0.extendedcrafting.crafting;

import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.item.ItemMaterial;
import com.blakebr0.extendedcrafting.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;

public class ModRecipes {

	public static void init() {
		TableRecipeManager.getInstance().addShaped(ItemMaterial.itemCrystaltineIngot,
				CraftingHelper.parseShaped(
						"DLLLLLD",
						"DNIGIND",
						"DNIGIND",
						"DLLLLLD",

						'D', "gemDiamond",
						'L', new ItemStack(Items.DYE, 1, 4),
						'N', ItemMaterial.itemNetherStarNugget,
						'I', "ingotIron",
						'G', "ingotGold"
				)
		);

		EnderCrafterRecipeManager.getInstance().addShaped(ItemMaterial.itemEnderStar, ModConfig.confEnderTimeRequired,
				CraftingHelper.parseShaped(
						" E ",
						"ENE",
						" E ",

						'E', Items.ENDER_EYE,
						'N', Items.NETHER_STAR
				)
		);

		EnderCrafterRecipeManager.getInstance().addShaped(new ItemStack(ModItems.itemMaterial, 4, 48), ModConfig.confEnderTimeRequired,
				CraftingHelper.parseShaped(
						" I ",
						"INI",
						" I ",

						'I', ItemMaterial.itemEnderIngot,
						'N', ItemMaterial.itemEnderStar
				)
		);

		ModItems.itemSingularity.initRecipes();
		ModItems.itemSingularityUltimate.initRecipe();
		ModItems.itemSingularityCustom.initRecipes();

		// test recipes
		/*
		if (Loader.isModLoaded("mcp")) {
			CombinationRecipeManager.getInstance().addRecipe(new ItemStack(Items.DIAMOND), 50000, 100,

					Ingredient.fromItem(Items.COAL),

					NonNullList.from(Ingredient.EMPTY,

							Ingredient.fromItem(Items.STICK),
							Ingredient.fromItem(Items.LAVA_BUCKET),
							new OreIngredient("ingotIron"),
							Ingredient.fromItem(Items.WHEAT),
							Ingredient.fromItem(Items.WHEAT),
							Ingredient.fromItem(Items.WHEAT),
							new OreIngredient("dyeRed"),
							Ingredient.fromItem(Items.WHEAT)
					)
			);

			TableRecipeManager.getInstance().addShaped(new ItemStack(Items.DIAMOND),
					CraftingHelper.parseShaped(
							"XXXXXXXXX",
							"XXXXXXXXX",
							"XXXXXXXXX",
							"XXXXXXXXX",
							"XXXXXXXXX",

							'X', new ItemStack(Items.DIAMOND)
					)
			);

			TableRecipeManager.getInstance().addShaped(4, new ItemStack(Items.CARROT),
					CraftingHelper.parseShaped(
							"X",

							'X', new ItemStack(Items.WATER_BUCKET))
			);

			TableRecipeManager.getInstance().addShapeless(new ItemStack(Items.ACACIA_DOOR),
					NonNullList.withSize(50, Ingredient.fromItem(Items.COAL)));

			TableRecipeManager.getInstance().addShapeless(1, new ItemStack(Items.APPLE),

					NonNullList.withSize(1, Ingredient.fromItem(Items.BEETROOT))
			);

			CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
					Ingredient.fromItem(Items.IRON_INGOT), 64,
					Ingredient.fromItem(Items.COAL), false,
					1234567);

			CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
					Ingredient.fromItem(Items.IRON_INGOT), 100,
					Ingredient.EMPTY, false,
					100000);

			CompressorRecipeManager.getInstance().addRecipe(new ItemStack(ModItems.itemSingularity, 1, 2),
					Ingredient.fromItem(Items.IRON_INGOT), 2000,
					Ingredient.fromItem(Items.DIAMOND), false,
					100000);

			CompressorRecipeManager.getInstance().addRecipe(new ItemStack(Blocks.ACACIA_FENCE),
					new OreIngredient("sand"), 50,
					Ingredient.fromStacks(ItemMaterial.itemUltimateCatalyst), false,
					1000000);
		}
		 */
	}

}
