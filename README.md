
# SamSkyBridge Plugin

## Overview
SamSkyBridge is a custom Spigot plugin designed to enhance your skyblock experience. It integrates with **BentoBox** and **BSkyBlock**, offering features such as **leveling systems**, **island upgrades**, **team member expansion**, **XP tracking**, and **ranking systems**.

## Features
- **Leveling System**: Level up your island by placing blocks (XP for different blocks can be customized).
- **Island Upgrades**: Increase your island size and max team members through a user-friendly GUI.
- **Ranking System**: View island rankings based on experience points, level, and island size.
- **Barrier System**: Display a white particle barrier when upgrading your island size.
- **Vault Integration**: Supports Vault for economy-based purchases and upgrades.
- **Pixelmon Support**: Includes specific support for Pixelmon-related blocks, adding XP based on Pixelmon block usage.

## Installation
1. Download the latest `.jar` from the GitHub Actions artifacts or compile using Maven.
2. Place the `.jar` file in your server's `plugins/` directory.
3. Start/restart your server.

## Configuration
The configuration files can be found in `plugins/SamSkyBridge/`:
- `config.yml`: General settings, including XP rates, upgrade costs, and particle configurations.
- `blocks.yml`: Customize XP values for different blocks placed on the island.
- `messages_ko.yml`: Localization (Korean). Customize messages shown to players.

## Commands
- `/섬 레벨`: View your island's level and experience.
- `/섬 업그레이드`: Open the GUI to upgrade your island size or team capacity.
- `/섬 랭킹`: Check the rankings of islands based on experience and level.

## GitHub Actions
This project uses **GitHub Actions** for automated builds:
1. Push your changes to GitHub.
2. The build will trigger automatically and a `.jar` file will be created and uploaded as an artifact.
3. You can download the latest `.jar` from the **Actions** tab on GitHub.

## License
This project is licensed under the MIT License.

