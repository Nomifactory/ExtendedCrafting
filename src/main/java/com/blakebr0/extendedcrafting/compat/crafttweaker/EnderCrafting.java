package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.endercrafter.EnderCrafterRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Iterator;

import static com.blakebr0.extendedcrafting.compat.crafttweaker.CraftTweakerUtils.toIngredients;
import static crafttweaker.api.minecraft.CraftTweakerMC.getItemStack;

@ZenClass("mods.extendedcrafting.EnderCrafting")
public class EnderCrafting {

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
		addShaped(output, ingredients, ModConfig.confEnderTimeRequired);
	}

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients, int seconds) {
		int rows = ingredients.length;
		int columns = 0;
		for (IIngredient[] row : ingredients)
			if (columns < row.length)
				columns = row.length;

		NonNullList<Ingredient> input = NonNullList.withSize(rows * columns, Ingredient.EMPTY);

		for (int row = 0; row < rows; row++)
			for (int column = 0; column < ingredients[row].length; column++)
				input.set(row * columns + column, CraftTweakerUtils.toIngredient(ingredients[row][column]));

		TableRecipeShaped recipe = new TableRecipeShaped(1, getItemStack(output), columns, rows, input);
		recipe.enderCrafterRecipeTimeRequired = seconds;
		CraftTweakerAPI.apply(new Add(recipe));
	}

	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
		addShapeless(output, ingredients, ModConfig.confEnderTimeRequired);
	}

	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients, int seconds) {
		TableRecipeShapeless recipe = new TableRecipeShapeless(1, getItemStack(output), toIngredients(ingredients));
		recipe.enderCrafterRecipeTimeRequired = seconds;
		CraftTweakerAPI.apply(new Add(recipe));
	}

	@ZenMethod
	public static void remove(IItemStack target) {
		CraftTweakerAPI.apply(new Remove(getItemStack(target)));
	}

	private static class Add implements IAction {
		final IRecipe recipe;

		public Add(IRecipe add) {
			this.recipe = add;
		}

		@Override
		public void apply() {
			EnderCrafterRecipeManager.getInstance().getRecipes().add(this.recipe);
		}

		@Override
		public String describe() {
			return "Adding an Ender Crafting recipe for " + this.recipe.getRecipeOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		final ItemStack remove;

		public Remove(ItemStack remove) {
			this.remove = remove;
		}

		@Override
		public void apply() {
			EnderCrafterRecipeManager.getInstance().removeRecipes(this.remove);
		}

		@Override
		public String describe() {
			return "Removing all Ender Crafting recipes for " + this.remove.getDisplayName();
		}
	}

}