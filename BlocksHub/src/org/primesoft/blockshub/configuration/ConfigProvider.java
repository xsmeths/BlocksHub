/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2013, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) BlocksHub contributors
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution,
 * 3. Redistributions of source code, with or without modification, in any form 
 *    other then free of charge is not allowed,
 * 4. Redistributions in binary form in any form other then free of charge is 
 *    not allowed.
 * 5. Any derived work based on or containing parts of this software must reproduce 
 *    the above copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided with the 
 *    derived work.
 * 6. The original author of the software is allowed to change the license 
 *    terms or the entire license of the software as he sees fit.
 * 7. The original author of the software is allowed to sublicense the software 
 *    or its parts using any license terms he sees fit.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.primesoft.blockshub.configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;
import org.primesoft.blockshub.platform.api.IConfiguration;
import org.primesoft.blockshub.platform.api.IConfigurationSection;
import org.primesoft.blockshub.platform.api.IPlatform;

/**
 * This class contains configuration
 *
 * @author SBPrime
 */
public class ConfigProvider {

    private static final int CONFIG_VERSION = 2;
    private static boolean m_isConfigUpdate = false;
    private static int m_configVersion;
    private final static HashSet<String> m_enabledWorlds = new HashSet<String>();

    public static int getConfigVersion() {
        return m_configVersion;
    }

    /**
     * Is the world being logged
     *
     * @param world
     * @return
     */
    public static boolean isLogging(String world) {
        if (world == null) {
            return false;
        }                
        
        if (m_enabledWorlds.contains(world.toLowerCase())) {
            return true;
        }
        
        for (String pattern : m_enabledWorlds) {
            if (Pattern.matches(pattern, world)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Is the configuration up to date
     *
     * @return
     */
    public static boolean isConfigUpdated() {
        return m_isConfigUpdate;
    }

    /**
     * Load configuration
     *
     * @param platform
     * @return true if config loaded
     */
    public static boolean load(IPlatform platform) {
        if (platform == null) {
            return false;
        }

        IConfiguration config = platform.getConfiguration();

        IConfigurationSection mainSection = config.getConfigurationSection("BlocksHub");
        if (mainSection == null) {
            return false;
        }
        m_configVersion = mainSection.getInt("version", 1);
        m_isConfigUpdate = m_configVersion == CONFIG_VERSION;

        List<String> worlds = mainSection.getStringList("worlds");
        if (worlds == null) {
            worlds = new ArrayList<String>();
        }
        m_enabledWorlds.clear();
        for (String world : worlds) {
            world = world.toLowerCase();
            if (!m_enabledWorlds.contains(world)) {
                m_enabledWorlds.add(world);
            }
        }
           
        return true;
    }
}