package com.blakebr0.extendedcrafting.compat.jei.endercrafter;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapedWrapper;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EnderCrafterCategory implements IRecipeCategory<IRecipeWrapper> {

	public static final String UID = "extendedcrafting:ender_crafting";
	private static final ResourceLocation TEXTURE = ResourceHelper.getResource(ExtendedCrafting.MOD_ID, "textures/jei/ender_crafting.png");

	private final IDrawable background;
	private final IDrawableAnimated arrow;

	public EnderCrafterCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEXTURE, 0, 0, 116, 54);
		
		IDrawableStatic arrowDrawable = helper.createDrawable(TEXTURE, 195, 0, 24, 17);
		this.arrow = helper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return Utils.localize("jei.ec.ender_crafting");
	}

	@Override
	public String getModName() {
		return ExtendedCrafting.NAME;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		this.arrow.draw(minecraft, 61, 19);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
		// Dimension N of the NxN square crafting grid
		final int GRID_N = 3;
		// Length in pixels of square cells in the NxN crafting grid
		final int CELL_PX = 18;

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		final IGuiItemStackGroup stackGroup = layout.getItemStacks();
		stackGroup.init(0, false, 94, 18);
		stackGroup.set(0, outputs);

		for (int y = 0; y < GRID_N; y++)
			for (int x = 0; x < GRID_N; x++)
				stackGroup.init(1 + x + y * GRID_N,
				                true, x * CELL_PX, y * CELL_PX);

		if (wrapper instanceof TableShapedWrapper) {
			TableShapedWrapper recipe = (TableShapedWrapper) wrapper;
			int inputIndex = 0;
			for (int row = 0; row < recipe.getHeight(); row++)
				for (int column = 0; column < recipe.getWidth(); column++)
					stackGroup.set(1 + column + row * GRID_N, inputs.get(inputIndex++));
		}
		else {
			for (int index = 0; index < inputs.size(); index++)
				stackGroup.set(index + 1, inputs.get(index));
			layout.setShapeless();
		}

		layout.setRecipeTransferButton(122, 41);
	}
}
