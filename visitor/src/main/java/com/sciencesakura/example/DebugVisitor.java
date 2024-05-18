package com.sciencesakura.example;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.NonReportingDetector;
import java.util.Map;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.ElementValue;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@code Detector} 実装例: デバッグ用の情報を出力する.
 */
public class DebugVisitor extends BytecodeScanningDetector implements NonReportingDetector {

  private static final Logger log = LoggerFactory.getLogger(DebugVisitor.class);

  public DebugVisitor(BugReporter reporter) {
  }

  /**
   * ①クラスファイルの解析スタート.
   */
  @Override
  public void visit(JavaClass obj) {
    log.info("① visit({}クラス)", getDottedClassName());
  }

  /**
   * ②解析対象のクラスファイルか判定.
   */
  @Override
  public boolean shouldVisit(JavaClass obj) {
    log.info("② shouldVisit({}クラス)", getDottedClassName());
    return true;
  }

  /**
   * ③フィールドの解析.
   */
  @Override
  public void visit(Field obj) {
    log.info("  ③ visit({}フィールド)", getFieldName());
  }

  /**
   * ④メソッドの解析.
   */
  @Override
  public void visit(Method obj) {
    log.info("  ④ visit({}メソッド)", getMethodName());
  }

  /**
   * ⑤オペコードの解析.
   */
  @Override
  public void sawOpcode(int seen) {
    log.info("    ⑤ sawOpcode({})", Const.getOpcodeName(seen));
  }

  /**
   * ⑥アノテーション（クラス、フィールド、メソッド）の解析.
   */
  @Override
  public void visitAnnotation(String annotationClass, Map<String, ElementValue> map, boolean runtimeVisible) {
    if (visitingField() || visitingMethod()) {
      // フィールドまたはメソッドのアノテーション
      log.info("    ⑥ visitAnnotation({})", annotationClass);
    } else {
      // クラスのアノテーション
      log.info("  ⑥ visitAnnotation({})", annotationClass);
    }
  }

  /**
   * ⑦アノテーション（パラメータ）の解析.
   */
  @Override
  public void visitParameterAnnotation(int p, String annotationClass, Map<String, ElementValue> map,
      boolean runtimeVisible) {
    log.info("    ⑦ visitParameterAnnotation({}, {})", p, annotationClass);
  }

  /**
   * ⑧クラスファイルの解析終了.
   */
  @Override
  public void visitAfter(JavaClass obj) {
    log.info("⑧ visitAfter({}クラス)", getDottedClassName());
  }
}
