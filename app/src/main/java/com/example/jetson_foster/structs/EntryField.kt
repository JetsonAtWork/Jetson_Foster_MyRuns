package com.example.jetson_foster.structs

enum class EntryField(val asInt: Int, val asString: String)  {
    DATE(0,"Date"),
    TIME(1,"Time"),
    DURATION(2,"Duration"),
    DISTANCE(3,"Distance"),
    CALORIES(4,"Calories"),
    HEART_RATE(5,"Heart Rate"),
    COMMENT(6,"Comment");




    companion object {
        fun fromInt(int: Int) = EntryField.values().first() {it.asInt == int}
        fun fromString(string: String) = EntryField.values().first() { it.asString == string }
    }
}
