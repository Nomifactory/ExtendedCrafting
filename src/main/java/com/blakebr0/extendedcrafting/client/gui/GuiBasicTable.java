package com.blakebr0.extendedcrafting.client.gui;

import com.blakebr0.cucumber.helper.ResourceHelper;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.client.container.ContainerBasicTable;
import net.minecraft.util.ResourceLocation;

public class GuiBasicTable extends AbstractTableGui {

	public static final ResourceLocation GUI = ResourceHelper.getResource(Tags.MODID, "textures/gui/basic_table.png");

	public GuiBasicTable(ContainerBasicTable container) {
		super(container, "basic", ContainerBasicTable.GRID_START_X, GUI);
		this.xSize = ContainerBasicTable.X_SIZE;
		this.ySize = ContainerBasicTable.Y_SIZE;
	}

}
