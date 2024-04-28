package com.csykes.allthesmallthings.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class InputSlotItemHandler extends SlotItemHandler {
    public InputSlotItemHandler(@NotNull IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean mayPickup(Player player) {
        // Always allow players to take stacks from this slot
        return true;
    }
}
