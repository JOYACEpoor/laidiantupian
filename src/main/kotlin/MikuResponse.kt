package nya.xfy

import kotlinx.serialization.Serializable

@Serializable
data class MikuResponse(val pid:Int,val uid:Int,val title: String,val url: String)