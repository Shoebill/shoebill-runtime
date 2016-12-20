package net.gtaun.shoebill.amx;

import net.gtaun.shoebill.amx.types.ReferenceFloat;
import net.gtaun.shoebill.amx.types.ReferenceInt;
import net.gtaun.shoebill.amx.types.ReferenceString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marvin on 18.06.16 in project shoebill-runtime.
 * Copyright (c) 2016 Marvin Haschker. All rights reserved.
 */
class AmxCallableClasses {

    static Map<Class<?>, String> VALID_CLASSES = new HashMap<>();

    static {
        VALID_CLASSES.put(Integer.class, "i");
        VALID_CLASSES.put(Integer[].class, "i[]");

        VALID_CLASSES.put(Float.class, "f");
        VALID_CLASSES.put(Float[].class, "f[]");

        VALID_CLASSES.put(String.class, "s");
        VALID_CLASSES.put(String[].class, "s[]");

        VALID_CLASSES.put(ReferenceInt.class, "ri");
        VALID_CLASSES.put(ReferenceFloat.class, "rf");
        VALID_CLASSES.put(ReferenceString.class, "rs");
    }

    static Object primitiveToComposite(Object object) {
        if (object.getClass() == int.class) {
            return object;
        } else if (object.getClass() == float.class) {
            return object;
        } else if (object.getClass() == float[].class) {
            float[] oldArray = (float[]) object;
            Float[] newArray = new Float[oldArray.length];
            for (int i = 0; i < oldArray.length; i++) newArray[i] = oldArray[i];
            return newArray;
        } else if (object.getClass() == int[].class) {
            int[] oldArray = (int[]) object;
            Integer[] newArray = new Integer[oldArray.length];
            for (int i = 0; i < oldArray.length; i++) newArray[i] = oldArray[i];
            return newArray;
        }
        return object;
    }

}
