package com.csykes.allthesmallthings.debarker;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;

/**
 * Tile entity that converts normal logs to stripped ones
 */
public class DebarkerTileEntity extends TileEntity implements INamedContainerProvider, ITickableTileEntity, IEnergyStorage,  INBTSerializable<CompoundNBT>  {
	  public static final int NUMBER_OF_SLOTS = 2;

    public static final int MAX_ENERGY_STORED = 1000; // Max energy machine can store
    public int ENERGY_STORED; // Current energy stored
    public static int ENERGY_COST = 100; // Energy cost for each run of the machine
    private static int TICKS_CONVERSION = 50; // How long it waits per cycle of ticks
    private static int TICKS_COUNTER = 0; // current tick counter

	public DebarkerTileEntity()
	{
    super(StartupCommon.debarkerTileEntity);
    chestContents = DebarkerContents.createForTileEntity(NUMBER_OF_SLOTS,
            this::canPlayerAccessInventory, this::markDirty);
            ENERGY_STORED = 1000;
	}

	// Return true if the given player is able to use this block. In this case it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	public boolean canPlayerAccessInventory(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	private static final String CHESTCONTENTS_INVENTORY_TAG = "contents";

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the chestContents, which contains the ItemStacks stored in the chest
	@Override
	public CompoundNBT write(CompoundNBT parentNBTTagCompound)
	{
		super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
    CompoundNBT inventoryNBT = chestContents.serializeNBT();
    parentNBTTagCompound.put(CHESTCONTENTS_INVENTORY_TAG, inventoryNBT);
    parentNBTTagCompound.putInt("energyStored", ENERGY_STORED); // Save energy

		return parentNBTTagCompound;
	}

	// This is where you load the data that you saved in write
	@Override
	public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
	{
		super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
    CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(CHESTCONTENTS_INVENTORY_TAG);
    this.ENERGY_STORED = parentNBTTagCompound.getInt("energyStored"); // Load energy
    chestContents.deserializeNBT(inventoryNBT);
    if (chestContents.getSizeInventory() != NUMBER_OF_SLOTS)
      throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
	}

