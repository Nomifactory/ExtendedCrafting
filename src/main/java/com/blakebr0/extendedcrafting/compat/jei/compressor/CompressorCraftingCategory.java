package com.blakebr0.extendedcrafting.compat.jei.compressor;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CompressorCraftingCategory implements IRecipeCategory<CompressorCraftingWrapper> {

	public static final String UID = "extendedcrafting:compressor";
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource(Tags.MODID, "textures/jei/compressor.png");

	private final IDrawable background;

	public CompressorCraftingCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 149, 78);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.compressor");
	}
	
	@Override
	public String getModName() {
		return Tags.MODNAME;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}

	@Override
	public void setRecipe(IRecipeLayout layout, CompressorCraftingWrapper wrapper, IIngredients ingredients) {
		IGuiItemStackGroup stacks = layout.getItemStacks();
		
		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, true, 57, 30);
		stacks.init(1, true, 30, 30);
		stacks.init(2, false, 127, 30);

		stacks.set(0, inputs.get(0));
		stacks.set(1, inputs.get(1));
		stacks.set(2, outputs);
	}
}