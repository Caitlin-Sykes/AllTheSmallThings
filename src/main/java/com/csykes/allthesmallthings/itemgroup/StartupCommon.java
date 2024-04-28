package com.csykes.allthesmallthings.itemgroup;

import net.minecraft.block.Block;

import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StartupCommon
{
  public static ItemGroup atsmCreativeTab; //holds ATSM Creative Tab

  @SubscribeEvent
  public static void onBlocksRegistration(final RegistryEvent.Register<Block> blockRegisterEvent) {
  }

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    atsmCreativeTab = new ItemGroup("allthesmallthingstab") {
      @Override
      public ItemStack createIcon() {
        return new ItemStack(com.csykes.allthesmallthings.items.sawblade.StartupCommon.sawbladeItem);
      }
    };
  }
}
