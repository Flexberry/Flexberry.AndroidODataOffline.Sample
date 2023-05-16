package com.flexberry.androidodataofflinesample.data.network.datasource

import com.flexberry.androidodataofflinesample.data.network.models.NetworkApplicationUser

class ApplicationUserOdataDataSource:
    OdataDataSource<NetworkApplicationUser>(
        "EmberFlexberryDummyApplicationUsers",
        NetworkApplicationUser::class)
{

}