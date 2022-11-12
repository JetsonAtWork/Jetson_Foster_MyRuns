package com.example.jetson_foster.structs

enum class ActivityType(val asInt: Int,val asString: String) {
    RUNNING(0,"Running"),
    WALKING(1,"Walking"),
    STANDING(2,"Standing"),
    CYCLING(3,"Cycling"),
    HIKING(4,"Hiking"),
    DOWNHILL_SKIING(5,"Downhill Skiing"),
    CROSS_COUNTRY_SKIING(6,"Cross-Country Skiing"),
    SNOWBOARDING(7,"Snowboarding"),
    SKATING(8,"Skating"),
    SWIMMING(9,"Swimming"),
    MOUNTAIN_BIKING(10,"Mountain Biking"),
    WHEELCHAIR(11,"Wheelchair"),
    ELLIPTICAL(12,"Elliptical"),
    OTHER(13,"Other");

    companion object {
        fun stringFromInt(int: Int) : String {return fromInt(int).asString}
        fun fromInt(int: Int) = values().first() {it.asInt == int}
        fun fromString(string: String) = values().first() { it.asString == string }
    }
}