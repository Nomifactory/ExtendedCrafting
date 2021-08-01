package com.blakebr0.extendedcrafting.block;

import com.blakebr0.cucumber.block.BlockBase;
import com.blakebr0.cucumber.iface.IModelHelper;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class BlockLamp extends BlockBase implements IModelHelper {
	
	public static final PropertyEnum<Type> VARIANT = PropertyEnum.create("variant", Type.class);

	public BlockLamp() {
		super("ec.lamp", Material.IRON, SoundType.METAL, 5.0F, 10.0F);
		this.setCreativeTab(ExtendedCrafting.CREATIVE_TAB);
		this.setLightLevel(1.0F);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Nonnull
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(VARIANT).getMetadata();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> stacks) {
		for (Type type : Type.values()) {
			stacks.add(new ItemStack(this, 1, type.getMetadata()));
		}
	}

	@Override
	public void initModels() {
		for (Type type : Type.values()) {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), type.getMetadata(), new ModelResourceLocation(Objects.requireNonNull(getRegistryName()).toString() + "_" + Type.byMetadata(type.getMetadata()).getName()));
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(VARIANT, Type.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMetadata();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, VARIANT);
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		int meta = stack.getMetadata();
		if (meta < 2) {
			tooltip.add(Utils.localize("tooltip.ec.lamp_" + Type.byMetadata(meta).getName()));
		}
	}

	public enum Type implements IStringSerializable {

		GLOWSTONE(0, "glowstone"),
		LUMINESSENCE(1, "luminessence");

		private static final Type[] META_LOOKUP = new Type[values().length];
		private final int meta;
		private final String name;

		Type(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMetadata() {
			return this.meta;
		}

		@Override
		public String getName() {
			return this.name;
		}

		public static Type byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}
			return META_LOOKUP[meta];
		}

		static {
			for (Type type : values()) {
				META_LOOKUP[type.getMetadata()] = type;
			}
		}
	}
}
