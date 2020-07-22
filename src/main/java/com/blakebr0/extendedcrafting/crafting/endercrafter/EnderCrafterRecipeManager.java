package com.blakebr0.extendedcrafting.crafting.endercrafter;

import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.table.ITieredRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableCrafting;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import com.blakebr0.extendedcrafting.lib.AddonReferenced;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.ArrayList;
import java.util.List;

@AddonReferenced
public class EnderCrafterRecipeManager {

	private static final EnderCrafterRecipeManager INSTANCE = new EnderCrafterRecipeManager();
	private final List<IRecipe> recipes = new ArrayList<>();

	@AddonReferenced
	public static EnderCrafterRecipeManager getInstance() {
		return INSTANCE;
	}

	public void addShaped(ItemStack result, int time, CraftingHelper.ShapedPrimer primer) {
		addShaped(result, time, primer.input);
	}

	public void addShaped(ItemStack result, int time, NonNullList<Ingredient> recipe) {
		TableRecipeShaped craft = new TableRecipeShaped(1, result, 3, 3, recipe);

		if (ModConfig.confEnderEnabled) {
			craft.enderCrafterRecipeTimeRequired = time;
			this.recipes.add(craft);
		}
	}

	public void addShapeless(ItemStack result, int time, NonNullList<Ingredient> ingredients) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(1, result, ingredients);

		if (ModConfig.confEnderEnabled) {
			recipe.enderCrafterRecipeTimeRequired = time;
			this.recipes.add(recipe);
		}
	}

	public IEnderCraftingRecipe findMatchingRecipe(TableCrafting grid, World world) {
		for (IRecipe iRecipe : this.recipes) {
			if (iRecipe.matches(grid, world)) {
				return (IEnderCraftingRecipe) iRecipe;
			}
		}

		return null;
	}

	@AddonReferenced
	public List<IRecipe> getRecipes() {
		return this.recipes;
	}

	public void removeRecipes(ItemStack stack) {
		this.recipes.removeIf(o -> o != null && o.getRecipeOutput().isItemEqual(stack));
	}

	@AddonReferenced
	public List<IRecipe> getRecipes(int size) {
		List<IRecipe> recipes = new ArrayList<>();
		for (IRecipe recipe : getRecipes()) {
			if (recipe.canFit(size, size)) {
				recipes.add(recipe);
			}
		}

		return recipes;
	}

	/**
	 * Gets all the recipes for the specified tier Basic is tier 1, Advanced
	 * tier 2, etc
	 *
	 * @param tier the tier of the recipe
	 * @return a list of recipes for this tier
	 */
	public List<IRecipe> getRecipesTiered(int tier) {
		List<IRecipe> recipes = new ArrayList<>();
		for (Object o : getRecipes()) {
			if (o instanceof ITieredRecipe) {
				ITieredRecipe recipe = (ITieredRecipe) o;
				if (recipe.getTier() == tier) {
					recipes.add(recipe);
				}
			}
		}

		return recipes;
	}
}
