package app.revanced.patches.spotify.navbar

import app.revanced.patcher.extensions.InstructionExtensions.addInstructions
import app.revanced.patcher.patch.bytecodePatch
import app.revanced.patcher.patch.resourcePatch
import app.revanced.patches.shared.misc.mapping.get
import app.revanced.patches.shared.misc.mapping.resourceMappingPatch
import app.revanced.patches.shared.misc.mapping.resourceMappings

@Suppress("unused")
val premiumNavbarTabPatch = bytecodePatch(
    name = "Premium navbar tab",
    description = "Hides the premium tab from the navigation bar.",
) {
    dependsOn(premiumNavbarTabResourcePatch)

    compatibleWith("com.spotify.music")

    val addNavbarItemFingerprintResult by addNavBarItemFingerprint

    // If the navigation bar item is the premium tab, do not add it.
    execute {
        addNavbarItemFingerprintResult.mutableMethod.addInstructions(
            0,
            """
                const v1, $premiumTabId
                if-ne p5, v1, :continue
                return-void
                :continue
                nop
            """,
        )
    }
}

internal var showBottomNavigationItemsTextId = -1L
    private set
internal var premiumTabId = -1L
    private set

@Suppress("unused")
val premiumNavbarTabResourcePatch = resourcePatch {
    dependsOn(resourceMappingPatch)

    execute { context ->
        premiumTabId = resourceMappings["id", "premium_tab"]

        showBottomNavigationItemsTextId = resourceMappings[
            "bool",
            "show_bottom_navigation_items_text",
        ]
    }
}
