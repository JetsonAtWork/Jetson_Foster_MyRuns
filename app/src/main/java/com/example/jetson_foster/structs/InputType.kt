package com.example.jetson_foster.structs

enum class InputType(val asInt: Int, val asString: String) {
    MANUAL(0,"Manual"),
    GPS(1,"GPS"),
    AUTOMATIC(2,"Automatic");


    override fun toString(): String {
        return "$asString Entry"
    }


    companion object {
        fun stringFromInt(int: Int) : String {return fromInt(int).toString()}
        fun fromInt(int: Int) = values().first() {it.asInt == int}
        fun fromString(string: String) = values().first() { it.asString == string }
    }
}