package com.blakebr0.extendedcrafting.compat.crafttweaker;

import com.blakebr0.extendedcrafting.crafting.table.ITieredRecipe;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeManager;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShaped;
import com.blakebr0.extendedcrafting.crafting.table.TableRecipeShapeless;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

@ZenClass("mods.extendedcrafting.TableCrafting")
public class TableCrafting {

	@ZenMethod
	public static void addShaped(IItemStack output, IIngredient[][] ingredients) {
		addShaped(0, output, ingredients);
	}

	@ZenMethod
	public static void addShaped(int tier, IItemStack output, IIngredient[][] ingredients) {
		CraftTweakerAPI.apply(new Add(makeShaped(tier, output, ingredients)));
	}

	@ZenMethod
	public static void addShapedMirrored(IItemStack output, IIngredient[][] ingredients) {
		addShapedMirrored(0, output, ingredients);
	}

	@ZenMethod
	public static void addShapedMirrored(int tier, IItemStack output, IIngredient[][] ingredients) {
		TableRecipeShaped recipe = makeShaped(tier, output, ingredients);

		CraftTweakerAPI.apply(new Add(recipe.setMirrored(true)));
	}

	private static TableRecipeShaped makeShaped(int tier, IItemStack output, IIngredient[][] ingredients) {
		TableRecipeShaped recipe;
		{
			if (tier > 4 || tier < 0) {
				CraftTweakerAPI.getLogger().logError("Unable to assign a tier to the Table Recipe for stack " + output.getDisplayName() + ". Tier cannot be greater than 4 or less than 0.");
				tier = 0;
			}

			int rows = ingredients.length;
			int columns = 0;
			for (IIngredient[] row : ingredients)
				if (columns < row.length)
					columns = row.length;

			NonNullList<Ingredient> input = NonNullList.withSize(rows * columns, Ingredient.EMPTY);
			Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < ingredients[row].length; column++) {
					IIngredient iing = ingredients[row][column];
					Ingredient ing = CraftTweakerUtils.toIngredient(iing);
					input.set(row * columns + column, ing);
					if (ing != Ingredient.EMPTY && iing.hasNewTransformers()) {
						transformers.put(row * columns + column, stack -> {
							IItemStack istack = iing.applyNewTransform(CraftTweakerMC.getIItemStack(stack));
							return CraftTweakerMC.getItemStack(istack);
						});
					}
				}
			}

			recipe = new TableRecipeShaped(tier, CraftTweakerMC.getItemStack(output), columns, rows, input).withTransformers(transformers);
		}
		return recipe;
	}


	@ZenMethod
	public static void addShapeless(IItemStack output, IIngredient[] ingredients) {
		addShapeless(0, output, ingredients);
	}

	@ZenMethod
	public static void addShapeless(int tier, IItemStack output, IIngredient[] ingredients) {
		if (tier > 4 || tier < 0) {
			CraftTweakerAPI.getLogger().logError("Unable to assign a tier to the Table Recipe for stack " + output.getDisplayName() + ". Tier cannot be greater than 4 or less than 0.");
			tier = 0;
		}

		NonNullList<Ingredient> input = NonNullList.withSize(ingredients.length, Ingredient.EMPTY);
		Map<Integer, Function<ItemStack, ItemStack>> transformers = new HashMap<>();

		for (int i = 0; i < ingredients.length; i++) {
			IIngredient iing = ingredients[i];
			Ingredient ing = CraftTweakerUtils.toIngredient(iing);
			input.set(i, ing);
			if (ing != Ingredient.EMPTY && iing.hasNewTransformers()) {
				transformers.put(i, stack -> {
					IItemStack istack = iing.applyNewTransform(CraftTweakerMC.getIItemStack(stack));
					return CraftTweakerMC.getItemStack(istack);
				});
			}
		}

		CraftTweakerAPI.apply(new Add(new TableRecipeShapeless(tier, CraftTweakerMC.getItemStack(output), input).withTransforms(transformers)));
	}

	@ZenMethod
	public static void remove(IItemStack target) {
		CraftTweakerAPI.apply(new Remove(CraftTweakerMC.getItemStack(target)));
	}

	private static class Add implements IAction {
		final ITieredRecipe recipe;

		public Add(ITieredRecipe add) {
			this.recipe = add;
		}

		@Override
		public void apply() {
			TableRecipeManager.getInstance().getRecipes().add(this.recipe);
		}

		@Override
		public String describe() {
			return "Adding a Table Crafting recipe for " + this.recipe.getRecipeOutput().getDisplayName();
		}
	}

	private static class Remove implements IAction {
		final ItemStack remove;

		public Remove(ItemStack remove) {
			this.remove = remove;
		}

		@Override
		public void apply() {
			TableRecipeManager.getInstance().removeRecipes(this.remove);
		}

		@Override
		public String describe() {
			return "Removing all Table Crafting recipes for " + this.remove.getDisplayName();
		}
	}
}