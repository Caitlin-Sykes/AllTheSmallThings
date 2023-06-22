package com.csykes.allthesmallthings.container;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import com.csykes.allthesmallthings.AllTheSmallThings;

public class ModContainer {
    public static DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS,
            AllTheSmallThings.MOD_ID);

    /**
     *  Registers the container for the debarker
     * */
    public static final RegistryObject<ContainerType<DebarkerContainer>> DEBARKER_CONTAINER = CONTAINERS.register(
            "debarker_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new DebarkerContainer(windowId, world, pos, inv, inv.player);
            })));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
