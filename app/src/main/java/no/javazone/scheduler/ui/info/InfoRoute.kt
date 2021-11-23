package no.javazone.scheduler.ui.info

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.WifiLock
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

@Composable
fun InfoRoute(

) {
    val context = LocalContext.current

    InfoContent(
        onCodeOfConductClick = {
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.java.no/principles.html"))
            context.startActivity(intent)
        },
        onLicenseDisplay = {
            val intent = Intent(context, OssLicensesMenuActivity::class.java)
            context.startActivity(intent)
        },
        onGithubClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/javaBin/javazone-android-app")
            )
            context.startActivity(intent)
        },
        onJavaBinClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.java.no/"))
            context.startActivity(intent)
        },
        onPolicyClick = {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.java.no/policy.html#"))
            context.startActivity(intent)
        },
    )
}

@Composable
fun InfoContent(
    onCodeOfConductClick: () -> Unit,
    onLicenseDisplay: () -> Unit,
    onGithubClick: () -> Unit,
    onJavaBinClick: () -> Unit,
    onPolicyClick: () -> Unit
) {
    Surface() {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            InfoContentSection(
                title = "JavaZone",
                contents = listOf(
                    Triple(Icons.Filled.WifiLock, "WI-FI SSID: JavaZone", {}),
                    Triple(Icons.Filled.Launch, "Code of conduct", onCodeOfConductClick)
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            InfoContentSection(
                title = "",
                contents = listOf(
                    Triple(Icons.Filled.Launch, "Open source licences", onLicenseDisplay),
                    Triple(Icons.Filled.Launch, "github", onGithubClick)
                )
            )
            Spacer(modifier = Modifier.padding(5.dp))
            InfoContentSection(
                title = "javaBin",
                contents = listOf(
                    Triple(Icons.Filled.Launch, "javaBin", onJavaBinClick),
                    Triple(Icons.Filled.Launch, "Terms and Condition", onPolicyClick)
                )
            )
        }
    }
}

@Composable
fun InfoContentSection(
    title: String,
    contents: List<Triple<ImageVector, String, () -> Unit>>
) {
    Surface() {
        Column() {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = title
                )
            }
            Spacer(modifier = Modifier.padding(2.dp))
            contents.forEach { (icon, text, onClick) ->
                Row(
                    modifier = Modifier
                        .clickable {
                            onClick()
                        }
                ) {
                    Box(modifier = Modifier.layoutId("icon")) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null
                        )
                    }
                    Text(
                        modifier = Modifier.padding(start = 5.dp, top = 2.dp),
                        text = text
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun InfoContentPreview() {
    InfoContent(
        onCodeOfConductClick = {},
        onLicenseDisplay = {},
        onGithubClick = {},
        onJavaBinClick = {},
        onPolicyClick = {}
    )
}