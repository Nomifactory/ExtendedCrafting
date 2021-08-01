package com.blakebr0.extendedcrafting.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class ItemBlockStorage extends ItemBlock {

	public ItemBlockStorage(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey() + "_" + BlockStorage.Type.byMetadata(stack.getMetadata()).getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return stack.getMetadata() == 4 || stack.getMetadata() == 6 || stack.getMetadata() == 7 ? EnumRarity.EPIC : EnumRarity.COMMON;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == 4 || stack.getMetadata() == 6 || stack.getMetadata() == 7;
	}
}
