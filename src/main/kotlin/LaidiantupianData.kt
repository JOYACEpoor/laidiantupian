package nya.xfy

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object LaidiantupianData:AutoSavePluginData("data") {
    val loliconApi:String by value("https://api.lolicon.app/setu/v2?r18=2&proxy=i.pixiv.re")
    val iw233Api:String by value("https://iw233.cn/api/Random.php")
    val mikuApi:String by value("https://hatsunemiku-tov.imwork.net/api/miku?proxy=i.pixiv.re")
    val touhouApi:String by value("https://img.paulzzh.com/touhou/random")
    val toubiecApi:String by value("https://acg.toubiec.cn/random.php")
    val btstuApi:String by value("https://api.btstu.cn/sjbz/api.php?lx=dongman&format=images")
    val mtyApi:String by value("https://api.mtyqx.cn/api/random.php")
}