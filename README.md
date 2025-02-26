# MCVersionRenamer
MCVersionRenamer is a tool designed to assist Minecraft mod developers in managing and renaming Minecraft version files. It streamlines the process of updating and maintaining mod compatibility across different Minecraft versions.

## Features
- Version Management: Easily rename and organize Minecraft version files to match your mod's requirements.
- Compatibility Support: Ensure your mod functions correctly across various Minecraft versions.
- User-Friendly Interface: Navigate through the tool with ease, thanks to its intuitive design.

## Installation
- Download: Visit the Releases section of the repository to download the latest version.
- Setup: Put the `mcversionrenamer-<latest-release-version>.jar` into your `mods` folder of your Fabric installation.
- Run Minecraft: Pretty simple, just run Minecraft.

## (IMPORTANT!)
MCVersionRenamer requires a few dependencies to work (some dependencies may be shaded later once I get around to that):
- Fabric API: https://modrinth.com/mod/fabric-api
- MCVersionRenamer: https://github.com/KyfStore11k/MCVersionRenamer/releases (PS: This is the mod your viewing right now!)

MCVersionRenamer also recommends dependencies! Here are some you should consider:
- Mod Menu: https://modrinth.com/mod/modmenu
- BetterF3: https://modrinth.com/mod/betterf3

## External Mod Notes
- BetterF3: If you first launch Minecraft with BetterF3 and see the default Minecraft version text, then you will have to do some extra configuring. (FROM HERE, IT IS recommended TO INSTALL MOD MENU TO EDIT THE BETTERF3 CONFIG!) Begin by going to the mods section and entering the BetterF3 config. Select `Left Modules` and remove the default `Minecraft` module at the very top. Then select `Add New Module`, and change the default `Separator` to `MCVersionRenamer Custom Version Text`. If it doesn't appear on top, scroll all the way down, and click the up arrow until it reaches the top of the list. `Save and Quit` the changes and play the game, it should now reflect your MCVersionRenamer custom text!

## Working On
Nothing!

## License
This project is licensed under the MIT License. See the LICENSE file for more details.

## Acknowledgments
Special thanks to the Minecraft modding community for their continuous support and contributions.

For more information and updates, visit the [GitHub](https://github.com/KyfStore11k/MCVersionRenamer) repository.