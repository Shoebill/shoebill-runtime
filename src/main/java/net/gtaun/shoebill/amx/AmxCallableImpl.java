package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.SampNativeFunction;

/**
 * Created by marvin on 20.12.2014 in project shoebill-runtime.
 * Copyright (c) 2014 Marvin Haschker. All rights reserved.
 */
public class AmxCallableImpl implements AmxCallable {

    private AmxInstance amxInstance;
    private String name;
    private AmxCallableType type;
    private int id;

    public AmxCallableImpl(AmxInstance instance, int id, String name, AmxCallableType type)
    {
        this.amxInstance = instance;
        this.name = name;
        this.type = type;
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object call(Object... args) {
        try {
            if (type == AmxCallableType.NATIVE)
                return SampNativeFunction.callFunction(amxInstance.getHandle(), id, args);
            else if (type == AmxCallableType.PUBLIC)
                return SampNativeFunction.callPublic(amxInstance.getHandle(), id, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
