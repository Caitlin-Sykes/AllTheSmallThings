package com.csykes.allthesmallthings.tileentity;
import com.csykes.allthesmallthings.block.ModBlocks;
import com.csykes.allthesmallthings.AllTheSmallThings;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, AllTheSmallThings.MOD_ID);

    //Registers Tiles
    public static RegistryObject<TileEntityType<DebarkerTile>> DEBARKER_TILE = TILE_ENTITIES.register("debarker_tile", () -> TileEntityType.Builder.create(DebarkerTile::new, ModBlocks.DEBARKER.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
    
}
