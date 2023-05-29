package com.flexberry.androidodataofflinesample.ui.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel.ApplicationUserListFormModelScreen
import com.flexberry.androidodataofflinesample.ui.detaileditform.DetailEditFormScreen
import com.flexberry.androidodataofflinesample.ui.detaillistform.DetailListFormScreen
import com.flexberry.androidodataofflinesample.ui.mainmodel.MainScreen
import com.flexberry.androidodataofflinesample.ui.mastereditformmodel.MasterEditFormScreen
import com.flexberry.androidodataofflinesample.ui.masterlistformmodel.MasterListFormScreen
import com.flexberry.androidodataofflinesample.ui.votelistformmodel.VoteListFormModelScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun BaseNavigation(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavigationEffects(
        navigationChannel = navigationViewModel.navigationChannel,
        navHostController = navController
    )

    BaseNavHost(
        navController = navController,
        startDestination = Destination.MainScreen
    ) {
        baseComposable(destination = Destination.MainScreen) {
            MainScreen()
        }
        baseComposable(destination = Destination.ApplicationUserListFormModelScreen) {
            ApplicationUserListFormModelScreen()
        }
        baseComposable(destination = Destination.VoteListFormModelScreen) {
            VoteListFormModelScreen()
        }
        baseComposable(destination = Destination.MasterListForm) {
            MasterListFormScreen()
        }
        baseComposable(destination = Destination.DetailListForm) {
            DetailListFormScreen()
        }
        baseComposable(destination = Destination.MasterEditScreen) {
            MasterEditFormScreen()
        }
        baseComposable(destination = Destination.DetailEditScreen) {
            DetailEditFormScreen()
        }
    }
}

@Composable
fun NavigationEffects(
    navigationChannel: Channel<NavigationIntent>,
    navHostController: NavHostController
) {
    val activity = (LocalContext.current as? Activity)
    LaunchedEffect(activity, navHostController, navigationChannel) {
        navigationChannel.receiveAsFlow().collect { intent ->
            if (activity?.isFinishing == true) {
                return@collect
            }
            when (intent) {
                is NavigationIntent.NavigateBack -> {
                    if (intent.route != null) {
                        navHostController.popBackStack(intent.route, intent.inclusive)
                    } else {
                        navHostController.popBackStack()
                    }
                }
                is NavigationIntent.NavigateTo -> {
                    navHostController.navigate(intent.route) {
                        launchSingleTop = intent.isSingleTop
                        intent.popUpToRoute?.let { popUpToRoute ->
                            popUpTo(popUpToRoute) { inclusive = intent.inclusive }
                        }
                    }
                }
            }
        }
    }
}