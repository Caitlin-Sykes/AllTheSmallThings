package com.csykes.allthesmallthings.debarker;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by TGG on 4/04/2020.
 *
 * This class is used to encapsulate the contents of the chest and provide the link between the parent TileEntity and
 *    the container.
 * 1) stores information about the items in the chest: allows the container to manipulate the data stored in the tile entity
 * 2) provides a way for the container to ask if certain actions are permitted (eg isUsableByPlayer, isItemValidForSlot)
 * 3) provides a way for the container to notify the TileEntity that the container has changed (eg markDirty, openInventory)
 *
 * Typical usage for a TileEntity which needs to store Items:
 * 1) When constructing the TileEntity, create and store a ChestContents using createForTileEntity()
 * 2) In your ContainerType<MyContainer>, create a ChestContents using createForClientSideContainer() and pass it to
 *    the constructor of your client-side container.
 * 3) In your TileEntity write() and read() methods, call the serializeNBT() and deserializeNBT() methods
 * Vanilla and the container code will take care of everything else.
 *
 */

public class DebarkerContents implements IInventory {

  private static final String REGEX = ":(.+)_";

  /**
   * Use this constructor to create a ChestContents which is linked to its parent TileEntity.
   * On the server, this link will be used by the Container to request information and provide notifications to the parent
   * On the client, the link will be unused.
   * There are additional notificationLambdas available; these two are explicitly specified because your TileEntity will
   *   nearly always need to implement at least these two
   * @param size  the max number of ItemStacks in the inventory
   * @param canPlayerAccessInventoryLambda the function that the container should call in order to decide if the given player
   *                                       can access the container's contents not.  Usually, this is a check to see
   *                                       if the player is closer than 8 blocks away.
   * @param markDirtyNotificationLambda  the function that the container should call in order to tell the parent TileEntity
   *                                     that the contents of its inventory have been changed and need to be saved.  Usually,
   *                                     this is TileEntity::markDirty
   * @return the new ChestContents.
   */
  public static DebarkerContents createForTileEntity(int size,
                                                     Predicate<PlayerEntity> canPlayerAccessInventoryLambda,
                                                     Notify markDirtyNotificationLambda) {
     return new DebarkerContents(size, canPlayerAccessInventoryLambda, markDirtyNotificationLambda);
  }

  /**
   * Use this constructor to create a ChestContents which is not linked to any parent TileEntity; i.e. is used by
   *   the client side container:
   *   * does not permanently store items
   *   * cannot ask questions/provide notifications to a parent TileEntity
   * @param size  the max number of ItemStacks in the inventory
   * @return the new ChestContents
   */
  public static DebarkerContents createForClientSideContainer(int size) {
    return new DebarkerContents(size);
  }

  // ----Methods used to load / save the contents to NBT

  /**
   * Writes the chest contents to a CompoundNBT tag (used to save the contents to disk)
   * @return the tag containing the contents
   */
  public CompoundNBT serializeNBT()  {
    return chestContents.serializeNBT();
  }

  /**
   * Fills the chest contents from the nbt; resizes automatically to fit.  (used to load the contents from disk)
   * @param nbt
   */
  public void deserializeNBT(CompoundNBT nbt)   {
    chestContents.deserializeNBT(nbt);
  }

  //  ------------- linking methods  -------------
  //  The following group of methods are used to establish a link between the parent TileEntity and the chest contents,
  //    so that the container can communicate with the parent TileEntity without having to talk to it directly.
  //  This is important because the link to the TileEntity only exists on the server side.  On the client side, the
  //    container gets a dummy link instead- there is no link to the client TileEntity.  Linking to the client TileEntity
  //    is prohibited because of synchronisation clashes, i.e. vanilla would attempt to synchronise the TileEntity in two
  //    different ways at the same time: via the tileEntity server->client packets and via the container directly poking
  //    around in the inventory contents.
  //  I've used lambdas to make the decoupling more explicit.  You could instead
  //  * provide an Optional TileEntity to the ChestContents constructor (and ignore the markDirty() etc calls), or
  //  * implement IInventory directly in your TileEntity, and construct your client-side container using an Inventory
  //    instead of passing it a TileEntity.  (This is how vanilla does it)
  //

