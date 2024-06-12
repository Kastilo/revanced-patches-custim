package app.revanced.patches.reddit.customclients.joeyforreddit.api

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.extensions.InstructionExtensions.replaceInstructions
import app.revanced.patches.reddit.customclients.joeyforreddit.detection.piracy.disablePiracyDetectionPatch
import app.revanced.patches.reddit.customclients.spoofClientPatch

@Suppress("unused")
val spoofClientPatch = spoofClientPatch(redirectUri = "https://127.0.0.1:65023/authorize_callback") { clientIdOption ->
    dependsOn(disablePiracyDetectionPatch)

    compatibleWith(
        "o.o.joey",
        "o.o.joey.pro",
        "o.o.joey.dev",
    )

    val getClientIdFingerprintResult by getClientIdFingerprint
    val authUtilityUserAgentFingerprintResult by authUtilityUserAgentFingerprint

    val clientId by clientIdOption

    execute {
        // region Patch client id.

        getClientIdFingerprintResult.mutableMethod.addInstructions(
            0,
            """
                 const-string v0, "$clientId"
                 return-object v0
            """,
        )

        // endregion

        // region Patch user agent.

        // Use a random user agent.
        val randomName = (0..100000).random()
        val userAgent = "$randomName:app.revanced.$randomName:v1.0.0 (by /u/revanced)"

        authUtilityUserAgentFingerprintResult.mutableMethod.replaceInstructions(
            0,
            """
                const-string v0, "$userAgent"
                return-object v0
            """,
        )

        // endregion
    }
}
