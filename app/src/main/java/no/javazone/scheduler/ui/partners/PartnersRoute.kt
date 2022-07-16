package no.javazone.scheduler.ui.partners

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import no.javazone.scheduler.AppContainer
import no.javazone.scheduler.model.Partner
import no.javazone.scheduler.viewmodels.PartnersViewModel

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PartnersRoute(
    appContainer: AppContainer
) {
    val viewModel: PartnersViewModel = viewModel(
        factory = PartnersViewModel.provideFactory(appContainer.partnersRepository)
    )

    val partners = viewModel.partners.collectAsState().value
    val context = LocalContext.current

    PartnersContent(
        forwardToWeb = { url ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        },
        imageLoader = appContainer.imageLoader,
        partners = partners
    )
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun PartnersContent(
    forwardToWeb: (String) -> Unit,
    partners: List<Partner>,
    imageLoader: ImageLoader
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(partners) { partner ->
            Card(
                modifier = Modifier.padding(5.dp)
            ) {
                Image(
                    painter = rememberImagePainter(partner.logoUrl, imageLoader = imageLoader),
                    contentDescription = partner.name,
                    modifier = Modifier
                        .size(74.dp)
                        .clickable(enabled = partner.homepageUrl.isNotEmpty()) {
                            forwardToWeb(partner.homepageUrl)
                        },
                )
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
@Preview
fun PartnersContentPreview() {
    val partners = listOf(
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = "https://d3o108dy577i1m.cloudfront.net/2019/logos/systek.svg"
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = "https://d3o108dy577i1m.cloudfront.net/2020/logos/storebrand.png"
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
        Partner(
            name = "Foo Inc",
            homepageUrl = "https://wwww.vg.no",
            logoUrl = ""
        ),
        Partner(
            name = "Bar Inc",
            homepageUrl = "https://www.nettavisen.no",
            logoUrl = ""
        ),
    )

    PartnersContent(partners = partners, imageLoader = LocalImageLoader.current, forwardToWeb = { })
}