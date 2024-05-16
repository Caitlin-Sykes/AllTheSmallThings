package com.csykes.allthesmallthings.integrations.jei;

import com.csykes.allthesmallthings.AllTheSmallThings;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = AllTheSmallThings.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
@JeiPlugin
public class jeiplugin implements IModPlugin {
    private static final ResourceLocation PLUGIN_UID = new ResourceLocation(AllTheSmallThings.MOD_ID, "jei_recipe_dumper");

    @Override
    public ResourceLocation getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
//        RecipeManager recipeManager = registration.getJeiHelpers();
//        System.out.println("Chipped Recipes: " + recipeNames);
    }
}
