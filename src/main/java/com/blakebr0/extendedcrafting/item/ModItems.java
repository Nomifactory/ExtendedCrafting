package com.blakebr0.extendedcrafting.item;

import com.blakebr0.cucumber.registry.ModRegistry;
import com.blakebr0.extendedcrafting.ExtendedCrafting;
public class ModItems {
    
    // 删除这行
    // public static final ItemGuide itemGuide = new ItemGuide("extendedcrafting", ExtendedCrafting.CREATIVE_TAB, ModGuide.GUIDE);

    public static final ItemMaterial itemMaterial = new ItemMaterial();

    public static final ItemSingularity itemSingularity = new ItemSingularity();
    public static final ItemSingularityCustom itemSingularityCustom = new ItemSingularityCustom();
    public static final ItemSingularityUltimate itemSingularityUltimate = new ItemSingularityUltimate();

    public static void init() {
        final ModRegistry registry = ExtendedCrafting.REGISTRY;
        
        // 删除这行
        // registry.register(itemGuide, "guide");

        registry.register(itemMaterial, "material");

        registry.register(itemSingularity, "singularity");
        registry.register(itemSingularityCustom, "singularity_custom");
        registry.register(itemSingularityUltimate, "singularity_ultimate");
    }
}
