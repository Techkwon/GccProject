package woogear.kwon.gccproject.model

data class GccResponse(
    val msg: String,
    val data: Places
) {
    data class Places(
        val totalCount: Int,
        val product: List<Place>
    )
}