/**
 * Copyright (C) 2011-2014 MK124
 * Copyright (C) 2011 JoJLlmAn
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

package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.SampObjectStoreImpl;
import net.gtaun.shoebill.constant.TextDrawAlign;
import net.gtaun.shoebill.constant.TextDrawFont;
import net.gtaun.shoebill.data.Color;
import net.gtaun.shoebill.data.Vector2D;
import net.gtaun.shoebill.event.destroyable.DestroyEvent;
import net.gtaun.shoebill.exception.CreationFailedException;
import net.gtaun.shoebill.object.Player;
import net.gtaun.shoebill.object.Textdraw;
import net.gtaun.util.event.EventManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author MK124, JoJLlmAn
 */
public class TextdrawImpl implements Textdraw {
    private final EventManager rootEventManager;

    private int id = INVALID_ID;
    private Vector2D position;
    private String text;

    private boolean[] isPlayerShowed = new boolean[SampObjectStoreImpl.MAX_PLAYERS];


    public TextdrawImpl(EventManager eventManager, SampObjectStoreImpl store, float x, float y, String text) {
        this(eventManager, store, x, y, text, true, -1);
    }

    public TextdrawImpl(EventManager eventManager, SampObjectStoreImpl store, float x, float y, String text, boolean doInit, int id) throws CreationFailedException {
        this.rootEventManager = eventManager;
        this.text = text;
        position = new Vector2D(x, y);
        for (int i = 0; i < isPlayerShowed.length; i++)
            isPlayerShowed[i] = false;

        if (StringUtils.isEmpty(text)) this.text = " ";

        if (doInit || id < 0)
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> setup(store, SampNativeFunction.textDrawCreate(x, y, this.text)));
        else
            setup(store, id);
    }

    private void setup(SampObjectStoreImpl store, int id) {
        if (id == INVALID_ID) throw new CreationFailedException();

        this.id = id;
        store.setTextdraw(id, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE).append("id", id).toString();
    }

    @Override
    public void destroy() {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawDestroy(id));

        destroyWithoutExec();
    }

    public void destroyWithoutExec() {
        if (isDestroyed()) return;
        DestroyEvent destroyEvent = new DestroyEvent(this);
        rootEventManager.dispatchEvent(destroyEvent, this);
        id = INVALID_ID;
    }

    @Override
    public boolean isDestroyed() {
        return id == INVALID_ID;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Vector2D getPosition() {
        return position.clone();
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        if (isDestroyed()) return;
        if (text == null) throw new NullPointerException();
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawSetString(id, text));
        setTextWithoutExec(text);
    }

    @Override
    public void setLetterSize(float x, float y) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawLetterSize(id, x, y);
    }

    @Override
    public void setLetterSize(Vector2D vec) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawLetterSize(id, vec.getX(), vec.getY());
    }

    @Override
    public void setTextSize(float x, float y) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawTextSize(id, x, y);
    }

    @Override
    public void setTextSize(Vector2D vec) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawTextSize(id, vec.getX(), vec.getY());
    }

    @Override
    public void setAlignment(TextDrawAlign alignment) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawAlignment(id, alignment.getValue());
    }

    @Override
    public void setColor(Color color) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawColor(id, color.getValue());
    }

    @Override
    public void setUseBox(boolean use) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawUseBox(id, use);
    }

    @Override
    public void setBoxColor(Color color) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawBoxColor(id, color.getValue());
    }

    @Override
    public void setShadowSize(int size) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetShadow(id, size);
    }

    @Override
    public void setOutlineSize(int size) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetOutline(id, size);
    }

    @Override
    public void setBackgroundColor(Color color) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawBackgroundColor(id, color.getValue());
    }

    @Override
    public void setFont(TextDrawFont font) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawFont(id, font.getValue());
    }

    @Override
    public void setProportional(boolean set) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetProportional(id, set ? 1 : 0);
    }

    @Override
    public void setSelectable(boolean set) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetSelectable(id, set ? 1 : 0);
    }

    public void setTextWithoutExec(String text) {
        if (isDestroyed()) return;
        if (text == null) throw new NullPointerException();
        this.text = text;
    }

    @Override
    public void setPreviewModel(int modelindex) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetPreviewModel(id, modelindex);
    }

    @Override
    public void setPreviewModelRotation(float rotX, float rotY, float rotZ) {
        setPreviewModelRotation(rotX, rotY, rotZ, 1.0f);
    }

    @Override
    public void setPreviewModelRotation(float rotX, float rotY, float rotZ, float zoom) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetPreviewRot(id, rotX, rotY, rotZ, zoom);
    }

    @Override
    public void setPreviewVehicleColor(int color1, int color2) {
        if (isDestroyed()) return;

        SampNativeFunction.textDrawSetPreviewVehCol(id, color1, color2);
    }

    @Override
    public void show(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;

        int playerId = player.getId();

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawShowForPlayer(playerId, id));
        showWithoutExec(player);
    }

    public void showWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        isPlayerShowed[playerId] = true;
    }

    @Override
    public void hide(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawHideForPlayer(playerId, id));
        hideWithoutExec(player);
    }

    public void hideWithoutExec(Player player) {
        if (isDestroyed()) return;
        if (!player.isOnline()) return;
        int playerId = player.getId();
        isPlayerShowed[playerId] = false;
    }

    @Override
    public void showForAll() {
        if (isDestroyed()) return;

        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawShowForAll(id));
        showForAllWithoutExec();
    }

    public void showForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerShowed.length; i++)
            isPlayerShowed[i] = true;
    }

    @Override
    public void hideForAll() {
        if (isDestroyed()) return;
        SampEventDispatcher.getInstance().executeWithoutEvent(() -> SampNativeFunction.textDrawHideForAll(id));
        hideForAllWithoutExec();
    }

    public void hideForAllWithoutExec() {
        if (isDestroyed()) return;
        for (int i = 0; i < isPlayerShowed.length; i++)
            isPlayerShowed[i] = false;
    }

    @Override
    public boolean isShowed(Player player) {
        return !isDestroyed() && isPlayerShowed[player.getId()];

    }
}
