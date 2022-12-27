package me.tud.critic.data.types;

import com.google.common.collect.HashBiMap;
import me.tud.critic.exception.ParseException;
import me.tud.critic.util.Checker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class Types {

    /**
     * A list of primitive types that are recognized by this lexer.
     */
    public static final String[] PRIMITIVE_TYPES = {
            "byte", "short", "int", "long", "float", "double", "char", "string", "boolean"
    };

    private static final Checker<String> primitiveTypeChecker = string -> {
        for (String primitiveType : PRIMITIVE_TYPES) {
            if (primitiveType.equals(string))
                return true;
        }
        return false;
    };

    private static final HashMap<String, Type> primitiveTypeMap = new HashMap<>(PRIMITIVE_TYPES.length);
    private static final HashBiMap<Class<? extends Number>, Type> numberTypeMap = HashBiMap.create(6);
    private static final Set<Type> types = new HashSet<>();

    static {
        for (String primitiveType : PRIMITIVE_TYPES)
            primitiveTypeMap.put(primitiveType, registerPrimitive(primitiveType));
        numberTypeMap.put(Byte.class, getPrimitiveType("byte"));
        numberTypeMap.put(Short.class, getPrimitiveType("short"));
        numberTypeMap.put(Integer.class, getPrimitiveType("int"));
        numberTypeMap.put(Long.class, getPrimitiveType("long"));
        numberTypeMap.put(Float.class, getPrimitiveType("float"));
        numberTypeMap.put(Double.class, getPrimitiveType("double"));
        registerClass("Object");
    }

    private Types() {
        throw new UnsupportedOperationException();
    }

    static Type registerPrimitive(String name) {
        Type primitive = new Type(name, true);
        primitiveTypeMap.put(name, primitive);
        return primitive;
    }

    public static Type registerClass(String name) {
        if (primitiveTypeChecker.check(name))
            throw new ParseException("The class name cannot be a primitive's");
        Type type = new Type(name, false);
        if (!types.add(type))
            throw new ParseException("Could not register class '" + name + '\'');
        return type;
    }

    public static Type getPrimitiveTypeNoError(String name) {
        return primitiveTypeMap.get(name);
    }

    public static Type getPrimitiveType(String name) {
        Type type = getPrimitiveTypeNoError(name);
        if (type == null)
            throw new ParseException("There's no primitive type with name '" + name + '\'');
        return type;
    }

    public static Type lookupType(String name) {
        Type primitive = getPrimitiveTypeNoError(name);
        if (primitive != null)
            return primitive;

        return types.stream()
                .filter(type -> type.getName().equals(name))
                .findFirst().orElse(null);
    }

    public static boolean isNumberPrimitive(Type type) {
        return type == getPrimitiveType("int") || type == getPrimitiveType("float") || type == getPrimitiveType("double");
    }

    public static Type getNumberPrimitive(Type... types) {
        Type primitive = getPrimitiveType("int");
        for (Type type : types) {
            if (!type.isPrimitive())
                return null;
            if (type == getPrimitiveType("float") && primitive != getPrimitiveType("float"))
                primitive = getPrimitiveType("float");
            if (type == getPrimitiveType("double"))
                return getPrimitiveType("double");
        }
        return primitive;
    }

    public Type getTypeFromNumberClass(Class<? extends Number> numberClass) {
        return numberTypeMap.get(numberClass);
    }

    public Class<? extends Number> getClassFromNumberType(Type numberType) {
        return numberTypeMap.inverse().get(numberType);
    }

}
