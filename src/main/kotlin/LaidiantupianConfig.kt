package nya.xfy

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object LaidiantupianConfig: AutoSavePluginConfig("config") {
    val command:String by value("""好无聊""")
    val num:Int by value(1)
}