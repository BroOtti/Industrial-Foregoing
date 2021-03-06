/*
 * This file is part of Industrial Foregoing.
 *
 * Copyright 2019, Buuz135
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.buuz135.industrial.jei.ore;

import com.buuz135.industrial.utils.Reference;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.util.ResourceLocation;

public class OreSieveCategory implements IRecipeCategory<OreSieveWrapper> {

    public final IGuiHelper helper;
    private IDrawable tankOverlay;

    public OreSieveCategory(IGuiHelper helper) {
        this.helper = helper;
        tankOverlay = helper.createDrawable(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 1, 207, 12, 48);
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public Class<? extends OreSieveWrapper> getRecipeClass() {
        return null;
    }

    @Override
    public String getTitle() {
        return /*BlockRegistry.oreSieveBlock.getLocalizedName()TODO*/ "Sieve";
    }

    @Override
    public IDrawable getBackground() {
        return helper.createDrawable(new ResourceLocation(Reference.MOD_ID, "textures/gui/jei.png"), 4, 78, 77, 50);
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(OreSieveWrapper oreSieveWrapper, IIngredients iIngredients) {

    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, OreSieveWrapper oreSieveWrapper, IIngredients iIngredients) {

    }

    //@Override
    //public void setRecipe(IRecipeLayout recipeLayout, OreSieveWrapper recipeWrapper, IIngredients ingredients) {
    //    recipeLayout.getFluidStacks().init(0, true, 1, 1, 12, 48, 300, true, tankOverlay);
    //    recipeLayout.getFluidStacks().set(0, ingredients.getInputs(FluidStack.class).get(0));
//
    //    recipeLayout.getItemStacks().init(1, false, 55, 17);
    //    recipeLayout.getItemStacks().set(1, ingredients.getOutputs(ItemStack.class).get(0));
//
    //    recipeLayout.getItemStacks().init(2, true, 25, 33);
    //    recipeLayout.getItemStacks().set(2, ingredients.getInputs(ItemStack.class).get(0));
    //}
//
    //@Override
    //public void drawExtras(Minecraft minecraft) {
    //    Minecraft.getInstance().currentScreen.drawTexturedModalRect(25, 33, 0, 4, 18, 18);
    //}
}
