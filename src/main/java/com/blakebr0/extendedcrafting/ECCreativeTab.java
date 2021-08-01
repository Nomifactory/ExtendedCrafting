package com.blakebr0.extendedcrafting;

import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.item.ModItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ECCreativeTab extends CreativeTabs {

	public ECCreativeTab() {
		super(ExtendedCrafting.MOD_ID);
	}

	@Nonnull
	@Override
	public ItemStack createIcon() {
		return ModConfig.confCraftingCoreEnabled ? new ItemStack(ModBlocks.blockCraftingCore) : new ItemStack(ModItems.itemMaterial);
	}
}
