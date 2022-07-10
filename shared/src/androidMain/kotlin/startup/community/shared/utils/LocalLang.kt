package startup.community.shared.utils
import java.util.*

actual fun getDeviceLang(): String? = Locale.getDefault().language