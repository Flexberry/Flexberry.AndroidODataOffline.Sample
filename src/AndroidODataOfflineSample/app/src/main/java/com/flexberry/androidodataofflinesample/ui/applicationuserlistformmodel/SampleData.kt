package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel
import android.os.Build
import androidx.annotation.RequiresApi
import com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel.ApplicationUserListFormViewModel.User
import java.time.LocalDate

object SampleData {
    @RequiresApi(Build.VERSION_CODES.O)
    val usersSample = listOf(
        User(
            name = "Alex",
            email = "alex@alex.com",
            phone = "+790999999999",
            activated = true,
            vK = "http://vk.com/alex",
            facebook = "http://facebook.com/alex",
            birthday = LocalDate.parse("1999-12-12")
        ),
        User(
            name = "Peter",
            email = "peter@peter.com",
            phone = "+7909888899",
            birthday = LocalDate.parse("2000-12-12")
        ),
        User(
            name = "Simona",
            email = "simona@alex.com",
            phone = "+793999333999",
            activated = true,
            vK = "http://vk.com/simona",
            birthday = LocalDate.parse("2003-12-12")
        ),
    )
}