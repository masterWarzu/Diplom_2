package ru.yandex.praktikum;

import java.util.List;

public class Order
{
    private List<String> ingredients;

    public Order(List<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public Order()
    {

    }

    public List<String> getIngredients()
    {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients)
    {
        this.ingredients = ingredients;
    }

    public void addIngredient(String ingredient)
    {
        ingredients.add(ingredient);
    }
}
