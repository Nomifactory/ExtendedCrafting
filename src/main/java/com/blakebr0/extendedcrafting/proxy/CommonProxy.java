package com.blakebr0.extendedcrafting.proxy;

import com.blakebr0.extendedcrafting.ExtendedCrafting;
import com.blakebr0.extendedcrafting.Tags;
import com.blakebr0.extendedcrafting.block.ModBlocks;
import com.blakebr0.extendedcrafting.client.gui.GuiHandler;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CombinationCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.CompressionCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.EnderCrafting;
import com.blakebr0.extendedcrafting.compat.crafttweaker.TableCrafting;
import com.blakebr0.extendedcrafting.config.ModConfig;
import com.blakebr0.extendedcrafting.crafting.ModRecipes;
import com.blakebr0.extendedcrafting.item.ModItems;
import com.blakebr0.extendedcrafting.lib.AddonReferenced;
import com.blakebr0.extendedcrafting.lib.ModGuide;
import com.blakebr0.extendedcrafting.network.NetworkThingy;
import com.blakebr0.extendedcrafting.tile.ModTiles;
import crafttweaker.CraftTweakerAPI;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		ModConfig.init(new File(event.getModConfigurationDirectory(), Tags.MODID + ".cfg"));
		MinecraftForge.EVENT_BUS.register(new ModConfig());

		ModBlocks.init();
		ModItems.init();
		ModTiles.init();

		MinecraftForge.EVENT_BUS.register(ExtendedCrafting.REGISTRY);
		MinecraftForge.EVENT_BUS.register(this);

		if (Loader.isModLoaded("crafttweaker")) {
			CraftTweakerAPI.registerClass(TableCrafting.class);
			CraftTweakerAPI.registerClass(CombinationCrafting.class);
			CraftTweakerAPI.registerClass(CompressionCrafting.class);
			CraftTweakerAPI.registerClass(EnderCrafting.class);
		}

		FMLCommonHandler.instance().registerCrashCallable(new AddonReferenced.CrashCallable());
	}

	public void init(FMLInitializationEvent event) {
		NetworkThingy.init();
		NetworkRegistry.INSTANCE.registerGuiHandler(ExtendedCrafting.instance, new GuiHandler());
		FMLInterModComms.sendMessage("waila", "register", "com.blakebr0.extendedcrafting.compat.WailaDataProvider.callbackRegister");
	}

	public void postInit(FMLPostInitializationEvent event) {
		ModRecipes.post();
		ModGuide.setup();
	}

	@SubscribeEvent
	public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		ModRecipes.init();
	}
}