  // When the world loads from disk, the server needs to send the TileEntity information to the client
  @Override
  @Nullable
  public SUpdateTileEntityPacket getUpdatePacket()
  {
    CompoundNBT nbtTagCompound = new CompoundNBT();
    write(nbtTagCompound);
    int tileEntityType = 42;  // arbitrary number; only used for vanilla TileEntities.  You can use it, or not, as you want.
    return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
  }


  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    BlockState blockState = world.getBlockState(pos);
    read(blockState, pkt.getNbtCompound());
  }

  /* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client
   */
  @Override
  public CompoundNBT getUpdateTag()
  {
    CompoundNBT nbtTagCompound = new CompoundNBT();
    write(nbtTagCompound);
    return nbtTagCompound;
  }

  /* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client
  *  The vanilla default is suitable for this example but I've included an explicit definition anyway.
   */
  @Override
  public void handleUpdateTag(BlockState blockState, CompoundNBT tag)
  {
    this.read(blockState, tag);
  }

  

  /**
   * When this tile entity is destroyed, drop all of its contents into the world
   * @param world
   * @param blockPos
   */
	public void dropAllContents(World world, BlockPos blockPos) {
    InventoryHelper.dropInventoryItems(world, blockPos, chestContents);
  }

	// -------------  The following two methods are used to make the TileEntity perform as a NamedContainerProvider, i.e.
  //  1) Provide a name used when displaying the container, and
  //  2) Creating an instance of container on the server, and linking it to the inventory items stored within the TileEntity

  /**
   *  standard code to look up what the human-readable name is.
   *  Can be useful when the tileentity has a customised name (eg "David's footlocker")
    */
	@Override
	public ITextComponent getDisplayName() {
    return new TranslationTextComponent("container.allthesmallthings.debarker_container");
	}

  /**
   * The name is misleading; createMenu has nothing to do with creating a Screen, it is used to create the Container on the server only
   * @param windowID
   * @param playerInventory
   * @param playerEntity
   * @return
   */
  @Nullable
  @Override
  public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return DebarkerContainer.createContainerServerSide(windowID, playerInventory, chestContents);
  }

  //Beginning of Debarker Logic
  
  public void tick() {
    // If world not null and is not remote
    if (world != null && !world.isRemote) {
      // Only perform actions on the server side
      if (canExtract()) {
        TICKS_COUNTER++;

        if (TICKS_COUNTER >= TICKS_CONVERSION) {
          convertLogToStripped();
          TICKS_CONVERSION = 0;
        }
      }
    }
  }

  /**
     * A function to convert the logs into their stripped variant
     */
    public void convertLogToStripped() {

        //If energy stored is over or = to the energy cost
        if (ENERGY_STORED >= ENERGY_COST) {
            try {

                //Gets stack of items in slot 0
                ItemStack inputStack = chestContents.getStackInSlot(0);

                // If the input stack is not empty and the item is valid
                if (!inputStack.isEmpty() && chestContents.isItemValidForSlot(0, inputStack)) {
                    //Gets stripped variant of log as ItemStack. Returns empty if cannot find it
                    ItemStack strippedStack = getStrippedVersion(inputStack);
    
                    //If stripped stack is not empty, gets stripped logs as new item stack, and gets one of it
                    if (!strippedStack.isEmpty()) {
                        ItemStack strippedLogs = new ItemStack(strippedStack.getItem(), 1);

                        //Takes one item out of slot 0, and adds one item to slot 1
                        chestContents.decrStackSize(0, 1);
                        chestContents.insertItem(1, strippedLogs);

                        //Deducts cost of machine process
                        ENERGY_STORED -= ENERGY_COST;
                        //Saves
                        markDirty();

                    }
                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the stripped variant of a log
     * @param stack log to check
     * @return stripped log version or null
     */
    private ItemStack getStrippedVersion(ItemStack stack) {
        Block blockToConvert = chestContents.getBlockFromItem(stack);

        // Checks to see if an item is a log and converts
        if (chestContents.isLog(stack)) {
            try {
                return chestContents.createItemStack(("minecraft:stripped_" + chestContents.getTypeOfLog(blockToConvert.toString()) + "_log"));
            }

            catch (Exception e) {
                return null;
            }
        }
        return null;
    }

/**
 * ----------------------------------------------------------------
 * Beginning of all Energy Related Code
 * ----------------------------------------------------------------
 */
     
  /**
   * A function to add energy
   * 
   * @param maxReceive - max energy to receive
   * @param simulate
   * @return received energy
   */
  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    int energyReceived = Math.min(getMaxEnergyStored() - getEnergyStored(), maxReceive);

    if (!simulate) {
      ENERGY_STORED += energyReceived;
      markDirty();
    }

    return energyReceived;
  }

  /**
   * A function to extract energy
   * 
   * @param maxExtract - maximum amount of energy to extract
   * @param simulate
   * @return energy extracted
   */
  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {

    if (!canExtract()) {
      return 0;
    }

    int energyExtracted = Math.min(ENERGY_STORED, maxExtract);

    if (!simulate) {
      ENERGY_STORED -= energyExtracted;
      markDirty();
    }

    return energyExtracted;
  }

  /**
   * A function to get the current energy stored
   * 
   * @return current energy_stored as int
   */
  @Override
  public int getEnergyStored() {
    return this.ENERGY_STORED;
  }

  /**
   * A function to get the max energy stored
   * 
   * @return max energy as int
   */
  @Override
  public int getMaxEnergyStored() {
    return MAX_ENERGY_STORED;
  }

  /**
   * A function to extract energy
   * 
   * @return true if energy is over 0, false otherwise
   */
  @Override
  public boolean canExtract() {
    // For example, you might check if the tile entity is active or has enough
    // energy stored
    return ENERGY_STORED > 0;
  }

  /**
   * A function to determine whether it can receive energy
   * 
   * @return false if energy is full, true otherwise
   */
  @Override
  public boolean canReceive() {
    return !isFull();
  }

  /**
   * A function to determine whether the energy stored is full or not
   * 
   * @return true if full, false otherwise
   */
  private boolean isFull() {
    if (ENERGY_STORED < MAX_ENERGY_STORED) {
      return true;
    }
    return false;
  }

  private final DebarkerContents chestContents; // holds the ItemStacks in the Chest
}
