package no.javazone.scheduler.ui.info

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.core.net.toUri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Accessible
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiLock
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.oss.licenses.v2.OssLicensesMenuActivity
import no.javazone.scheduler.R
import no.javazone.scheduler.ui.theme.DarkOceanColors
import no.javazone.scheduler.ui.theme.JavaZoneTypography
import no.javazone.scheduler.ui.theme.JavaZoneTheme
import no.javazone.scheduler.ui.theme.LightOceanColors
import no.javazone.scheduler.utils.CONFERENCE_LOCATION
import no.javazone.scheduler.utils.PREVIEW_CONFERENCE_DATES
import no.javazone.scheduler.utils.PREVIEW_CONFERENCE_NAME
import no.javazone.scheduler.utils.PREVIEW_WORKSHOP_DATE

@Composable
fun InfoRoute(conferenceName: String, conferenceDates: String, workshopDate: String) {
    val context = LocalContext.current

    InfoContent(
        conferenceName = conferenceName,
        conferenceDates = conferenceDates,
        workshopDate = workshopDate,
        onCodeOfConductClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "https://www.java.no/principles.html".toUri())
            )
        },
        onLicenseDisplay = {
            OssLicensesMenuActivity.setTheme(LightOceanColors, DarkOceanColors, JavaZoneTypography)
            context.startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        },
        onGithubClick = {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "https://github.com/javaBin/javazone-android-app".toUri()
                )
            )
        },
        onJavaBinClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "https://www.java.no/".toUri())
            )
        },
        onPolicyClick = {
            context.startActivity(
                Intent(Intent.ACTION_VIEW, "https://www.java.no/policy.html#".toUri())
            )
        },
        onMapsClick = {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    (
                        "https://www.google.com/maps/place/NOVA+Spektrum/@59.9517849,11.0453395,1053m/" +
                            "data=!3m1!1e3!4m6!3m5!1s0x46417ca4649f3065:0xfb5d44914550f304!" +
                            "8m2!3d59.9501903!4d11.0564425!16s%2Fg%2F122tzzg4"
                    ).toUri()
                )
            )
        },
        onAccessibilityEmailClick = {
            context.startActivity(
                Intent(Intent.ACTION_SENDTO, "mailto:javazone@macsimum.no".toUri())
            )
        }
    )
}

@Composable
fun InfoContent(
    conferenceName: String,
    conferenceDates: String,
    workshopDate: String,
    onCodeOfConductClick: () -> Unit,
    onLicenseDisplay: () -> Unit,
    onGithubClick: () -> Unit,
    onJavaBinClick: () -> Unit,
    onPolicyClick: () -> Unit,
    onMapsClick: () -> Unit,
    onAccessibilityEmailClick: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = conferenceName,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "$conferenceDates · $CONFERENCE_LOCATION",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                InfoCard(title = stringResource(R.string.info_venue), icon = Icons.Filled.LocationOn) {
                    Text(
                        text = stringResource(R.string.info_venue_name),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.info_venue_address),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.info_venue_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onMapsClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Launch,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.open_in_google_maps))
                    }
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_getting_there), icon = Icons.Filled.Train) {
                    TransportRow(
                        icon = Icons.Filled.Train,
                        label = stringResource(R.string.info_train_label),
                        description = stringResource(R.string.info_train_description)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TransportRow(
                        icon = Icons.Filled.Flight,
                        label = stringResource(R.string.info_flytoget_label),
                        description = stringResource(R.string.info_flytoget_description)
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TransportRow(
                        icon = Icons.Filled.DirectionsBus,
                        label = stringResource(R.string.info_bus_label),
                        description = stringResource(R.string.info_bus_description)
                    )
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_parking), icon = Icons.Filled.LocalParking) {
                    Text(
                        text = stringResource(R.string.info_parking_spaces),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(R.string.info_parking_payment),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    ParkingRateRow(
                        label = stringResource(R.string.info_parking_small_vehicles),
                        rate = stringResource(R.string.info_parking_rate_small)
                    )
                    Spacer(Modifier.height(4.dp))
                    ParkingRateRow(
                        label = stringResource(R.string.info_parking_large_vehicles),
                        rate = stringResource(R.string.info_parking_rate_large)
                    )
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    TransportRow(
                        icon = Icons.AutoMirrored.Filled.Accessible,
                        label = stringResource(R.string.info_parking_disabled),
                        description = stringResource(R.string.info_parking_disabled_location)
                    )
                    Spacer(Modifier.height(4.dp))
                    TransportRow(
                        icon = Icons.Filled.EvStation,
                        label = stringResource(R.string.info_ev_charging),
                        description = stringResource(R.string.info_ev_charging_description)
                    )
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_workshop_venue), icon = Icons.Filled.Build) {
                    Text(
                        text = "$workshopDate · ${stringResource(R.string.info_workshop_times)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.info_workshop_venue_name),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.info_workshop_venue_address),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.info_workshop_directions),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_accessibility), icon = Icons.AutoMirrored.Filled.Accessible) {
                    Text(
                        text = stringResource(R.string.info_accessibility_description),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = onAccessibilityEmailClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.info_accessibility_email))
                    }
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_wifi), icon = Icons.Filled.Wifi) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.WifiLock,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.info_wifi_ssid_label),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                            Text(
                                text = stringResource(R.string.info_wifi_ssid_value),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_javazone_section), icon = Icons.Outlined.Info) {
                    LinkItem(label = stringResource(R.string.info_code_of_conduct), onClick = onCodeOfConductClick)
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_javabin_section), icon = Icons.Outlined.Groups) {
                    LinkItem(label = stringResource(R.string.info_javabin_website), onClick = onJavaBinClick)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    LinkItem(label = stringResource(R.string.info_terms_and_conditions), onClick = onPolicyClick)
                }
            }

            item {
                InfoCard(title = stringResource(R.string.info_about_app), icon = Icons.Filled.Code) {
                    LinkItem(label = stringResource(R.string.info_open_source_licences), onClick = onLicenseDisplay)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    LinkItem(label = stringResource(R.string.info_github), onClick = onGithubClick)
                }
            }
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            content()
        }
    }
}

@Composable
fun TransportRow(
    icon: ImageVector,
    label: String,
    description: String
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ParkingRateRow(label: String, rate: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = rate,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun LinkItem(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Launch,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
@Preview
fun InfoContentLightPreview() {
    InfoContent(
        conferenceName = PREVIEW_CONFERENCE_NAME,
        conferenceDates = PREVIEW_CONFERENCE_DATES,
        workshopDate = PREVIEW_WORKSHOP_DATE,
        onCodeOfConductClick = {},
        onLicenseDisplay = {},
        onGithubClick = {},
        onJavaBinClick = {},
        onPolicyClick = {},
        onMapsClick = {},
        onAccessibilityEmailClick = {}
    )
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun InfoContentDarkPreview() {
    JavaZoneTheme(useDarkTheme = true) {
        InfoContent(
            conferenceName = PREVIEW_CONFERENCE_NAME,
            conferenceDates = PREVIEW_CONFERENCE_DATES,
            workshopDate = PREVIEW_WORKSHOP_DATE,
            onCodeOfConductClick = {},
            onLicenseDisplay = {},
            onGithubClick = {},
            onJavaBinClick = {},
            onPolicyClick = {},
            onMapsClick = {},
            onAccessibilityEmailClick = {}
        )
    }
}
