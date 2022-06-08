package ss.team16.nthulostfound.domain.model

data class ItemsConnection(
    val edges: List<ItemEdge>,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean
)

data class ItemEdge (
    val item: ItemData,
    val cursor: String
)