package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.cucumber.helper.RenderHelper;
import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.client.container.ContainerUltimateTable;
import net.minecraft.util.ResourceLocation;

public class GuiUltimateTable extends AbstractTableGui {

	public static final ResourceLocation GUI = ResourceHelper.getResource(Tags.MODID, "textures/gui/ultimate_table.png");

	public GuiUltimateTable(ContainerUltimateTable container) {
		super(container, "ultimate", ContainerUltimateTable.GRID_START_X, GUI);
		this.xSize = ContainerUltimateTable.X_SIZE;
		this.ySize = ContainerUltimateTable.Y_SIZE;
	}

	@Override
	protected void drawBackground(int x, int y) {
		RenderHelper.drawTexturedModelRect(x, y, 0, 0, this.xSize, this.ySize, 512, 512);
	}
}
