package com.csykes.allthesmallthings.itemgroup;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * The Startup classes for this example are called during startup, in the following order:
 *  See MinecraftByExample class for more information
 */
public class StartupClientOnly
{
  /**
   * Tell the renderer this is a solid block
   * @param event
   */
  @SubscribeEvent
  public static void onClientSetupEvent(FMLClientSetupEvent event) {
  }
}