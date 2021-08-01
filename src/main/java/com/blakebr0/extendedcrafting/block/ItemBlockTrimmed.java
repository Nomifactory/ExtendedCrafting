package com.blakebr0.extendedcrafting.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

@MethodsReturnNonnullByDefault
public class ItemBlockTrimmed extends ItemBlock {

	public ItemBlockTrimmed(Block block) {
		super(block);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
	}

	@Override
	public String getTranslationKey(ItemStack stack) {
		return super.getTranslationKey() + "_" + BlockTrimmed.Type.byMetadata(stack.getMetadata()).getName();
	}

	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == 5;
	}
}
