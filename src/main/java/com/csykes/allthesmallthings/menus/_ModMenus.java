package com.csykes.allthesmallthings.menus;

import com.csykes.allthesmallthings.AllTheSmallThings;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.RegistryObject;

public class _ModMenus {
    public static final RegistryObject<MenuType<DebarkerMenu>> DEBARKER = AllTheSmallThings.MENUS.register("debarker_menu",
            () -> IForgeMenuType.create(DebarkerMenu::new));
    public static void register(IEventBus modEventBus) {
        // This method can be empty. It's used to ensure that the class is loaded and the static initializers are run.
    }
}
