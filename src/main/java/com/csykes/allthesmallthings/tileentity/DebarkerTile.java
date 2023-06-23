package com.csykes.allthesmallthings.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class DebarkerTile extends TileEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);


    public DebarkerTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public DebarkerTile() {
        this(TileEntities.DEBARKER_TILE.get());
    }

     private ItemStackHandler createHandler() {
        return new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                markDirty();

               if (slot != 1) {
                stripLogs(slot);
               }
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem() == Items.OAK_LOG;
                    case 1: return stack.isEmpty() || stack.getItem() == Items.STRIPPED_OAK_LOG;
                    default:
                        return false;
                }     
        }
        

            /**
             * Limit of items in inventory slot
             * Slot: inventory slot.
             */
            @Override
            public int getSlotLimit(int slot) {
               return 64;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if(!isItemValid(slot, stack)) {
                    return stack;
                }
                
                else {
                    return super.insertItem(slot, stack, simulate);
                }
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                checkItemRemoved(slot, amount);
                return super.extractItem(slot, amount, simulate);
            }
        };

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    /**
     * reads data on world load
     * @param state
     * @param nbt
     */
    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inv"));
        super.read(state, nbt);
    }

    /**
     * writes data on world save
     * @param compound
     */
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("inv", itemHandler.serializeNBT());
        return super.write(compound);
    }

    /**
     * A function to strip logs
     */
    public void stripLogs(int slot) {
        boolean hasUnstrippedLogInSlot = this.itemHandler.getStackInSlot(0).getCount() > 0 && this.itemHandler.getStackInSlot(0).getItem() == Items.OAK_LOG && slot == 0 && itemHandler.getStackInSlot(1).isEmpty();

        //If condition is true
        if (hasUnstrippedLogInSlot) {

            //Removes the number of normal logs, replaces with same number of stripped logs.
            this.itemHandler.insertItem(1, new ItemStack(Items.STRIPPED_OAK_LOG, this.itemHandler.getStackInSlot(0).getCount()), false);
         
          

        }

    }
    /**
     * A function to check if the item is removed
     * @param slot - slot to check
     * @param count - number of items to remove
     */
    private void checkItemRemoved(int slot, int count) {
        
        System.out.println("THE ITEM OF DEATH: " + this.itemHandler.getStackInSlot(1).getItem());
        System.out.println("THE Stackcount OF DEATH: " + this.itemHandler.getStackInSlot(1).getCount());
        System.out.println("THE Stackcountleft OF DEATH: " + this.itemHandler.getStackInSlot(0).getCount());
        System.out.println("THE count OF DEATH: " + count);


        if (slot == 1) {
            this.itemHandler.getStackInSlot(0).shrink(count);
            //(count);
        }
    }

}
