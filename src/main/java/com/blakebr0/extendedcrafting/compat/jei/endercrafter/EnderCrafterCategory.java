package com.blakebr0.extendedcrafting.compat.jei.endercrafter;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapedWrapper;
import com.blakebr0.extendedcrafting.compat.jei.tablecrafting.TableShapelessWrapper;
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
		IGuiItemStackGroup stacks = layout.getItemStacks();

		List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
		List<ItemStack> outputs = ingredients.getOutputs(VanillaTypes.ITEM).get(0);

		stacks.init(0, false, 94, 18);
		stacks.set(0, outputs);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				int index = 1 + j + (i * 3);
				stacks.init(index, true, j * 18, i * 18);
			}
		}

		if (wrapper instanceof TableShapedWrapper) {
			TableShapedWrapper shaped = (TableShapedWrapper) wrapper;

			int stackIndex = 0;
			for (int i = 0; i < shaped.getHeight(); i++) {
				for (int j = 0; j < shaped.getWidth(); j++) {
					int index = 1 + (i * 3) + j;

					stacks.set(index, inputs.get(stackIndex));

					stackIndex++;
				}
			}
		} else if (wrapper instanceof TableShapelessWrapper) {
			int i = 1;
			for (List<ItemStack> stack : inputs) {
				stacks.set(i, stack);
				i++;
			}
			layout.setShapeless();
		}

		layout.setRecipeTransferButton(122, 41);
	}
}
