package com.csykes.allthesmallthings.items.sawblade;

import com.csykes.allthesmallthings.itemgroup.StartupCommon;
import net.minecraft.item.Item;

public class Sawblade extends Item {
private static final int MAXIMUM_STACK = 64; // maximum stack size

  public Sawblade()
  {
    super(new Item.Properties().maxStackSize(MAXIMUM_STACK).group(StartupCommon.atsmCreativeTab) 
    );
  }
    
}
