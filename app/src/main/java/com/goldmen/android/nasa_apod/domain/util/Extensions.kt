package com.goldmen.android.nasa_apod.domain.util

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline loadFromDb: () -> Flow<ResultType>,
    crossinline createCall: suspend () -> RequestType,
    crossinline saveToDb: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true },
    crossinline onCallSuccess: () -> Unit = { },
    crossinline onCallFailed: (Throwable) -> Unit = { }
) = channelFlow {
    val data = loadFromDb().first()

    if (shouldFetch(data)) {
        val loading = launch {
            loadFromDb().collect { send(Resource.Loading(it)) }
        }

        try {
            saveToDb(createCall())
            onCallSuccess()
            loading.cancel()
            loadFromDb().collect { send(Resource.Success(it)) }
        } catch (t: Throwable) {
            onCallFailed(t)
            loading.cancel()
            loadFromDb().collect { send(Resource.Error(t, it)) }
        }
    } else {
        loadFromDb().collect { send(Resource.Success(it)) }
    }
}

inline fun <reified T> T.toJsonString(gson: Gson): String = gson.toJson(this)

inline fun <reified T> T.toCustomMap(gson: Gson): Map<String, String> = gson.fromJson(
    toJsonString(gson),
    object : TypeToken<Map<String, String>>() {}.type
)

enum class Refresh {
    FORCE, NORMAL
}

sealed class Event {
    data class ShowErrorMessage(val error: Throwable) : Event()
}

fun Fragment.showSnackBarError(message: String, view: View = requireView()) =
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()

fun FragmentManager.showCalender(onDatePicked: (Long) -> Unit) {
    getCalender().also {
        it.addOnPositiveButtonClickListener(onDatePicked)
    }.show(this, "tag")
}

fun getCalender() = MaterialDatePicker.Builder.datePicker()
    .setTitleText("Choose End Date")
    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
    .setCalendarConstraints(
        CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now()).build()
    )
    .build()

fun LocalDate.formatter(): String = this.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

val currentDate: LocalDate = LocalDate.now()

fun Long.toFormattedDate(): String = this.instantToLocal().formatter()

fun Long.toPastFormattedDate(): String = this.instantToLocal().minus().formatter()

val getCurrentFormattedDate: String = currentDate.formatter()

val getPastFormattedDate: String = currentDate.minus().formatter()

fun LocalDate.minus(): LocalDate = this.minusDays(30)

fun Long.instantToLocal(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()