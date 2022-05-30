package ss.team16.nthulostfound.domain.model

import java.util.*

data class ItemData(
    var name: String = "",
    var description: String = "",
    var date: Date = Date(),
    var place: String = "",
    var how: String = "",
    var images: List<UploadedImage> = emptyList(),
)
