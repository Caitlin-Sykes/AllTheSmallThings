package com.csykes.allthesmallthings;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(AllTheSmallThings.MOD_ID)
public class AllTheSmallThings {
  public static final String MOD_ID = "allthesmallthings";

  public static IEventBus MOD_EVENT_BUS;

  /**
   * Constructor to initialise the event bus and events
   * @deprecated
   */
  @Deprecated
  public AllTheSmallThings() {
    MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
    registerCommonEvents();
    DistExecutor.runWhenOn(Dist.CLIENT, () -> AllTheSmallThings::registerClientOnlyEvents);
  }

  public static void registerCommonEvents() {

    //Item Group Registry
    MOD_EVENT_BUS.register(com.csykes.allthesmallthings.itemgroup.StartupCommon.class);

    //Start of Item Registry
    MOD_EVENT_BUS.register(com.csykes.allthesmallthings.items.sawblade.StartupCommon.class);

    //Start of Block Registry
    MOD_EVENT_BUS.register(com.csykes.allthesmallthings.debarker.StartupCommon.class);


  }

  public static void registerClientOnlyEvents() {
    //Item Group Registry
    MOD_EVENT_BUS.register(com.csykes.allthesmallthings.itemgroup.StartupClientOnly.class);

    //Start of Item Registry

    //Start of Block Registry
    MOD_EVENT_BUS.register(com.csykes.allthesmallthings.debarker.StartupClientOnly.class);

  }

}