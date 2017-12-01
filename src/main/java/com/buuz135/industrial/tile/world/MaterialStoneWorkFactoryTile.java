package com.buuz135.industrial.tile.world;

import com.buuz135.industrial.proxy.client.infopiece.MaterialStoneWorkInfoPiece;
import com.buuz135.industrial.tile.CustomColoredItemHandler;
import com.buuz135.industrial.tile.CustomElectricMachine;
import com.buuz135.industrial.utils.CraftingUtils;
import com.google.common.collect.Iterators;
import lombok.Getter;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.ndrei.teslacorelib.TeslaCoreLib;
import net.ndrei.teslacorelib.gui.BasicTeslaGuiContainer;
import net.ndrei.teslacorelib.gui.IGuiContainerPiece;
import net.ndrei.teslacorelib.netsync.SimpleNBTMessage;

import java.util.*;

public class MaterialStoneWorkFactoryTile extends CustomElectricMachine {

    private static final String NBT_MODE = "Mode";

    @Getter
    private LinkedHashMap<ItemStackHandler, Mode> modeList;


    public MaterialStoneWorkFactoryTile() {
        super(MaterialStoneWorkFactoryTile.class.getName().hashCode());
    }

    @Override
    protected void initializeInventories() {
        super.initializeInventories();
        modeList = new LinkedHashMap<>();
        EnumDyeColor[] colors = new EnumDyeColor[]{EnumDyeColor.YELLOW, EnumDyeColor.BLUE, EnumDyeColor.GREEN, EnumDyeColor.ORANGE, EnumDyeColor.PURPLE};
        for (int i = 0; i < 5; ++i) {
            ItemStackHandler item = new ItemStackHandler(2);
            this.addInventory(new CustomColoredItemHandler(item, colors[i], "Material process", 50 + 24 * i, 25, 1, 2) {
                @Override
                public boolean canInsertItem(int slot, ItemStack stack) {
                    return false;
                }
            });
            this.addInventoryToStorage(item, "item" + i);
            modeList.put(item, Mode.NONE);
        }
    }

