package me.tud.critic.util;

import org.jetbrains.annotations.Nullable;

public enum ArithmeticOperator {

    PLUS('+') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            Class<? extends Number> numberClass = NumberUtils.getNumberClass(leftOperand, rightOperand);
            if (numberClass == Byte.class || numberClass == Short.class || numberClass == Integer.class || numberClass == Long.class) {
                return leftOperand.longValue() + rightOperand.longValue();
            } else if (numberClass == Float.class) {
                return leftOperand.floatValue() + rightOperand.floatValue();
            } else {
                return leftOperand.doubleValue() + rightOperand.doubleValue();
            }
        }
    },
    MINUS('-') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            Class<? extends Number> numberClass = NumberUtils.getNumberClass(leftOperand, rightOperand);
            if (numberClass == Byte.class || numberClass == Short.class || numberClass == Integer.class || numberClass == Long.class) {
                return leftOperand.longValue() - rightOperand.longValue();
            } else if (numberClass == Float.class) {
                return leftOperand.floatValue() - rightOperand.floatValue();
            } else {
                return leftOperand.doubleValue() - rightOperand.doubleValue();
            }
        }
    },
    MULTIPLICATION('*') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            Class<? extends Number> numberClass = NumberUtils.getNumberClass(leftOperand, rightOperand);
            if (numberClass == Byte.class || numberClass == Short.class || numberClass == Integer.class || numberClass == Long.class) {
                return leftOperand.longValue() * rightOperand.longValue();
            } else if (numberClass == Float.class) {
                return leftOperand.floatValue() * rightOperand.floatValue();
            } else {
                return leftOperand.doubleValue() * rightOperand.doubleValue();
            }
        }
    },
    DIVISION('/') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            Class<? extends Number> numberClass = NumberUtils.getNumberClass(leftOperand, rightOperand);
            if (numberClass == Byte.class || numberClass == Short.class || numberClass == Integer.class || numberClass == Long.class) {
                return leftOperand.longValue() / rightOperand.longValue();
            } else if (numberClass == Float.class) {
                return leftOperand.floatValue() / rightOperand.floatValue();
            } else {
                return leftOperand.doubleValue() / rightOperand.doubleValue();
            }
        }
    },
    MODULO('%') {
        @Override
        public Number calculate(Number leftOperand, Number rightOperand) {
            Class<? extends Number> numberClass = NumberUtils.getNumberClass(leftOperand, rightOperand);
            if (numberClass == Byte.class || numberClass == Short.class || numberClass == Integer.class || numberClass == Long.class) {
                return leftOperand.longValue() % rightOperand.longValue();
            } else if (numberClass == Float.class) {
                return leftOperand.floatValue() % rightOperand.floatValue();
            } else {
                return leftOperand.doubleValue() % rightOperand.doubleValue();
            }
        }
    };

    private final char sign;

    ArithmeticOperator(char sign) {
        this.sign = sign;
    }

    public char getSign() {
        return sign;
    }

    public abstract Number calculate(Number leftOperand, Number rightOperand);

    @Nullable
    public static ArithmeticOperator bySign(char sign) {
        for (ArithmeticOperator value : values()) {
            if (sign == value.getSign())
                return value;
        }
        return null;
    }
}
