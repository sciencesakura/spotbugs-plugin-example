package com.sciencesakura.example;

import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.bcel.OpcodeStackDetector;
import org.apache.bcel.Const;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

/**
 * {@code Detector} 実装例: ミュータブルなSpring Beanを検出する.
 */
public class DetectMutableSpringBean extends OpcodeStackDetector {

  private final BugReporter reporter;

  /**
   * {@code <init>} , {@code <clinit>} を訪問中かどうか.
   */
  private boolean visitingInit;

  public DetectMutableSpringBean(BugReporter reporter) {
    this.reporter = reporter;
  }

  /**
   * Spring Beanであるクラスのみ訪問する.
   */
  @Override
  public boolean shouldVisit(JavaClass obj) {
    return SpringBeans.isBean(obj);
  }

  /**
   * 非privateかつ非finalなインスタンスフィールドを検出する.
   */
  @Override
  public void visit(Field obj) {
    if (obj.isStatic() || obj.isFinal() || obj.isPrivate()) {
      return;
    }
    reporter.reportBug(new BugInstance(this, "X_MUTABLE_SPRING_BEAN", NORMAL_PRIORITY)
        .addClass(this)
        .addField(this));
  }

  @Override
  public void visit(Method obj) {
    var methodName = obj.getName();
    visitingInit = Const.CONSTRUCTOR_NAME.equals(methodName) || Const.STATIC_INITIALIZER_NAME.equals(methodName);
  }

  @Override
  public boolean beforeOpcode(int seen) {
    // <init> , <clinit> なら無視
    return super.beforeOpcode(seen) && !visitingInit;
  }

  @Override
  public void sawOpcode(int seen) {
    switch (seen) {
      // インスタンスフィールドへの代入
      case Const.PUTFIELD -> detectSettingField();
      // インスタンスメソッド, インターフェースメソッドの呼び出し
      case Const.INVOKEVIRTUAL, Const.INVOKEINTERFACE -> detectMutateField();
      default -> {
      }
    }
  }

  /**
   * インスタンスフィールドに代入を実行しているか検出する.
   */
  private void detectSettingField() {
    // フィールドが自クラス・親クラスのものでないなら無視
    var targetClassName = getClassConstantOperand();
    if (!getClassName().equals(targetClassName) && !getSuperclassName().equals(targetClassName)) {
      return;
    }
    reporter.reportBug(new BugInstance(this, "X_MUTABLE_SPRING_BEAN", NORMAL_PRIORITY)
        .addClassAndMethod(this)
        .addReferencedField(this)
        .addSourceLine(this));
  }

  /**
   * インスタンスフィールドに対して {@code add} , {@code put} , {@code set} で始まるインスタンスメソッドを実行しているか検出する.
   */
  private void detectMutateField() {
    // メソッドを実行するオブジェクトの参照をスタックから取得
    var objectRef = stack.getStackItem(getNumberArguments(getSigConstantOperand()));
    // 自クラス・親クラスのインスタンスフィールドでないなら無視
    var targetField = objectRef.getXField();
    if (targetField == null || targetField.isStatic()) {
      return;
    }
    var targetClassName = targetField.getClassName();
    if (!getDottedClassName().equals(targetClassName) && !getDottedSuperclassName().equals(targetClassName)) {
      return;
    }
    // メソッド名がadd/put/setで始まるか検出
    var methodName = getNameConstantOperand();
    if (methodName != null && methodName.matches("^(add|put|set)([A-Z]\\w*)?$")) {
      reporter.reportBug(new BugInstance(this, "X_MUTABLE_SPRING_BEAN", NORMAL_PRIORITY)
          .addClassAndMethod(this)
          .addField(targetField)
          .addSourceLine(this));
    }
  }
}
