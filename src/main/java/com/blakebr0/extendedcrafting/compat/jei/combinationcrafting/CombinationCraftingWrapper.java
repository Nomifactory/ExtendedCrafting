package com.blakebr0.extendedcrafting.compat.jei.combinationcrafting;

import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@MethodsReturnNonnullByDefault
public class CombinationCraftingWrapper implements IRecipeWrapper {

	private final ITickTimer timer;
	private final IJeiHelpers helpers;
	private final CombinationRecipe recipe;

	public CombinationCraftingWrapper(IJeiHelpers helpers, CombinationRecipe recipe) {
		this.helpers = helpers;
		this.recipe = recipe;
		int period = Stream.concat(Stream.of(recipe.getInputIngredient()), recipe.getPedestalIngredients().stream())
				.map(Ingredient::getMatchingStacks)
				.mapToInt(a -> a.length)
				.map(a -> Math.max(1, a))
				.reduce(1, (a, b) -> a * b);
		timer = helpers.getGuiHelper()
				.createTickTimer(period * 20, period, false);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		if (mouseX > 1 && mouseX < 14 && mouseY > 9 && mouseY < 86) {
			return Arrays.asList(Utils.format(this.recipe.getCost()) + " FE", Utils.format(this.recipe.getPerTick()) + " FE/t");
		}

		if (mouseX > 5 && mouseX < 23 && mouseY > 144 && mouseY < 165) {
			ArrayList<String> tooltip = new ArrayList<>();
			tooltip.add(Utils.localize("tooltip.ec.items_required"));
			Object2IntLinkedOpenHashMap<String> tooltipCount = new Object2IntLinkedOpenHashMap<>();

			ItemStack[] coreStacks = recipe.getInputIngredient().getMatchingStacks();
			if(coreStacks.length != 0) {
				String line = coreStacks[timer.getValue() % coreStacks.length].getDisplayName();
				tooltipCount.put(line, tooltipCount.getInt(line) + 1);
			}

			for (Ingredient ing : recipe.getPedestalIngredients()) {
				ItemStack[] stacks = ing.getMatchingStacks();
				if(stacks.length == 0) continue;
				String line = stacks[timer.getValue() % stacks.length].getDisplayName();
				tooltipCount.put(line, tooltipCount.getInt(line) + 1);
			}
			tooltipCount.forEach((string, count) ->
					tooltip.add(String.format(" %dx %s", count, string)));
			return tooltip;
		}

		return Collections.emptyList();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		IStackHelper helper = this.helpers.getStackHelper();
		ItemStack output = this.recipe.getOutput();

		List<List<ItemStack>> inputs = new ArrayList<>();
		inputs.add(helper.toItemStackList(this.recipe.getInputIngredient()));
		inputs.addAll(helper.expandRecipeItemStackInputs(this.recipe.getPedestalIngredients()));

		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, output);
	}
}
