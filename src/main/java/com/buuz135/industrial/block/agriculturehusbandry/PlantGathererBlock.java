package com.buuz135.industrial.block.agriculturehusbandry;

import com.buuz135.industrial.block.IndustrialBlock;
import com.buuz135.industrial.block.agriculturehusbandry.tile.PlantGathererTile;
import com.buuz135.industrial.module.ModuleAgricultureHusbandry;
import com.hrznstudio.titanium.api.IFactory;
import net.minecraft.block.Blocks;

import javax.annotation.Nonnull;

public class PlantGathererBlock extends IndustrialBlock<PlantGathererTile> {

    public PlantGathererBlock() {
        super("plant_gatherer", Properties.from(Blocks.IRON_BLOCK), PlantGathererTile.class, ModuleAgricultureHusbandry.TAB_AG_HUS);
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public IFactory<PlantGathererTile> getTileEntityFactory() {
        return PlantGathererTile::new;
    }
}
