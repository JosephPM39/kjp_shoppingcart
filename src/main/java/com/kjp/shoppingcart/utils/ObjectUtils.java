package com.kjp.shoppingcart.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ObjectUtils {
    public static Set<String> getNullPropertyNames(Object object) {
        BeanWrapper src = new BeanWrapperImpl(object);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd: pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames;
    }

    public static <T> T copiarPropiedades(Object origen, Object destino, Class<T> claseDestino) {
        if (origen == null || destino == null || claseDestino == null) {
            throw new IllegalArgumentException("Los objetos y la clase destino no pueden ser nulos");
        }

        Map<String, Object> valores = new HashMap<>();
        Field[] camposOrigen = origen.getClass().getDeclaredFields();
        Field[] camposDestino = destino.getClass().getDeclaredFields();

        for (Field campoDestino : camposDestino) {
            // Ignorar campos estáticos y finales
            // if (Modifier.isStatic(campoDestino.getModifiers()) || Modifier.isFinal(campoDestino.getModifiers())) {
                // continue;
            //}

            // Buscar el campo correspondiente en el objeto de origen
            Field campoOrigen = encontrarCampo(camposOrigen, campoDestino.getName());

            // Almacenar el valor si el campo no es nulo en el objeto de origen
            if (campoOrigen != null) {
                campoDestino.setAccessible(true);
                campoOrigen.setAccessible(true);

                Object valorOrigen = campoOrigen.get(origen);
                Object valorDestion = campoDestino.get(destino);
                if (valorOrigen != null) {
                    valores.put(campoDestino.getName(), valorOrigen);
                } else {
                    valores.put(campoDestino.getName(), valorDestion);
                }
            }
        }

        return instanciarObjeto(claseDestino, valores);
    }

    private static Field encontrarCampo(Field[] campos, String nombreCampo) {
        for (Field campo : campos) {
            if (campo.getName().equals(nombreCampo)) {
                return campo;
            }
        }
        return null;
    }

    private static <T> T instanciarObjeto(Class<T> clase, Map<String, Object> valores) {
        Constructor<T>[] constructores = (Constructor<T>[]) clase.getDeclaredConstructors();

        // Buscar un constructor compatible con la lista de parámetros
        for (Constructor<T> constructor : constructores) {
            Parameter[] tiposParametros = constructor.getParameters();
            if (tiposParametros.length == valores.size() && parametrosCoinciden(tiposParametros, valores.keySet())) {
                constructor.setAccessible(true);
                Object[] parametersValues = new Object[valores.size()];
                for (int i = 0; i < parametersValues.length; i++) {
                    parametersValues[i] = valores.get(tiposParametros[i].getName());
                }

                // Crear la instancia llamando al constructor con los valores
                try {
                    return constructor.newInstance(parametersValues);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }

        throw new IllegalArgumentException("No se encontró un constructor compatible en la clase destino");
    }

    private static boolean parametrosCoinciden(Parameter[] parametros, Set<String> nombres) {
        if (parametros.length != nombres.size()) {
            return false;
        }

        for (Parameter parametro : parametros) {
            if (!nombres.contains(parametro.getName())) {
                return false;
            }
        }

        return true;
    }

}
