package com.RafalEngiWork.MLTraders.regexps

private val dateRegex =
    Regex("^(?<year>\\d{4})(?<month>\\d{2})(?<day>\\d{2})(?<hour>\\d{2})(?<minutes>\\d{2})(?<seconds>\\d{2}).*_(?<steps>\\d*)\$")

fun getDateFromTimestamp(timestamp: String, data: Map<String, Any>): Date {
    val match = dateRegex.find(timestamp)?.groups
    val keys = data.keys
    val mappedData: Map<String, Int?> = keys.associateWith { data[it].toString().toIntOrNull() }.toMap()
    return Date(
        match?.get("year")?.value,
        match?.get("month")?.value,
        match?.get("day")?.value,
        match?.get("hour")?.value,
        match?.get("minutes")?.value,
        match?.get("seconds")?.value,
        match?.get("steps")?.value?.toIntOrNull(),
        mappedData
    )
}

data class Date(
    val year: String?,
    val month: String?,
    val day: String?,
    val hour: String?,
    val minutes: String?,
    val seconds: String?,
    val steps: Int?,
    var data: Map<String, Int?>?,
) {
    override fun toString(): String {
        return "$day.$month.$year, $hour:$minutes:$seconds"
    }
}
