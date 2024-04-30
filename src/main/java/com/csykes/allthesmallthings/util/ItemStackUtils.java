package com.csykes.allthesmallthings.util;

import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemStackUtils {

    /**
     * Converts a NonNullList of ItemStacks to a ComputerCraft MethodResult.
     * @param items NonNullList of ItemStacks to convert.
     * @return MethodResult containing the details of each ItemStack.
     */
    public static MethodResult convertToMethodResult(NonNullList<ItemStack> items) {
        List<Map<String, Object>> itemList = new ArrayList<>();

        for (ItemStack item : items) {
            if (!item.isEmpty()) {
                Map<String, Object> itemDetails = new HashMap<>();
                itemDetails.put("item", item.getItem().getDescriptionId());
                itemDetails.put("count", item.getCount());
                itemDetails.put("maxStackSize", item.getMaxStackSize());

                // Add more item properties as needed
                if (item.hasTag()) {
                    itemDetails.put("tag", item.getTag().toString());
                }

                itemList.add(itemDetails);
            }
        }

        return MethodResult.of(itemList);
    }
}

