/**
 * Copyright (C) 2011-2014 MK124
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.gtaun.shoebill;

import net.gtaun.shoebill.samp.SampCallbackHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author MK124
 */
public class SampEventLogger implements SampCallbackHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShoebillImpl.class);


    public SampEventLogger() {

    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean onPlayerConnect(int playerId) {
        LOGGER.info("[join] " + SampNativeFunction.getPlayerName(playerId) + " has joined the server (" + playerId + ":" + SampNativeFunction.getPlayerIp(playerId) + ")");
        return true;
    }

    @Override
    public boolean onPlayerDisconnect(int playerId, int reason) {
        LOGGER.info("[part] " + SampNativeFunction.getPlayerName(playerId) + " has left the server (" + playerId + ":" + reason + ")");
        return true;
    }

    @Override
    public int onPlayerText(int playerId, String text) {
        LOGGER.info("[chat] " + SampNativeFunction.getPlayerName(playerId) + ": " + text);
        return 1;
    }

    @Override
    public int onRconCommand(String cmd) {
        LOGGER.info("[rcon] command: " + cmd);
        return 1;
    }

    @Override
    public int onRconLoginAttempt(String ip, String password, int isSuccess) {
        if (isSuccess == 0) {
            LOGGER.info("[rcon] " + " bad rcon attempy by: " + ip + " (" + password + ")");
        } else {
            LOGGER.info("[rcon] " + ip + " has logged.");
        }

        return 1;
    }
}
