package com.csykes.allthesmallthings.items.sawblade;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class StartupCommon {
    public static Sawblade sawbladeItem;  // this holds the unique instance of your block

  @SubscribeEvent
  public static void onItemsRegistration(final RegistryEvent.Register<Item> itemRegisterEvent) {
    sawbladeItem = new Sawblade();
    sawbladeItem.setRegistryName("sawblade");
    itemRegisterEvent.getRegistry().register(sawbladeItem);
  }
}
