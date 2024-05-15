package com.csykes.allthesmallthings.blockEntities;

import com.csykes.allthesmallthings.menus.DebarkerMenu;
import com.csykes.allthesmallthings.menus.chipper.ChipperMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChipperBlockEntity extends BlockEntity implements ICapabilityProvider, MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT)
                return super.isItemValid(slot, stack);
            else
                return false;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == OUTPUT_SLOT || !simulate ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
    };
    private final EnergyStorage energyHandler = new EnergyStorage(60000, 256) {};
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.of(() -> itemHandler);
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    private int progress = 0;
    private int maxProgress = TICKS_PER_ITEM;
    private int energy = energyHandler.getEnergyStored();
    private int maxEnergy = energyHandler.getMaxEnergyStored();
    protected final ContainerData data;

    public ChipperBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(_ModBlockEntities.CHIPPER_ENTITY_TYPE.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ChipperBlockEntity.this.progress;
                    case 1 -> ChipperBlockEntity.this.maxProgress;
                    case 2 -> ChipperBlockEntity.this.energy;
                    case 3 -> ChipperBlockEntity.this.maxEnergy;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ChipperBlockEntity.this.progress = value;
                    case 1 -> ChipperBlockEntity.this.maxProgress = value;
                    case 2 -> ChipperBlockEntity.this.energy = value;
                    case 3 -> ChipperBlockEntity.this.maxEnergy = value;
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    public EnergyStorage getEnergyHandler() {
        return this.energyHandler;
    }
    //region Items

    /**
     * Drop Items that are loaded in the block
     */
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        assert this.level != null;
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        super.load(nbt);
    }
//endregion
//region Capabilities
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER)
            return lazyItemHandler.cast();
        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> energyHandler);
    }

//endregion
//region Menu


@Override
public @NotNull Component getDisplayName() {
    return Component.literal("Chipper");
}

@Nullable
@Override
public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory, @NotNull Player player) {
    return new ChipperMenu(id, inventory, this, this.data);
}
//endregion
    public float getProgressFraction() {
        return (float) this.progress / this.maxProgress;
    }
    private static final int ENERGY_REQ = 600;
    private static final int TICKS_PER_ITEM = 10;
    private static final short INPUT_SLOT = 0;
    private static final short OUTPUT_SLOT = 1;
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity) {
        if (pBlockEntity instanceof ChipperBlockEntity db) {
            db.energy = db.energyHandler.getEnergyStored();
            db.maxEnergy = db.energyHandler.getMaxEnergyStored();
            if ((1-(db.progress/db.maxProgress))*ENERGY_REQ <= db.energy ) {
                db.itemHandler.getStackInSlot(OUTPUT_SLOT);
                if (isInputValid(db) && isOutputValid(db)) {
                    db.energyHandler.extractEnergy((ENERGY_REQ/TICKS_PER_ITEM), false);
                    if (db.progress == db.maxProgress) {
                        ItemStack inStack = db.itemHandler.getStackInSlot(INPUT_SLOT);
                        ItemStack outStack = db.itemHandler.getStackInSlot(OUTPUT_SLOT);
                        ItemStack translateLogType = translateLogType(new ItemStack(inStack.getItem(), 1));
                        inStack.setCount(inStack.getCount() - 1);
                        if (!ItemStack.isSame(outStack, translateLogType))
                            db.itemHandler.setStackInSlot(OUTPUT_SLOT, translateLogType);
                        else
                            outStack.setCount(outStack.getCount() + 1);
                        db.progress = 0;
                    }
                    db.progress++;
                }
            }
        }
    }

    public static boolean isInputValid(ChipperBlockEntity db) {
        db.itemHandler.getStackInSlot(INPUT_SLOT);
        return translateLogType(db.itemHandler.getStackInSlot(INPUT_SLOT)).getItem() != Items.AIR;
    }
    public static boolean isOutputValid(ChipperBlockEntity db) {
        ItemStack inSlt = db.itemHandler.getStackInSlot(INPUT_SLOT);
        ItemStack outSlt = db.itemHandler.getStackInSlot(OUTPUT_SLOT);
        return outSlt.getItem() == Items.AIR || translateLogType(inSlt).getItem() == outSlt.getItem() && outSlt.getCount() < outSlt.getMaxStackSize();
    }

    public static ItemStack translateLogType(ItemStack stack) {

        return new ItemStack(Items.AIR, 1);
    }
}
