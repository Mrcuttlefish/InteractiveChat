/*
 * This file is part of InteractiveChat4.
 *
 * Copyright (C) 2020 - 2025. LoohpJames <jamesloohp@gmail.com>
 * Copyright (C) 2020 - 2025. Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.loohp.interactivechat.hooks.craftengine;

import com.loohp.interactivechat.api.InteractiveChatAPI;
import com.loohp.interactivechat.objectholders.ICPlayer;
import com.loohp.interactivechat.objectholders.ICPlayerFactory;
import net.momirealms.craftengine.bukkit.api.BukkitAdaptors;
import net.momirealms.craftengine.bukkit.api.CraftEngineItems;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class CraftEngineItemStackHook {

    public static void init() {
        Plugin craftEngine = Bukkit.getPluginManager().getPlugin("CraftEngine");
        InteractiveChatAPI.registerItemStackTransformProvider(craftEngine, 100, (itemStack, uuid) -> {
            if (itemStack == null || !CraftEngineItems.isCustomItem(itemStack)) {
                return itemStack;
            }
            try {
                Player player = null;
                if (uuid != null) {
                    ICPlayer icPlayer = ICPlayerFactory.getICPlayer(uuid);
                    if (icPlayer != null && icPlayer.isLocal()) {
                        player = icPlayer.getLocalPlayer();
                    }
                }
                net.momirealms.craftengine.core.entity.player.Player cePlayer =
                        player != null ? BukkitAdaptors.adapt(player) : null;
                return BukkitItemManager.instance().s2c(itemStack.clone(), cePlayer).orElse(itemStack);
            } catch (Throwable t) {
                return itemStack;
            }
        });
    }

    public static void unregister() {
        Plugin craftEngine = Bukkit.getPluginManager().getPlugin("CraftEngine");
        if (craftEngine != null) {
            InteractiveChatAPI.unregisterItemStackTransformProvider(craftEngine);
        }
    }

}
