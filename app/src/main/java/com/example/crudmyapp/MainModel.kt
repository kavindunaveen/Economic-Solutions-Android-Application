package com.example.crudmyapp


// Class declaration.
class MainModel {
    var itemName: String? = null
    var price: String? = null
    var description: String? = null

    internal constructor() {}

    // Parameterized constructor to initialize the data model.
    constructor(itemName: String?, price: String?, description: String?) {
        this.itemName = itemName
        this.price = price
        this.description = description
    }



}