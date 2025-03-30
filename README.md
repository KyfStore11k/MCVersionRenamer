# MCVersionRenamer
MCVersionRenamer is a tool designed to assist Minecraft mod developers in managing and renaming Minecraft version files. It streamlines the process of updating and maintaining mod compatibility across different Minecraft versions.

## Features
- Version Management: Easily rename your Minecraft Version, Title, and F3 version!
- Compatibility Support: Supports and integrates with some mods including: ModMenu; BetterF3; Clean F3
- User-Friendly Interface: Navigate through the tool with ease, thanks to its intuitive design! (Possible with owo-lib and LibGui)
## Installation
- Download: Visit the Releases section of the repository to download the latest version.
- Setup: Put the `mcversionrenamer-<latest-release-version>.jar` into your `mods` folder of your Fabric installation.
- Run Minecraft: Pretty simple, just run Minecraft.

## (IMPORTANT!)
MCVersionRenamer requires a few dependencies to work (some dependencies may be shaded later once I get around to that):
- Fabric API: https://modrinth.com/mod/fabric-api
- MCVersionRenamer: https://github.com/KyfStore11k/MCVersionRenamer/releases (PS: This is the mod your viewing right now!)

MCVersionRenamer also recommends a few dependencies:
- ModMenu: https://modrinth.com/mod/modmenu
- BetterF3: https://modrinth.com/mod/betterf3
- CleanF3: https://modrinth.com/mod/clean-f3

**If you encounter any errors, please make an issue immediately and I will fix it as fast as possible.**

## External Mod Notes

### FancyMenu:

FancyMenu (as of now) currently has half-implemented functionality.
I will be attempting to fix the scenario in which if `Current Screen Customization` is enabled,
it will overwrite MCVersionRenamer's custom version text
(TitleScreen only).

## Working On (*Not Implemented*)
- A complete FancyMenu hook for MCVersionRenamer!

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Acknowledgments
Special thanks to the Minecraft modding community for their continuous support and contributions.

For more information and updates, visit the [GitHub](https://github.com/KyfStore11k/MCVersionRenamer) repository.