    @Override
    public List<IGuiContainerPiece> getGuiContainerPieces(BasicTeslaGuiContainer<?> container) {
        List<IGuiContainerPiece> pieces = super.getGuiContainerPieces(container);
        for (int i = 0; i < modeList.size() - 1; ++i)
            pieces.add(new MaterialStoneWorkInfoPiece(this, 62 + 24 * i, 64, i));
        return pieces;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound compound1 = super.writeToNBT(compound);
        NBTTagCompound m = new NBTTagCompound();
        modeList.forEach((handler, mode) -> m.setString(String.valueOf(Iterators.indexOf(modeList.entrySet().iterator(), input -> input.getKey().equals(handler))), mode.toString()));
        compound1.setTag(NBT_MODE, m);
        return compound1;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(NBT_MODE)) {
            compound.getCompoundTag(NBT_MODE).getKeySet().forEach(s -> {
                Map.Entry<ItemStackHandler, Mode> it = Iterators.get(modeList.entrySet().iterator(), Integer.parseInt(s));
                modeList.replace(it.getKey(), Mode.valueOf(compound.getCompoundTag(NBT_MODE).getString(s)));
            });
        }
    }

    @Override
    protected float performWork() {
        int i = 0;
        Iterator<Map.Entry<ItemStackHandler, Mode>> it = modeList.entrySet().iterator();
        int id = 0;
        int work = (int) Math.pow(2, speedUpgradeLevel());
        while (it.hasNext()) {
            Map.Entry<ItemStackHandler, Mode> entry = it.next();
            if (!it.hasNext()) break;
            ItemStack cobble = new ItemStack(Blocks.COBBLESTONE, work);
            if (id == 0) {
                if (!cobble.equals(ItemHandlerHelper.insertItem(entry.getKey(), cobble, true))) {
                    i = 1;
                    ItemHandlerHelper.insertItem(entry.getKey(), cobble, false);
                }
            }
            Map.Entry<ItemStackHandler, Mode> nextEntry = getEntry(id + 1);
            if (entry.getValue() == Mode.FURNACE) {
                for (int slot = 0; slot < entry.getKey().getSlots(); ++slot) {
                    ItemStack result = FurnaceRecipes.instance().getSmeltingResult(new ItemStack(entry.getKey().getStackInSlot(slot).getItem(), 1)).copy();
                    result.setCount(work);
                    if (!result.isEmpty() && ItemHandlerHelper.insertItem(nextEntry.getKey(), result, true).isEmpty()) {
                        ItemHandlerHelper.insertItem(nextEntry.getKey(), result, false);
                        entry.getKey().getStackInSlot(slot).shrink(work);
                        break;
                    }
                }
            }
            if (entry.getValue() == Mode.CRAFT_BIG || entry.getValue() == Mode.CRAFT_SMALL) {
                int size = entry.getValue() == Mode.CRAFT_BIG ? 3 : 2;
                for (int slot = 0; slot < entry.getKey().getSlots(); ++slot) {
                    if (entry.getKey().getStackInSlot(slot).getCount() >= size * size) {
                        ItemStack result = CraftingUtils.findOutput(size, entry.getKey().getStackInSlot(slot), world);
                        if (!result.isEmpty() && ItemHandlerHelper.insertItem(nextEntry.getKey(), result, true).isEmpty()) {
                            ItemHandlerHelper.insertItem(nextEntry.getKey(), result, false);
                            entry.getKey().getStackInSlot(slot).shrink(size * size);
                            break;
                        }
                    }
                }
            }
            if (entry.getValue() == Mode.GRIND) {
                for (int slot = 0; slot < entry.getKey().getSlots(); ++slot) {
                    ItemStack result = CraftingUtils.getCrushOutput(new ItemStack(entry.getKey().getStackInSlot(slot).getItem(), 1)).copy();
                    result.setCount(work);
                    if (!result.isEmpty() && ItemHandlerHelper.insertItem(nextEntry.getKey(), result, true).isEmpty()) {
                        ItemHandlerHelper.insertItem(nextEntry.getKey(), result, false);
                        entry.getKey().getStackInSlot(slot).shrink(work);
                        break;
                    }
                }
            }
            ++id;
        }
        return i;
    }

    @Override
    protected boolean shouldAddFluidItemsInventory() {
        return false;
    }

    public void nextMode(int id) {
        Map.Entry<ItemStackHandler, Mode> it = getEntry(id);
        modeList.replace(it.getKey(), Mode.values()[(Arrays.asList(Mode.values()).indexOf(it.getValue()) + 1) % Mode.values().length]);
        if (TeslaCoreLib.INSTANCE.isClientSide()) {
            NBTTagCompound compound = this.setupSpecialNBTMessage("CHANGE_MODE");
            Map.Entry<ItemStackHandler, Mode> mode = getEntry(id);
            compound.setInteger("id", id);
            compound.setString("value", mode.getValue().toString());
            this.sendToServer(compound);
        }
    }

    @Override
    protected SimpleNBTMessage processClientMessage(String messageType, NBTTagCompound compound) {
        super.processClientMessage(messageType, compound);
        if (messageType.equals("CHANGE_MODE")) {
            Map.Entry<ItemStackHandler, Mode> entry = getEntry(compound.getInteger("id"));
            modeList.replace(entry.getKey(), Mode.valueOf(compound.getString("value")));
        }
        return null;
    }

    public Map.Entry<ItemStackHandler, Mode> getEntry(int id) {
        return Iterators.get(modeList.entrySet().iterator(), id);
    }

    public enum Mode {
        NONE(new ItemStack(Blocks.BARRIER), "Stopped"), FURNACE(new ItemStack(Blocks.FURNACE), "Furnace mode"), CRAFT_SMALL(new ItemStack(Blocks.PLANKS), "2x2 Craft mode"), GRIND(new ItemStack(Items.DIAMOND_PICKAXE), "Grind mode"), CRAFT_BIG(new ItemStack(Blocks.CRAFTING_TABLE), "3x3 Craft mode");

        @Getter
        private ItemStack itemStack;
        @Getter
        private String name;

        Mode(ItemStack itemStack, String name) {
            this.itemStack = itemStack;
            this.name = name;
        }
    }
}
