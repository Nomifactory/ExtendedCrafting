package com.blakebr0.extendedcrafting.tile;

import com.blakebr0.cucumber.energy.EnergyStorageCustom;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.cucumber.util.VanillaPacketDispatcher;
import com.blakebr0.extendedcrafting.block.BlockPedestal;
import com.blakebr0.extendedcrafting.compat.gregtech.IGTCapHolder;
import com.blakebr0.extendedcrafting.compat.gregtech.IGTEnergyContainer;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipe;
import com.blakebr0.extendedcrafting.crafting.CombinationRecipeManager;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.*;

@Optional.Interface(modid = "gregtech",
		iface = "com.blakebr0.extendedcrafting.compat.gregtech.IGTEnergyContainer")
public class TileCraftingCore extends TileEntity implements ITickable, IGTEnergyContainer, IGTCapHolder {

	private final ItemStackHandler inventory = new StackHandler();
	private final EnergyStorageCustom energy = new EnergyStorageCustom(ModConfig.confCraftingCoreRFCapacity);

	private int progress;
	private int oldEnergy;
	private int pedestalCount;

	private static List<CombinationRecipe> getValidRecipes(ItemStack stack) {
		List<CombinationRecipe> valid = new ArrayList<>();

		if (!stack.isEmpty()) {
			for (CombinationRecipe recipe : CombinationRecipeManager.getInstance().getRecipes()) {
				if (recipe.getInputIngredient().apply(stack)) {
					valid.add(recipe);
				}
			}
		}

		return valid;
	}

	@Override
	public void update() {
		boolean mark = false;

		List<BlockPos> pedestalLocations = this.locatePedestals();

		if (!this.getWorld().isRemote) {
			CombinationRecipe recipe = this.getRecipe();
			if (recipe != null && this.getEnergy().getEnergyStored() > 0) {
				List<TilePedestal> pedestals = this.getPedestalsWithStuff(recipe, pedestalLocations);
				boolean done = this.process(recipe);
				mark = true;
				if (done) {
					for (TilePedestal pedestal : pedestals) {
						IItemHandlerModifiable inventory = pedestal.getInventory();
						inventory.setStackInSlot(0, StackHelper.decrease(inventory.getStackInSlot(0), 1, true));
						pedestal.markDirty();
						((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, pedestal.getPos().getX() + 0.5D, pedestal.getPos().getY() + 1.1D, pedestal.getPos().getZ() + 0.5D, 20, 0, 0, 0, 0.1D);
					}
					((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.END_ROD, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 50, 0, 0, 0, 0.1D);
					this.getInventory().setStackInSlot(0, recipe.getOutput().copy());
					this.progress = 0;
				} else {
					((WorldServer) this.getWorld()).spawnParticle(EnumParticleTypes.SPELL, false, this.getPos().getX() + 0.5D, this.getPos().getY() + 1.1D, this.getPos().getZ() + 0.5D, 2, 0, 0, 0, 0.1D);
				}
			}
		}

		if (this.oldEnergy != this.energy.getEnergyStored()) {
			this.oldEnergy = this.energy.getEnergyStored();
			mark = true;
		}

		if (mark) {
			this.markDirty();
		}
	}

	private List<BlockPos> locatePedestals() {
		ArrayList<BlockPos> pedestals = new ArrayList<>();
		Iterable<BlockPos> blocks = BlockPos.getAllInBox(this.getPos().add(-3, 0, -3), this.getPos().add(3, 0, 3));

		for (BlockPos aoePos : blocks) {
			Block block = this.getWorld().getBlockState(aoePos).getBlock();
			if (block instanceof BlockPedestal) {
				pedestals.add(aoePos);
			}
		}

		this.pedestalCount = pedestals.size();

		return pedestals;
	}

	private List<TilePedestal> getPedestalsWithStuff(CombinationRecipe recipe, List<BlockPos> locations) {
		List<Ingredient> remaining = new LinkedList<>(recipe.getPedestalIngredients());
		List<TilePedestal> pedestals = new ArrayList<>();

		if (locations.isEmpty()) return Collections.emptyList();

		for (BlockPos pos : locations) {
			TileEntity tile = this.getWorld().getTileEntity(pos);
			if (tile instanceof TilePedestal) {
				TilePedestal pedestal = (TilePedestal) tile;
				for (Iterator<Ingredient> it = remaining.iterator(); it.hasNext(); ) {
					Ingredient i = it.next();
					ItemStack stack = pedestal.getInventory().getStackInSlot(0);

					if (i.apply(stack)) {
						pedestals.add(pedestal);
						it.remove();
						break;
					}
				}
			}
		}

		if (pedestals.size() != recipe.getPedestalIngredients().size())
			return Collections.emptyList();

		if (!remaining.isEmpty()) return Collections.emptyList();

		return pedestals;
	}

	private boolean process(CombinationRecipe recipe) {
		int extract = recipe.getPerTick();
		long difference = recipe.getCost() - this.progress;
		if (difference < recipe.getPerTick()) {
			extract = (int) difference;
		}

		int extracted = this.getEnergy().extractEnergy(extract, false);
		this.progress += extracted;

		return this.progress >= recipe.getCost();
	}

	@Nullable
	public CombinationRecipe getRecipe() {
		return getRecipe(locatePedestals());
	}

	@Nullable
	public CombinationRecipe getRecipe(List<BlockPos> locations) {
		List<CombinationRecipe> recipes = getValidRecipes(this.getInventory().getStackInSlot(0));

		if (!recipes.isEmpty()) {
			for (CombinationRecipe recipe : recipes) {
				if (recipe.getPedestalIngredients().isEmpty())
					return recipe;
				List<TilePedestal> pedestals = this.getPedestalsWithStuff(recipe, locations);
				if (!pedestals.isEmpty())
					return recipe;
			}
		}

		return null;
	}

	public int getProgress() {
		return this.progress;
	}

	public IItemHandlerModifiable getInventory() {
		return this.inventory;
	}

	@Override
	public EnergyStorageCustom getEnergy() {
		return this.energy;
	}

	public int getPedestalCount() {
		return this.pedestalCount;
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.inventory.deserializeNBT(tag);
		this.progress = tag.getInteger("Progress");
		this.energy.setEnergy(tag.getInteger("Energy"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) {
		tag = super.writeToNBT(tag);
		tag.merge(this.inventory.serializeNBT());
		tag.setInteger("Progress", this.progress);
		tag.setInteger("Energy", this.energy.getEnergyStored());

		return tag;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.getPos(), -1, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public final NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		return this.getCapability(capability, side) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.inventory);
		} else if (capability == CapabilityEnergy.ENERGY) {
			return CapabilityEnergy.ENERGY.cast(this.energy);
		} else if (ModConfig.confCraftingCoreAcceptGTEU) {
			T cap = getGTCapability(capability);
			if (cap != null) {
				return cap;
			}
		}

		return super.getCapability(capability, side);
	}

	public boolean isUseableByPlayer(EntityPlayer player) {
		return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5, 0.5, 0.5)) <= 64;
	}

	class StackHandler extends ItemStackHandler {
		StackHandler() {
			super(1);
		}

		@Override
		protected int getStackLimit(int slot, ItemStack stack) {
			return 1;
		}

		@Override
		public void onContentsChanged(int slot) {
			TileCraftingCore.this.markDirty();
		}
	}
}
