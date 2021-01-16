
package application.controllers;

import com.google.gson.internal.$Gson$Types;
import com.google.gson.Gson;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * author: larryjason
 * website: http://larryjason.com
 * 
 * PS: copy from com.google.gson.reflect.TypeToken :)))
 */

public class GenericParser<T> {
  final Type type;

  public GenericParser() {
    this.type = getSuperclassTypeParameter(getClass());
  }

  static Type getSuperclassTypeParameter(Class<?> subclass) {
    Type superclass = subclass.getGenericSuperclass();
    
    if (superclass instanceof Class) {
      throw new RuntimeException("Missing type parameter.");
    }
    ParameterizedType parameterized = (ParameterizedType) superclass;
    return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
  }

  public final Type getType() {
    return type;
  }

  public T parse(Object obj) {
    Gson gson = new Gson();
    return gson.fromJson(gson.toJson(obj), this.type);
  }

  public T parse(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, this.type);
  }
}
