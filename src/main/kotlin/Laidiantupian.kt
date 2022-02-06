package nya.xfy

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.sendImage
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeMessages
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.info
import nya.xfy.LaidiantupianConfig.command
import nya.xfy.LaidiantupianConfig.num
import nya.xfy.LaidiantupianData.btstuApi
import nya.xfy.LaidiantupianData.iw233Api
import nya.xfy.LaidiantupianData.loliconApi
import nya.xfy.LaidiantupianData.mikuApi
import nya.xfy.LaidiantupianData.mtyApi
import nya.xfy.LaidiantupianData.toubiecApi
import nya.xfy.LaidiantupianData.touhouApi
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit

object Laidiantupian : KotlinPlugin(
    JvmPluginDescription(
        id = "nya.xfy.laidiantupian",
        version = "1.0-SNAPSHOT",
    )
) {
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).writeTimeout(5, TimeUnit.SECONDS).connectTimeout(5, TimeUnit.SECONDS).build()
    @OptIn(ExperimentalSerializationApi::class)
    override fun onEnable() {
        logger.info { "Plugin loaded" }
        LaidiantupianConfig.reload()
        LaidiantupianConfig.reload()
        this.globalEventChannel().subscribeMessages {
            matching(Regex(command)){
                lateinit var response:Response
                try {
                    for (i in 1..num+1){
                        when (Date().time%7) {
                            0L -> {
                                response = okHttpClient.newCall(Request.Builder().url(loliconApi).build()).execute()
                                logger.info("正在从${response.request.url.host}获取图片")
                                if (response.isSuccessful) {
                                    val loliconResponse: LoliconResponse = Json.decodeFromString(response.body!!.string())
                                    if (loliconResponse.data.isNotEmpty()) {
                                        for (item in loliconResponse.data) {
                                            sendImg(subject, item.urls.original)
                                        }
                                    }
                                }
                            }
                            1L -> sendImg(subject, iw233Api)
                            2L -> {
                                response = okHttpClient.newCall(Request.Builder().url(mikuApi).build()).execute()
                                logger.info("正在从${response.request.url.host}获取图片")
                                if (response.isSuccessful) {
                                    val mikuResponse: MikuResponse = Json.decodeFromString(response.body!!.string())
                                    sendImg(subject, mikuResponse.url)
                                }
                            }
                            3L -> sendImg(subject, touhouApi)
                            4L -> sendImg(subject, toubiecApi)
                            5L -> sendImg(subject, btstuApi)
                            6L -> sendImg(subject, mtyApi)
                        }
                    }
                } catch (e: IllegalStateException) {
                    subject.sendMessage("图片上传失败")
                } catch (e: SocketTimeoutException) {
                    subject.sendMessage("图片获取超时")
                } catch (e: SocketException) {
                    subject.sendMessage("图片获取失败")
                } catch (e: Throwable) {
                    logger.error(e)
                    subject.sendMessage("哎呀，出错了")
                }finally {
                    response.close()
                }
            }
        }
    }

    override fun onDisable() {
        okHttpClient.dispatcher.executorService.shutdown()
        okHttpClient.connectionPool.evictAll()
        okHttpClient.cache?.close()
        super.onDisable()
    }
    private suspend fun sendImg(subject: Contact,api:String){
        val response = okHttpClient.newCall(Request.Builder().url(api).build()).execute()
        logger.info("正在从${response.request.url.host}获取图片")
        if(response.isSuccessful)
            subject.sendImage(response.body!!.byteStream().toExternalResource().toAutoCloseable())
        else
            subject.sendMessage("哎呀，图片失踪了")
        response.close()
        logger.info("发送完毕")
    }
}

