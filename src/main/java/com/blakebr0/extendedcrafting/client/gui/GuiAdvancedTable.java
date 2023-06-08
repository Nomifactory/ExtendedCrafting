package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.client.container.ContainerAdvancedTable;
import net.minecraft.util.ResourceLocation;

public class GuiAdvancedTable extends AbstractTableGui {

	public static final ResourceLocation GUI = ResourceHelper.getResource(Tags.MODID, "textures/gui/advanced_table.png");

	public GuiAdvancedTable(ContainerAdvancedTable container) {
		super(container, "advanced", ContainerAdvancedTable.GRID_START_X, GUI);
		this.xSize = ContainerAdvancedTable.X_SIZE;
		this.ySize = ContainerAdvancedTable.Y_SIZE;
	}

}