  /**
   * sets the function that the container should call in order to decide if the given player can access the container's
   *   contents not.  The lambda function is only used on the server side
   */
  public void setCanPlayerAccessInventoryLambda(Predicate<PlayerEntity> canPlayerAccessInventoryLambda) {
    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // contents of its inventory have been changed.
  // default is "do nothing"
  public void setMarkDirtyNotificationLambda(Notify markDirtyNotificationLambda) {
    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been opened by a player (eg so that the chest can animate its lid being opened)
  // default is "do nothing"
  public void setOpenInventoryNotificationLambda(Notify openInventoryNotificationLambda) {
    this.openInventoryNotificationLambda = openInventoryNotificationLambda;
  }

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been closed by a player
  // default is "do nothing"
  public void setCloseInventoryNotificationLambda(Notify closeInventoryNotificationLambda) {
    this.closeInventoryNotificationLambda = closeInventoryNotificationLambda;
  }

/**
 * Can be used by a player
 */
  @Override
  public boolean isUsableByPlayer(PlayerEntity player) {
    return canPlayerAccessInventoryLambda.test(player);  
  }

  /**
   * is the item valid for the slot
   */
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    switch (index) {
      case 0:
        return (isLog(stack) && hasStrippedLog(stack));
      case 1:
        return stack.isEmpty() || isStrippedLog(stack);
      default:
        return false;

    }
  }

  @FunctionalInterface
  public interface Notify {  
    void invoke();
  }

  /**
   * Saves the contents of the block
   */
  @Override
  public void markDirty() {
    markDirtyNotificationLambda.invoke();
  }

  /**
   * Opens the inventory
   */
  @Override
  public void openInventory(PlayerEntity player) {
    openInventoryNotificationLambda.invoke();
  }

  /**
   * Closes the inventory
   */
  @Override
  public void closeInventory(PlayerEntity player) {
    closeInventoryNotificationLambda.invoke();
  }


  /**
   * Gets the size of the inventory
   */
  @Override
  public int getSizeInventory() {
    return chestContents.getSlots();
  }

  /**
   * Method for if the inventory is empty
   */
  @Override
  public boolean isEmpty() {
    for (int i = 0; i < chestContents.getSlots(); ++i) {
      if (!chestContents.getStackInSlot(i).isEmpty()) return false;
    }
    return true;
  }

  /**
   * Gets the stack in a specific slot
   * @param index - index to check
   */
  @Override
  public ItemStack getStackInSlot(int index) {
    return chestContents.getStackInSlot(index);
  }

  /**
   * Decreases the stack size
   * @param index - index to check
   * @param count - number to be removed
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
    return chestContents.extractItem(index, count, false);
  }

  /**
   * Removes a stack from the slot
   * @param index - index to check
   */
  @Override
  public ItemStack removeStackFromSlot(int index) {
    int maxPossibleItemStackSize = chestContents.getSlotLimit(index);
    return chestContents.extractItem(index, maxPossibleItemStackSize, false);
  }

  /**
   * Inserts item in slot
   */
  public ItemStack insertItem(int index, ItemStack stack) {
    return chestContents.insertItem(index, stack, false);
  }

  /**
   * Sets the number of items in a slot
   * @param index - index to be checked
   * @param stack - itemStack to be set to
   */
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    chestContents.setStackInSlot(index, stack);
  }

  /**
   * Clears the inventory
   */
  @Override
  public void clear() {
    for (int i = 0; i < chestContents.getSlots(); ++i) {
      chestContents.setStackInSlot(i, ItemStack.EMPTY);
    }
  }

  /**
   * Handler of the chest contents
   * @param size
   */
  private DebarkerContents(int size) {
    this.chestContents = new ItemStackHandler(size);
  }

  /**
   * Method to get the chest contents
   * @param size
   * @param canPlayerAccessInventoryLambda
   * @param markDirtyNotificationLambda
   */
  private DebarkerContents(int size, Predicate<PlayerEntity> canPlayerAccessInventoryLambda, Notify markDirtyNotificationLambda) {
    this.chestContents = new ItemStackHandler(size);
    this.canPlayerAccessInventoryLambda = canPlayerAccessInventoryLambda;
    this.markDirtyNotificationLambda = markDirtyNotificationLambda;
  }

  // the function that the container should call in order to decide if the
  // given player can access the container's Inventory or not.  Only valid server side
  //  default is "true".
  private Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x-> true;

  // the function that the container should call in order to tell the parent TileEntity that the
  // contents of its inventory have been changed.
  // default is "do nothing"
  private Notify markDirtyNotificationLambda = ()->{};

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been opened by a player (eg so that the chest can animate its lid being opened)
  // default is "do nothing"
  private Notify openInventoryNotificationLambda = ()->{};

  // the function that the container should call in order to tell the parent TileEntity that the
  // container has been closed by a player
  // default is "do nothing"
  private Notify closeInventoryNotificationLambda = ()->{};

  private final ItemStackHandler chestContents;

  // ------------------------->
  // Debarker Code

  /**
     * Checks if a given itemStack is a log
     * 
     * @param stack to check
     * @return true if the itemStack is a log and does not contain "stripped" in the
     *         name, false otherwise
     */
    public boolean isLog(ItemStack stack) {
        return BlockTags.LOGS.contains(getBlockFromItem(stack)) && !stack.toString().contains("stripped");
    }

    /**
     * A function to get the block from an item
     * 
     * @param stack - item stack to convert
     * @return Block
     */
    public static Block getBlockFromItem(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem());
    }

    /**
     * A function to confirm whether it has a stripped log variant
     * 
     * @param stack - itemStack to check
     * @return true if it has a stripped log variant, false otherwise
     */
    private boolean hasStrippedLog(ItemStack stack) {
      if (getStrippedLog(stack) != null) {
        return true;
      }
      return false;
    }

    /**
     * A function to get the stripped version of a block
     * 
     * @param stack
     * @return ItemStack with the name of string, null if not found
     */
    private ItemStack getStrippedLog(ItemStack stack) {
      Block blockToConvert = getBlockFromItem(stack);

      // Checks to see if an item is a log and converts
      if (isLog(stack)) {
        try {
          return createItemStack(("minecraft:stripped_" + getTypeOfLog(blockToConvert.toString()) + "_log"));
        }

        catch (Exception e) {
          return null;
        }
      }
      return null;
    }

     /**
     * Gets the type of log using a regex pattern
     * 
     * @param block
     * @return type of log as String or null if not found
     */
    public String getTypeOfLog(String block) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matched = pattern.matcher(block);
        if (matched.find()) {
            return matched.group(1);
        }
        return null;
    }


    /**
     * A function to create a new item stack based on a string name
     * 
     * @param itemType itemType = name of item formed by @getStrippedLog
     * @return ItemStack as found item, empty if not found
     */
    public ItemStack createItemStack(String itemType) {
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemType));
        if (item != null) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

    /**
     * Function to check if stripped
     * 
     * @param stack - item stack to check
     * @return true if contains "stripped"
     */
    public boolean isStrippedLog(ItemStack stack) {
      return stack.toString().toLowerCase().contains("stripped");
    }

  }
