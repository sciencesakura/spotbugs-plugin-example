package com.sciencesakura.example;

import edu.umd.cs.findbugs.util.ClassName;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.bcel.classfile.JavaClass;

/**
 * Spring Beanに関するユーティリティクラス.
 */
public final class SpringBeans {

  private static final List<String> SPRING_BEAN_ANNOTATIONS = List.of(
      "org/springframework/stereotype/Component",
      "org/springframework/stereotype/Controller",
      "org/springframework/stereotype/Repository",
      "org/springframework/stereotype/Service",
      "org/springframework/web/bind/annotation/ControllerAdvice",
      "org/springframework/web/bind/annotation/ExceptionHandler",
      "org/springframework/web/bind/annotation/RestController",
      "org/springframework/web/bind/annotation/RestControllerAdvice"
  );

  /**
   * Spring Beanであるかどうかを判定する.
   *
   * @param javaClass 判定対象のクラス
   * @return Spring Beanである場合は {@code true}
   */
  public static boolean isBean(JavaClass javaClass) {
    return Stream.of(javaClass.getAnnotationEntries())
        .map(a -> ClassName.fromFieldSignature(a.getAnnotationType()))
        .filter(Objects::nonNull)
        .anyMatch(SPRING_BEAN_ANNOTATIONS::contains);
  }
}
