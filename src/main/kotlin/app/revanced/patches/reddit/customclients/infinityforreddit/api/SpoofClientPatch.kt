package app.revanced.patches.reddit.customclients.infinityforreddit.api

import app.revanced.patcher.util.proxy.mutableTypes.MutableMethod.Companion.toMutable
import app.revanced.patcher.util.smali.toInstructions
import app.revanced.patches.reddit.customclients.spoofClientPatch
import com.android.tools.smali.dexlib2.AccessFlags
import com.android.tools.smali.dexlib2.immutable.ImmutableMethod
import com.android.tools.smali.dexlib2.immutable.ImmutableMethodImplementation

@Suppress("unused")
val spoofClientPatch = spoofClientPatch(redirectUri = "infinity://localhost") { clientIdOption ->
    compatibleWith("ml.docilealligator.infinityforreddit")

    val apiUtilsFingerprintResult by apiUtilsFingerprint

    val clientId by clientIdOption

    execute {
        apiUtilsFingerprintResult.mutableClass.methods.apply {
            val getClientIdMethod = single { it.name == "getId" }.also(::remove)

            val newGetClientIdMethod = ImmutableMethod(
                getClientIdMethod.definingClass,
                getClientIdMethod.name,
                null,
                getClientIdMethod.returnType,
                AccessFlags.PUBLIC.value or AccessFlags.STATIC.value,
                null,
                null,
                ImmutableMethodImplementation(
                    1,
                    """
                        const-string v0, "$clientId"
                        return-object v0
                    """.toInstructions(getClientIdMethod),
                    null,
                    null,
                ),
            ).toMutable()

            add(newGetClientIdMethod)
        }
    }
}
