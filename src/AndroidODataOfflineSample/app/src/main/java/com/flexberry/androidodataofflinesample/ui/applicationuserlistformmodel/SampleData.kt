package com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel
import com.flexberry.androidodataofflinesample.data.model.ApplicationUser
import java.sql.Date
import java.sql.Timestamp
import java.util.UUID

@Suppress("*")
object SampleData {
    val usersSample = listOf(
        ApplicationUser(
            primarykey = UUID.randomUUID(),
            createTime = Timestamp(60 * 60),
            creator = "",
            editTime = Timestamp(120 * 60),
            editor = "",
            name = "Alex",
            email = "alex@alex.com",
            phone1 = "+790999999999",
            phone2 = "",
            phone3 = "",
            activated = true,
            vK = "",
            facebook = "",
            twitter = "",
            birthday = Date.valueOf("2019-01-26"),
            gender = "male",
            vip = true,
            karma = 0.3,
            votes = emptyList()
        ),
        ApplicationUser(
            primarykey = UUID.randomUUID(),
            createTime = Timestamp(60 * 60),
            creator = "",
            editTime = Timestamp(120 * 60),
            editor = "",
            name = "Peter",
            email = "peter@peter.com",
            phone1 = "+7909888899",
            phone2 = "",
            phone3 = "",
            activated = false,
            vK = "",
            facebook = "",
            twitter = "",
            birthday = Date.valueOf("2019-3-28"),
            gender = "male",
            vip = true,
            karma = 2.3,
            votes = emptyList()
        ),
        ApplicationUser(
            primarykey = UUID.randomUUID(),
            createTime = Timestamp(60 * 60),
            creator = "",
            editTime = Timestamp(120 * 60),
            editor = "",
            name = "Simona",
            email = "simona@alex.com",
            phone1 = "+793999333999",
            phone2 = "",
            phone3 = "",
            activated = false,
            vK = "",
            facebook = "",
            twitter = "",
            birthday = Date.valueOf("2020-4-25"),
            gender = "male",
            vip = false,
            karma = 0.8,
            votes = emptyList()
        )
    )
}