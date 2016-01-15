package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.SampNativeFunction;
import net.gtaun.shoebill.amx.types.ReturnType;

/**
 * Created by marvin on 20.12.2014 in project shoebill-runtime.
 * Copyright (c) 2014 Marvin Haschker. All rights reserved.
 */
public class AmxCallableImpl implements AmxCallable {

    private AmxInstance amxInstance;
    private String name;
    private AmxCallableType type;
    private int id;
    private ReturnType returnType;

    public AmxCallableImpl(AmxInstance instance, int id, String name, AmxCallableType type, ReturnType returnType) {
        this.amxInstance = instance;
        this.name = name;
        this.type = type;
        this.id = id;
        this.returnType = returnType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ReturnType getReturnType() {
        return returnType;
    }

    @Override
    public Object call(Object... args) {
        for(int i = 0; i < args.length; i++) {
            if(args[i] instanceof Boolean) {
                Boolean bool = (Boolean) args[i];
                args[i] = (bool) ? 1 : 0;
            }
        }
        try {
            if (type == AmxCallableType.NATIVE)
                return SampNativeFunction.callFunction(amxInstance.getHandle(), id, returnType.getValue(), args);
            else if (type == AmxCallableType.PUBLIC)
                return SampNativeFunction.callPublic(amxInstance.getHandle(), id, returnType.getValue(), args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
