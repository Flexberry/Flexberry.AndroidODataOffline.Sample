package com.flexberry.androidodataofflinesample.navigation

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.flexberry.androidodataofflinesample.ui.applicationuserlistformmodel.ApplicationUserListFormModelScreen
import com.flexberry.androidodataofflinesample.ui.mainmodel.MainScreen
import com.flexberry.androidodataofflinesample.ui.theme.AndroidODataOfflineSampleTheme
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
    AndroidODataOfflineSampleTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
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
            }
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