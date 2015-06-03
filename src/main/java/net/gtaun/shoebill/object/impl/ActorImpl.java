package net.gtaun.shoebill.object.impl;

import net.gtaun.shoebill.SampEventDispatcher;
import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.data.AngledLocation;
import net.gtaun.shoebill.data.Location;
import net.gtaun.shoebill.data.Vector3D;
import net.gtaun.shoebill.object.Actor;
import net.gtaun.shoebill.object.Player;

/**
 * Created by marvin on 01.05.15 in project shoebill-runtime.
 * Copyright (c) 2015 Marvin Haschker. All rights reserved.
 */
public class ActorImpl implements Actor {
    private int id, modelid;


    public ActorImpl(int modelid, Vector3D pos, float angle) {
        this(modelid, pos, angle, true, -1);
    }

    public ActorImpl(int modelid, Vector3D pos, float angle, boolean doExec, int id) {
        this.modelid = modelid;
        if (doExec)
            SampEventDispatcher.getInstance().executeWithoutEvent(() -> this.id = SampNativeFunction.createActor(modelid, pos.x, pos.y, pos.z, angle));
        else this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getModel() {
        return modelid;
    }

    @Override
    public AngledLocation getLocation() {
        AngledLocation pos = new AngledLocation();
        SampNativeFunction.getActorPos(id, pos);

        pos.angle = SampNativeFunction.getActorFacingAngle(id);
        pos.worldId = SampNativeFunction.getActorVirtualWorld(id);

        return pos;
    }

    @Override
    public int getVirtualWorld() {
        return SampNativeFunction.getActorVirtualWorld(id);
    }

    @Override
    public float getAngle() {
        return SampNativeFunction.getActorFacingAngle(id);
    }

    @Override
    public float getHealth() {
        return SampNativeFunction.getActorHealth(id);
    }

    @Override
    public void setLocation(AngledLocation loc) {
        SampNativeFunction.setActorPos(id, loc.x, loc.y, loc.z);
        SampNativeFunction.setActorVirtualWorld(id, loc.worldId);
        SampNativeFunction.setActorFacingAngle(id, loc.angle);
    }

    @Override
    public void setLocation(Location loc) {
        SampNativeFunction.setActorPos(id, loc.x, loc.y, loc.z);
        SampNativeFunction.setActorVirtualWorld(id, loc.worldId);
    }

    @Override
    public void setLocation(Vector3D pos) {
        SampNativeFunction.setActorPos(id, pos.x, pos.y, pos.z);
    }

    @Override
    public void setAngle(float angle) {
        SampNativeFunction.setActorFacingAngle(id, angle);
    }

    @Override
    public void setVirtualWorld(int world) {
        SampNativeFunction.setActorVirtualWorld(id, world);
    }

    @Override
    public void setHealth(float health) {
        SampNativeFunction.setActorHealth(id, health);
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        SampNativeFunction.setActorInvulnerable(id, invulnerable);
    }

    @Override
    public boolean isInvulnerable() {
        return SampNativeFunction.isActorInvulnerable(id) > 0;
    }

    @Override
    public void applyAnimation(String animLib, String animName, float animSpeed, boolean loop, boolean lockX, boolean lockY, boolean freeze, int time) {
        SampNativeFunction.applyActorAnimation(id, animLib, animName, animSpeed, loop ? 1 : 0, lockX ? 1 : 0, lockY ? 1 : 0, freeze ? 1 : 0, time);
    }

    @Override
    public void clearAnimation() {
        SampNativeFunction.clearActorAnimations(id);
    }

    @Override
    public boolean isActorStreamedIn(Player player) {
        return SampNativeFunction.isActorStreamedIn(id, player.getId()) > 0;
    }

    @Override
    public void destroy() {
        if (isDestroyed()) return;

        SampNativeFunction.destroyActor(id);
        this.id = -1;
    }

    @Override
    public boolean isDestroyed() {
        return id < 0;
    }

    public void destroyWithoutExec() {
        if (isDestroyed()) return;
        this.id = -1;
    }
}
