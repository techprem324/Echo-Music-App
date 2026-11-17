@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package echo.music.iad1tya.ui.screens.settings

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import echo.music.iad1tya.BuildConfig
import echo.music.iad1tya.LocalPlayerAwareWindowInsets
import echo.music.iad1tya.R
import echo.music.iad1tya.ui.component.IconButton
import echo.music.iad1tya.ui.component.Material3SettingsGroup
import echo.music.iad1tya.ui.component.Material3SettingsItem
import echo.music.iad1tya.ui.utils.backToMain

private const val GITHUB_PROFILE_URL = "https://github.com/techprem324"
private const val GITHUB_AVATAR_URL = "https://github.com/techprem324.png"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
  navController: NavController,
  scrollBehavior: TopAppBarScrollBehavior,
  onBack: (() -> Unit)? = null,
  highlightKey: String? = null
) {
  val uriHandler = LocalUriHandler.current

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.surface,
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = stringResource(R.string.about),
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
        },
        navigationIcon = {
          IconButton(
            onClick = { onBack?.invoke() ?: navController.navigateUp() },
            onLongClick = navController::backToMain,
          ) {
            Icon(painterResource(R.drawable.arrow_back), contentDescription = null)
          }
        },
        windowInsets = TopAppBarDefaults.windowInsets,
        colors =
          TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
          ),
        scrollBehavior = scrollBehavior,
      )
    },
  ) { innerPadding ->
    LazyColumn(
      modifier =
        Modifier.fillMaxSize()
          .windowInsetsPadding(
            LocalPlayerAwareWindowInsets.current.only(
              WindowInsetsSides.Horizontal,
            ),
          ),
      contentPadding =
        PaddingValues(
          start = 16.dp,
          top = innerPadding.calculateTopPadding() + 8.dp,
          end = 16.dp,
          bottom =
            androidx.compose.foundation.layout.WindowInsets.systemBars
              .asPaddingValues()
              .calculateBottomPadding() + 32.dp,
        ),
      verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
      item { AboutAppCard() }

      item {
        Column(modifier = Modifier.fillMaxWidth()) {
          echo.music.iad1tya.ui.component.PreferenceGroupTitle(title = "Contributors")
          Column(
            modifier =
              Modifier.clickable { uriHandler.openUri(GITHUB_PROFILE_URL) }
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            coil3.compose.AsyncImage(
              model = GITHUB_AVATAR_URL,
              contentDescription = "GitHub profile picture for techprem324",
              modifier = Modifier.size(48.dp).clip(CircleShape),
              contentScale = androidx.compose.ui.layout.ContentScale.Crop,
              error = painterResource(R.drawable.person),
              placeholder = painterResource(R.drawable.person)
            )
            Text(
              text = "techprem324",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurface
            )
          }
        }
      }

      item {
        Material3SettingsGroup(
          title = "Developer",
          items =
            listOf(
              Material3SettingsItem(
                icon = painterResource(R.drawable.website),
                title = { Text("Website") },
                description = { Text("techprem324.github.io/portfolio/") },
                onClick = { uriHandler.openUri("https://techprem324.github.io/portfolio/") }
              ),
              Material3SettingsItem(
                icon = painterResource(R.drawable.ic_x_new),
                title = { Text("X (Twitter)") },
                description = { Text("@prem_sri889") },
                onClick = { uriHandler.openUri("https://x.com/prem_sri889") }
              )
            )
        )
      }
    }
  }
}

@Composable
private fun AboutAppCard() {
  Column(
    modifier = Modifier.fillMaxWidth().padding(vertical = 28.dp, horizontal = 20.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    val isDark = MaterialTheme.colorScheme.surface.luminance() < 0.5f

    var isEasterEggActive by remember { mutableStateOf(false) }
    val rotation by
      animateFloatAsState(
        targetValue = if (isEasterEggActive) 180f else 0f,
        animationSpec =
          spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "flip"
      )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by
      animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec =
          spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
          ),
        label = "scale"
      )

    Box(
      modifier =
        Modifier.size(100.dp)
          .graphicsLayer {
            rotationY = rotation
            scaleX = scale
            scaleY = scale
            cameraDistance = 12f * density
          }
          .clip(CircleShape)
          .clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = { isEasterEggActive = !isEasterEggActive }
          ),
      contentAlignment = Alignment.Center
    ) {
      if (rotation <= 90f) {
        Image(
          painter = painterResource(R.drawable.ic_launcher_nobg),
          contentDescription = null,
          colorFilter = ColorFilter.tint(if (isDark) Color.White else Color(0xFFEA3829)),
          modifier = Modifier.fillMaxSize()
        )
      } else {
        coil3.compose.AsyncImage(
          model = GITHUB_AVATAR_URL,
          contentDescription = "GitHub profile picture for techprem324",
          modifier =
            Modifier.fillMaxSize().graphicsLayer { rotationY = 180f }, // Un-flip the backside image
          contentScale = androidx.compose.ui.layout.ContentScale.Crop,
          error = painterResource(R.drawable.person),
          placeholder = painterResource(R.drawable.person)
        )
      }
    }

    Spacer(Modifier.height(4.dp))

    Text(
      text = if (rotation <= 90f) "Echo Music" else "techprem324",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface,
    )
    Row(
      horizontalArrangement = Arrangement.spacedBy(6.dp),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
      ) {
        Text(
          text = BuildConfig.VERSION_NAME,
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Medium,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
        )
      }
      if (BuildConfig.DEBUG) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.error.copy(alpha = 0.10f),
        ) {
          Text(
            text = "DEBUG",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          )
        }
      } else {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f),
        ) {
          Text(
            text = BuildConfig.ARCHITECTURE.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
          )
        }
      }
    }
  }
}
