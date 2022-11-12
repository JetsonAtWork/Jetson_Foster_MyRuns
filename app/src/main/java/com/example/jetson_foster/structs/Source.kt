package com.example.jetson_foster.structs

enum class Source(val asInt: Int) {
    DEFAULT(0),
    TEMPORARY(1),
     STORED(2);

    /**
     * get Source with enum ordinal value passed
     */
    companion object {
        fun fromInt(int: Int) = Source.values().first() {it.asInt == int}
    }
}