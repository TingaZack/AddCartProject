package com.example.admin.addcartproject;

/**
 * Created by Admin on 11/09/2017.
 */

public class Product {
    String m_name;
    double m_value;

    Product(String name, double value)
    {
        m_name = name;
        m_value = value;
    }

    String getName()
    {
        return m_name;
    }

    double getValue()
    {
        return m_value;
    }
}
