package com.csykes.allthesmallthings.items;

import com.csykes.allthesmallthings.ui.CreativeTab;
import net.minecraft.world.item.Item;

public class Sawblade extends Item {
private static final int MAXIMUM_STACK = 64; // maximum stack size

  public Sawblade()
  {
    super(new Item.Properties().stacksTo(MAXIMUM_STACK).tab(CreativeTab.MY_CUSTOM_TAB)
    );
  }
    
}
