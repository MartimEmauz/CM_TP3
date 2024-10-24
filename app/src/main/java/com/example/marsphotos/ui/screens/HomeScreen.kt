package com.example.marsphotos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.R
import com.example.marsphotos.model.MarsPhoto
import com.example.marsphotos.model.PicsumPhoto
import com.example.marsphotos.ui.theme.MarsPhotosTheme

@Composable
fun HomeScreen(
    marsUiState: MarsUiState,
    picsumUiState: PicsumUiState,
    marsViewModel: MarsViewModel,
    picsumViewModel: PicsumViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        when (picsumUiState) {
            is PicsumUiState.Loading -> LoadingScreen()
            is PicsumUiState.Success -> PicsumResultScreen(
                message = "Success: Picsum photo retrieved",
                picsumPhoto = picsumUiState.randomPhoto
            )
            is PicsumUiState.Error -> ErrorScreen()
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (marsUiState) {
            is MarsUiState.Loading -> LoadingScreen()
            is MarsUiState.Success -> ResultScreen(
                photos = marsUiState.photos,
                randomPhoto = marsUiState.randomPhoto
            )
            is MarsUiState.Error -> ErrorScreen()
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Button(onClick = {
                marsViewModel.getMarsPhotos()
                picsumViewModel.getPicsumPhotos()
            }) {
                Text("Roll")
            }
            Button(onClick = {
                val currentPicsumPhoto = (picsumUiState as? PicsumUiState.Success)?.randomPhoto
                currentPicsumPhoto?.let { picsumViewModel.applyBlur(it) }
            }) {
                Text("Blur")
            }
            Button(onClick = {
                val currentPicsumPhoto = (picsumUiState as? PicsumUiState.Success)?.randomPhoto
                currentPicsumPhoto?.let { picsumViewModel.applyGrayscale(it) }
            }) {
                Text("Gray")
            }
        }
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
        Text(text = stringResource(R.string.loading), modifier = Modifier.padding(16.dp))
    }
}

/**
 * The home screen displaying an error message.
 */
@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = stringResource(R.string.loading_failed)
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

/**
 * ResultScreen displaying a Mars photo.
 */
@Composable
fun ResultScreen(photos: String, randomPhoto: MarsPhoto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = photos)
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(randomPhoto.imgSrc)
                .crossfade(true)
                .build(),
            contentDescription = "A photo of Mars",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

/**
 * ResultScreen displaying the Picsum photo.
 */
@Composable
fun PicsumResultScreen(message: String, picsumPhoto: PicsumPhoto, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = message)

        AsyncImage(
            model = picsumPhoto.download_url,
            contentDescription = "Picsum Photo",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MarsPhotosTheme {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    MarsPhotosTheme {
        ErrorScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PhotosGridScreenPreview() {
    MarsPhotosTheme {
        ResultScreen(
            photos = "Photos Loaded",
            randomPhoto = MarsPhoto(id = "1", imgSrc = "https://example.com/photo.jpg"),
            modifier = Modifier
        )
    }
}
