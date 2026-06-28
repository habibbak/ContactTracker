package com.contacttracker.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.contacttracker.app.ui.ContactViewModel
import com.contacttracker.app.ui.screens.AddEditContactScreen
import com.contacttracker.app.ui.screens.ContactDetailScreen
import com.contacttracker.app.ui.screens.ContactListScreen

private const val ROUTE_LIST = "list"
private const val ROUTE_ADD = "add"
private const val ROUTE_EDIT = "edit/{contactId}"
private const val ROUTE_DETAIL = "detail/{contactId}"

@Composable
fun AppNavigation(navController: NavHostController, viewModel: ContactViewModel) {
    NavHost(navController = navController, startDestination = ROUTE_LIST) {

        composable(ROUTE_LIST) {
            ContactListScreen(
                viewModel = viewModel,
                onContactClick = { id -> navController.navigate("detail/$id") },
                onAddClick = { navController.navigate(ROUTE_ADD) }
            )
        }

        composable(ROUTE_ADD) {
            AddEditContactScreen(
                viewModel = viewModel,
                contactId = null,
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            ROUTE_EDIT,
            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("contactId")
            AddEditContactScreen(
                viewModel = viewModel,
                contactId = id,
                onDone = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            ROUTE_DETAIL,
            arguments = listOf(navArgument("contactId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("contactId") ?: return@composable
            ContactDetailScreen(
                viewModel = viewModel,
                contactId = id,
                onBack = { navController.popBackStack() },
                onEdit = { editId -> navController.navigate("edit/$editId") },
                onDeleted = { navController.popBackStack() }
            )
        }
    }
}
